package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EndidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CadastroRestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    public Restaurante salvar(Restaurante restaurante){
        var cozinhaID = restaurante.getCozinha().getId();
        var cozinha = cozinhaRepository.findById(cozinhaID)
                .orElseThrow( ()-> new EndidadeNaoEncontradaException(
                        String.format("Nao existe cadastro de cozinha com o codigo %d",cozinhaID)));

        restaurante.setCozinha(cozinha);
        return restauranteRepository.save(restaurante);
    }

}
