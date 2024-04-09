package com.algaworks.algafood.api.model.input;

import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GrupoInput {

    @NotBlank
    private String nome;
}
