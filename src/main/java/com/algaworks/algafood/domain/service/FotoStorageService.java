package com.algaworks.algafood.domain.service;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

public interface FotoStorageService {


    FotoRecuperada recuperar(String momeArquivo);

    /**
     * Para armazenar a foto precisamos dos dados do arquivo sendo passados na nosssa interface
     * Poderiamos passar `void armazenar(MultipartFile arquivo);`
     * Mas por se tratar de uma classe da Web `web.multipart.MultipartFile` , na e legal termos dentro da nossa classe de dominio
     */
    void armazenar(NovaFoto novaFoto);

    void remover(String nomeArquivo);

    /**
     * criamos uma implementacao na interface para ficar mas fluido a leitura do codigo
     * @param nomeArquivoAntigo
     * @param novaFoto
     */
    default void substituir(String nomeArquivoAntigo, NovaFoto novaFoto){
        this.armazenar(novaFoto);
        if(nomeArquivoAntigo != null){
            this.remover(nomeArquivoAntigo);
        }
    }

    /**
     *
     * @param nomeOriginal
     * @return
     * Metodo que fizemos para alterar o nome da imagem quando enviarmos para a base de dados.
     * Para que quando o cliente envie a informacao, caso haja um arquivo com mesmo nome, nao sobreescreva
     */
    default String gerarNomeArquivo(String nomeOriginal){
        return UUID.randomUUID().toString() + "_" + nomeOriginal;
    }


    /**
     * Classe interna para informarmos o que precisamos para cadastrar uma foto
     *  @Builder fica mas facil de construirmos um objeto utilizando o padrao builder
     */
    @Getter
    @Builder
    class NovaFoto{

        private String nomeArquivo;

        /**
         * Adicionando o ContentType para ser passado para AMAZON S3
         */
        private String contentType;

        /**
         * E o Fluxo de entrada do arquivo
         * Se entrarmos no controller onde pegamos o arquivo e dermos um
         * arquivo.getInputStream() e o fluxo de leitura que podemos ler o arquivo que acabamos de fazer o upload
         * a partir dele podemos salvar a foto.
         */
        private InputStream inputStream;
    }

    /**
     * Novo tipo de classe para recuperar arquivo
     */
    @Builder
    @Getter
    class FotoRecuperada {

        private InputStream inputStream;
        private String url;

        public boolean temUrl() {
            return url != null;
        }

        public boolean temInputStream() {
            return inputStream != null;
        }

    }
}
