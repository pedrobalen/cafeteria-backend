package com.cafeteria.gestao_cafeteria.service;

import com.cafeteria.gestao_cafeteria.dto.EstoqueResponseDTO;
import com.cafeteria.gestao_cafeteria.infra.exceptions.ResourceNotFoundException;
import com.cafeteria.gestao_cafeteria.model.*;
import com.cafeteria.gestao_cafeteria.repository.EstoqueRepository;
import com.cafeteria.gestao_cafeteria.repository.MovimentoEstoqueRepository;
import com.cafeteria.gestao_cafeteria.repository.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstoqueService {

    private static final Logger logger = LoggerFactory.getLogger(EstoqueService.class);

    @Autowired
    private EstoqueRepository estoqueRepository;
    @Autowired
    private MovimentoEstoqueRepository movimentoEstoqueRepository;
    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public void registrarEntrada(Long produtoId, BigDecimal quantidade, String unidadeMedida, String motivo) {
        // Busca o produto ou lança exceção
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + produtoId));

        // Busca o estoque do produto. Se não existir, cria um novo.
        Estoque estoque = estoqueRepository.findByProdutoId(produtoId)
            .orElseGet(() -> {
                Estoque novoEstoque = new Estoque();
                novoEstoque.setProduto(produto);
                novoEstoque.setQuantidade(BigDecimal.ZERO); // Começa com zero antes da primeira entrada
                novoEstoque.setUnidadeMedida(unidadeMedida);
                return novoEstoque;
            });

        // Adiciona a quantidade ao estoque atual
        estoque.setQuantidade(estoque.getQuantidade().add(quantidade));
        Estoque estoqueSalvo = estoqueRepository.save(estoque);

        // Cria o registro do movimento de entrada
        MovimentoEstoque movimento = new MovimentoEstoque();
        movimento.setEstoque(estoqueSalvo);
        movimento.setTipo(TipoMovimentoEstoque.ENTRADA);
        movimento.setQuantidade(quantidade);
        movimento.setMotivo(motivo);
        movimento.setDataMovimento(LocalDateTime.now());
        movimentoEstoqueRepository.save(movimento);
    }

    @Transactional
    public void registrarSaidaPorVenda(List<ItemComanda> itensPagos) {
        for (ItemComanda item : itensPagos) {
            Produto produtoVendido = item.getProduto();

            // Busca o registro de estoque para o produto vendido
            estoqueRepository.findByProdutoId(produtoVendido.getId()).ifPresentOrElse(estoque -> {
                // Se o produto tem controle de estoque, atualiza a quantidade
                BigDecimal quantidadeVendida = new BigDecimal(item.getQuantidade());
                estoque.setQuantidade(estoque.getQuantidade().subtract(quantidadeVendida));
                estoqueRepository.save(estoque);

                // Cria o registro do movimento de saída para rastreabilidade
                MovimentoEstoque movimento = new MovimentoEstoque();
                movimento.setEstoque(estoque);
                movimento.setTipo(TipoMovimentoEstoque.SAIDA);
                movimento.setQuantidade(quantidadeVendida);
                movimento.setMotivo("Venda - Comanda ID: " + item.getComanda().getId());
                movimento.setDataMovimento(LocalDateTime.now());
                movimentoEstoqueRepository.save(movimento);

            }, () -> {
                // Se o produto vendido não tem um registro de estoque, apenas logamos um aviso.
                // Isso permite vender itens que não têm controle de estoque (ex: "Serviço de Entrega").
                logger.warn("Produto vendido (ID: {}) não possui registro de estoque. Nenhuma baixa foi realizada.", produtoVendido.getId());
            });
        }
    }

    // Dentro da classe EstoqueService
    @Transactional(readOnly = true)
    public List<EstoqueResponseDTO> listarEstoque() {
        return estoqueRepository.findAll().stream()
                .map(estoque -> {
                    EstoqueResponseDTO dto = new EstoqueResponseDTO();
                    dto.setProdutoId(estoque.getProduto().getId());
                    dto.setNomeProduto(estoque.getProduto().getNome());
                    dto.setQuantidade(estoque.getQuantidade());
                    dto.setUnidadeMedida(estoque.getUnidadeMedida());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    
    // Futuramente, implementaremos os outros métodos aqui: registrarSaida, registrarAjuste, listarEstoque, etc.
}