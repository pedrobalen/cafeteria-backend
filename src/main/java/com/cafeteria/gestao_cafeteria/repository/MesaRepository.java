package com.cafeteria.gestao_cafeteria.repository;

import com.cafeteria.gestao_cafeteria.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
    List<Mesa> findByAtivo(boolean ativo);
}