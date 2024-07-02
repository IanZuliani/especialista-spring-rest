package com.algaworks.algafood.core.openapi;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {

    /**
     * Metodo que cria uma instancia de docket,
     * docket e uma classe do SpringFox que representa a configuracao da api para gerar definicao usando a expecificacao
     * OpenAPI
     * @return
     */

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.any())//pode colocar tudo o que achar na api
                .build();
    }
}
