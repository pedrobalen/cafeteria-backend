package com.cafeteria.gestao_cafeteria.dto;

import lombok.Data;

@Data
public class ItemComandaDTO {
    private Long id;
    private String nomeProduto;
    private int quantidade;
    private Long comandaId; // Only include the ID, not the whole object

    // Constructors, Getters, Setters
}