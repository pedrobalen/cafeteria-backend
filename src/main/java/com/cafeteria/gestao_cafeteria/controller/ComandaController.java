package com.cafeteria.gestao_cafeteria.controller;

import com.cafeteria.gestao_cafeteria.dto.*;
import com.cafeteria.gestao_cafeteria.model.Comanda;
import com.cafeteria.gestao_cafeteria.model.ItemComanda;
import com.cafeteria.gestao_cafeteria.model.StatusComanda;
import com.cafeteria.gestao_cafeteria.service.ComandaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comandas")
public class ComandaController {

    @Autowired
    private ComandaService comandaService;

    @PostMapping("/abrir")
    public ResponseEntity<Comanda> abrirComanda(@RequestBody AbrirComandaDTO abrirComandaDTO) {
        Comanda novaComanda = comandaService.abrirComanda(abrirComandaDTO);
        return new ResponseEntity<>(novaComanda, HttpStatus.CREATED);
    }

    @PostMapping("/{comandaId}/itens")
    public ResponseEntity<ItemComanda> adicionarItem(@PathVariable Long comandaId, @RequestBody AddItemDTO addItemDTO) {
        ItemComanda novoItem = comandaService.adicionarItem(
                comandaId,
                addItemDTO.getProdutoId(),
                addItemDTO.getQuantidade()
        );
        return new ResponseEntity<>(novoItem, HttpStatus.CREATED);
    }

    @GetMapping("/{comandaId}")
    public ResponseEntity<ComandaResponseDTO> buscarPorId(@PathVariable Long comandaId) {
        ComandaResponseDTO comandaDTO = comandaService.buscarPorId(comandaId);
        return ResponseEntity.ok(comandaDTO);
    }

    @PostMapping("/{comandaId}/pagar")
    public ResponseEntity<PagamentoResponseDTO> registrarPagamento(@PathVariable Long comandaId, @RequestBody RegistrarPagamentoDTO pagamentoDTO) {
        PagamentoResponseDTO response = comandaService.registrarPagamento(comandaId, pagamentoDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ComandaResumoDTO>> listarComandas(@RequestParam(required = false) StatusComanda status) {
        if (status != null) {
            return ResponseEntity.ok(comandaService.listarComandasPorStatus(status));
        }
        return ResponseEntity.ok(List.of());
    }
}