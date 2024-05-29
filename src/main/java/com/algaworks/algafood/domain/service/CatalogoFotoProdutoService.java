package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.FotoProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Optional;

/**
 * 5. Uma classe salva os dados da foto no banco de dados `CatalogoFotoProdutoService`
 * 6. Uma classe salva o arquivo em algum lugar que queremos, `LocalFotoStorageService`
 * 7. e diminuimos o acoplamento fazendo elas conversar entre si atraves de uma interface
 * `FotoStorageService`
 * 8. Podendo ter outras implementacoes sem a necessidade que a classe `CatalogoFotoProdutoService`
 * , saiba para onde vai o arquivo, apenas instanciando a interface `FotoStorageService`
 */
@Service
public class CatalogoFotoProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * vAMOS UTILIZAR ESSA INTANCIA DE INTERFACE PARA ENVIAR AS FOTOS PARA ALGUMA PASTA LOCALMENTE
     * Como temos apenas uma instaNCIA que implemente FotoStorageService , no caso e LocalFotoStorageService  sabemos
     * que quando chamarmos essa interface  uma instancia de LocalFotoStorageService que vira
     * Mas ate no final do curso teremos outras implementacoes ex Amazon S3
     */
    @Autowired
    private FotoStorageService fotoStorage;

    @Transactional
    public FotoProduto salvar(FotoProduto foto, InputStream dadosArquivo) {
        /**
         * Excluir foto se existir
         */
        Long restauranteId = foto.getRestauranteId();
        Long proditoId = foto.getProduto().getId();
        //Gerenado o nome do arquivo
        String nomeNovoArquivo = fotoStorage.gerarNomeArquivo(foto.getNomeArquivo());
        String nomeArquivoExistente = null;


        Optional<FotoProduto> fotoExiste = produtoRepository.findFotoById(restauranteId,
                proditoId);

        //se existir a foto chama o metodo delete
        if (fotoExiste.isPresent()) {
            nomeArquivoExistente = fotoExiste.get().getNomeArquivo();//se existir o arquivo, coloca o nome dele dentro dessa variavel.
            produtoRepository.delete(fotoExiste.get());
        }

        /**
         * Jogarmos o metodo save para uma variavel foto, e executarmos o save() antes de enviarmos a foto
         * Apos executarmos o save, enviamos o arquivo para o diretorio
         * E apos isso retornamos os dados da foto
         * Pos se ouver algum problema no banco de dados e melhor que de antes de salvarmos a imagem.
         */
        foto.setNomeArquivo(nomeNovoArquivo);//alterando o nome do arquivo para o novo nome
        foto =  produtoRepository.save(foto);

        /**
         * Vamos fazer um flush e forcar o JPA a executar o save logo apos pedirmos, pos normalmente demora um pouco para descaregar
         */
        produtoRepository.flush();

        /**
         * Por termos anotado a classe FotoStorageService.NovaFoto
         * Com Builder, vamos construir o Objeto assim
         *
         */
        var novaFoto = FotoStorageService.NovaFoto.builder()
                .nomeArquivo(foto.getNomeArquivo())//como ja demos o set Logo acima ja vai ser alterado o nome aqui
                .contentType(foto.getContentType())//pegando o ContentType para passar para Amazon s3
                .inputStream(dadosArquivo)
                .build();

        /**
         * Depos utilizamos a interface para enviar o arquivo
         */
        fotoStorage.substituir(nomeArquivoExistente, novaFoto);

        /**
         * Temos que tomar cuidado que as vezes quando dermos o metodo save(foto)
         * As fotos ja foram armazenadas, e por algum erro a implementacao lanca um exception
         * Os dados nao estaram salvos na base de dados, mas a foto sera enviada para o servidor.
         */
        //return produtoRepository.save(foto);

        return foto;
    }
    public FotoProduto buscarOuFalhar(Long restauranteId, Long produtoId){
        return produtoRepository.findFotoById(restauranteId,
                produtoId).orElseThrow(()-> new FotoProdutoNaoEncontradoException(restauranteId, produtoId));
    }

    public void excluir(Long restauranteId, Long produtoId){
        var fotoProduto = buscarOuFalhar(restauranteId, produtoId);

        produtoRepository.delete(fotoProduto);
        produtoRepository.flush();

        fotoStorage.remover(fotoProduto.getNomeArquivo());


    }

}
