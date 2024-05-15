package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.model.input.FotoProdutoInput;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {

    /**
     *
     * @param restauranteId -> Pega o id do restaurante na uri
     * @param produtoId -> Pega o id do Produto Na URI
     *arquivo -> Pega o arquivo Binario que vem como parametro quando chamamos a funcao
     * MediaType.MULTIPART_FORM_DATA_VALUE -> A funcao fica mapeada apenas para metodos do tipo MULTIPART_FORM_DATA_VALUE
     * @param  fotoProdutoInput -> Esse e um modelo do que recebemos na funcao, poderiamos colocar parametro por parametro
     *                         Mas a medida que cresce a quantidade de parametro enviada, teriamos que adicionar mas nomes
     *                         na funcao
     * Removemos @RequestParam MultipartFile arquivo,  por que ja esta em fotoProdutoInput
     *  e onde era arquivo trocamos para fotoProdutoInput
     *                          e Temos sempre que dar fotoProdutoInput.getArquivo() para pega
     */
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId,
                              FotoProdutoInput fotoProdutoInput){

        /**
         * Vamos transferir o arquivo que fizemos UPLOAD para um novo nome criando um UUID+nome do arquivo
         */
        var nomeArquivo = UUID.randomUUID().toString()
                + "_" + fotoProdutoInput.getArquivo().getOriginalFilename();

        /**
         * Jogar o arquivo para essa pasta que criamos no nosso desktop
         */
        var arquivoFoto = Path.of("/home/ian/Desktop/upload", nomeArquivo);


        System.out.println(fotoProdutoInput.getDescricao());
        System.out.println(arquivoFoto);
        /**
         * Vai nos retornar qual o contentType do nosso aequivo
         */
        System.out.println(fotoProdutoInput.getArquivo().getContentType());


        try {
            /**
             * Transferindo o arquivo com o metodo transferTo
             */
            fotoProdutoInput.getArquivo().transferTo(arquivoFoto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
