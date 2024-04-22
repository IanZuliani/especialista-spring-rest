package com.algaworks.algafood.domain.exception;

public class PedidoNaoEncontradoException extends EndidadeNaoEncontradaException{
    private static final long serialVersionUID = 1L;

    public PedidoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public PedidoNaoEncontradoException(Long id){
        this(String.format("Nao existe um Pedido com o codigo %d ", id));
    }
}
