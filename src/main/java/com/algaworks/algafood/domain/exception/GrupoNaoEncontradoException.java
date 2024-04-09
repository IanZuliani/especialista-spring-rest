package com.algaworks.algafood.domain.exception;

public class GrupoNaoEncontradoException extends EndidadeNaoEncontradaException{
    private static final long serialVersionUID = 1L;
    public GrupoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public GrupoNaoEncontradoException(Long id){
        this(String.format("Nao existe um Grupo com o codigo %d ", id));
    }
}
