package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.assembler.PermissaoModelAssemble;
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.service.CadastroGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController {

    @Autowired
    private PermissaoModelAssemble assemble;
    @Autowired
    private CadastroGrupoService grupoService;
    @Autowired
    private AlgaLinks algaLinks;


    @GetMapping
    public CollectionModel<PermissaoModel> listar(@PathVariable Long grupoId) {
        Grupo grupo = grupoService.buscarOuFalhar(grupoId);

        CollectionModel<PermissaoModel> permissoesModel
                = assemble.toCollectionModel(grupo.getPermissoes())
                .removeLinks()
                .add(algaLinks.linkToGrupoPermissoes(grupoId))
                .add(algaLinks.linkToGrupoPermissaoAssociacao(grupoId, "associar"));

        permissoesModel.getContent().forEach(permissaoModel -> {
            permissaoModel.add(algaLinks.linkToGrupoPermissaoDesassociacao(
                    grupoId, permissaoModel.getId(), "desassociar"));
        });

        return permissoesModel;
    }
  /*  @GetMapping
    public List<PermissaoModel> listar(@PathVariable Long grupoId){
        var grupo = grupoService.buscarOuFalhar(grupoId);
        return assemble.toCollectionModel(grupo.getPermissoes());

    }*/

    @DeleteMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId){
        grupoService.desassociarPermissao(grupoId, permissaoId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void>  associar(@PathVariable Long grupoId, @PathVariable Long permissaoId){
        grupoService.associarPermissao(grupoId, permissaoId);
        return ResponseEntity.noContent().build();
    }
}
