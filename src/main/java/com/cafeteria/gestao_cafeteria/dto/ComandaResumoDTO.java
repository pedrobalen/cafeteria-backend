package com.cafeteria.gestao_cafeteria.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ComandaResumoDTO {
    private Long id;
    private Integer numeroMesa;
    private String identificadorCliente;
    private BigDecimal valorTotal; // Precisaremos calcular isso
    private LocalDateTime dataAbertura;
}