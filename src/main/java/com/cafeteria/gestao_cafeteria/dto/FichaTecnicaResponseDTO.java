package com.cafeteria.gestao_cafeteria.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FichaTecnicaResponseDTO {
    private Long fichaTecnicaId;
    private Long insumoId;
    private String nomeInsumo;
    private BigDecimal quantidade;
    private String unidadeMedida;
}