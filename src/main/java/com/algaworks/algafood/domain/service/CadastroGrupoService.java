package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.GrupoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroGrupoService {

    private static final String MSG_GRUPO_EM_USO = "Grupo de codigo %d nao pode ser removido pois esta em uso";
    @Autowired
    private GrupoRepository repository;

    @Transactional
    public Grupo save(Grupo grupo){
        return repository.save(grupo);
    }

    @Transactional
    public void delete(Long id){
        try {
            var grupo = buscarOuFalhar(id);
            repository.delete(grupo);
        }catch (EmptyResultDataAccessException e){
            throw new GrupoNaoEncontradoException(id);
        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(
                    String.format(MSG_GRUPO_EM_USO, id));
        }
    }

    public Grupo buscarOuFalhar(Long id){
        return repository.findById(id).orElseThrow( ()-> new GrupoNaoEncontradoException(id));
    }
}
