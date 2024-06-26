package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CadastroRestauranteService {

    public static final String MSG_RESTAURANTE_NAO_ENCONTRADO = "Nao existe cadastro de cozinha com o codigo %d";
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroCozinhaService cozinhaService;

    @Autowired
    private CadastroCidadeService cadastroCidade;

    @Autowired
    private FormaPagamentoService formaPagamentoService;

    @Autowired
    private CadastroUsuarioService usuarioService;

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

    @Transactional
    public void ativar(List<Long> restaurantesId){
        restaurantesId.forEach(this::ativar);
    }
    @Transactional
    public void inativar(List<Long> restaurantesId){
        restaurantesId.forEach(this::inativar);
    }


    @Transactional
    public void desassociarFormaPAgamento(Long restauranteId, Long formaPagamentoId){
        Restaurante restaurante = buscarOuFalhar(restauranteId);
        FormaPagamento formaPagamento = formaPagamentoService.buscarOuFalhar(formaPagamentoId);
        restaurante.removerFormaPagamento(formaPagamento);
    }

    @Transactional
    public void asossiarFormaPagamento(Long restauranteId, Long formaPagamentoId){
        Restaurante restaurante = buscarOuFalhar(restauranteId);
        FormaPagamento formaPagamento = formaPagamentoService.buscarOuFalhar(formaPagamentoId);
        restaurante.adicionarFormaPagamento(formaPagamento);
    }

    @Transactional
    public void desassociarResponsavel(Long restauranteId, Long usuarioId){
        var restaurante = buscarOuFalhar(restauranteId);
        var usuario = usuarioService.buscarOuFalhar(usuarioId);
        restaurante.removerResponsavel(usuario);
    }

    @Transactional
    public void associarResponsavel(Long restauranteId, Long usuarioId){
        var restaurante = buscarOuFalhar(restauranteId);
        var usuario = usuarioService.buscarOuFalhar(usuarioId);
        restaurante.adicionarResponsavel(usuario);
    }


    @Transactional
    public void abrir(Long restauranteId) {
        Restaurante restauranteAtual = buscarOuFalhar(restauranteId);

        restauranteAtual.abrir();
    }

    @Transactional
    public void fechar(Long restauranteId) {
        Restaurante restauranteAtual = buscarOuFalhar(restauranteId);

        restauranteAtual.fechar();
    }

    public Restaurante buscarOuFalhar(Long id){
        return restauranteRepository.findById(id).orElseThrow(()-> new RestauranteNaoEncontradoException(id));
    }



}
