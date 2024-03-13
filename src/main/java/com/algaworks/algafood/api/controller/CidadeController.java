package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.exceptionHandler.Problema;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioExceptional;
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

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

    @Autowired
    private CadastroCidadeService cadastroCidade;
    @Autowired
    private CidadeRepository cidadeRepository;

    @GetMapping("/{cidadeId}")
    public Cidade buscar(@PathVariable Long cidadeId) {
       return cadastroCidade.buscarOuFalhar(cidadeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cidade adicionar(@RequestBody Cidade cidade) {
        try {
         return cadastroCidade.salvar(cidade);
        } catch (EstadoNaoEncontradoException e){
            throw new NegocioExceptional(e.getMessage(), e);
        }

    }

    @PutMapping("/{id}")
    public Cidade atualizar(@PathVariable Long id, @RequestBody Cidade cidade) {

        try {
            var cidadeAtual = cadastroCidade.buscarOuFalhar(id);

            BeanUtils.copyProperties(cidade, cidadeAtual, "id");
            return cadastroCidade.salvar(cidadeAtual);
        }catch (EstadoNaoEncontradoException e){
           throw new NegocioExceptional(e.getMessage(), e);
        }

    }

    @DeleteMapping("/{cidadeAId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cidadeAId) {
            cadastroCidade.excluir(cidadeAId);

    }
    @ExceptionHandler(EndidadeNaoEncontradaException.class)
    public ResponseEntity<?> tratarEstadoNaoEncontradoException(EndidadeNaoEncontradaException e){
        var problema = Problema.builder()
                .dataHora(LocalDateTime.now())
                .mensagem(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(problema);
    }

    @ExceptionHandler(NegocioExceptional.class)
    public ResponseEntity<?> tratarNegocioExceptional(NegocioExceptional e){
        var problema = Problema.builder()
                .dataHora(LocalDateTime.now())
                .mensagem(e.getMessage()).build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(problema);
    }

}
