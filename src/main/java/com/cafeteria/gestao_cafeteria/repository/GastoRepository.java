package com.cafeteria.gestao_cafeteria.repository;

import com.cafeteria.gestao_cafeteria.model.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {
}