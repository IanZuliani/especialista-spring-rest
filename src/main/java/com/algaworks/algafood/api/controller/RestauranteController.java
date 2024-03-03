package com.algaworks.algafood.api.controller;


import com.algaworks.algafood.domain.exception.EndidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
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

        return restauranteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscar(@PathVariable long id){
        var restaurante = restauranteRepository.findById(id);

        if(restaurante.isPresent()){
            return ResponseEntity.ok(restaurante.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
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
            var restauranteAtual = restauranteRepository.findById(id);

             if (restauranteAtual.isPresent()){

                BeanUtils.copyProperties(restaurante, restauranteAtual.get(),
                        "id", "formaPagamento", "endereco","dataCadastro", "produtos");
                var restauranteSalvo = cadastroRestaurante.salvar(restauranteAtual.get());
                return ResponseEntity.ok(restauranteSalvo);
            }
            return ResponseEntity.notFound().build();
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }


    }

    @PatchMapping("/{restauranteId}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId,
                                              @RequestBody Map<String, Object> campos) {
        var restauranteAtual = restauranteRepository.findById(restauranteId).orElse(null);
        System.out.println("Entrei no metodo");

        if(restauranteAtual == null){
            return ResponseEntity.notFound().build();
        }

        merge(campos, restauranteAtual);

        return editar(restauranteId, restauranteAtual);
    }

    private static void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
        ObjectMapper objectMapper = new ObjectMapper();
        Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

        dadosOrigem.forEach((nome, valor)->{
            Field field = ReflectionUtils.findField(Restaurante.class, nome);
            field.setAccessible(true);

            Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

            //System.out.println(nome +" = "+ valor);
            ReflectionUtils.setField(field, restauranteDestino, novoValor);

        });
    }


}
