package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/responsaveis")
public class RestauranteUsuarioResponsavelController {

    @Autowired
    private CadastroRestauranteService restauranteService;
    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;

    @Autowired
    private AlgaLinks algaLinks;

 /*   @GetMapping
    public CollectionModel<UsuarioModel> findByRestauranteId(@PathVariable Long restauranteId) {
        var restaurante = restauranteService.buscarOuFalhar(restauranteId);
        return usuarioModelAssembler.toCollectionModel(restaurante.getResponsaveis())
                .removeLinks()
                .add(algaLinks.linkToRestauranteResponsaveis(restauranteId));
    }*/
 @GetMapping
 public CollectionModel<UsuarioModel> listar(@PathVariable Long restauranteId) {
     Restaurante restaurante = restauranteService.buscarOuFalhar(restauranteId);

     CollectionModel<UsuarioModel> usuariosModel = usuarioModelAssembler
             .toCollectionModel(restaurante.getResponsaveis())
             .removeLinks()
             .add(algaLinks.linkToRestauranteResponsaveis(restauranteId))
             .add(algaLinks.linkToRestauranteResponsavelAssociacao(restauranteId, "associar"));

     usuariosModel.getContent().stream().forEach(usuarioModel -> {
         usuarioModel.add(algaLinks.linkToRestauranteResponsavelDesassociacao(
                 restauranteId, usuarioModel.getId(), "desassociar"));
     });

     return usuariosModel;
 }

    @DeleteMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> desassociar(@PathVariable Long restauranteId, @PathVariable Long usuarioId){
        restauranteService.desassociarResponsavel(restauranteId, usuarioId);
        return ResponseEntity.noContent().build();
    }



    @PutMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associar(@PathVariable Long restauranteId, @PathVariable Long usuarioId){
        restauranteService.associarResponsavel(restauranteId, usuarioId);
        return ResponseEntity.noContent().build();
    }

}
