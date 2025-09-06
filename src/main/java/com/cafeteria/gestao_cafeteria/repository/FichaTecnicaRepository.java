package com.cafeteria.gestao_cafeteria.repository;

import com.cafeteria.gestao_cafeteria.model.FichaTecnica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FichaTecnicaRepository extends JpaRepository<FichaTecnica, Long> {

    // Método para encontrar todos os insumos de um determinado produto
    List<FichaTecnica> findByProdutoId(Long produtoId);
}