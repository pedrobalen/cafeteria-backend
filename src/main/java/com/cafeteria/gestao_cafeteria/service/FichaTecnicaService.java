package com.cafeteria.gestao_cafeteria.service;

import com.cafeteria.gestao_cafeteria.dto.FichaTecnicaItemDTO;
import com.cafeteria.gestao_cafeteria.dto.FichaTecnicaResponseDTO;
import com.cafeteria.gestao_cafeteria.infra.exceptions.ResourceNotFoundException;
import com.cafeteria.gestao_cafeteria.model.FichaTecnica;
import com.cafeteria.gestao_cafeteria.model.Insumo;
import com.cafeteria.gestao_cafeteria.model.Produto;
import com.cafeteria.gestao_cafeteria.repository.FichaTecnicaRepository;
import com.cafeteria.gestao_cafeteria.repository.InsumoRepository;
import com.cafeteria.gestao_cafeteria.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FichaTecnicaService {

    @Autowired
    private FichaTecnicaRepository fichaTecnicaRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private InsumoRepository insumoRepository;

    @Transactional
    public FichaTecnica adicionarItem(Long produtoId, FichaTecnicaItemDTO itemDTO) {
        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + produtoId));
        
        Insumo insumo = insumoRepository.findById(itemDTO.getInsumoId())
            .orElseThrow(() -> new ResourceNotFoundException("Insumo não encontrado com ID: " + itemDTO.getInsumoId()));

        FichaTecnica novoItem = new FichaTecnica();
        novoItem.setProduto(produto);
        novoItem.setInsumo(insumo);
        novoItem.setQuantidade(itemDTO.getQuantidade());

        return fichaTecnicaRepository.save(novoItem);
    }

    @Transactional(readOnly = true)
    public List<FichaTecnicaResponseDTO> getFichaTecnicaDoProduto(Long produtoId) {
        List<FichaTecnica> itens = fichaTecnicaRepository.findByProdutoId(produtoId);
        
        return itens.stream().map(item -> {
            FichaTecnicaResponseDTO dto = new FichaTecnicaResponseDTO();
            dto.setFichaTecnicaId(item.getId());
            dto.setInsumoId(item.getInsumo().getId());
            dto.setNomeInsumo(item.getInsumo().getNome());
            dto.setQuantidade(item.getQuantidade());
            dto.setUnidadeMedida(item.getInsumo().getUnidadeMedida());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void removerItem(Long fichaTecnicaId) {
        if (!fichaTecnicaRepository.existsById(fichaTecnicaId)) {
            throw new ResourceNotFoundException("Item da ficha técnica não encontrado com ID: " + fichaTecnicaId);
        }
        fichaTecnicaRepository.deleteById(fichaTecnicaId);
    }
}