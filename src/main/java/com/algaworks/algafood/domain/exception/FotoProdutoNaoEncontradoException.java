package com.algaworks.algafood.domain.exception;

public class FotoProdutoNaoEncontradoException extends EndidadeNaoEncontradaException {
    private static final long serialVersionUID = 1L;
    public FotoProdutoNaoEncontradoException(String mensagem){
        super(mensagem);
    }

    public FotoProdutoNaoEncontradoException(Long restauranteId, Long produtoId){
        this( String.format("Nao existe um Cadastro de foto do Produto com codigo %d para o restaurante de codigo %d ", produtoId, restauranteId));

    }
}
