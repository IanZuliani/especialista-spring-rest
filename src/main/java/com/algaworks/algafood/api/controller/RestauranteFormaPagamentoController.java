package com.algaworks.algafood.api.controller;


import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.assembler.FormaPagamentoAssemble;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/formas-pagamento")
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

        CollectionModel<FormaPagamentoModel> formasPagamentoModel = assemble.toCollectionModel(restaurante.getFormasPagamento())
                .removeLinks()
                .add(algaLinks.linkToRestauranteFormasPagamento(restauranteId));

        formasPagamentoModel.getContent().forEach(formaPagamentoModel -> {
            formaPagamentoModel.add(algaLinks.linkToRestauranteFormasPagamentoDesassociacao(restauranteId,
                    formaPagamentoModel.getId(), "desassociar"));
        });

        return formasPagamentoModel;
    }

/*    @GetMapping
    public List<FormaPagamentoModel> listar(@PathVariable Long id){
        var restaurante = cadastroRestaurante.buscarOuFalhar(id);
        return assemble.toCollectionModel(restaurante.getFormasPagamento());
    }*/

    @DeleteMapping("/{idFormaPagamento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> desassociar(@PathVariable Long restauranteId, @PathVariable Long idFormaPagamento){
        cadastroRestaurante.desassociarFormaPAgamento(restauranteId, idFormaPagamento);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idFormaPagamento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void asossiar(@PathVariable Long restauranteId,@PathVariable Long idFormaPagamento){
        cadastroRestaurante.asossiarFormaPagamento(restauranteId, idFormaPagamento);
    }
}
