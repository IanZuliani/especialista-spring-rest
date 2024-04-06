package com.algaworks.algafood.api.model.input;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CidadeInput {

    private String nome;
    private EstadoIdInput estado;
}
