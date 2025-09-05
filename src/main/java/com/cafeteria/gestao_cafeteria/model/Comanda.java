package com.cafeteria.gestao_cafeteria.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comandas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_mesa")
    private Integer numeroMesa;

    @Column(name = "identificador_cliente", length = 100)
    private String identificadorCliente;

    @Enumerated(EnumType.STRING) // Grava o nome do Enum ('ABERTA') no banco, mais legível
    @Column(nullable = false)
    private StatusComanda status;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "data_fechamento")
    private LocalDateTime dataFechamento;

    // Relação: Uma comanda tem MUITOS itens.
    // O 'mappedBy' indica que a entidade 'ItemComanda' é a dona do relacionamento.
    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemComanda> itens;
}