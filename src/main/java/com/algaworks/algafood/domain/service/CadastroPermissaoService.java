package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.GrupoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.PermissaoNaoEncontradaException;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.transaction.Transactional;

public class CadastroPermissaoService {

    private static final String MSG_GRUPO_EM_USO = "Permissao de codigo %d nao pode ser removido pois esta em uso";

    @Autowired
    private PermissaoRepository repository;


    @Transactional
    public Permissao save(Permissao permissao){
        return repository.save(permissao);
    }

    @Transactional
    public void delete(Long id){
        try {
            var permissao = buscarOuFalhar(id);
            repository.delete(permissao);
        }catch (EmptyResultDataAccessException e){
            throw new PermissaoNaoEncontradaException(id);
        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(
                    String.format(MSG_GRUPO_EM_USO, id));
        }
    }

    public Permissao buscarOuFalhar(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new PermissaoNaoEncontradaException(id));
    }
}
