package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EndidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CadastroRestauranteService {

    public static final String MSG_RESTAURANTE_NAO_ENCONTRADO = "Nao existe cadastro de cozinha com o codigo %d";
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroCozinhaService cozinhaService;

    @Autowired
    private CadastroCidadeService cadastroCidade;

    @Transactional
    public Restaurante salvar(Restaurante restaurante){
        var cozinhaID = restaurante.getCozinha().getId();
        var cidadeId = restaurante.getEndereco().getCidade().getId();

        var cozinha = cozinhaService.buscarOuFalhar(cozinhaID);

        var cidade = cadastroCidade.buscarOuFalhar(cidadeId);

        /*var cozinha = cozinhaRepository.findById(cozinhaID)
                .orElseThrow( ()-> new EndidadeNaoEncontradaException(
                        String.format(MSG_RESTAURANTE_NAO_ENCONTRADO,cozinhaID)));*/

        restaurante.setCozinha(cozinha);
        restaurante.getEndereco().setCidade(cidade);
        return restauranteRepository.save(restaurante);
    }

    @Transactional
    public void ativar(Long id){
        var restauranteAtual = buscarOuFalhar(id);
        restauranteAtual.ativar();
    }

    @Transactional
    public void inativar(Long id){
        var restauranteAtual = buscarOuFalhar(id);
        restauranteAtual.inativar();
    }


    public Restaurante buscarOuFalhar(Long id){
        return restauranteRepository.findById(id).orElseThrow(()-> new RestauranteNaoEncontradoException(id));
    }

}
