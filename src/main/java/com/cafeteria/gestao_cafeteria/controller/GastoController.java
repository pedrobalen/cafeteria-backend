package com.cafeteria.gestao_cafeteria.controller;

import com.cafeteria.gestao_cafeteria.dto.GastoCreateDTO;
import com.cafeteria.gestao_cafeteria.model.Gasto;
import com.cafeteria.gestao_cafeteria.service.GastoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gastos")
public class GastoController {

    @Autowired
    private GastoService gastoService;

    @PostMapping
    public ResponseEntity<Gasto> criarGasto(@RequestBody GastoCreateDTO dto) {
        Gasto novoGasto = gastoService.criarGasto(dto);
        return new ResponseEntity<>(novoGasto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Gasto>> listarGastos() {
        return ResponseEntity.ok(gastoService.listarTodos());
    }
}