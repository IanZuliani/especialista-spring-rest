package com.algaworks.algafood.infrastructure.repository;

import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repository.ProdutorepositoryQueries;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe que faz a logica de negocio da nossa interface de ProdutorepositoryQueries
 * Estamos fazendo isso por que FotoProduto, nao possui uma entidade por nao ser agregateRoot
 * 1 - criar ProdutorepositoryQueries
 * 2 - Extender na classe ProdutorepositoryQueries
 * 3 - Criar a classe ProdutoRepositoryImpl implementando a primeira classe ProdutorepositoryQueries
 */
@Repository
public class ProdutoRepositoryImpl implements ProdutorepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Transactional
    @Override
    public FotoProduto save(FotoProduto foto) {
        return manager.merge(foto);
    }

    @Transactional
    @Override
    public void delete(FotoProduto foto) {
        manager.remove(foto);
    }
}
