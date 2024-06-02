package com.algaworks.algafood.core.storage;

import com.algaworks.algafood.domain.service.FotoStorageService;
import com.algaworks.algafood.infrastructure.storage.LocalFotoStorageService;
import com.algaworks.algafood.infrastructure.storage.S3FotoStorageService;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuracao da Amazon s3
 * Tornado um Bean Spring
 */
@Configuration
public class StorageConfig {

    @Autowired
    private StorageProperties storageProperties;

    /**
     * Metodo Que retorna uma instancia de amazon S3
     * Vamos anotar metodo com Bean, registrando que e um metodo que retorna uma instancia de Amazon S3
     * O tornando um Bean Spring
     */
    @Bean
    @ConditionalOnProperty(name = "algafood.storage.tipo", havingValue = "s3")
    public AmazonS3 amazonS3(){

        /**
         * Passamos as credenciais da amazon
         * accessKey
         * secretKey
         */
        var credentials = new BasicAWSCredentials(storageProperties.getS3().getIdChaveAcesso(),
                storageProperties.getS3().getChaveAcessoSecreta());

        /**
         * Retornamos uma instancia de amazon s3 e podemos utilizar nos metodos normalmente
         * Porem precisamos por as credenciais
         * Para isso injetamos a classe StorageProperties
         * withCredentials -> precisa por parametro uma instancia de AWSStaticCredentialsProvider
         * TemOs que passar a regiao que queremos trabalha
         */
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(storageProperties.getS3().getRegiao())
                .build();

    }

    /**
     * Vamos retornar uma instancia de FotoStorageService
     * 4. Lembrando que FotoStorageService e uma interface, entao qualquer uma das duas implementacoes que temos hoje,
     * pode ser retornada por esse metodo. Orientacao a Objeto
     * @return
     * Pode ser return new Se3FotoStorageService();
     * ou return new LocalFotoStorageService();
     */
    @Bean
    public FotoStorageService fotoStorageService(){
        /**
         * Para saber qual vamos retornar vamos fazer o seguinte
         * Verifica se storageProperties.getTipo() == s3 retorna uma instancia de s3
         * Caso Contrario e local
         */
        if(StorageProperties.TipoStorage.S3.equals(storageProperties.getTipo())){
            return new S3FotoStorageService();
        } else {
            return new LocalFotoStorageService();
        }


    }

}
