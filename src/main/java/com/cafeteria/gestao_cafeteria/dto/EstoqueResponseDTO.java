package com.cafeteria.gestao_cafeteria.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class EstoqueResponseDTO {
    private Long produtoId;
    private String nomeProduto;
    private BigDecimal quantidade;
    private String unidadeMedida;
}