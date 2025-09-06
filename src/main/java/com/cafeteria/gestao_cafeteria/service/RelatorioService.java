package com.cafeteria.gestao_cafeteria.service;

import com.cafeteria.gestao_cafeteria.dto.VendasDiaDTO;
import com.cafeteria.gestao_cafeteria.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class RelatorioService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    public VendasDiaDTO calcularVendasDoDia(LocalDate data) {
        // Define o início e o fim do dia para a consulta
        LocalDateTime inicioDoDia = data.atStartOfDay(); // Ex: 2025-09-06 00:00:00
        LocalDateTime fimDoDia = data.atTime(LocalTime.MAX);    // Ex: 2025-09-06 23:59:59.999...

        // Usa o método otimizado do repositório para somar os valores
        BigDecimal totalVendas = pagamentoRepository.sumTotalByDataPagamentoBetween(inicioDoDia, fimDoDia);
        
        // Se não houver vendas, o resultado da soma pode ser null
        if (totalVendas == null) {
            totalVendas = BigDecimal.ZERO;
        }

        // Para contar a quantidade de vendas, usamos o outro método
        int quantidadeVendas = pagamentoRepository.findByDataPagamentoBetween(inicioDoDia, fimDoDia).size();

        VendasDiaDTO relatorio = new VendasDiaDTO();
        relatorio.setData(data);
        relatorio.setTotalVendas(totalVendas);
        relatorio.setQuantidadeVendas(quantidadeVendas);
        
        return relatorio;
    }
}