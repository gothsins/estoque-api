package com.guiverme.estoque.controller;

import com.guiverme.estoque.dto.ProdutoRequestDTO;
import com.guiverme.estoque.model.Produto;
import com.guiverme.estoque.repository.ProdutoRepository;
import com.guiverme.estoque.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Produto> listarTodos() {
        return service.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@RequestBody @Valid ProdutoRequestDTO dados) {
        Produto novoProduto = new Produto();
        novoProduto.setNome(dados.nome());
        novoProduto.setPreco(dados.preco());
        novoProduto.setQuantidade(BigDecimal.ZERO);

        Produto produtoSalvo = service.salvar(novoProduto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        return service.atualizar(id, produto);
    }
}