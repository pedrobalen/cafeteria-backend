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
    public ResponseEntity<List<Mesa>> listarMesas(@RequestParam(required = false) Boolean ativo) {
        if (ativo != null) {
            return ResponseEntity.ok(mesaRepository.findByAtivo(ativo));
        }
        return ResponseEntity.ok(mesaRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mesa> atualizarMesa(@PathVariable Long id, @RequestBody MesaCreateDTO mesaDTO) {
        Mesa mesaAtualizada = mesaService.atualizarMesa(id, mesaDTO);
        return ResponseEntity.ok(mesaAtualizada);
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Mesa> ativarMesa(@PathVariable Long id) {
        Mesa mesa = mesaService.alterarStatusAtivo(id, true);
        return ResponseEntity.ok(mesa);
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Mesa> desativarMesa(@PathVariable Long id) {
        Mesa mesa = mesaService.alterarStatusAtivo(id, false);
        return ResponseEntity.ok(mesa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMesa(@PathVariable Long id) {
        mesaService.deletarMesa(id);
        return ResponseEntity.noContent().build();
    }
}