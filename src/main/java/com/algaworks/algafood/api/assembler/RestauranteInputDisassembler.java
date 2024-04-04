package com.algaworks.algafood.api.assembler;

import com.algaworks.algafood.api.model.input.Restauranteinput;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.stereotype.Component;

@Component
public class RestauranteInputDisassembler {
    public Restaurante toDomainObject(Restauranteinput restauranteinput){
        var restaurante = new Restaurante();
        restaurante.setNome(restauranteinput.getNome());
        restaurante.setTaxaFrete(restauranteinput.getTaxaFrete());
        var cozinha = new Cozinha();
        cozinha.setId(restauranteinput.getCozinha().getId());
        restaurante.setCozinha(cozinha);

        return restaurante;

    }
}
