package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.PedidoInputDisassembler;
import com.algaworks.algafood.api.assembler.PedidoModelAssemble;
import com.algaworks.algafood.api.assembler.PedidoResumoModelAssemble;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.repository.filter.PedidoFilter;
import com.algaworks.algafood.domain.service.EmissaoPedidoService;
import com.algaworks.algafood.infrastructure.repository.spec.PedidoSpecs;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoModelAssemble assemble;
    @Autowired
    private PedidoInputDisassembler disassembler;

    @Autowired
    private PedidoModelAssemble pedidoModelAssembler;
    @Autowired
    private PedidoResumoModelAssemble pedidoResumoModelAssemble;
    @Autowired
    private EmissaoPedidoService service;
    @Autowired
    private PedidoRepository repository;


/*    @GetMapping
    public MappingJacksonValue list(@RequestParam(required = false) String campos){
        var pedidos = repository.findAll();
        var pedidosModel = pedidoResumoModelAssemble.toCollectionModel(pedidos);

        MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosModel);

        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());

        if (StringUtils.isNotBlank(campos)) {
            filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(",")));
        }

        pedidosWrapper.setFilters(filterProvider);

        return pedidosWrapper;
    }*/
    @GetMapping
    public List<PedidoResumoModel> pesquisar(PedidoFilter filter){
        var pedidos = repository.findAll(PedidoSpecs.usandoFiltro(filter));

        return pedidoResumoModelAssemble.toCollectionModel(pedidos);
    }

    @GetMapping("/{id}")
    public PedidoModel findById(@PathVariable String id){
        return assemble.toModel(service.buscarOuFalhar(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
        try {
            Pedido novoPedido = disassembler.toDomainObject(pedidoInput);

            // TODO pegar usuário autenticado
            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(1L);

            novoPedido = service.emitir(novoPedido);

            return pedidoModelAssembler.toModel(novoPedido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }
}
