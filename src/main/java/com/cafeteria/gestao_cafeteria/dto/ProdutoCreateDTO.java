package com.cafeteria.gestao_cafeteria.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdutoCreateDTO {
    private String nome;
    private String descricao;
    private BigDecimal precoVenda;
    private boolean ativo = true;
    private Long categoriaId;
}