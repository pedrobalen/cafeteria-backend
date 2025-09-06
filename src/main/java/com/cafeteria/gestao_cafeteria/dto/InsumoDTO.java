package com.cafeteria.gestao_cafeteria.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InsumoDTO {
    private Long id;
    private String nome;
    private BigDecimal quantidadeEstoque;
    private String unidadeMedida;
    private BigDecimal estoqueMinimo;
}