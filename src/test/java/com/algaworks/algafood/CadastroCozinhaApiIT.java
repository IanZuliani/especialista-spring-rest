package com.algaworks.algafood;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-teste.properties")
public class CadastroCozinhaApiIT {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private CozinhaRepository repository;

    @BeforeEach
    public void setup(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/cozinhas";

        databaseCleaner.clearTables();
        prepararDados();
    }

    @Test
    public void deveRetornarStatus200_QuandoConsultarCozinha(){

        RestAssured.given()
                .basePath("/cozinhas")
                .accept(ContentType.JSON)
                .when()
                    .get()
                .then()
                    .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void deveConter4Cozinhas_QuandoConsultarCozinha(){

        RestAssured.given()
                        .basePath("/cozinhas")
                        .accept(ContentType.JSON)
                    .when()
                        .get()
                    .then()
                        .body("", Matchers.hasSize(2))
                        .body("nome", Matchers.hasItems("Americana", "Tailandesa"));
    }

    @Test
    public void deveRetornarStatus201_QuandoCadastrarCozinha(){

       RestAssured.given()
                       .body("{ \"nome\": \"chinesa\" }")
                       .contentType(ContentType.JSON)
                       .accept(ContentType.JSON)
                   .when()
                        .post()
                   .then()
                        .statusCode(HttpStatus.CREATED.value());
    }

    private void prepararDados(){
        var cozinha1 = new Cozinha();
        cozinha1.setNome("Tailandesa");
        repository.save(cozinha1);

        var cozinha2 = new Cozinha();
        cozinha2.setNome("Americana");
        repository.save(cozinha2);
    }
}
