package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.google.protobuf.Empty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FormaPagamentoService {
    public static final String MSG_FORMA_PAGAMENTO_EM_USO = "Forma de Pagamento de codigo %d nao pode ser removido pos esta em uso";
    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Transactional
    public FormaPagamento salvar(FormaPagamento formaPagamento){
        return formaPagamentoRepository.save(formaPagamento);
    }

    @Transactional
    public void excluir(Long id){
        try {
            formaPagamentoRepository.deleteById(id);
            formaPagamentoRepository.flush();
        }catch (EmptyResultDataAccessException e){
            throw new FormaPagamentoNaoEncontradaException(id);
        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(
                    String.format(MSG_FORMA_PAGAMENTO_EM_USO, id));
        }
    }

    public FormaPagamento buscarOuFalhar(Long id){
        return formaPagamentoRepository.findById(id).orElseThrow(()-> new FormaPagamentoNaoEncontradaException(id));
    }


}
