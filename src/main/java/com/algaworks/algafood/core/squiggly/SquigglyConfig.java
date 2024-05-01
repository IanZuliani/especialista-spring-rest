package com.algaworks.algafood.core.squiggly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.web.RequestSquigglyContextProvider;
import com.github.bohnman.squiggly.web.SquigglyRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SquigglyConfig {

    @Bean
    public FilterRegistrationBean<SquigglyRequestFilter> squigglyRequestFilter(ObjectMapper objectMapper){
        Squiggly.init(objectMapper, new RequestSquigglyContextProvider("campos", null));

        var urlPatterns = Arrays.asList("/pedidos/*", "/restaurantes/*");
        //criando uma instancia de FilterRegistrationBean
        var filterRegistration = new FilterRegistrationBean<SquigglyRequestFilter>();
        //atribuindo uma instancia do filtro que queremos
        filterRegistration.setFilter(new SquigglyRequestFilter());
        //colocamos a ordem que queremos
        filterRegistration.setOrder(1);
        filterRegistration.setUrlPatterns(urlPatterns);


        //retornando a instancia de filterRegistration
        return filterRegistration;
    }
}
