package com.algaworks.algafood;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CadastroCozinhaIntegrationTests {

    @Autowired
    private CadastroCozinhaService service;

    @Test
    public void testarCadastroCozinhaComSucesso(){
        //cenario
        var novaCozinha = new Cozinha();
        novaCozinha.setNome("Cozinha Chinesa");

        //acao
        novaCozinha = service.salvar(novaCozinha);

        //validacao
        assertThat(novaCozinha).isNotNull();
        assertThat(novaCozinha.getId()).isNotNull();

    }

    @Test
    public void testarCadastroCozinhaSemNome(){
        var cozinha = new Cozinha();
        cozinha.setNome(null);

        ConstraintViolationException erroEsperado = Assertions.assertThrows(ConstraintViolationException.class,
                ()->{
                    service.salvar(cozinha);
                });
        assertThat(erroEsperado).isNotNull();


    }
}
