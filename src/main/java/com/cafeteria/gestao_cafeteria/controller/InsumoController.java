package com.cafeteria.gestao_cafeteria.controller;

import com.cafeteria.gestao_cafeteria.dto.InsumoCreateDTO;
import com.cafeteria.gestao_cafeteria.dto.InsumoEntradaDTO;
import com.cafeteria.gestao_cafeteria.model.Insumo;
import com.cafeteria.gestao_cafeteria.service.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insumos")
public class InsumoController {

    @Autowired
    private InsumoService insumoService;

    @PostMapping
    public ResponseEntity<Insumo> criarInsumo(@RequestBody InsumoCreateDTO dto) {
        Insumo novoInsumo = insumoService.criarInsumo(dto);
        return new ResponseEntity<>(novoInsumo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Insumo>> listarInsumos() {
        return ResponseEntity.ok(insumoService.listarTodos());
    }

    @PostMapping("/{insumoId}/entrada")
    public ResponseEntity<Void> registrarEntrada(@PathVariable Long insumoId, @RequestBody InsumoEntradaDTO dto) {
        insumoService.registrarEntrada(insumoId, dto.getQuantidade(), dto.getMotivo());
        return ResponseEntity.ok().build();
    }
}