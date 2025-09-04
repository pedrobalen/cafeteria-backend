package com.cafeteria.gestao_cafeteria.dto;

import com.cafeteria.gestao_cafeteria.model.FormaPagamento;
import lombok.Data;
import java.util.List;

@Data
public class RegistrarPagamentoDTO {
    private List<Long> itemIds;
    private FormaPagamento formaPagamento;
}