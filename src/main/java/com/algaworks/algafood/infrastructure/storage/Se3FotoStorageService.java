package com.algaworks.algafood.infrastructure.storage;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.service.FotoStorageService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class Se3FotoStorageService implements FotoStorageService {

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private StorageProperties storageProperties;

    @Override
    public InputStream recuperar(String momeArquivo) {
        return null;
    }

    @Override
    public void armazenar(NovaFoto novaFoto) {
        try {
            /**
             * Pega o caminho do arquivo utilizando a funcao getCaminhoArquivo
             */
            String caminhoArquivo = getCaminhoArquivo(novaFoto.getNomeArquivo());

            var objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(novaFoto.getContentType());

            /**
             * Essa instancia vai ser usada para submeter a api da amazon que estamos fazendo uma requisicao para inserir um objeto.
             * Passamos
             * 1 - Nome Bucket
             * 2 - Caminho que o Objeto esta com nome do arquivo
             * 3 - PutStream
             * 4 - Object metadata
             * aQUI ESTAMOS PREPARANDO O PAYLOAD, A REQUISICAO QUE VAMOS FAZER NA API DA AMAZON
             *
             * withCannedAcl(CannedAccessControlList.PublicRead);
             * Na hora que fizer a requisicao para por esse objeto dentro do S3, coloca tambem essa permissao de PublicRead ou leitura publica
             */
            var putObjectRequest = new PutObjectRequest(
                    storageProperties.getS3().getBucket(),
                    caminhoArquivo,
                    novaFoto.getInputStream(),
                    objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);


            /**
             * Agora vamos fazer a chamada utilizando a instancia S3
             * Poderiamos tambem passar sem o PutObject, no caso direto poderia tambem.
             * Pos existe uma sobrecarga
             */
            amazonS3.putObject(putObjectRequest);
        }catch (Exception e){
            throw new StorageException("Nao foi possivel enviar arquivo para Amazon s3", e);
        }
    }

    /**
     * Vai retornar para gente o caminho do arquivo.
     * @param nomeArquivo
     * @return
     */
    private String getCaminhoArquivo(String nomeArquivo) {
        return String.format("%s/%s", storageProperties.getS3().getDiretorioFotos(), nomeArquivo);
    }

    @Override
    public void remover(String nomeArquivo) {

        /**
         * Deletar um arquivo na amazon
         * Primeiro Eu crio o caminho do arquivo que quero deletar
         */
        String caminhoArquivo = getCaminhoArquivo(nomeArquivo);

        var deleteObjectRequest = new DeleteObjectRequest(storageProperties.getS3().getBucket(), caminhoArquivo);
        amazonS3.deleteObject(deleteObjectRequest);

    }
}
