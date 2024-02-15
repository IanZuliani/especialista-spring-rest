package com.algaworks.algafood.jpa;

import com.algaworks.algafood.domain.model.Cozinha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Component
public class CadastroCozinha {

    @PersistenceContext //a notacao do JPA para instanciarmos um EntityManager
    private EntityManager manager;
    public List<Cozinha> listar(){
       TypedQuery<Cozinha> query =  manager.createQuery("from Cozinha", Cozinha.class);

       return query.getResultList();
    }
}
