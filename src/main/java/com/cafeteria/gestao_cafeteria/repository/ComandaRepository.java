package com.cafeteria.gestao_cafeteria.repository;

import com.cafeteria.gestao_cafeteria.model.Comanda;
import com.cafeteria.gestao_cafeteria.model.StatusComanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComandaRepository extends JpaRepository<Comanda, Long> {
    // Dentro da interface ComandaRepository
    List<Comanda> findByStatus(StatusComanda status);
}