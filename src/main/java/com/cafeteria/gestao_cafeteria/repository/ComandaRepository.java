package com.cafeteria.gestao_cafeteria.repository;

import com.cafeteria.gestao_cafeteria.model.Comanda;
import com.cafeteria.gestao_cafeteria.model.StatusComanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComandaRepository extends JpaRepository<Comanda, Long> {
    // Dentro da interface ComandaRepository
    List<Comanda> findByStatus(StatusComanda status);
    @Query("SELECT DISTINCT c FROM Comanda c LEFT JOIN FETCH c.mesa LEFT JOIN FETCH c.itens WHERE c.status = :status")
    List<Comanda> findByStatusWithDetails(@Param("status") StatusComanda status);

    @Query("SELECT c FROM Comanda c LEFT JOIN FETCH c.mesa LEFT JOIN FETCH c.itens i LEFT JOIN FETCH i.produto WHERE c.id = :comandaId")
    Optional<Comanda> findByIdWithDetails(@Param("comandaId") Long comandaId);
}