package com.guiverme.estoque.service;

import com.guiverme.estoque.infra.RecursoNaoEncontradoException;
import com.guiverme.estoque.infra.RegraDeNegocioException;
import com.guiverme.estoque.model.Produto;
import com.guiverme.estoque.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.Normalizer;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Page<Produto> listarTodos (Pageable paginacao) {
        return repository.findAll(paginacao);
    }

    public Produto salvar(Produto produto) {
        String nomeLimpo = limparNome(produto.getNome());

        var produtoExistente = repository.findByNomeIgnoreCase(nomeLimpo);

        if (produtoExistente.isPresent()) {
            throw new RegraDeNegocioException("Já existe um produto cadastrado com o nome: " + nomeLimpo);
        }
        produto.setNome(nomeLimpo);

        return repository.save(produto);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public Produto atualizar(Long id, Produto produtoNovo) {

        Produto produtoAtual = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com o ID: " + id));

        String nomeLimpo = limparNome(produtoNovo.getNome());

        var produtoExistente = repository.findByNomeIgnoreCase(nomeLimpo);

        if (produtoExistente.isPresent() && !produtoExistente.get().getId().equals(id)) {
            throw new RegraDeNegocioException("Já existe OUTRO produto cadastrado com o nome: " + nomeLimpo);
        }

        produtoAtual.setNome(nomeLimpo);
        produtoAtual.setPreco(produtoNovo.getPreco());

        return repository.save(produtoAtual);
    }

    private String limparNome(String texto) {
        if (texto == null) return null;

        String semAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return semAcentos.toLowerCase().trim();
    }
}