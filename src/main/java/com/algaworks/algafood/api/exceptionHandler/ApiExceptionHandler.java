package com.algaworks.algafood.api.exceptionHandler;

import com.algaworks.algafood.domain.exception.EndidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioExceptional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(EndidadeNaoEncontradaException.class)
    public ResponseEntity<?> tratarEstadoNaoEncontradoException(EndidadeNaoEncontradaException e){
        var problema = Problema.builder()
                .dataHora(LocalDateTime.now())
                .mensagem(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(problema);
    }

    @ExceptionHandler(NegocioExceptional.class)
    public ResponseEntity<?> tratarNegocioExceptional(NegocioExceptional e){
        var problema = Problema.builder()
                .dataHora(LocalDateTime.now())
                .mensagem(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(problema);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> tratarHttpMediaTypeNotSupportedException(){
        var problema = Problema.builder()
                .dataHora(LocalDateTime.now())
                .mensagem("O tipo de media nao e aceito").build();

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(problema);
    }

}
