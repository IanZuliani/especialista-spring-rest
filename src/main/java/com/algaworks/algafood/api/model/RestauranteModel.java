package com.algaworks.algafood.api.model;

import com.algaworks.algafood.api.model.view.RestauranteView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Setter
@Getter
public class RestauranteModel extends RepresentationModel<RestauranteModel> {

    //@JsonView({RestauranteView.Resumo.class,RestauranteView.ApenasNome.class})
    private Long id;
    //@JsonView({RestauranteView.Resumo.class,RestauranteView.ApenasNome.class})
    private String nome;
    //@JsonView(RestauranteView.Resumo.class)
    private BigDecimal taxaFrete;
    //@JsonView(RestauranteView.Resumo.class)
    private CozinhaModel cozinha;

    private Boolean ativo;
    private EnderecoModel endereco;
    private Boolean aberto;
}
