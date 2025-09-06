package com.cafeteria.gestao_cafeteria.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentos_insumo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "insumo")
@EqualsAndHashCode(exclude = "insumo")
public class MovimentoInsumo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insumo_id", nullable = false)
    private Insumo insumo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentoInsumo tipo;

    @Column(nullable = false)
    private BigDecimal quantidade;

    private String motivo;

    @Column(nullable = false)
    private LocalDateTime dataMovimento;
}