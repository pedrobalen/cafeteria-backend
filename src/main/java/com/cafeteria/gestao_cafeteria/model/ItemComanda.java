package com.cafeteria.gestao_cafeteria.model;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "itens_comanda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"comanda", "produto", "pagamento"})
@EqualsAndHashCode(exclude = {"comanda", "produto", "pagamento"})
public class ItemComanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relação: MUITOS itens pertencem a UMA comanda.
    // Esta é a chave estrangeira (FK). 'nullable = false' garante que um item não pode existir sem uma comanda.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comanda_id", nullable = false)
    private Comanda comanda;

    // Relação: MUITOS itens podem se referir ao MESMO produto.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    // Relação: MUITOS itens podem estar associados a UM pagamento (na divisão da conta)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagamento_id") // Permite nulo, pois o item é adicionado antes de ser pago
    private Pagamento pagamento;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "preco_unitario_momento", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitarioMomento; // Congela o preço no momento da venda

    @Column(name = "data_adicao", nullable = false)
    private LocalDateTime dataAdicao;
}