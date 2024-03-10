package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class EstadoNaoEncontradoException extends EndidadeNaoEncontradaException {
    private static final long serialVersionUID = 1L;
    public EstadoNaoEncontradoException(String mensagem){
        super(mensagem);
    }

    public EstadoNaoEncontradoException(Long id){
        this( String.format("Nao existe um cadastro de Estado com o codigo %d ", id));

    }
}
