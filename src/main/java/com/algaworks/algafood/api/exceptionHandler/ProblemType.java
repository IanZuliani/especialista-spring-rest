package com.algaworks.algafood.api.exceptionHandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade nao Encontrad");

    private String title;
    private String uri;

    ProblemType(String path, String title){
        this.uri = "htts.algafoof.com.br" + path;
        this.title = title;
    }
}
