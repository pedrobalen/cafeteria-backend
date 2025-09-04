package com.cafeteria.gestao_cafeteria.dto;

import lombok.Getter;
import lombok.Setter;

// DTO para receber os dados para abrir uma comanda
@Getter
@Setter
public class AbrirComandaDTO {
    private Integer numeroMesa;
    private String identificadorCliente;
}