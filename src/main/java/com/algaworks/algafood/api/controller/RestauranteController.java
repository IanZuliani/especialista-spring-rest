package com.algaworks.algafood.api.controller;


import com.algaworks.algafood.domain.exception.EndidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestaurante;
    @GetMapping
    public List<Restaurante> listar(){
        return restauranteRepository.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscar(@PathVariable long id){
        Restaurante restaurante = restauranteRepository.buscar(id);

        if(restaurante != null){
            return ResponseEntity.ok(restaurante);
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> adicionar (@RequestBody Restaurante restaurante){
        try {
            Restaurante restauranteSalvo =  cadastroRestaurante.salvar(restaurante);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(restauranteSalvo);
        }catch (EndidadeNaoEncontradaException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable long id, @RequestBody Restaurante restaurante){
        try{
            Restaurante restauranteAtual = restauranteRepository.buscar(id);

             if (restauranteAtual != null){

                BeanUtils.copyProperties(restaurante, restauranteAtual, "id");
                restauranteAtual = restauranteRepository.salvar(restauranteAtual);
                return ResponseEntity.ok(restauranteAtual);
            }
            return ResponseEntity.notFound().build();
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }


    }

    @PatchMapping("/id")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> campos){
        Restaurante restauranteAtual = restauranteRepository.buscar(id);

        if(restauranteAtual == null){
            return ResponseEntity.notFound().build();
        }

        merge(campos, restauranteAtual);

        return editar(id, restauranteAtual);
    }

    private static void merge(Map<String, Object> campos, Restaurante restauranteDestino) {
        campos.forEach((nome, valor)->{
            System.out.println(nome +" = "+ valor);
        });
    }


}
