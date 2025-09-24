package com.cafeteria.gestao_cafeteria.service;

import com.cafeteria.gestao_cafeteria.dto.MesaCreateDTO;
import com.cafeteria.gestao_cafeteria.infra.exceptions.ResourceNotFoundException;
import com.cafeteria.gestao_cafeteria.model.Mesa;
import com.cafeteria.gestao_cafeteria.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    @Transactional
    public Mesa criarMesa(MesaCreateDTO dto) {
        Mesa novaMesa = new Mesa();
        novaMesa.setNumero(dto.getNumero());
        novaMesa.setNome(dto.getNome());
        novaMesa.setCapacidade(dto.getCapacidade());
        novaMesa.setAtivo(true);

        return mesaRepository.save(novaMesa);
    }

    @Transactional
    public Mesa atualizarMesa(Long id, MesaCreateDTO dto) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada com ID: " + id));

        mesa.setNumero(dto.getNumero());
        mesa.setNome(dto.getNome());
        mesa.setCapacidade(dto.getCapacidade());

        return mesaRepository.save(mesa);
    }

    @Transactional
    public void deletarMesa(Long id) {
        if (!mesaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mesa não encontrada com ID: " + id);
        }
        // Adicionar verificação se a mesa tem comandas associadas antes de deletar
        mesaRepository.deleteById(id);
    }

    @Transactional
    public Mesa alterarStatusAtivo(Long id, boolean ativo) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada com ID: " + id));
        mesa.setAtivo(ativo);
        return mesaRepository.save(mesa);
    }
}