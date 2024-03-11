package com.algaworks.algafood.domain.exception;

public class RestauranteNaoEncontradoException extends EndidadeNaoEncontradaException {
    private static final long serialVersionUID = 1L;
    public RestauranteNaoEncontradoException(String mensagem){
        super(mensagem);
    }

    public RestauranteNaoEncontradoException(Long id){
        this( String.format("Nao existe um cadastro de Restaurante com o codigo %d ", id));

    }
}
