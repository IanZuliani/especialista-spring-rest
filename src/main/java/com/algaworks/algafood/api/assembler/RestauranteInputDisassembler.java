package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.input.Restauranteinput;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestauranteInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;
    public Restaurante toDomainObject(Restauranteinput restauranteinput){
       return modelMapper.map(restauranteinput, Restaurante.class);
    }
}
