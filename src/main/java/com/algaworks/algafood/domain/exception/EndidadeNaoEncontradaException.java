package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entidade nao encontrada")
public class EndidadeNaoEncontradaException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public EndidadeNaoEncontradaException(String mensagem){
        super(mensagem);

    }
}
