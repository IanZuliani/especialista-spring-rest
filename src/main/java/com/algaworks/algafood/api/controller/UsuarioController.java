package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.UsuarioInputDisassembler;
import com.algaworks.algafood.api.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.api.model.input.SenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {

    @Autowired
    private CadastroUsuarioService service;
    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private UsuarioModelAssembler assembler;
    @Autowired
    private UsuarioInputDisassembler disassembler;


    //listar
    @GetMapping
    public List<UsuarioModel> list(){
        return assembler.toCollectionModel(repository.findAll());
    }
    //buscar
    @GetMapping("/{id}")
    public UsuarioModel findById(@PathVariable Long id){
        return assembler.toModel(service.buscarOuFalhar(id));
    }
    //adicionar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioModel adicionar(@RequestBody @Valid UsuarioComSenhaInput usuario){
        var usuarioSave = disassembler.toDomainObject(usuario);
        return assembler.toModel(service.save(usuarioSave));
    }
    //atualizar

    @PutMapping("/{id}")
    public UsuarioModel atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioInput usuarioInput){
        var usuarioAtual = service.buscarOuFalhar(id);
        disassembler.copyToDomainObject(usuarioInput, usuarioAtual);
        usuarioAtual = service.save(usuarioAtual);

        return assembler.toModel(usuarioAtual);
    }

    //alterar senha

    @PutMapping("/{id}/senha")
    public void changePassword(@PathVariable Long id, @RequestBody @Valid SenhaInput senha){
        service.alterarSenha(id, senha.getSenhaAtual(), senha.getNovaSenha());
    }
}
