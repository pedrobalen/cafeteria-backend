package com.cafeteria.gestao_cafeteria.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FichaTecnicaItemDTO {
    private Long insumoId;
    private BigDecimal quantidade;
}