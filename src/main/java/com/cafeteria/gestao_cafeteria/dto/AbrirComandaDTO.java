package com.cafeteria.gestao_cafeteria.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// DTO para receber os dados para abrir uma comanda
@Getter
@Setter
@Data
public class AbrirComandaDTO {
    private Long mesaId;
    private String identificadorCliente;
}