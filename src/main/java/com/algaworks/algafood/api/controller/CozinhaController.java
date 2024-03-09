package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EndidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping(value = "/cozinhas", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@RequestMapping(value = "/cozinhas")
public class CozinhaController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinha;

    //@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @GetMapping
    public List<Cozinha> listar(){
        return cozinhaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cozinha> buscar(@PathVariable long id){
        Optional<Cozinha> cozinnha = cozinhaRepository.findById(id);
        if(cozinnha.isPresent()){
            return ResponseEntity.ok(cozinnha.get());
        }
        return ResponseEntity.notFound().build();

    }

    //@GetMapping("/{id}")
    public ResponseEntity<Cozinha> getById(@PathVariable Long id){

        Optional<Cozinha> cozinha = cozinhaRepository.findById(id);
        //return ResponseEntity.status(HttpStatus.OK).body(cozinha);
        //return ResponseEntity.status(HttpStatus.OK).build();
        //return ResponseEntity.ok(cozinha);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "http://api.algafood.local:8080/cozinhas");

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .headers(headers)
                .build();
    }

    @PostMapping
    public ResponseEntity<Cozinha> adicionar(@RequestBody Cozinha cozinha){
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastroCozinha.salvar(cozinha));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Cozinha> atualizar(@PathVariable Long id, @RequestBody Cozinha cozinha){
        Optional<Cozinha> cozinhaAtual = cozinhaRepository.findById(id);

        if (cozinhaAtual != null) {
            //cozinhaAtual.setNome(cozinha.getNome());
            BeanUtils.copyProperties(cozinha, cozinhaAtual.get(), "id");

            Cozinha cozinhaSalva = cadastroCozinha.salvar(cozinhaAtual.get());
            return ResponseEntity.ok(cozinhaSalva);
        }
        return ResponseEntity.notFound().build();

    }

   /* @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        try {
            cadastroCozinha.excluir(id);
            return ResponseEntity.noContent().build();
        }catch (EntidadeEmUsoException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e);
        }catch (EndidadeNaoEncontradaException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }

    }*/

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        try{
            cadastroCozinha.excluir(id);
        }catch (EndidadeNaoEncontradaException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
            //throw new ServerWebInputException(e.getMessage());
        }
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
