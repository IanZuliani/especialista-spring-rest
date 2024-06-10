package com.algaworks.algafood.domain.listener;

import com.algaworks.algafood.domain.event.PedidoConfirmadoEvent;
import com.algaworks.algafood.domain.service.EnvioEmailService;
import com.algaworks.algafood.domain.service.EnvioEmailService.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**\
 * Clase que fica escutando o evento quando o pedido e salvo, para disparar um email
 */
@Component
public class NotificacaoClientePedidoConfirmadoListener {

    @Autowired
    private EnvioEmailService envioEmail;

    /**
     * Recebe Por parametro a classe de evento que estamos disparando ao confirmar pedido
     * @param event
     * Para escutar um evento disparado temos que anotar o metodo com @EventListener
     * @EventListener → metodo que esta interessado sempre que PedidoConfirmadoEvent for disparado
     *
     * QUando estivermos escutando esse PedidoConfirmadoEvent, estamos passando ele quando disparamos o pedido
     */
    @EventListener
    public void aoConfirmarPedido(PedidoConfirmadoEvent event){

        /**
         * Criando o corpo do email
         *Para nao precisarmos instanciar um Set e passar no destinatario podemos anotar a propriedade  destinatarios com @Singular do lombok
         * Nesse caso a propriedade vai para o cingular
         * destinatario
         * e podemos passar apenas um email
         * .destinatario(pedido.getCliente().getEmail());
         * se quisermos passar outro email basta colocarmos outro destinatario
         * .destinatario(pedido.getCliente().getEmail());
         */
        var mensagem = Mensagem.builder()
                .assunto(event.getPedido().getRestaurante().getNome() + "Pedido Confirmado")
                .variavel("pedido", event.getPedido())
                .corpo("pedido-confirmado.html")
                .destinatario(event.getPedido().getCliente().getEmail())
                .build();

        envioEmail.enviar(mensagem);
    }
}
