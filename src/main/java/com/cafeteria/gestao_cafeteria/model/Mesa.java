package com.cafeteria.gestao_cafeteria.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mesas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private int numero;

    private String nome;
    
    private int capacidade;
    
    @Column(nullable = false)
    private boolean ativo = true;
}