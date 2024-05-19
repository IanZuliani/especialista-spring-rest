package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * ProdutorepositoryQueries -> Por nao termos um repository para FotoProduto criamos uma interface
 * Com um metodo que vai ter que ser desenvolvido pelo agregate root
 */
public interface ProdutoRepository extends JpaRepository<Produto, Long>, ProdutorepositoryQueries {

    //Busca o produto passando o id do restaurante e o id do produto
    @Query("from Produto where restaurante.id = :restaurante and id = :produto")
    Optional<Produto> findById(@Param("restaurante") Long restauranteId, @Param("produto") Long produtoId);

    //buscar uma lista de produtos passando o id do restaurante
    List<Produto> findTodosByRestaurante(Restaurante restaurante);

    //Buscar pelos ati
    @Query("from Produto p where p.ativo = true and p.restaurante = :restaurante")
    List<Produto> findByAtivobyRestaurante(Restaurante restaurante);


}
