package com.algaworks.algafood;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradoException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CadastroCozinhaIT {

    @Autowired
    private CadastroCozinhaService service;

    @Test
    public void deveAtribuirId_QuandoCadastrarCozinhaComDadosCorretos() {
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
    public void deveFalhar_QuandoCadastrarCozinhaSemNome() {
        var cozinha = new Cozinha();
        cozinha.setNome(null);

        ConstraintViolationException erroEsperado = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            service.salvar(cozinha);
        });
        assertThat(erroEsperado).isNotNull();
    }

    @Test
    public void deveFalhar_QuandoExcluirCozinhaEmUso() {
        EntidadeEmUsoException erroEsperado = Assertions.assertThrows(EntidadeEmUsoException.class, () -> {
            service.excluir(1L);
        });
        assertThat(erroEsperado).isNotNull();
    }

    @Test
    public void deveFalhar_QuandoExcluirCozinhaInexistente() {

        CozinhaNaoEncontradoException erroEsperado = Assertions.assertThrows(CozinhaNaoEncontradoException.class, () -> {
            service.excluir(100L);
        });
        assertThat(erroEsperado).isNotNull();
    }
}
