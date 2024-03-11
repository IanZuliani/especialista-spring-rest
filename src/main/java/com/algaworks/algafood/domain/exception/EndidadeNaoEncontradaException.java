package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

//public class EndidadeNaoEncontradaException extends ResponseStatusException {
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entidade nao encontrada")
public abstract class EndidadeNaoEncontradaException extends NegocioExceptional {
    private static final long serialVersionUID = 1L;

//    public EndidadeNaoEncontradaException(HttpStatus status, String reason){
//        super(status, reason);
//    }

    public EndidadeNaoEncontradaException(String mensagem){
        super(mensagem);
    }
}
