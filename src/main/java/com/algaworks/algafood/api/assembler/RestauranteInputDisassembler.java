package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.input.Restauranteinput;
import com.algaworks.algafood.domain.model.Cidade;
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

    public void copyToDomainObject(Restauranteinput restauranteinput, Restaurante restaurante){

        /*
        - Para evitar essa exception
        - nested exception is org.hibernate.HibernateException: identifier of an instance of com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2
         */
        restaurante.setCozinha(new Cozinha());

        if(restaurante.getEndereco() != null){
            restaurante.getEndereco().setCidade(new Cidade());
        }

        modelMapper.map(restauranteinput, restaurante);
    }
}
