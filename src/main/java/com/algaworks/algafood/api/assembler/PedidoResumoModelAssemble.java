package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.controller.PedidoController;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.api.controller.UsuarioController;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoResumoModelAssemble  extends RepresentationModelAssemblerSupport<Pedido, PedidoResumoModel> {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AlgaLinks algaLinks;

    public PedidoResumoModelAssemble() {
        super(PedidoController.class, PedidoResumoModel.class);
    }

    @Override
    public PedidoResumoModel toModel(Pedido pedido) {
        PedidoResumoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
        modelMapper.map(pedido, pedidoModel);

        pedidoModel.add(algaLinks.linkToPedidos("pedidos"));

        pedidoModel.getRestaurante().add(
                algaLinks.linkToRestaurante(pedido.getRestaurante().getId()));

        pedidoModel.getCliente().add(algaLinks.linkToUsuario(pedido.getCliente().getId()));

        return pedidoModel;
    }

 /*   public List<PedidoResumoModel> toCollectionModel(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(pedido -> toModel(pedido))
                .collect(Collectors.toList());
    }*/
}
