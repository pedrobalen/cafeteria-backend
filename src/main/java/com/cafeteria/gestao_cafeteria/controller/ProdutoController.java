package com.cafeteria.gestao_cafeteria.controller;

import com.cafeteria.gestao_cafeteria.dto.ProdutoCreateDTO;
import com.cafeteria.gestao_cafeteria.dto.ProdutoDTO;
import com.cafeteria.gestao_cafeteria.model.Produto;
import com.cafeteria.gestao_cafeteria.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService; // Injeta o Service, não o Repository

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@RequestBody ProdutoCreateDTO dto) {
        Produto novoProduto = produtoService.criarProduto(dto);
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarProdutos() {
        List<ProdutoDTO> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }
}