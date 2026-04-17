package com.guiverme.estoque.controller;

import com.guiverme.estoque.dto.ProdutoRequestDTO;
import com.guiverme.estoque.dto.ProdutoResponseDTO;
import com.guiverme.estoque.model.Produto;
import com.guiverme.estoque.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDTO>> listarTodos(
            @PageableDefault(size = 10, page = 0, sort = "nome") Pageable paginacao) {

        Page<Produto> paginaDeProdutos = service.listarTodos(paginacao);

        Page<ProdutoResponseDTO> paginaConvertida = paginaDeProdutos
                .map(produto -> new ProdutoResponseDTO(produto.getId(), produto.getNome(), produto.getPreco()));

        return ResponseEntity.ok(paginaConvertida);
    }

    // 2. POST - Criar Novo
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@RequestBody @Valid ProdutoRequestDTO dados) {
        Produto produtoSujo = new Produto();
        produtoSujo.setNome(dados.nome());
        produtoSujo.setPreco(dados.preco());
        produtoSujo.setQuantidade(BigDecimal.ZERO);

        Produto produtoSalvo = service.salvar(produtoSujo); // Chama o SALVAR

        ProdutoResponseDTO dtoDeSaida = new ProdutoResponseDTO(
                produtoSalvo.getId(),
                produtoSalvo.getNome(),
                produtoSalvo.getPreco()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(dtoDeSaida);
    }

    // 3. PUT - Atualizar Existente
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoRequestDTO dados) {
        Produto produtoSujo = new Produto();
        produtoSujo.setNome(dados.nome());
        produtoSujo.setPreco(dados.preco());

        Produto produtoAtualizado = service.atualizar(id, produtoSujo); // Chama o ATUALIZAR

        ProdutoResponseDTO dtoDeSaida = new ProdutoResponseDTO(
                produtoAtualizado.getId(),
                produtoAtualizado.getNome(),
                produtoAtualizado.getPreco()
        );

        return ResponseEntity.ok(dtoDeSaida);
    }

    // 4. DELETE - Apagar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}