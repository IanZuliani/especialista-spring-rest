package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.PermissaoModelAssemble;
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.domain.service.CadastroGrupoService;
import com.algaworks.algafood.domain.service.CadastroPermissaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController {

    @Autowired
    private PermissaoModelAssemble assemble;
    @Autowired
    private CadastroGrupoService grupoService;


    @GetMapping
    private List<PermissaoModel> listar(@PathVariable Long grupoId){
        var grupo = grupoService.buscarOuFalhar(grupoId);
        return assemble.toCollectionModel(grupo.getPermissoes());

    }

    @DeleteMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId){
        grupoService.desassociarPermissao(grupoId, permissaoId);
    }

    @PutMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void asossiarPermissao(@PathVariable Long grupoId, @PathVariable Long permissaoId){
        grupoService.associarPermissao(grupoId, permissaoId);
    }
}
