package com.cafeteria.gestao_cafeteria.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "estoque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "produto")
@EqualsAndHashCode(exclude = "produto")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relação um-para-um: Cada produto tem uma única entrada de estoque.
    @OneToOne
    @JoinColumn(name = "produto_id", nullable = false, unique = true)
    private Produto produto;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal quantidade;

    @Column(name = "unidade_medida", nullable = false, length = 20)
    private String unidadeMedida; // ex: 'unidade', 'kg', 'litro'

    @Column(name = "estoque_minimo", precision = 10, scale = 3)
    private BigDecimal estoqueMinimo;
}