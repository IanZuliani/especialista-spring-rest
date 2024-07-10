package com.algaworks.algafood.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class UsuarioModel extends RepresentationModel<UsuarioModel> {

    private Long id;
    private String nome;
    private String email;
}
