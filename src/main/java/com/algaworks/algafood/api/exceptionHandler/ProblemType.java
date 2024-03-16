package com.algaworks.algafood.api.exceptionHandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensivel"),
    ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade nao Encontrad"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
    ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio");

    private String title;
    private String uri;

    ProblemType(String path, String title){
        this.uri = "htts.algafoof.com.br" + path;
        this.title = title;
    }
}
