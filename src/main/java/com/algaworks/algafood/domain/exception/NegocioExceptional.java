package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NegocioExceptional extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public NegocioExceptional(String mensagem){
        super(mensagem);
    }

    public NegocioExceptional(String mensage, Throwable causa){
        super(mensage, causa);
    }
}
