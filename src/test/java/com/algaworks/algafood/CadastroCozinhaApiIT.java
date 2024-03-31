package com.algaworks.algafood;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CadastroCozinhaApiIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/cozinhas";
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
                        .body("", Matchers.hasSize(4))
                        .body("nome", Matchers.hasItems("Indiana", "Tailandesa"));
    }
}
