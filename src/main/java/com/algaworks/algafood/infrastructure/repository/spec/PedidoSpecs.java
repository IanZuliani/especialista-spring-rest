package com.algaworks.algafood.infrastructure.repository.spec;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;

public class PedidoSpecs {

    public static Specification<Pedido> usandoFiltro(PedidoFilter filtro) {
        return (root, query, builder) -> {

            /*
             * Esse if foid feito, para que apos a paginacao vir aqui buscar os dados, logo depos vai voltar para verificar
             * a quantidade de Registro para retornar
             * nao tem como fazer um fetch num count
             * Se for um tipo pedido faz um fetch se nao for, nao faz
             */
            if (Pedido.class.equals(query.getResultType())) {
                root.fetch("restaurante").fetch("cozinha");
                root.fetch("cliente");
            }
            // from Pedido p join fetch p.cliente

            //vamos ter uma Lista de predicate
            var predicates = new ArrayList<Predicate>();

            //adicionar Predicates no ArrayList

            //Verificando se o clienteId nao for nulo vamos add um predicate a lista
            if (filtro.getClienteId() != null) {
                /*
                    Construindo um predicate de igualdade clienteId = Cliente.id
                    root.get("cliente") -> nome da propriedade que queremos filtrar na classe Pedido
                    filtro.getClienteId() -> QUal o valor que queremos que seja filtrado
                 */
                predicates.add(builder.equal(root.get("cliente"), filtro.getClienteId()));
            }
            if (filtro.getRestauranteID() != null) {
                //Pegar Pelo ID do restaurante
                predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteID()));
            }
            /*
                Pegar Pela data De criacao do pedido tem que ser maior que a data de criacaoInicio ou igual e para isso
                Utilizamos a funcao
                greaterThanOrEqualTo
                dataCriaao -> campo na entidade Pedido
             */

            if (filtro.getDataCriacaoInicio() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"),
                        filtro.getDataCriacaoInicio()));
            }
            /*
                Pegar Pela data De criacao do pedido tem que ser menor que a data de criacaoInicio ou igual e para isso
                Utilizamos a funcao
                lessThanOrEqualTo
                dataCriaao -> campo na entidade Pedido
             */
            if (filtro.getDataCriacaoFim() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"),
                        filtro.getDataCriacaoFim()));
            }

            //vamos pegar todos esses predicates do arrayList pegar o buil chamando o end passando o array de predicates
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
