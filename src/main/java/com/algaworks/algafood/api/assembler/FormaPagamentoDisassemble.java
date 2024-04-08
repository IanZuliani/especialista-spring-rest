package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormaPagamentoDisassemble {

    @Autowired
    private ModelMapper modelMapper;

    public FormaPagamento toDomainObjetct(FormaPagamentoInput input){
        return modelMapper.map(input, FormaPagamento.class);
    }

    public void copyToDomainObject(FormaPagamentoInput input, FormaPagamento formaPagamento){
        modelMapper.map(input, formaPagamento);
    }

}
