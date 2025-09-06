package com.cafeteria.gestao_cafeteria.service;

import com.cafeteria.gestao_cafeteria.dto.InsumoCreateDTO;
import com.cafeteria.gestao_cafeteria.infra.exceptions.ResourceNotFoundException;
import com.cafeteria.gestao_cafeteria.model.*;
import com.cafeteria.gestao_cafeteria.repository.FichaTecnicaRepository;
import com.cafeteria.gestao_cafeteria.repository.InsumoRepository;
import com.cafeteria.gestao_cafeteria.repository.MovimentoInsumoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InsumoService {

    private static final Logger logger = LoggerFactory.getLogger(InsumoService.class);

    @Autowired
    private InsumoRepository insumoRepository;
    @Autowired
    private MovimentoInsumoRepository movimentoInsumoRepository;
    @Autowired
    private FichaTecnicaRepository fichaTecnicaRepository;

    @Transactional
    public Insumo criarInsumo(InsumoCreateDTO dto) {
        Insumo novoInsumo = new Insumo();
        novoInsumo.setNome(dto.getNome());
        novoInsumo.setUnidadeMedida(dto.getUnidadeMedida());
        novoInsumo.setQuantidadeEstoque(dto.getQuantidadeInicial() != null ? dto.getQuantidadeInicial() : BigDecimal.ZERO);
        novoInsumo.setEstoqueMinimo(dto.getEstoqueMinimo());

        Insumo insumoSalvo = insumoRepository.save(novoInsumo);

        if (novoInsumo.getQuantidadeEstoque().compareTo(BigDecimal.ZERO) > 0) {
            MovimentoInsumo movimento = new MovimentoInsumo();
            movimento.setInsumo(insumoSalvo);
            movimento.setTipo(TipoMovimentoInsumo.ENTRADA);
            movimento.setQuantidade(novoInsumo.getQuantidadeEstoque());
            movimento.setMotivo("Estoque inicial");
            movimento.setDataMovimento(LocalDateTime.now());
            movimentoInsumoRepository.save(movimento);
        }

        return insumoSalvo;
    }

    @Transactional
    public void registrarEntrada(Long insumoId, BigDecimal quantidade, String motivo) {
        Insumo insumo = insumoRepository.findById(insumoId)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo não encontrado com ID: " + insumoId));

        insumo.setQuantidadeEstoque(insumo.getQuantidadeEstoque().add(quantidade));
        insumoRepository.save(insumo);

        MovimentoInsumo movimento = new MovimentoInsumo();
        movimento.setInsumo(insumo);
        movimento.setTipo(TipoMovimentoInsumo.ENTRADA);
        movimento.setQuantidade(quantidade);
        movimento.setMotivo(motivo);
        movimento.setDataMovimento(LocalDateTime.now());
        movimentoInsumoRepository.save(movimento);
    }

    @Transactional(readOnly = true)
    public List<Insumo> listarTodos() {
        return insumoRepository.findAll();
    }

    @Transactional
    public void registrarSaidaPorVenda(List<ItemComanda> itensVendidos) {
        for (ItemComanda itemVendido : itensVendidos) {
            Produto produtoVendido = itemVendido.getProduto();
            int quantidadeVendidaProduto = itemVendido.getQuantidade();

            List<FichaTecnica> receita = fichaTecnicaRepository.findByProdutoId(produtoVendido.getId());

            if (receita.isEmpty()) {
                logger.warn("Produto vendido (ID: {}) não possui ficha técnica. Nenhuma baixa de insumo foi realizada.", produtoVendido.getId());
                continue;
            }

            for (FichaTecnica itemReceita : receita) {
                Insumo insumo = itemReceita.getInsumo();
                BigDecimal quantidadeInsumoPorProduto = itemReceita.getQuantidade();
                BigDecimal quantidadeADebitar = quantidadeInsumoPorProduto.multiply(new BigDecimal(quantidadeVendidaProduto));

                insumo.setQuantidadeEstoque(insumo.getQuantidadeEstoque().subtract(quantidadeADebitar));
                insumoRepository.save(insumo);

                MovimentoInsumo movimento = new MovimentoInsumo();
                movimento.setInsumo(insumo);
                movimento.setTipo(TipoMovimentoInsumo.SAIDA_POR_VENDA);
                movimento.setQuantidade(quantidadeADebitar);
                movimento.setMotivo("Venda do produto: " + produtoVendido.getNome() + " (Comanda ID: " + itemVendido.getComanda().getId() + ")");
                movimento.setDataMovimento(LocalDateTime.now());
                movimentoInsumoRepository.save(movimento);
            }
        }
    }
}