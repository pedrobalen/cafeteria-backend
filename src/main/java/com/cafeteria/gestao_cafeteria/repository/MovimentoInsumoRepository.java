package com.cafeteria.gestao_cafeteria.repository;

import com.cafeteria.gestao_cafeteria.model.MovimentoInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimentoInsumoRepository extends JpaRepository<MovimentoInsumo, Long> {
}