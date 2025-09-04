package com.cafeteria.gestao_cafeteria.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddItemDTO {
    private Long produtoId;
    private int quantidade;
}