package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EndidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CadastroEstadoService {

    public static final String MSG_ESTADO_NAO_ENCONTRADO = "Nao existe um cadastro de Estado com o codigo %d ";
    public static final String MSG_ESTADO_EM_USO = "Estado de codigo %d nao pode ser removido pos esta emuso";
    @Autowired
    private EstadoRepository estadoRepository;

    public Estado salvar(Estado estado){
        return estadoRepository.save(estado);
    }

    public void excluir(Long id){
        try {
            estadoRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new EndidadeNaoEncontradaException(
                    String.format(MSG_ESTADO_NAO_ENCONTRADO, id));
        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(
                    String.format(MSG_ESTADO_EM_USO, id));
        }
    }

    public Estado buscarOuFalhar(Long id){
        return estadoRepository.findById(id).orElseThrow(()-> new EndidadeNaoEncontradaException(
                String.format(MSG_ESTADO_NAO_ENCONTRADO, id)
        ));
    }

}
