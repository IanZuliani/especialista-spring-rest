package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.PedidoModelAssemble;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.service.EmissaoPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoModelAssemble assemble;
    @Autowired
    private EmissaoPedidoService service;
    @Autowired
    private PedidoRepository repository;

    @GetMapping
    public List<PedidoModel> list(){
        return assemble.toCollectionModel(repository.findAll());
    }

    @GetMapping("/{id}")
    public PedidoModel findById(@PathVariable Long id){
        return assemble.toModel(service.buscarOuFalhar(id));
    }
}
