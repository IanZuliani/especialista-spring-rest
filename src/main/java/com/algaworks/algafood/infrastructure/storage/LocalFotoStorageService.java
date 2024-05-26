package com.algaworks.algafood.infrastructure.storage;

import com.algaworks.algafood.domain.service.FotoStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class LocalFotoStorageService implements FotoStorageService {

    /**
     * Pegando o diretorio de application.properties
     */
    @Value("${algafood.storage.local.diretorio-fotos}")
    private Path diretorioFotos;


    @Override
    public InputStream recuperar(String momeArquivo) {
        Path arquivoPath = getArquivoPath(momeArquivo);
        try {
            return Files.newInputStream(arquivoPath);
        } catch (Exception e) {
            throw new StorageException("Nao foi possivel recuperar arquivo", e);
        }
    }

    @Override
    public void armazenar(NovaFoto novaFoto) {

        var arquivoFoto = Path.of("/home/ian/Desktop/upload", novaFoto.getNomeArquivo());
        /**
         * Caminho para salvar o nova foto
         */
        Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());

        /**
         * Utilizando a classe FileCopyUtils.copy qye tem uma sobrecarga que recebe por parametro um
         * InputStream -> novaFoto.getInputStream() -> arquivo que queremos que seja salvo
         * OutPutStream -> que recebe um tipo path, local que queremos que seja salvo a foto
         * Podemos criar um com a funcao  Files.newOutputStream(arquivoPath), que recebe como parametro um tipo
         * Path que no caso e o caminho que queremos que seja salvo
         * Pode ser Lancada uma exption por isso temos que fazer o try catch
         */
        try {
            //FileCopyUtils.copy(novaFoto.getInputStream(),  Files.newOutputStream(arquivoFoto));
           FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
        } catch (Exception e) {
            throw new StorageException("Nao foi possivel armazenar arquivo", e);
        }

    }

    /**
     * Criando o metodo de remover o arquivo  do diretorio
     * @param nomeArquivo
     */
    @Override
    public void remover(String nomeArquivo) {
        /**
         * Pegando o nome do arquivo que queremos remover
         * getArquivoPath -> pega o caminho completo do arquivo que queremos seja excluido
         */
        Path arquivoPath = getArquivoPath(nomeArquivo);

        try {
            /**
             * Deletamos o arquivo se o mesmo existir. no disco local
             */
            Files.deleteIfExists(arquivoPath);
        } catch (Exception e) {
            throw new StorageException("Nao foi possivel excluir arquivo", e);
        }

    }

    /**
     *
     * @param nomeArquivo -> nome do arquivo
     * @return -> vamos retornar o Path caminho para a foto ser salva     *
     *  return diretorioFotos ->
     */
    private Path getArquivoPath(String nomeArquivo){
        /**
         *return diretorioFotos → Mas nesse diretorio temos apenas o caminho da pasta que vem de application.properties
         * Mas Precisamos do path completo do arquivo Path + NomeDoArquivo
         * Para junta uma coisa com a outra utilizamos o resolve passando um novo path         *
         */
        return diretorioFotos.resolve(Path.of(nomeArquivo));
    }
}
