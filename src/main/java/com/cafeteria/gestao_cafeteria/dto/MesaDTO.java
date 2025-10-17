package com.cafeteria.gestao_cafeteria.dto;

import lombok.Data;

@Data
public class MesaDTO {
    private Long id;
    private int numero;
    private String nome;
    private int capacidade;
    private boolean ativo;
}