package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.StatusPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class FluxoPedidoService {

    @Autowired
    private EmissaoPedidoService emissaoPedido;


    @Transactional
    public void confirmar(Long pedidoId){
        Pedido pedido = emissaoPedido.buscarOuFalhar(pedidoId);
        if(!pedido.getStatus().equals(StatusPedido.CRIADO)){
            throw new NegocioException(
                    String.format("Estatus do pedido %d nao pode ser alterado de %s para %s",
                            pedido.getId(), pedido.getStatus(), StatusPedido.CONFIRMADO.getDescricao())
            );
        }

        pedido.setStatus(StatusPedido.CONFIRMADO);
        pedido.setDataConfirmacao(OffsetDateTime.now());
    }

    @Transactional
    public void cancelar(Long pedidoId){
        var pedido = emissaoPedido.buscarOuFalhar(pedidoId);
        if(!pedido.getStatus().equals(StatusPedido.CRIADO)){
            throw new NegocioException(
                    String.format("Estatus do pedido %d nao pode ser alterado de %s para %s",
                            pedido.getId(), pedido.getStatus(), StatusPedido.CANCELADO.getDescricao())
            );
        }
        pedido.setStatus(StatusPedido.CANCELADO);
        pedido.setDataCancelamento(OffsetDateTime.now());
    }

    @Transactional
    public void entregue(Long pedidoId){
        var pedido = emissaoPedido.buscarOuFalhar(pedidoId);
        if(!pedido.getStatus().equals(StatusPedido.CONFIRMADO)){
            throw new NegocioException(
                    String.format("Estatus do pedido %d nao pode ser alterado de %s para %s",
                            pedido.getId(), pedido.getStatus(), StatusPedido.ENTREGUE.getDescricao())
            );
        }
        pedido.setStatus(StatusPedido.ENTREGUE);
        pedido.setDataEntrega(OffsetDateTime.now());
    }
}
