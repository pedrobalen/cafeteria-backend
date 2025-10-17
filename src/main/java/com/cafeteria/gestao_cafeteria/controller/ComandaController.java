package com.cafeteria.gestao_cafeteria.controller;

import com.cafeteria.gestao_cafeteria.dto.*;
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
    public ResponseEntity<ComandaResponseDTO> abrirComanda(@RequestBody AbrirComandaDTO abrirComandaDTO) {
        ComandaResponseDTO responseDto = comandaService.abrirComanda(abrirComandaDTO);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/{comandaId}/itens")
    public ResponseEntity<ItemComandaResponseDTO> adicionarItem(@PathVariable Long comandaId, @RequestBody AddItemDTO addItemDTO) {
        ItemComandaResponseDTO novoItemDto = comandaService.adicionarItem(
                comandaId,
                addItemDTO.getProdutoId(),
                addItemDTO.getQuantidade()
        );
        return new ResponseEntity<>(novoItemDto, HttpStatus.CREATED);
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
        // Retornar uma lista vazia é uma boa prática quando nenhum filtro é aplicado e não há um default.
        return ResponseEntity.ok(List.of());
    }
}