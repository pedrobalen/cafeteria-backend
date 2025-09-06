package com.cafeteria.gestao_cafeteria.service;

import com.cafeteria.gestao_cafeteria.dto.GastoCreateDTO;
import com.cafeteria.gestao_cafeteria.model.Gasto;
import com.cafeteria.gestao_cafeteria.repository.GastoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GastoService {

    @Autowired
    private GastoRepository gastoRepository;

    public Gasto criarGasto(GastoCreateDTO dto) {
        Gasto novoGasto = new Gasto();
        novoGasto.setDescricao(dto.getDescricao());
        novoGasto.setValor(dto.getValor());
        novoGasto.setDataGasto(dto.getDataGasto());
        novoGasto.setCategoria(dto.getCategoria());

        return gastoRepository.save(novoGasto);
    }

    public List<Gasto> listarTodos() {
        return gastoRepository.findAll();
    }
}