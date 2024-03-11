package com.algaworks.algafood.domain.exception;

public class CozinhaNaoEncontradoException extends EndidadeNaoEncontradaException {
    private static final long serialVersionUID = 1L;
    public CozinhaNaoEncontradoException(String mensagem){
        super(mensagem);
    }

    public CozinhaNaoEncontradoException(Long id){
        this( String.format("Nao existe um cadastro de Cozinha com o codigo %d ", id));

    }
}
