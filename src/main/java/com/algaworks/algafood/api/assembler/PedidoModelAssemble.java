package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.controller.*;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoModelAssemble extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> {

    @Autowired
    private ModelMapper modelMapper;

    public PedidoModelAssemble() {
        super(PedidoController.class, PedidoModel.class);
    }
    @Override
    public PedidoModel toModel(Pedido pedido) {
        PedidoModel pedidoModel = createModelWithId(pedido.getId(), pedido);
        modelMapper.map(pedido, pedidoModel);

       // pedidoModel.add(WebMvcLinkBuilder.linkTo(PedidoController.class).withRel("pedidos"));

        /**
         * Adicionando links com template variables
         * 1. vamos criar novamente o link, mas dessa vez vamos utilizar uma classe do hateos para fazer
         * essa classe vai ajudar a gente a criar uma URI com template Variables
         *
         * Vamos utilizar o metodo of -> UriTemplate.of()
         * Esse metodo vai receber dois parametros.
         * 1 a string da url → http://api.algafood.local:8080/pedidos
         * 2 e um template variabels que vamos criar
         */

        /**
         * Vamos criar uma lista de variaveis para passar como parametro na funcao UriTemplate.of
         */
        TemplateVariables pageVariables = new TemplateVariables(
                /**
                 * TemplateVariables -> lista de TemplateVariable
                 * Podemos criar varios,
                 * O primeiro parametro e o nome
                 * o segundo parametro e o tipo da variavel
                 * Vamos fala que e um Request Param
                 */
            new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
            new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM),
            new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM));

        TemplateVariables filtroVariables = new TemplateVariables(
                new TemplateVariable("ClienteId", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("restauranteId", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("dataCriacaoInicio", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("dataCriacaoFim", TemplateVariable.VariableType.REQUEST_PARAM));

        /**
         * Pegando a url do pedido dinamicamente
         */
        String pedidoUrl = WebMvcLinkBuilder.linkTo(PedidoController.class).toUri().toString();

        pedidoModel.add(Link.of(UriTemplate.of(pedidoUrl, pageVariables.concat(filtroVariables)), "pedidos"));


        pedidoModel.getRestaurante().add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RestauranteController.class)
                .buscar(pedido.getRestaurante().getId())).withSelfRel());

        pedidoModel.getCliente().add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                .findById(pedido.getCliente().getId())).withSelfRel());

        // Passamos null no segundo argumento, porque é indiferente para a
        // construção da URL do recurso de forma de pagamento
        pedidoModel.getFormaPagamento().add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FormaPagamentoController.class)
                .findById(pedido.getFormaPagamento().getId())).withSelfRel());

        pedidoModel.getEnderecoEntrega().getCidade().add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CidadeController.class)
                .buscar(pedido.getEnderecoEntrega().getCidade().getId())).withSelfRel());

        pedidoModel.getItens().forEach(item -> {
            item.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RestauranteProdutoController.class)
                    .buscar(pedidoModel.getRestaurante().getId(), item.getProdutoId()))
                    .withRel("produto"));
        });

        return pedidoModel;

        //return modelMapper.map(pedido, PedidoModel.class);
    }
    @Override
    public CollectionModel<PedidoModel> toCollectionModel(Iterable<? extends Pedido> entities) {
        return super.toCollectionModel(entities).add(WebMvcLinkBuilder.linkTo(PedidoController.class).withSelfRel());
    }

    /*    public List<PedidoModel> toCollectionModel(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(pedido -> toModel(pedido))
                .collect(Collectors.toList());
    }*/
}
