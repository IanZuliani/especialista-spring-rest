package com.algaworks.algafood.api.controller;


import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.assembler.FormaPagamentoAssemble;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes/{id}/formas-pagamento")
public class RestauranteFormaPagamentoController {

    @Autowired
    private CadastroRestauranteService cadastroRestaurante;
    @Autowired
    private FormaPagamentoAssemble assemble;
    @Autowired
    private AlgaLinks algaLinks;


    @GetMapping
    public CollectionModel<FormaPagamentoModel> listar(@PathVariable Long restauranteId) {
        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

        return assemble.toCollectionModel(restaurante.getFormasPagamento())
                .removeLinks()
                .add(algaLinks.linkToRestauranteFormasPagamento(restauranteId));
    }

/*    @GetMapping
    public List<FormaPagamentoModel> listar(@PathVariable Long id){
        var restaurante = cadastroRestaurante.buscarOuFalhar(id);
        return assemble.toCollectionModel(restaurante.getFormasPagamento());
    }*/

    @DeleteMapping("/{idFormaPagamento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desassociar(@PathVariable Long id,@PathVariable Long idFormaPagamento){
        cadastroRestaurante.desassociarFormaPAgamento(id, idFormaPagamento);
    }

    @PutMapping("/{idFormaPagamento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void asossiar(@PathVariable Long id,@PathVariable Long idFormaPagamento){
        cadastroRestaurante.asossiarFormaPagamento(id, idFormaPagamento);
    }
}
