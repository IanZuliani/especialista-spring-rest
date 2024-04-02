package com.algaworks.algafood.api.exceptionHandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class Problem {

    private Integer status;//codigo Http da resposta
    private String type;//URI expecifica o tipo do problema
    private String title;//descricao Generica Tipo do problema, mesmo para todos os controllers com mesmo erro
    private String detail;//descricao expecifica sobre a ocorencia do problema gerado

    private String userMenssage;
    private OffsetDateTime timestamp;
    private List<Object> objects;

    @Getter
    @Builder
    public static class Object{

        private String name;
        private String userMessage;
    }

}
