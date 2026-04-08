package com.guiverme.estoque.service;

import com.guiverme.estoque.model.Produto;
import com.guiverme.estoque.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public Produto salvar(Produto produto) {
        return repository.save(produto);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public Produto atualizar(Long id,  Produto produtoNovo) {
        Produto produto = repository.findById(id).orElseThrow();
        produto.setNome(produtoNovo.getNome());
        produto.setPreco(produtoNovo.getPreco());
        produto.setQuantidade(produtoNovo.getQuantidade());
        return repository.save(produto);
    }
}