package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.FotoProdutoModelAssembler;
import com.algaworks.algafood.api.model.FotoProdutoModel;
import com.algaworks.algafood.api.model.input.FotoProdutoInput;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CatalogoFotoProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
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
    public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId, @Valid
                              FotoProdutoInput fotoProdutoInput){

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


        FotoProduto fotoSalva = catalogoFotoProduto.salvar(foto);

        /**
         * Retornamos o Objeto Convertido
         */
        return fotoProdutoModelAssembler.toModel(fotoSalva);

    }

}
