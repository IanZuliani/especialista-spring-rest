package com.algaworks.algafood.domain.exception;

public class CidadeNaoEncontradoException extends EndidadeNaoEncontradaException {
    private static final long serialVersionUID = 1L;
    public CidadeNaoEncontradoException(String mensagem){
        super(mensagem);
    }

    public CidadeNaoEncontradoException(Long id){
        this( String.format("Nao existe um cadastro de Cidade com o codigo %d ", id));

    }
}
