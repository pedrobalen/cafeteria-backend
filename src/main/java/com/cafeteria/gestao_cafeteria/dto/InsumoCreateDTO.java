package com.cafeteria.gestao_cafeteria.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InsumoCreateDTO {
    private String nome;
    private BigDecimal quantidadeInicial;
    private String unidadeMedida;
    private BigDecimal estoqueMinimo;
}