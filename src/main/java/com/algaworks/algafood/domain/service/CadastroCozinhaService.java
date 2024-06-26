package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradoException;
import com.algaworks.algafood.domain.exception.EndidadeNaoEncontradaException;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CadastroCozinhaService {

    public static final String MSG_COZINHA_NAO_ENCONTRADA = "Nao existe um cadastro de cozinha com o codigo %d ";
    public static final String MSG_COZINHA_EM_USO = "Cozinha de codigo %d nao pode ser removida pos esta emuso";
    @Autowired
    private CozinhaRepository cozinhaRepository;
    @Transactional
    public Cozinha salvar(Cozinha cozinha){
        return cozinhaRepository.save(cozinha);
    }

    @Transactional
    public void excluir(Long id){
        try {
            cozinhaRepository.deleteById(id);
            cozinhaRepository.flush();
        }catch (EmptyResultDataAccessException e){
            throw new CozinhaNaoEncontradoException(id);
        }catch (DataIntegrityViolationException e){
           throw new EntidadeEmUsoException(
                   String.format(MSG_COZINHA_EM_USO, id));
        }
    }

    public Cozinha buscarOuFalhar(Long cozinhaId){
        return cozinhaRepository.findById(cozinhaId)
                .orElseThrow(()-> new CozinhaNaoEncontradoException(cozinhaId));
    }
}
