package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.FormaPagamentoAssemble;
import com.algaworks.algafood.api.assembler.FormaPagamentoDisassemble;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.service.FormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {

    @Autowired
    private FormaPagamentoRepository repository;

    @Autowired
    private FormaPagamentoService cadastroFormaPagamento;

    @Autowired
    private FormaPagamentoAssemble assemble;

    @Autowired
    private FormaPagamentoDisassemble disassemble;

    @GetMapping
    public ResponseEntity<List<FormaPagamentoModel>> findAll(){

        List<FormaPagamento> todasFormasPagamentos = repository.findAll();

        List<FormaPagamentoModel> formaPagamentoModels =  assemble
                .toCollectionModel(todasFormasPagamentos);

        /**
         * Criando a resposta cacheada para que o navegador do cliente cacheie ela por 10 segundos
         * atraves de um parametro no header
         * Cache-control: max-age=10
         * Fazemos no HesponseEntity para poder alterar o cabecalho
         */
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                //.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePrivate())
                //.cacheControl(CacheControl.noCache())
                //.cacheControl(CacheControl.noStore())
                .body(formaPagamentoModels);

    }

    //findByid
    @GetMapping("/{id}")
    public ResponseEntity<FormaPagamentoModel> findById(@PathVariable Long id){
        var formaPagemento = assemble
                .toModel(cadastroFormaPagamento.buscarOuFalhar(id));
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
                .body(formaPagemento);
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
