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
        return estadoRepository.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Estado> buscar(@PathVariable Long id){

       var estado = estadoRepository.findById(id);
       if(estado.isPresent()){
           return ResponseEntity.ok(estado.get());
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

            var estadoAtual = estadoRepository.findById(id);

            if (estadoAtual.isPresent()){
                BeanUtils.copyProperties(estado, estadoAtual.get(), "id");
                var estadoSalvo = cadastroEstado.salvar(estadoAtual.get());
                return ResponseEntity.ok(estadoSalvo);
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
