package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.controller.CidadeController;
import com.algaworks.algafood.api.controller.EstadoController;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.domain.model.Cidade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Vamos criar HyperMedia a partir da classe de assemble, que e um padrao utilizado pelos desenvolvedores do
 * SpringHateos
 * Extendemos a classe RepresentationModelAssemblerSupport, passamos
 * Primeiro Parametro e a partir de qual entidade de Origem, Cidade
 * Segundo Parametro e qual o tipo de representention Model que montamos nessa classe Assemble CidadeModel
 */
@Component
public class CidadeModelAssembler extends RepresentationModelAssemblerSupport<Cidade, CidadeModel> {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Temos que criar um construtor ja que extendemos RepresentationModelAssemblerSupport
     * Temos que passar dois parametros
     * CidadeController -> Classe do Controllador //controllador que gerencia cidades
     *  CidadeModel -> A classe do cidadeModel // classe do cidadeModel
     */
    public CidadeModelAssembler() {
        super(CidadeController.class, CidadeModel.class);
    }

    /**
     * o Metodo toModel sobreescreve o metodo da classe pai
     *
     * @param cidade
     * @return
     */
    @Override
    public CidadeModel toModel(Cidade cidade){

        /**
         * Uma outra maneira de criar uma instancia de cidadeModel, com o id da cidade, e gerar links automatico
         */
        CidadeModel cidadeModel = createModelWithId(cidade.getId(), cidade);
        /**
         * Mas temos que passar o modelMapper tambem
         * Vamos fala para ele copiar a instancia de cidade para cidaedModel
         * com isso reduzimos as linhas abaixo
         */
        modelMapper.map(cidade, cidadeModel);

        //CidadeModel cidadeModel =  modelMapper.map(cidade, CidadeModel.class);

       /* cidadeModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CidadeController.class)
                .buscar(cidadeModel.getId())).withSelfRel());*/

        cidadeModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CidadeController.class)
                .listar()).withRel("cidades"));
        cidadeModel.getEstado().add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EstadoController.class)
                .buscar(cidadeModel.getEstado().getId())).withRel("estado"));

        return cidadeModel;
    }

    /**
     * Sobreescrevendo o metodo toCollectionModel, pegamos a resposta do metodo e adicionamos o add
     * Para isso temos que sobreescrever o metodo toCollectionModel para adicionar a funcao que faz  o LINK
     * Utilizando .add(WebMvcLinkBuilder.linkTo(CidadeController.class).withSelfRel());
     */
    @Override
    public CollectionModel<CidadeModel> toCollectionModel(Iterable<? extends Cidade> entities) {
        return super.toCollectionModel(entities).add(WebMvcLinkBuilder.linkTo(CidadeController.class).withSelfRel());
    }

    /**
     * Como estamos utilizando a interface
     * Ja temos o metodo toCollectionModel implementado nela, entao nao precisamos mas
     */
    /*public List<CidadeModel> toCollectionModel(List<Cidade> cidades){

        return cidades.stream()
                .map(cidade -> toModel(cidade))
                .collect(Collectors.toList());

    }*/
}
