package com.cafeteria.gestao_cafeteria.model;

public enum TipoMovimentoEstoque {
    ENTRADA, // Compra de fornecedor, produção interna
    SAIDA,   // Venda de produto
    AJUSTE   // Perda, quebra, correção de contagem
}