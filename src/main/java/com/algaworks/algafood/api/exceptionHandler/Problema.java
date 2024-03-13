package com.algaworks.algafood.api.exceptionHandler;

import lombok.Getter;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@Builder
public class Problema {

    private LocalDateTime dataHora;
    private String mensagem;

}
