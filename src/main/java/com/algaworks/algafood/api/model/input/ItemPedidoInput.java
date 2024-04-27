package com.algaworks.algafood.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ItemPedidoInput {
    @NotNull
    private Long produtoId;
    @NotNull
    private Integer quantidade;
    private String observacao;
}
