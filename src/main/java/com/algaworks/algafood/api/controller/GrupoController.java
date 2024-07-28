package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.GrupoInputDisassembler;
import com.algaworks.algafood.api.assembler.GrupoModelAssemble;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import com.algaworks.algafood.domain.service.CadastroGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

    @Autowired
    private GrupoRepository repository;
    @Autowired
    private CadastroGrupoService service;
    @Autowired
    private GrupoInputDisassembler disassembler;
    @Autowired
    private GrupoModelAssemble assemble;

    //listar
    @GetMapping
    public CollectionModel<GrupoModel> listar() {
        List<Grupo> todosGrupos = repository.findAll();

        return assemble.toCollectionModel(todosGrupos);
    }
   /* @GetMapping
    public List<GrupoModel> findAll(){
        return assemble.toCollectionModel(repository.findAll());
    }*/


    //buscar id
    @GetMapping("/{id}")
    public GrupoModel findById(@PathVariable Long id){
        return assemble.toModel(service.buscarOuFalhar(id));
    }

    //adicionar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoModel add(@RequestBody @Valid GrupoInput grupoInput){
            var grupo = disassembler.toDomainObject(grupoInput);
            return assemble.toModel(service.save(grupo));
    }

    //atualizar
    @PutMapping("/{grupoId}")
    public GrupoModel atualizar(@PathVariable Long grupoId,
                                @RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupoAtual = service.buscarOuFalhar(grupoId);

        disassembler.copyToDomainObject(grupoInput, grupoAtual);

        grupoAtual = service.save(grupoAtual);

        return assemble.toModel(grupoAtual);
    }

    //Remover
    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long grupoId) {
        service.delete(grupoId);
    }


}

