package com.cafeteria.gestao_cafeteria.controller;

import com.cafeteria.gestao_cafeteria.dto.FichaTecnicaItemDTO;
import com.cafeteria.gestao_cafeteria.dto.FichaTecnicaResponseDTO;
import com.cafeteria.gestao_cafeteria.model.FichaTecnica;
import com.cafeteria.gestao_cafeteria.service.FichaTecnicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos/{produtoId}/ficha-tecnica")
public class FichaTecnicaController {

    @Autowired
    private FichaTecnicaService fichaTecnicaService;

    @PostMapping
    public ResponseEntity<FichaTecnica> adicionarItem(@PathVariable Long produtoId, @RequestBody FichaTecnicaItemDTO itemDTO) {
        FichaTecnica novoItem = fichaTecnicaService.adicionarItem(produtoId, itemDTO);
        return new ResponseEntity<>(novoItem, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FichaTecnicaResponseDTO>> getFichaTecnica(@PathVariable Long produtoId) {
        List<FichaTecnicaResponseDTO> fichaTecnica = fichaTecnicaService.getFichaTecnicaDoProduto(produtoId);
        return ResponseEntity.ok(fichaTecnica);
    }

    // Usamos um path diferente para o DELETE para evitar ambiguidade
    @DeleteMapping("/itens/{fichaTecnicaId}")
    public ResponseEntity<Void> removerItem(@PathVariable Long produtoId, @PathVariable Long fichaTecnicaId) {
        // O produtoId no path ajuda a manter o contexto, mas a operação usa o fichaTecnicaId
        fichaTecnicaService.removerItem(fichaTecnicaId);
        return ResponseEntity.noContent().build();
    }
}