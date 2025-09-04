package com.cafeteria.gestao_cafeteria.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class EstoqueMovimentoDTO {
    private BigDecimal quantidade;
    private String motivo;
    // Para a criação inicial do item em estoque
    private String unidadeMedida;
}