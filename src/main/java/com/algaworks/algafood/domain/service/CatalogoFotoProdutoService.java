package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CatalogoFotoProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;


    @Transactional
    public FotoProduto salvar(FotoProduto foto){
        /**
         * Excluir foto se existir
         */
        Long restauranteId = foto.getRestauranteId();
        Long proditoId = foto.getProduto().getId();
        Optional<FotoProduto> fotoExiste = produtoRepository.findFotoById(restauranteId,
                proditoId);

        //se existir a foto chama o metodo delete
        if(fotoExiste.isPresent()){
            produtoRepository.delete(fotoExiste.get());
        }

        return produtoRepository.save(foto);
    }
}
