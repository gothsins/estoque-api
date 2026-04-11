package com.guiverme.estoque.service;

import com.guiverme.estoque.infra.RecursoNaoEncontradoException;
import com.guiverme.estoque.infra.RegraDeNegocioException;
import com.guiverme.estoque.model.Produto;
import com.guiverme.estoque.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> listarTodos() {return repository.findAll();}

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

    public Produto atualizar(Long id,  Produto produtoNovo) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com o ID: " + id));
        produto.setNome(produtoNovo.getNome());
        produto.setPreco(produtoNovo.getPreco());
        produto.setQuantidade(produtoNovo.getQuantidade());
        produto.setNome(limparNome(produto.getNome()));
        return repository.save(produto);
    }

    private String limparNome(String texto) {
        if (texto == null) return null;

        String semAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return semAcentos.toLowerCase().trim();
    }
}