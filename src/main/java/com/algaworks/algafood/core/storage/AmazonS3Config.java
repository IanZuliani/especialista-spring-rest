package com.algaworks.algafood.core.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuracao da Amazon s3
 * Tornado um Bean Spring
 */
@Configuration
public class AmazonS3Config {

    @Autowired
    private StorageProperties storageProperties;

    /**
     * Metodo Que retorna uma instancia de amazon S3
     * Vamos anotar metodo com Bean, registrando que e um metodo que retorna uma instancia de Amazon S3
     * O tornando um Bean Spring
     */
    @Bean
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
}
