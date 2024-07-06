package com.algaworks.algafood.api;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

/**
 * Classe utilizaria para setar o Location cada vez que criarmos um recurso novo
 */
@UtilityClass
public class ResourceUriHelper {

    public static void addUriInResponseHeader(Object resourceId){
        /**
         * 3. Vamos utilizar a classe utilitaria `ServletUriComponentsBuilder`
         * 4. Ajuda a gente a criar uma uri utilizando as informacoes da requisicao Atual
         * 5. Pega o Protocolo Http ou Https
         * 6. Pega qual o dominio
         * 7. Pega qual a porta
         *
         */
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")//a partir da uri Local http://api.algafood.local:8080/cidades adiciona id
                .buildAndExpand(resourceId)//vamos adicionar o id vindo da resposta
                .toUri();//retorna uma instancia do tipo URI
        /**
         * Vamos utilizar servelet, utilizando a API de HttpServletResponse , para trabalha com respostas Http
         * Podemos pegar na assinatura do metodo, mas nao vamos sujar mas o codigo, vamos pegar de uma funcao do spring
         */
        HttpServletResponse response = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes()))
                .getResponse();

        response.setHeader(HttpHeaders.LOCATION, uri.toString());
    }
}
