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

        System.out.println(" Entrou aqui");
        return estadoRepository.listar();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Estado> buscar(@PathVariable Long id){

       Estado estado = estadoRepository.buscar(id);
       if(estado != null){
           return ResponseEntity.ok(estado);
       }

       return ResponseEntity.notFound().build();

    }

    @PostMapping
    public ResponseEntity<Estado> adicionar(@RequestBody Estado estado){
            Estado estadoSalvo = cadastroEstado.salvar(estado);
            return ResponseEntity.ok(estadoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Estado estado){

            Estado estadoAtual = estadoRepository.buscar(id);

            if (estadoAtual != null){
                BeanUtils.copyProperties(estado, estadoAtual, "id");
                estadoAtual = cadastroEstado.salvar(estadoAtual);
                return ResponseEntity.ok(estadoAtual);
            }
           return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try {
            cadastroEstado.excluir(id);
            return ResponseEntity.noContent().build();
        }catch (EntidadeEmUsoException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e);
        }catch (EndidadeNaoEncontradaException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }
}
