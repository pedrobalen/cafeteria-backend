package com.cafeteria.gestao_cafeteria.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentos_estoque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "estoque")
@EqualsAndHashCode(exclude = "estoque")
public class MovimentoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_id", nullable = false)
    private Estoque estoque;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentoEstoque tipo;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal quantidade;

    @Column
    private String motivo;

    @Column(name = "data_movimento", nullable = false)
    private LocalDateTime dataMovimento;
}