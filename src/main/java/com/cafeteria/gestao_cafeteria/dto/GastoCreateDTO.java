package com.cafeteria.gestao_cafeteria.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GastoCreateDTO {
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataGasto;
    private String categoria;
}