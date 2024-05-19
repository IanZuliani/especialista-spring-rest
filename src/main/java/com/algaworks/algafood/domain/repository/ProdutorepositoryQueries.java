package com.algaworks.algafood.domain.repository;


import com.algaworks.algafood.domain.model.FotoProduto;

/**\
 * Vamos criar essa interface pos a classe FotoProduto nao possui um repository,
 * Por ser um agregate, tem que utilizar o repository do agregate root
 * No caso e Produto
 * Vamos criar uma interface para fazer os metodos de save e persistencia no banco de dados
 * Para a classe FotoProduto
 */
public interface ProdutorepositoryQueries {

   FotoProduto save(FotoProduto foto);
   void delete(FotoProduto foto);
}
