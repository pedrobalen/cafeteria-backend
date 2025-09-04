package com.cafeteria.gestao_cafeteria.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categorias_produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @Column
    private String descricao;
}