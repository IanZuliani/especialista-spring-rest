package com.algaworks.algafood.domain.exception;

//public class EndidadeNaoEncontradaException extends ResponseStatusException {
//@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entidade nao encontrada")
public abstract class EndidadeNaoEncontradaException extends NegocioException {
    private static final long serialVersionUID = 1L;

//    public EndidadeNaoEncontradaException(HttpStatus status, String reason){
//        super(status, reason);
//    }

    public EndidadeNaoEncontradaException(String mensagem){
        super(mensagem);
    }
}
