package com.algaworks.algafood.domain.exception;

public class EntidadeNaoEncontradaException extends NegocioExceptional {
    private static final long serialVersionUID = 1L;

    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
