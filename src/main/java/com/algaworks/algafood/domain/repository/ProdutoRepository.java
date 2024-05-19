package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.FotoProduto;
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

    /**
     * Verifica se existe uma foto ja cadastrada para o produto
     * 5. Vamos sempre buscar a foto de um produto Passando RestauranteId e ProdutoId
     * 6. Nao apenas pelo produto, por que esse produto pode nao ser do restaurante
     * 8. Mas temos que pegar o restaurante que temos dentro da tabela Produto, entao vamos fazer um Join
     * 9. FotoProduto f join f.produto p
     * 10. Vamos informar que o [restaurante.id](http://restaurante.id) dentro de produto = restauranteId da funcao
     *
     * select f from → que queremos os dados apenas de fotoProduto
     *
     */
    @Query("select f from FotoProduto f join f.produto p where p.restaurante.id = :restauranteId and f.produto.id = :produtoId")
    Optional<FotoProduto> findFotoById(Long restauranteId, Long produtoId);

}
