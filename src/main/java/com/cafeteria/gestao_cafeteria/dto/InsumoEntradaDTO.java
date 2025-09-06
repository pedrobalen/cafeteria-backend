package com.cafeteria.gestao_cafeteria.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InsumoEntradaDTO {
    private BigDecimal quantidade;
    private String motivo;
}