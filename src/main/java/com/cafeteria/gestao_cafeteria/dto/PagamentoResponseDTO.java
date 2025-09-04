package com.cafeteria.gestao_cafeteria.dto;

import com.cafeteria.gestao_cafeteria.model.FormaPagamento;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PagamentoResponseDTO {
    private Long id;
    private Long comandaId;
    private BigDecimal valorPago;
    private FormaPagamento formaPagamento;
    private LocalDateTime dataPagamento;
    private List<Long> itemIdsPagos;
}