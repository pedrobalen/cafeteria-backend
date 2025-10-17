package com.cafeteria.gestao_cafeteria.service;

import com.cafeteria.gestao_cafeteria.dto.*;
import com.cafeteria.gestao_cafeteria.infra.exceptions.ResourceNotFoundException;
import com.cafeteria.gestao_cafeteria.model.*;
import com.cafeteria.gestao_cafeteria.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private InsumoService insumoService;

    @Transactional
    // MUDANÇA: O método agora retorna um DTO para evitar expor a entidade.
    public ComandaResponseDTO abrirComanda(AbrirComandaDTO dto) {
        if (dto.getMesaId() == null && (dto.getIdentificadorCliente() == null || dto.getIdentificadorCliente().isBlank())) {
            throw new IllegalArgumentException("É necessário fornecer um ID de mesa ou um identificador de cliente para abrir a comanda.");
        }

        Comanda novaComanda = new Comanda();

        if (dto.getMesaId() != null) {
            Mesa mesa = mesaRepository.findById(dto.getMesaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada com o ID: " + dto.getMesaId()));

            novaComanda.setMesa(mesa);
        } else {
            novaComanda.setIdentificadorCliente(dto.getIdentificadorCliente());
        }

        novaComanda.setStatus(StatusComanda.ABERTA);
        novaComanda.setDataAbertura(LocalDateTime.now());

        Comanda comandaSalva = comandaRepository.save(novaComanda);

        // Mapeia a entidade salva para um DTO antes de retornar.
        ComandaResponseDTO responseDTO = new ComandaResponseDTO();
        responseDTO.setId(comandaSalva.getId());
        responseDTO.setStatus(comandaSalva.getStatus());
        responseDTO.setDataAbertura(comandaSalva.getDataAbertura());
        if (comandaSalva.getMesa() != null) {
            responseDTO.setNumeroMesa(comandaSalva.getMesa().getNumero());
        }
        responseDTO.setIdentificadorCliente(comandaSalva.getIdentificadorCliente());
        responseDTO.setItens(new ArrayList<>());
        responseDTO.setValorTotal(BigDecimal.ZERO);

        return responseDTO;
    }

    @Transactional
    // MUDANÇA: O método agora retorna um DTO para desacoplar a resposta da entidade.
    public ItemComandaResponseDTO adicionarItem(Long comandaId, Long produtoId, int quantidade) {
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

        ItemComanda itemSalvo = itemComandaRepository.save(novoItem);

        // Mapeia a entidade salva para um DTO antes de retornar.
        ItemComandaResponseDTO responseDTO = new ItemComandaResponseDTO();
        responseDTO.setId(itemSalvo.getId());
        responseDTO.setNomeProduto(itemSalvo.getProduto().getNome());
        responseDTO.setQuantidade(itemSalvo.getQuantidade());
        responseDTO.setPrecoUnitario(itemSalvo.getPrecoUnitarioMomento());
        BigDecimal subtotal = itemSalvo.getPrecoUnitarioMomento().multiply(new BigDecimal(itemSalvo.getQuantidade()));
        responseDTO.setSubtotal(subtotal);

        return responseDTO;
    }

    // Este método já está correto.
    @Transactional(readOnly = true)
    public ComandaResponseDTO buscarPorId(Long comandaId) {
        Comanda comanda = comandaRepository.findByIdWithDetails(comandaId)
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
        if (comanda.getMesa() != null) {
            comandaDTO.setNumeroMesa(comanda.getMesa().getNumero());
        }
        comandaDTO.setIdentificadorCliente(comanda.getIdentificadorCliente());
        comandaDTO.setStatus(comanda.getStatus());
        comandaDTO.setDataAbertura(comanda.getDataAbertura());
        comandaDTO.setItens(itensDTO);
        comandaDTO.setValorTotal(valorTotal);

        return comandaDTO;
    }

    // Este método já está correto.
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

        insumoService.registrarSaidaPorVenda(itensParaPagar);

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

    // Este método já está correto.
    @Transactional(readOnly = true)
    public List<ComandaResumoDTO> listarComandasPorStatus(StatusComanda status) {
        List<Comanda> comandas = comandaRepository.findByStatusWithDetails(status);

        return comandas.stream().map(comanda -> {
            ComandaResumoDTO dto = new ComandaResumoDTO();
            dto.setId(comanda.getId());
            if (comanda.getMesa() != null) {
                dto.setNumeroMesa(comanda.getMesa().getNumero());
            }
            dto.setIdentificadorCliente(comanda.getIdentificadorCliente());
            dto.setDataAbertura(comanda.getDataAbertura());

            BigDecimal total = comanda.getItens().stream()
                    .map(item -> item.getPrecoUnitarioMomento().multiply(new BigDecimal(item.getQuantidade())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setValorTotal(total);

            return dto;
        }).collect(Collectors.toList());
    }
}