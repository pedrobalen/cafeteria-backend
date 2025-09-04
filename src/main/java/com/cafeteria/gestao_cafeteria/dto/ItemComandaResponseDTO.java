package com.cafeteria.gestao_cafeteria.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data // @Data é seguro aqui, pois DTOs não têm relações complexas
public class ItemComandaResponseDTO {
    private Long id;
    private String nomeProduto;
    private int quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
}