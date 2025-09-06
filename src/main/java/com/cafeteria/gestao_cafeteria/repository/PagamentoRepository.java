package com.cafeteria.gestao_cafeteria.repository;

import com.cafeteria.gestao_cafeteria.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    // Novo método para buscar pagamentos dentro de um intervalo de datas
    List<Pagamento> findByDataPagamentoBetween(LocalDateTime inicio, LocalDateTime fim);

    // Método ainda mais otimizado que soma os valores diretamente no banco de dados
    @Query("SELECT SUM(p.valorTotal) FROM Pagamento p WHERE p.dataPagamento BETWEEN :inicio AND :fim")
    BigDecimal sumTotalByDataPagamentoBetween(LocalDateTime inicio, LocalDateTime fim);
}