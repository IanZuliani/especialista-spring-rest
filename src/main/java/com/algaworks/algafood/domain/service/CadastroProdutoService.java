package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CadastroProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Transactional
    public Produto salvar(Produto produto){
        return repository.save(produto);
    }

    public Produto buscarOuFalhar(Long restauranteId, Long produtoId){
        return repository.findById(restauranteId, produtoId).orElseThrow(()-> new ProdutoNaoEncontradoException(produtoId, restauranteId));
    }
}
