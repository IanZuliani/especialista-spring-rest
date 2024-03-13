package com.algaworks.algafood.api.controller;


import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradoException;
import com.algaworks.algafood.domain.exception.EndidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioExceptional;
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
    public Restaurante buscar(@PathVariable long id){
        return cadastroRestaurante.buscarOuFalhar(id);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurante adicionar (@RequestBody Restaurante restaurante){
        try {
            return cadastroRestaurante.salvar(restaurante);
        } catch (CozinhaNaoEncontradoException e) {
            throw new NegocioExceptional(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Restaurante editar(@PathVariable long id, @RequestBody Restaurante restaurante){

        var restauranteAtual = cadastroRestaurante.buscarOuFalhar(id);

        BeanUtils.copyProperties(restaurante, restauranteAtual,
                    "id", "formaPagamento", "endereco","dataCadastro", "produtos");

        try {

            return cadastroRestaurante.salvar(restauranteAtual);
        }catch (CozinhaNaoEncontradoException e){
            throw new NegocioExceptional(e.getMessage());
        }

    }

    @PatchMapping("/{restauranteId}")
    public Restaurante atualizarParcial(@PathVariable Long restauranteId,
                                              @RequestBody Map<String, Object> campos) {
        Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);

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
