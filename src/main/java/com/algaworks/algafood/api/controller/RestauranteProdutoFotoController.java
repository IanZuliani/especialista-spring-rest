package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.FotoProdutoModelAssembler;
import com.algaworks.algafood.api.model.FotoProdutoModel;
import com.algaworks.algafood.api.model.input.FotoProdutoInput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CatalogoFotoProdutoService;
import com.algaworks.algafood.domain.service.FotoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.algaworks.algafood.domain.service.FotoStorageService.FotoRecuperada;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {

    /**
     * Vamos injetar nossa classe de catalogoFotoProduto
     */
    @Autowired
    private CatalogoFotoProdutoService catalogoFotoProduto;
    /**
     * Vamos injetar Essa classe para poder fazer a busca do produto
     */
    @Autowired
    private CadastroProdutoService cadastroProduto;
    /**
     * Vamos injetar essa classe para converter o ProdutoModel, que vem do metodo salvar() do service
     * Para FotoProdutoModel Que e o retorna da funcaoo
     */
    @Autowired
    private FotoProdutoModelAssembler fotoProdutoModelAssembler;

    @Autowired
    private FotoStorageService fotoStorage;

    /**
     * @param restauranteId
     * @param produtoId
     * @return Metodo que busca as informacoes da foto de restaurante, passando o id do restaurante e o id do produto
     * Lembrando que isso so e possivel pos e apenas 1 foto Por produto, se foce mas teriamos que passar
     * Um ArrayList
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public FotoProdutoModel searchImage(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        return fotoProdutoModelAssembler.toModel(catalogoFotoProduto.buscarOuFalhar(
                restauranteId, produtoId));
    }

    /**
     * @param restauranteId    -> Pega o id do restaurante na uri
     * @param produtoId        -> Pega o id do Produto Na URI
     *                         arquivo -> Pega o arquivo Binario que vem como parametro quando chamamos a funcao
     *                         MediaType.MULTIPART_FORM_DATA_VALUE -> A funcao fica mapeada apenas para metodos do tipo MULTIPART_FORM_DATA_VALUE
     * @param fotoProdutoInput -> Esse e um modelo do que recebemos na funcao, poderiamos colocar parametro por parametro
     *                         Mas a medida que cresce a quantidade de parametro enviada, teriamos que adicionar mas nomes
     *                         na funcao
     *                         Removemos @RequestParam MultipartFile arquivo,  por que ja esta em fotoProdutoInput
     *                         e onde era arquivo trocamos para fotoProdutoInput
     *                         e Temos sempre que dar fotoProdutoInput.getArquivo() para pega
     */
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId, @Valid
    FotoProdutoInput fotoProdutoInput) throws IOException {

        /**
         * Verificamos primeiro se o produto Existe e se esta vinculado ao restaurante que queremos
         * Se nao retorna uma exception
         */
        Produto produto = cadastroProduto.buscarOuFalhar(restauranteId, produtoId);

        /**
         * Pegando e alterando o nome do arquivo
         */
        var nomeArquivo = UUID.randomUUID().toString()
                + "_" + fotoProdutoInput.getArquivo().getOriginalFilename();

        /**
         * Pegando o ContentType
         */
        MultipartFile arquivo = fotoProdutoInput.getArquivo();

        /**
         * Para salvarmos os dados precisamos passar uma instancia de FotoProduto
         * 6. Em outros metodos Utilizamos o desasemble, Com ModelMapper,
         * 7. Para transformar o Tipo Input, Que vem Da API no tipo de dado que precisamos enviar para o Banco
         * Mas aqui Temos que fazer na Mao pos
         */
        FotoProduto foto = new FotoProduto();
        foto.setProduto(produto);
        foto.setDescricao(fotoProdutoInput.getDescricao());
        foto.setNomeArquivo(arquivo.getOriginalFilename());
        foto.setContentType(arquivo.getContentType());
        foto.setTamanho(arquivo.getSize());


        FotoProduto fotoSalva = catalogoFotoProduto.salvar(foto, arquivo.getInputStream());

        /**
         * Retornamos o Objeto Convertido
         */
        return fotoProdutoModelAssembler.toModel(fotoSalva);

    }

    /**
     * @param restauranteId
     * @param produtoId     Vamos buscar a foto Do Produto PAra servir para o consumidor da API
     */
    @GetMapping
    public ResponseEntity<?> servir(@PathVariable Long restauranteId,
                                                      @PathVariable Long produtoId,
                                                      @RequestHeader(name = "accept") String acceptHeader)
            throws HttpMediaTypeNotAcceptableException {

        try {
            /**
             * Buscando as informacoes da foto do produto, se nao tiver nada no banco e lancada uma exception
             */
            var fotoProtudo = catalogoFotoProduto.buscarOuFalhar(
                    restauranteId, produtoId);

            /**
             * Criando um media type para passar de parametro para funcao
             * MediaType.parseMediaType() -> Converte uma string em um media Type
             * fotoProtudo.getContentType() -> Isso e o mesmo que MediaType
             * Nesse caso pegamos o MediaType da imagem que vem do banco de dados
             */
            MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProtudo.getContentType());


            /**
             * o Cliente pode passar uma lista de MediaTypes, por isso criamos a list
             * Por que o cliente pode passar mas de um MediaType, sao a lista que eles aceitam
             */
            List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);

            /**
             * E vamos comparar com o tipo de MediaType que o consumidor esta passando por parametro quando chama a API
             */
            verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);

            /**
             * Buscando o InputStream da foto para devolver para o cliente
             *
             *
             */
            FotoRecuperada fotoRecuperada = fotoStorage.recuperar(fotoProtudo.getNomeArquivo());

            /**
             * Se tem URL quer dizer que estamos utilziando o S3, nao vamo retornar um inputStream
             * Visto que queremos que a amazon informe os arquvos diretamente do servidor
             * Sem que a aplicacao precise buscar ele e retornar um inputStrem
             * redirecionamos o cliente pra outro lugar
             */
            if (fotoRecuperada.temUrl()) {
                System.out.println("fotoRecuperada.toString() = " + fotoRecuperada.toString());
                return ResponseEntity
                        .status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, fotoRecuperada.getUrl())
                        .build();
            } else {


                /**
                 * Criando o responseEntity
                 * No body Precisamos passar uma nova instancia de InputStream passando os dados da foto
                 * -
                 *  precisamos pensar o seguinte, se estiver armazenada em disco local, o fotoRecuperada vai retornar
                 *  um inputStream la dentro Colocamos ele no body, e sem problema vao retornar normal
                 * Porem se estivermos utilizando o s3 ele vai retornar a URL, desssa forma quando fizermos
                 * `(fotoRecuperada.getInputStream())`  vai estar nullo
                 * por isso tivemos que fazer o if
                 */
                return ResponseEntity.ok()
                        .contentType(mediaTypeFoto)
                        .body(new InputStreamResource(fotoRecuperada.getInputStream()));
                /**
                 * Vamos lanca uma exception caso a imagem nao exista
                 *Apenas com o status, visto que o mesmo apenas aceita uma Aceppet image/jpeg ou png
                 */

            }
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping
    public void delete(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        catalogoFotoProduto.excluir(restauranteId, produtoId);
    }

    /**
     * @param mediaTypeFoto
     * @param mediaTypesAceitas Verificando se aceita o tipo de media type salva no banco de dados
     */
    private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto,
                                                   List<MediaType> mediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {

        /**
         * Passando a lista de media type para stream
         * .anyMatch() -> se apenas 1 e verdadeiro retorna true
         * isCompatibleWith -> caso o consumidor utilize image/* isso ira funcionar tambem
         * Se dentro da lista tem pelomenos 1 compativel
         */
        boolean compativel = mediaTypesAceitas.stream()
                .anyMatch(mediaTypesAceita -> mediaTypesAceita.isCompatibleWith(mediaTypeFoto));//Se pelo menos 1 da lista de media type e aceita esta tudo ok

        /**
         * Caso nao tenha na lista nenhum compativel com o que veio do banco de dados, vamos lanca essa exception
         * Passando a Lista De MediaTypes aceita
         */
        if (!compativel) {
            throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
        }

    }
}
