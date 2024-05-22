package com.algaworks.algafood.domain.service;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FotoStorageService {

    /**
     * Para armazenar a foto precisamos dos dados do arquivo sendo passados na nosssa interface
     * Poderiamos passar `void armazenar(MultipartFile arquivo);`
     * Mas por se tratar de uma classe da Web `web.multipart.MultipartFile` , na e legal termos dentro da nossa classe de dominio
     */
    void armazenar(NovaFoto novaFoto);


    /**
     * Classe interna para informarmos o que precisamos para cadastrar uma foto
     *  @Builder fica mas facil de construirmos um objeto utilizando o padrao builder
     */
    @Getter
    @Builder
    class NovaFoto{

        private String nomeArquivo;

        /**
         * E o Fluxo de entrada do arquivo
         * Se entrarmos no controller onde pegamos o arquivo e dermos um
         * arquivo.getInputStream() e o fluxo de leitura que podemos ler o arquivo que acabamos de fazer o upload
         * a partir dele podemos salvar a foto.
         */
        private InputStream inputStream;
    }
}
