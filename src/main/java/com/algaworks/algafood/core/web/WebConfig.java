package com.algaworks.algafood.core.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classe de configuracao que abilita o CORS da APLICACAO para podermos fazer chamada
 * Web de varios servicos diferente, sem que o navegador bloqueie a chamada
 *
 * WebMvcConfigurer -> define metodos de callback para podermos customizar springMVC
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        /**
         *  para habilitar o CORS AGORA globalmente, chamamos o regystry
         *  passando a funcao addMapping(), passando um padrao de URI de onde queremos abiltiar o CORS
         *  registry.addMapping("/admin/**"); → tudo depos de admin
         *  11. `.allowedOrigins("*");` → nesse caso podemos definir a origem, mas por padra ja e habilitad.
         * 12. Podemos tambem dizer qual site pode buscar requisicoes da nossa aplicacao com o comando
         * allowedMethods → Definir os metodos, os habilitados por padrao sao GET, HEAD, “POST”, se quisermos todos os metodos colocamo *
         */
        registry.addMapping("/**")
                .allowedOrigins("*")//ja e por padrao todas as origens mas podemos definir qual site faz a busca
                .allowedMethods("*");//Liberando os metodos
                //.maxAge(30);
    }
}
