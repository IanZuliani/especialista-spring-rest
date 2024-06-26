package com.algaworks.algafood.core.storage;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "algafood.storage")
public class StorageProperties {

    /**
     * Vamos criar uma propriedade e uma classe chamada local
     * que vai representar o local dentro do application.properties
     * Do jeito que esta aqui ele vai pegar algafood.storage.local.diretorioFotos
     * com o valor /home/ian/Desktop/upload
     */
    private Local local = new Local();

    private S3 s3 = new S3();

    /**
     * vamos criar uma propriedade que nos informa qual storage queremos utilizar
     * Se nao for configurado, por padrao ele utiliza LOCAL
     */
    private TipoStorage tipo = TipoStorage.LOCAL;

    public enum TipoStorage{
        LOCAL, S3
    }

    @Getter
    @Setter
    public class Local {
        private Path diretorioFotos;
    }

    @Getter
    @Setter
    public class S3 {
        private String idChaveAcesso;
        private String chaveAcessoSecreta;
        private String bucket;
        private Regions regiao;
        private String diretorioFotos;//nao e um Path pos nao e um caminho
    }
}
