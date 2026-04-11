package com.guiverme.estoque.model;
import jakarta.validation.Valid;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private BigDecimal preco;
    private BigDecimal quantidade;

    public Produto() {}

    public Produto(String nome, BigDecimal preco, BigDecimal quantidade) {
        setNome(nome);
        setPreco(preco);
        setQuantidade(quantidade);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getQuantidade() {return quantidade; }

    public BigDecimal getPreco() {
        return preco;
    }

    public BigDecimal calcularValorTotal() {
        return preco.multiply(quantidade);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }
}