package com.cafeteria.gestao_cafeteria.service;

import com.cafeteria.gestao_cafeteria.dto.MesaCreateDTO;
import com.cafeteria.gestao_cafeteria.dto.MesaDTO;
import com.cafeteria.gestao_cafeteria.infra.exceptions.ResourceNotFoundException;
import com.cafeteria.gestao_cafeteria.model.Mesa;
import com.cafeteria.gestao_cafeteria.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    @Transactional
    // MUDANÇA: O método agora retorna MesaDTO.
    public MesaDTO criarMesa(MesaCreateDTO dto) {
        Mesa novaMesa = new Mesa();
        novaMesa.setNumero(dto.getNumero());
        novaMesa.setNome(dto.getNome());
        novaMesa.setCapacidade(dto.getCapacidade());
        novaMesa.setAtivo(true);

        Mesa mesaSalva = mesaRepository.save(novaMesa);

        // Converte a entidade salva para DTO antes de retornar.
        return convertToDto(mesaSalva);
    }

    @Transactional
    // MUDANÇA: O método agora retorna MesaDTO.
    public MesaDTO atualizarMesa(Long id, MesaCreateDTO dto) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada com ID: " + id));

        mesa.setNumero(dto.getNumero());
        mesa.setNome(dto.getNome());
        mesa.setCapacidade(dto.getCapacidade());

        Mesa mesaAtualizada = mesaRepository.save(mesa);

        // Converte a entidade atualizada para DTO antes de retornar.
        return convertToDto(mesaAtualizada);
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
    // MUDANÇA: O método agora retorna MesaDTO.
    public MesaDTO alterarStatusAtivo(Long id, boolean ativo) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada com ID: " + id));
        mesa.setAtivo(ativo);
        Mesa mesaSalva = mesaRepository.save(mesa);

        // Converte a entidade salva para DTO antes de retornar.
        return convertToDto(mesaSalva);
    }

    // Este método já estava correto.
    @Transactional(readOnly = true)
    public MesaDTO buscarPorId(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa não encontrada com ID: " + id));
        return convertToDto(mesa);
    }

    // MUDANÇA: Novo método para encapsular a lógica de listagem.
    @Transactional(readOnly = true)
    public List<MesaDTO> listarMesas(Boolean ativo) {
        List<Mesa> mesas;
        if (ativo != null) {
            mesas = mesaRepository.findByAtivo(ativo);
        } else {
            mesas = mesaRepository.findAll();
        }

        // Converte a lista de entidades para uma lista de DTOs.
        return mesas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Este método auxiliar é a chave para evitar repetição de código.
    private MesaDTO convertToDto(Mesa mesa) {
        MesaDTO dto = new MesaDTO();
        dto.setId(mesa.getId());
        dto.setNumero(mesa.getNumero());
        dto.setNome(mesa.getNome());
        dto.setCapacidade(mesa.getCapacidade());
        dto.setAtivo(mesa.isAtivo());
        return dto;
    }
}