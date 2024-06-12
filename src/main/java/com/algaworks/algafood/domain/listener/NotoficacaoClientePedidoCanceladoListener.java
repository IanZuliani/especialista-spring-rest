package com.algaworks.algafood.domain.listener;

import com.algaworks.algafood.domain.event.PedidoCalceladoEvent;
import com.algaworks.algafood.domain.service.EnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotoficacaoClientePedidoCanceladoListener {


    @Autowired
    private EnvioEmailService envioEmail;

    @TransactionalEventListener
    public void aoCancelarPedido(PedidoCalceladoEvent event){

        var mensagem = EnvioEmailService.Mensagem.builder()
                .assunto(event.getPedido().getRestaurante().getNome() + "Pedido Cancelado")
                .variavel("pedido", event.getPedido())
                .corpo("pedido-calcelado.html")
                .destinatario(event.getPedido().getCliente().getEmail())
                .build();

        envioEmail.enviar(mensagem);
    }
}
