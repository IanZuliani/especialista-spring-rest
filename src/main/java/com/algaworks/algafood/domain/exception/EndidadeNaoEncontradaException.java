package com.algaworks.algafood.domain.exception;

public class EndidadeNaoEncontradaException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public EndidadeNaoEncontradaException(String mensagem){
        super(mensagem);

    }
}
