package com.guiverme.estoque.controller;

import com.guiverme.estoque.dto.ErroDeValidacaoDTO;
import com.guiverme.estoque.infra.RecursoNaoEncontradoException;
import com.guiverme.estoque.infra.RegraDeNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErroDeValidacaoDTO>> tratarErroValidacao(MethodArgumentNotValidException ex) {

        List<FieldError> errosDoSpring = ex.getFieldErrors();

        List<ErroDeValidacaoDTO> nossosErros = errosDoSpring.stream()
                .map(erro -> new ErroDeValidacaoDTO(erro.getField(), erro.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest().body(nossosErros);
    }
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<String> tratarErro404(RecursoNaoEncontradoException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<String> tratarRegraDeNegocio(RegraDeNegocioException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
