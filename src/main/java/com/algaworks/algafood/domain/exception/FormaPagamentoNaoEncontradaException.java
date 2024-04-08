package com.algaworks.algafood.domain.exception;

public class FormaPagamentoNaoEncontradaException extends EndidadeNaoEncontradaException{

    private static final long serialVersionUID = 1L;
    public FormaPagamentoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public FormaPagamentoNaoEncontradaException(Long id){
       this( String.format("Nao existe Forma de pagamento com o codigo %d ", id));
    }
}
