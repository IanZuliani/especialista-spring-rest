package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.FormaPagamentoAssemble;
import com.algaworks.algafood.api.assembler.FormaPagamentoDisassemble;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioExceptional;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.service.FormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController("/formaspagamento")
public class FormaPagamentoController {

    @Autowired
    private FormaPagamentoRepository repository;

    @Autowired
    private FormaPagamentoService cadastroFormaPagamento;

    @Autowired
    private FormaPagamentoAssemble assemble;

    @Autowired
    private FormaPagamentoDisassemble disassemble;

    //findall

    @GetMapping
    public List<FormaPagamentoModel> findAll(){
        return assemble.toCollectionModel(repository.findAll());
    }

    //findByid
    @GetMapping("/{id}")
    public FormaPagamentoModel findById(@PathVariable Long id){
        return assemble.toModel(cadastroFormaPagamento.buscarOuFalhar(id));
    }

    //post
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoModel save(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput){
            var formaPagamento = disassemble.toDomainObjetct(formaPagamentoInput);
            return assemble.toModel(cadastroFormaPagamento.salvar(formaPagamento));
    }

    //put
    @PutMapping("/{formaPagamentoId}")
    public FormaPagamentoModel atualizar(@PathVariable Long formaPagamentoId,
                                         @RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        var formaPagamentoAtual = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);

        disassemble.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);

        formaPagamentoAtual = cadastroFormaPagamento.salvar(formaPagamentoAtual);

        return assemble.toModel(formaPagamentoAtual);
    }
    //delete
    @DeleteMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long formaPagamentoId) {
        cadastroFormaPagamento.excluir(formaPagamentoId);
    }
}
