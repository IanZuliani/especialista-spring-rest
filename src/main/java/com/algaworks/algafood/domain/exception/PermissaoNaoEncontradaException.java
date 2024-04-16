package com.algaworks.algafood.domain.exception;

public class PermissaoNaoEncontradaException extends EndidadeNaoEncontradaException{
    public PermissaoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
    public PermissaoNaoEncontradaException(Long id){
        this( String.format("Nao existe um cadastro de Permissao com o codigo %d ", id));

    }
}
