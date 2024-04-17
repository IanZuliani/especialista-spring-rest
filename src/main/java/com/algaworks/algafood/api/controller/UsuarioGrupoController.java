package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.GrupoModelAssemble;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios/{userId}/grupos")
public class UsuarioGrupoController {

    @Autowired
    private CadastroUsuarioService usuarioService;

    @Autowired
    private GrupoModelAssemble assemble;

    @GetMapping
    public List<GrupoModel> listar(@PathVariable Long userId){
        var user = usuarioService.buscarOuFalhar(userId);
        return assemble.toCollectionModel(user.getGrupos());
    }

    //desassociar grupo
    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desassociar(@PathVariable Long userId, @PathVariable Long grupoId){
        usuarioService.desassociarGrupo(userId, grupoId);

    }

    //associar Grupo
    @PutMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associarGrupo(@PathVariable Long userId, @PathVariable Long grupoId){
        usuarioService.adicionarGrupo(userId, grupoId);

    }



}
