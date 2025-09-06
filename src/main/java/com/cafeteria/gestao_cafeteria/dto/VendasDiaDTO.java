package com.cafeteria.gestao_cafeteria.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class VendasDiaDTO {
    private LocalDate data;
    private BigDecimal totalVendas;
    private int quantidadeVendas;
}