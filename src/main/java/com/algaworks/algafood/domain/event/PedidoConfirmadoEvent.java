package com.algaworks.algafood.domain.event;

import com.algaworks.algafood.domain.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Nessa classe colocamos propriedades das  informacoes do evento que aconteceu
 * Nesse caso Pedido, se quisessemos ter outras informacoes aqui teriamos.
 */
@Getter
@AllArgsConstructor //Gerar construtor com as propriedades que setamos, sendo obrigatorio
public class PedidoConfirmadoEvent {

    private Pedido pedido;
}
