package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.EstadoModel;
import com.algaworks.algafood.api.model.FotoProdutoModel;
import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.model.FotoProduto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FotoProdutoModelAssembler {

    @Autowired
    private ModelMapper modelMapper;


    /**
     *
     * @param foto ->Recebemos dados DO tipo da entidade FotoProduto
     * @return -> convertemos e retornamos ele como FotoProdutoModel
     */
    public FotoProdutoModel toModel(FotoProduto foto) {
        return modelMapper.map(foto, FotoProdutoModel.class);
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
