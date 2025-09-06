package com.cafeteria.gestao_cafeteria.dto;

import lombok.Data;

@Data
public class MesaCreateDTO {
    private int numero;
    private String nome;
    private int capacidade;
}