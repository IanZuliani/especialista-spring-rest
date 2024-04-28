package com.algaworks.algafood.domain.exception;

public class PedidoNaoEncontradoException extends EndidadeNaoEncontradaException{
    private static final long serialVersionUID = 1L;


    public PedidoNaoEncontradoException(String codigoPedido){
        super(String.format("Nao existe um Pedido com o codigo %s ", codigoPedido));
    }
}
