package com.cafeteria.gestao_cafeteria.dto;

import com.cafeteria.gestao_cafeteria.model.StatusComanda;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ComandaResponseDTO {
    private Long id;
    private Integer numeroMesa;
    private String identificadorCliente;
    private StatusComanda status;
    private LocalDateTime dataAbertura;
    private List<ItemComandaResponseDTO> itens;
    private BigDecimal valorTotal;
}