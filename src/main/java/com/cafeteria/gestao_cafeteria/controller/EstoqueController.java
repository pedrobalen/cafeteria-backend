package com.cafeteria.gestao_cafeteria.controller;

import com.cafeteria.gestao_cafeteria.dto.EstoqueMovimentoDTO;
import com.cafeteria.gestao_cafeteria.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @PostMapping("/produtos/{produtoId}/entrada")
    public ResponseEntity<Void> registrarEntrada(
            @PathVariable Long produtoId,
            @RequestBody EstoqueMovimentoDTO movimentoDTO) {
        
        estoqueService.registrarEntrada(
            produtoId, 
            movimentoDTO.getQuantidade(),
            movimentoDTO.getUnidadeMedida(),
            movimentoDTO.getMotivo()
        );
        return ResponseEntity.ok().build();
    }

}