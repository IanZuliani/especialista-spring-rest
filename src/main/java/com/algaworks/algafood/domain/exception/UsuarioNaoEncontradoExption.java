package com.algaworks.algafood.domain.exception;

public class UsuarioNaoEncontradoExption extends EndidadeNaoEncontradaException{
    public UsuarioNaoEncontradoExption(String mensagem) {
        super(mensagem);
    }

    public UsuarioNaoEncontradoExption(Long id){
        this(String.format("Nao existe um usuario com o id %d", id));
    }
}
