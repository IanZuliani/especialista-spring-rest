package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("from pedido p join fetch p.cliente join fetch p.restaurante join fetch r.cozinha")
    public List<Pedido> findAll();
}
