package com.cafeteria.gestao_cafeteria.controller;

import com.cafeteria.gestao_cafeteria.dto.VendasDiaDTO;
import com.cafeteria.gestao_cafeteria.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/vendas-do-dia")
    public ResponseEntity<VendasDiaDTO> getVendasDoDia() {
        // Por padrão, calcula para a data de hoje
        VendasDiaDTO relatorio = relatorioService.calcularVendasDoDia(LocalDate.now());
        return ResponseEntity.ok(relatorio);
    }
}