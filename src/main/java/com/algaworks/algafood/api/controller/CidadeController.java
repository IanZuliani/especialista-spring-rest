package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.algaworks.algafood.domain.service.CadastroCidadeService;
import com.algaworks.algafood.domain.exception.EndidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;

import java.util.Optional;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

    @Autowired
    private CadastroCidadeService cadastroCidade;
    @Autowired
    private CidadeRepository cidadeRepository;

    @GetMapping("/{cidadeId}")
    public ResponseEntity<Cidade> buscar(@PathVariable Long cidadeId) {
        var cidade = cidadeRepository.findById(cidadeId);

        if(cidade.isPresent()){
            return ResponseEntity.ok(cidade.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
        try {
            cidade = cadastroCidade.salvar(cidade);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(cidade);
        } catch (EndidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{cidadeAId}")
    public ResponseEntity<Cidade> atualizar(@PathVariable Long cidadeId,
                                            @RequestBody Cidade cidade) {
        var cidadeAtual = cidadeRepository.findById(cidadeId);

        if (cidadeAtual.isPresent()) {
            BeanUtils.copyProperties(cidade, cidadeAtual.get(), "id");

           var cidadeSalva = cadastroCidade.salvar(cidadeAtual.get());
            return ResponseEntity.ok(cidadeSalva);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{cidadeAId}")
    public ResponseEntity<?> remover(@PathVariable Long cidadeAId) {
        try {
            cadastroCidade.excluir(cidadeAId);
            return ResponseEntity.noContent().build();

        } catch (EndidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();

        } catch (EntidadeEmUsoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }


}
