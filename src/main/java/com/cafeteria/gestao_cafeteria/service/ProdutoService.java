package com.cafeteria.gestao_cafeteria.service;

import com.cafeteria.gestao_cafeteria.dto.ProdutoCreateDTO;
import com.cafeteria.gestao_cafeteria.dto.ProdutoDTO;
import com.cafeteria.gestao_cafeteria.infra.exceptions.ResourceNotFoundException;
import com.cafeteria.gestao_cafeteria.model.Categoria;
import com.cafeteria.gestao_cafeteria.model.Produto;
import com.cafeteria.gestao_cafeteria.repository.CategoriaRepository;
import com.cafeteria.gestao_cafeteria.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional
    public Produto criarProduto(ProdutoCreateDTO dto) {
        Categoria categoria = null;
        if (dto.getCategoriaId() != null) {
            categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + dto.getCategoriaId()));
        }

        Produto novoProduto = new Produto();
        novoProduto.setNome(dto.getNome());
        novoProduto.setDescricao(dto.getDescricao());
        novoProduto.setPrecoVenda(dto.getPrecoVenda());
        novoProduto.setAtivo(dto.isAtivo());
        novoProduto.setCategoria(categoria);

        return produtoRepository.save(novoProduto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ProdutoDTO convertToDto(Produto produto) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setPrecoVenda(produto.getPrecoVenda());
        dto.setAtivo(produto.isAtivo());
        if (produto.getCategoria() != null) {
            dto.setNomeCategoria(produto.getCategoria().getNome());
        }
        return dto;
    }
}