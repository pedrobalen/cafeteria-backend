package com.cafeteria.gestao_cafeteria.service;

import com.cafeteria.gestao_cafeteria.dto.RegistrarPagamentoDTO;
import com.cafeteria.gestao_cafeteria.dto.ComandaResponseDTO;
import com.cafeteria.gestao_cafeteria.dto.ItemComandaResponseDTO;
import com.cafeteria.gestao_cafeteria.dto.PagamentoResponseDTO;
import com.cafeteria.gestao_cafeteria.model.StatusComanda;
import com.cafeteria.gestao_cafeteria.infra.exceptions.ResourceNotFoundException;
import com.cafeteria.gestao_cafeteria.model.Comanda;
import com.cafeteria.gestao_cafeteria.model.ItemComanda;
import com.cafeteria.gestao_cafeteria.model.Pagamento;
import com.cafeteria.gestao_cafeteria.model.Produto;
import com.cafeteria.gestao_cafeteria.repository.ComandaRepository;
import com.cafeteria.gestao_cafeteria.repository.ItemComandaRepository;
import com.cafeteria.gestao_cafeteria.repository.PagamentoRepository;
import com.cafeteria.gestao_cafeteria.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Usando a anotação do Spring

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComandaService {

    @Autowired
    private ComandaRepository comandaRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private ItemComandaRepository itemComandaRepository;
    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Transactional
    public Comanda abrirComanda(Integer numeroMesa, String identificadorCliente) {
        if (numeroMesa == null && (identificadorCliente == null || identificadorCliente.isBlank())) {
            throw new IllegalArgumentException("É necessário fornecer um número de mesa ou um identificador de cliente para abrir a comanda.");
        }

        Comanda novaComanda = new Comanda();
        novaComanda.setNumeroMesa(numeroMesa);
        novaComanda.setIdentificadorCliente(identificadorCliente);
        novaComanda.setStatus(StatusComanda.ABERTA);
        novaComanda.setDataAbertura(LocalDateTime.now());

        return comandaRepository.save(novaComanda);
    }

    @Transactional
    public ItemComanda adicionarItem(Long comandaId, Long produtoId, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        Comanda comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new ResourceNotFoundException("Comanda não encontrada com o ID: " + comandaId));

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + produtoId));

        if (comanda.getStatus() != StatusComanda.ABERTA) {
            throw new IllegalStateException("Não é possível adicionar itens a uma comanda que não está ABERTA.");
        }

        if (!produto.isAtivo()) {
            throw new IllegalStateException("Não é possível adicionar um produto inativo.");
        }

        ItemComanda novoItem = new ItemComanda();
        novoItem.setComanda(comanda);
        novoItem.setProduto(produto);
        novoItem.setQuantidade(quantidade);
        novoItem.setPrecoUnitarioMomento(produto.getPrecoVenda());
        novoItem.setDataAdicao(LocalDateTime.now());

        return itemComandaRepository.save(novoItem);
    }

    @Transactional(readOnly = true)
    public ComandaResponseDTO buscarPorId(Long comandaId) {
        Comanda comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new ResourceNotFoundException("Comanda não encontrada com o ID: " + comandaId));

        List<ItemComandaResponseDTO> itensDTO = comanda.getItens().stream().map(item -> {
            ItemComandaResponseDTO itemDTO = new ItemComandaResponseDTO();
            itemDTO.setId(item.getId());
            itemDTO.setNomeProduto(item.getProduto().getNome());
            itemDTO.setQuantidade(item.getQuantidade());
            itemDTO.setPrecoUnitario(item.getPrecoUnitarioMomento());

            BigDecimal subtotal = item.getPrecoUnitarioMomento().multiply(new BigDecimal(item.getQuantidade()));
            itemDTO.setSubtotal(subtotal);

            return itemDTO;
        }).collect(Collectors.toList());

        BigDecimal valorTotal = itensDTO.stream()
                .map(ItemComandaResponseDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        ComandaResponseDTO comandaDTO = new ComandaResponseDTO();
        comandaDTO.setId(comanda.getId());
        comandaDTO.setNumeroMesa(comanda.getNumeroMesa());
        comandaDTO.setIdentificadorCliente(comanda.getIdentificadorCliente());
        comandaDTO.setStatus(comanda.getStatus());
        comandaDTO.setDataAbertura(comanda.getDataAbertura());
        comandaDTO.setItens(itensDTO);
        comandaDTO.setValorTotal(valorTotal);

        return comandaDTO;
    }

    @Transactional
    public PagamentoResponseDTO registrarPagamento(Long comandaId, RegistrarPagamentoDTO pagamentoDTO) {
        if (pagamentoDTO.getItemIds() == null || pagamentoDTO.getItemIds().isEmpty()) {
            throw new IllegalArgumentException("A lista de itens para pagamento não pode ser vazia.");
        }
        Comanda comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new ResourceNotFoundException("Comanda não encontrada com o ID: " + comandaId));

        if (comanda.getStatus() != StatusComanda.ABERTA) {
            throw new IllegalStateException("Esta comanda não está aberta para pagamentos.");
        }

        List<ItemComanda> itensParaPagar = comanda.getItens().stream()
                .filter(item -> pagamentoDTO.getItemIds().contains(item.getId()))
                .collect(Collectors.toList());

        if (itensParaPagar.size() != pagamentoDTO.getItemIds().size()) {
            throw new ResourceNotFoundException("Um ou mais IDs de item não foram encontrados nesta comanda.");
        }

        for (ItemComanda item : itensParaPagar) {
            if (item.getPagamento() != null) {
                throw new IllegalStateException("O item " + item.getProduto().getNome() + " (ID: " + item.getId() + ") já foi pago.");
            }
        }

        BigDecimal valorDoPagamento = itensParaPagar.stream()
                .map(item -> item.getPrecoUnitarioMomento().multiply(new BigDecimal(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Pagamento novoPagamento = new Pagamento();
        novoPagamento.setComanda(comanda);
        novoPagamento.setValorTotal(valorDoPagamento);
        novoPagamento.setFormaPagamento(pagamentoDTO.getFormaPagamento());
        novoPagamento.setDataPagamento(LocalDateTime.now());
        Pagamento pagamentoSalvo = pagamentoRepository.save(novoPagamento);

        for (ItemComanda item : itensParaPagar) {
            item.setPagamento(pagamentoSalvo);
        }

        boolean todosItensPagos = comanda.getItens().stream().allMatch(item -> item.getPagamento() != null);
        if (todosItensPagos) {
            comanda.setStatus(StatusComanda.PAGA);
            comanda.setDataFechamento(LocalDateTime.now());
            comandaRepository.save(comanda);
        }

        PagamentoResponseDTO responseDTO = new PagamentoResponseDTO();
        responseDTO.setId(pagamentoSalvo.getId());
        responseDTO.setComandaId(comandaId);
        responseDTO.setValorPago(pagamentoSalvo.getValorTotal());
        responseDTO.setFormaPagamento(pagamentoSalvo.getFormaPagamento());
        responseDTO.setDataPagamento(pagamentoSalvo.getDataPagamento());
        responseDTO.setItemIdsPagos(pagamentoDTO.getItemIds());

        return responseDTO;
    }
}