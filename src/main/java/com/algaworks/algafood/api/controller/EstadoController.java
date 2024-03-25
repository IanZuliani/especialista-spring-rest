package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EndidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.algaworks.algafood.domain.service.CadastroEstadoService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/estados")
public class EstadoController {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CadastroEstadoService cadastroEstado;

    @GetMapping
    public List<Estado> listar() {
        return estadoRepository.findAll();
    }


    @GetMapping("/{id}")
    public Estado buscar(@PathVariable Long id){

      return cadastroEstado.buscarOuFalhar(id);


    }

    @PostMapping
    public ResponseEntity<Estado> adicionar(@RequestBody @Valid Estado estado){
            Estado estadoSalvo = cadastroEstado.salvar(estado);
            return ResponseEntity.ok(estadoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody @Valid Estado estado){

            var estadoAtual = cadastroEstado.buscarOuFalhar(id);
            BeanUtils.copyProperties(estado, estadoAtual, "id");
            var estadoSalvo = cadastroEstado.salvar(estadoAtual);
            return ResponseEntity.ok(estadoSalvo);

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
           cadastroEstado.excluir(id);
    }
}
