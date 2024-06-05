package com.algaworks.algafood.domain.service;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Map;
import java.util.Set;

public interface EnvioEmailService {

    void enviar(Mensagem mensagem);

    /**
     * Classe que vai representar a mensagem
     */
    @Getter
    @Builder
    class Mensagem{

        /**
         *Set pos nao podemos ter emails repetidos
         * Para nao precisarmos ficar instanciando um tipo set
         * Colocamos a Propriedade com @Singular
         *  @NonNull -> se nao passarmos o assunto ou o corpo um erro sera enviado quando fazemos o builder da aplicacao
         *
         */
        @Singular
        private Set<String> destinatarios;
        @NonNull
        private String assunto;
        @NonNull
        private String corpo;

        @Singular("variavel")
        private Map<String, Object> variaveis;

    }
}
