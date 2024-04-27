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
        pedido.confirmar();
    }

    @Transactional
    public void cancelar(Long pedidoId){
        var pedido = emissaoPedido.buscarOuFalhar(pedidoId);
        pedido.cancelar();
    }

    @Transactional
    public void entregue(Long pedidoId){
        var pedido = emissaoPedido.buscarOuFalhar(pedidoId);
        pedido.entregar();
    }
}
