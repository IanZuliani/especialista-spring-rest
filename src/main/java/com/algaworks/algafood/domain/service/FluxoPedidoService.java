package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.StatusPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.algaworks.algafood.domain.service.EnvioEmailService.Mensagem;


import java.time.OffsetDateTime;

@Service
public class FluxoPedidoService {

    @Autowired
    private EmissaoPedidoService emissaoPedido;

    @Autowired
    private EnvioEmailService envioEmail;

    @Transactional
    public void confirmar(String codigoPedido) {
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        pedido.confirmar();

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
                .assunto(pedido.getRestaurante().getNome() + "Pedido Confirmado")
                .variavel("pedido", pedido)
                .corpo("pedido-confirmado.html")
                .destinatario(pedido.getCliente().getEmail())
                .build();

        envioEmail.enviar(mensagem);
    }

    @Transactional
    public void cancelar(String codigoPedido){
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        pedido.cancelar();
    }

    @Transactional
    public void entregue(String codigoPedido){
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        pedido.entregar();
    }
}
