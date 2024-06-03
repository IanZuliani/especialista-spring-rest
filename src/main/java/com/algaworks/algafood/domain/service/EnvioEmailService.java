package com.algaworks.algafood.domain.service;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

public interface EnvioEmailService {

    void enviar(Mensagem mensagem);

    /**
     * Classe que vai representar a mensagem
     */
    @Getter
    @Builder
    class Mensagem{

        //Set pos nao podemos ter emails repetidos
        private Set<String> destinatario;
        private String assunto;
        private String corpo;

    }
}
