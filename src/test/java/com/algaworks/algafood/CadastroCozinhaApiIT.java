package com.algaworks.algafood;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.flywaydb.core.Flyway;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-teste.properties")
public class CadastroCozinhaApiIT {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private CozinhaRepository repository;

    private int quantidadeCozinhasCadastradas;
    private static final int COZINHA_ID_INEXISTENTE = 100;
    private Cozinha cozinhaAmericana;
    private String jsonCorretoCozinhaChinesa;
    @BeforeEach
    public void setup(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/cozinhas";

        databaseCleaner.clearTables();
        prepararDados();
        jsonCorretoCozinhaChinesa = ResourceUtils.getContentFromResource(
                "/json/correto/cozinha-chinesa.json");
    }

    @Test
    public void deveRetornarStatus200_QuandoConsultarCozinha(){

        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                    .get()
                .then()
                    .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void deveRetornarQuantidadeCorretaDeCozinhas_QuandoConsultarCozinhas(){

        RestAssured.given()
                        .accept(ContentType.JSON)
                    .when()
                        .get()
                    .then()
                        .body("", Matchers.hasSize(quantidadeCozinhasCadastradas));
                        //.body("nome", Matchers.hasItems("Americana", "Tailandesa"));
    }

    @Test
    public void deveRetornarStatus201_QuandoCadastrarCozinha(){

       RestAssured.given()
                       .body(jsonCorretoCozinhaChinesa)
                       .contentType(ContentType.JSON)
                       .accept(ContentType.JSON)
                   .when()
                        .post()
                   .then()
                        .statusCode(HttpStatus.CREATED.value());
    }

    //GET /cozinhas/1
    @Test
    public void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente(){
        RestAssured.given()
                    .pathParam("id", cozinhaAmericana.getId())
                        .accept(ContentType.JSON)
                    .when()
                        .get("/{id}")
                    .then()
                        .statusCode(HttpStatus.OK.value())
                        .body("nome", equalTo(cozinhaAmericana.getNome()));
    }

    @Test
    public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente(){
        RestAssured.given()
                .pathParam("id", COZINHA_ID_INEXISTENTE)
                .accept(ContentType.JSON)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }


    private void prepararDados(){
        var cozinha1 = new Cozinha();
        cozinha1.setNome("Tailandesa");
        repository.save(cozinha1);


        cozinhaAmericana = new Cozinha();
        cozinhaAmericana.setNome("Americana");
        repository.save(cozinhaAmericana);

        quantidadeCozinhasCadastradas = (int) repository.count();
    }
}
