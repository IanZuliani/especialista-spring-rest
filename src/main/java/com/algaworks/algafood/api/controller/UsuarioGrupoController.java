package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.assembler.GrupoModelAssemble;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/{userId}/grupos")
public class UsuarioGrupoController {

    @Autowired
    private CadastroUsuarioService usuarioService;

    @Autowired
    private GrupoModelAssemble assemble;

    @Autowired
    private AlgaLinks algaLinks;


    @GetMapping
    public CollectionModel<GrupoModel> listar(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioService.buscarOuFalhar(usuarioId);

        CollectionModel<GrupoModel> gruposModel = assemble.toCollectionModel(usuario.getGrupos())
                .removeLinks()
                .add(algaLinks.linkToUsuarioGrupoAssociacao(usuarioId, "associar"));

        gruposModel.getContent().forEach(grupoModel -> {
            grupoModel.add(algaLinks.linkToUsuarioGrupoDesassociacao(
                    usuarioId, grupoModel.getId(), "desassociar"));
        });

        return gruposModel;
    }

   /* @GetMapping
    public CollectionModel<GrupoModel> listar(@PathVariable Long userId){
        var user = usuarioService.buscarOuFalhar(userId);
        return assemble.toCollectionModel(user.getGrupos())
                .removeLinks();
    }*/

    //desassociar grupo
    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> desassociar(@PathVariable Long userId, @PathVariable Long grupoId){
        usuarioService.desassociarGrupo(userId, grupoId);
        return ResponseEntity.noContent().build();
    }

    //associar Grupo
    @PutMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associar(@PathVariable Long userId, @PathVariable Long grupoId){
        usuarioService.adicionarGrupo(userId, grupoId);
        return ResponseEntity.noContent().build();

    }



}
