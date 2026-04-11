package com.guiverme.estoque.infra;

public class RecursoNaoEncontradoException extends RuntimeException {

    public  RecursoNaoEncontradoException (String mensagem) {
        super(mensagem);
    }
}
