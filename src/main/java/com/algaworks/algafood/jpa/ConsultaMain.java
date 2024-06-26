package com.algaworks.algafood.jpa;

import com.algaworks.algafood.AlgafoodApiApplication;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class ConsultaMain {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        //Consultar Cozinha
        CozinhaRepository cozinhaRepository = applicationContext.getBean(CozinhaRepository.class);

//        List<Cozinha> cozinhas = cozinhaRepository.listar();
//
//        for (Cozinha cozinha : cozinhas) {
//            System.out.println(cozinha.getNome());
//
//        }

    }
}
