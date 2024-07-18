package com.algaworks.algafood.api;

import com.algaworks.algafood.api.controller.PedidoController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

/**
 * Classe com responsabilidade de gerar os links
 */
@Component
public class AlgaLinks {

    /**
     *   * Adicionando links com template variables
     * 1. vamos criar novamente o link, mas dessa vez vamos utilizar uma classe do hateos para fazer
     * essa classe vai ajudar a gente a criar uma URI com template Variables
     * Vamos utilizar o metodo of -> UriTemplate.of()
     * Esse metodo vai receber dois parametros.
     * 1 a string da url → http://api.algafood.local:8080/pedidos
     *2 e um template variabels que vamos criar
     * TemplateVariables -> lista de TemplateVariable
     * Podemos criar varios,
     * O primeiro parametro e o nome
     * o segundo parametro e o tipo da variavel
     * Vamos fala que e um Request Param
     */
    public static final TemplateVariables PAGINACAO_VARIABLES = new TemplateVariables(
            new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
            new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM),
            new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM));

    public Link linkToPedidos(){
        /**
         * Vamos criar uma lista de variaveis para passar como parametro na funcao UriTemplate.of
         * Essas variaveis abaixo nao pretendemos utilizar em outros metodos entao nao vamos alterar elas
         */
               TemplateVariables filtroVariables = new TemplateVariables(
                new TemplateVariable("ClienteId", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("restauranteId", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("dataCriacaoInicio", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("dataCriacaoFim", TemplateVariable.VariableType.REQUEST_PARAM));

        /**
         * Pegando a url do pedido dinamicamente
         */
        String pedidoUrl = WebMvcLinkBuilder.linkTo(PedidoController.class).toUri().toString();

        return Link.of(UriTemplate.of(pedidoUrl, PAGINACAO_VARIABLES.concat(filtroVariables)), "pedidos");
    }

}
