package com.cafeteria.gestao_cafeteria.service;

import com.cafeteria.gestao_cafeteria.dto.MesaCreateDTO;
import com.cafeteria.gestao_cafeteria.model.Mesa;
import com.cafeteria.gestao_cafeteria.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    public Mesa criarMesa(MesaCreateDTO dto) {
        Mesa novaMesa = new Mesa();
        novaMesa.setNumero(dto.getNumero());
        novaMesa.setNome(dto.getNome());
        novaMesa.setCapacidade(dto.getCapacidade());
        novaMesa.setAtivo(true);

        return mesaRepository.save(novaMesa);
    }
}