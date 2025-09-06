package com.cafeteria.gestao_cafeteria.controller;

import com.cafeteria.gestao_cafeteria.dto.MesaCreateDTO;
import com.cafeteria.gestao_cafeteria.model.Mesa;
import com.cafeteria.gestao_cafeteria.repository.MesaRepository;
import com.cafeteria.gestao_cafeteria.service.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private MesaService mesaService;

    @PostMapping
    public ResponseEntity<Mesa> criarMesa(@RequestBody MesaCreateDTO mesaDTO) {
        Mesa novaMesa = mesaService.criarMesa(mesaDTO);
        return new ResponseEntity<>(novaMesa, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Mesa>> listarMesas(@RequestParam(defaultValue = "true") boolean ativo) {
        return ResponseEntity.ok(mesaRepository.findByAtivo(ativo));
    }
}