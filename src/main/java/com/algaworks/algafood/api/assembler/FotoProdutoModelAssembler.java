package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.controller.RestauranteProdutoFotoController;
import com.algaworks.algafood.api.model.EstadoModel;
import com.algaworks.algafood.api.model.FotoProdutoModel;
import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.model.FotoProduto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FotoProdutoModelAssembler extends RepresentationModelAssemblerSupport<FotoProduto, FotoProdutoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AlgaLinks algaLinks;

    public FotoProdutoModelAssembler() {
        super(RestauranteProdutoFotoController.class, FotoProdutoModel.class);
    }

    /**
     *
     * @param foto ->Recebemos dados DO tipo da entidade FotoProduto
     * @return -> convertemos e retornamos ele como FotoProdutoModel
     */
   /* public FotoProdutoModel toModel(FotoProduto foto) {
        return modelMapper.map(foto, FotoProdutoModel.class);
    }*/
    public FotoProdutoModel toModel(FotoProduto foto) {
        FotoProdutoModel fotoProdutoModel = modelMapper.map(foto, FotoProdutoModel.class);

        fotoProdutoModel.add(algaLinks.linkToFotoProduto(
                foto.getRestauranteId(), foto.getProduto().getId()));

        fotoProdutoModel.add(algaLinks.linkToProduto(
                foto.getRestauranteId(), foto.getProduto().getId(), "produto"));

        return fotoProdutoModel;
    }

    /**
     * Essa Funcao e para trabalharmos com LIst de retorno de FotoProdutoModel, mas no momento
     * do curso nao estamos trabalhando.
     * @param produtos
     * @return
     */
    public List<FotoProdutoModel> toCollectionModel(List<FotoProduto> produtos) {
        return produtos.stream()
                .map(produto -> toModel(produto))
                .collect(Collectors.toList());
    }
}
