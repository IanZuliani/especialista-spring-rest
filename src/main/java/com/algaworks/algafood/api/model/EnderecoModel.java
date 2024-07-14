package com.algaworks.algafood.api.model;

import com.algaworks.algafood.domain.model.Cidade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Setter
@Getter
public class EnderecoModel extends RepresentationModel<EnderecoModel> {

    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private CidadeResumoModel cidade;
}