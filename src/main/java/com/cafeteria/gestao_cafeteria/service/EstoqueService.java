package com.cafeteria.gestao_cafeteria.service;

import com.cafeteria.gestao_cafeteria.infra.exceptions.ResourceNotFoundException;
import com.cafeteria.gestao_cafeteria.model.*;
import com.cafeteria.gestao_cafeteria.repository.EstoqueRepository;
import com.cafeteria.gestao_cafeteria.repository.MovimentoEstoqueRepository;
import com.cafeteria.gestao_cafeteria.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class EstoqueService {

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
    
    // Futuramente, implementaremos os outros métodos aqui: registrarSaida, registrarAjuste, listarEstoque, etc.
}