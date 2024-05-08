package com.algaworks.algafood.infrastructure.service;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.service.VendaQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {

    @PersistenceContext
    private EntityManager manager;//a partir do EntityManager conseguimos dar um criteriaBuilder

    @Override
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro) {

        /**
         * Obtemos uma instancia de CriteriaBuilder que e utilizada para criar
         * querys predicates funcao de agregacao etc
         */
        var builder = manager.getCriteriaBuilder();
        /**
         * Estamos criando um criterya builder que retorna um tipo especificado.
         * Nao estamos expecificando o FROM e sim apemas o retorno que queremos
         */
        var query = builder.createQuery(VendaDiaria.class);

        /**
         * Aqui informamos a tabela que queremos buscar as informacoes
         */
        var root = query.from(Pedido.class);

        /**
         * O resultado da pesquisa ele vai ser usado para chamar o construtor de uma classe
         * Qual classe
         * Venda Diaria
         * Para cada linha queremos transformar em um venda diaria.
         * Temos que passar o construction da classe venda diaria
         * 1  - data_criacao -> ai ja e mas complicado visto que o nao tem a funcao date() do sql
         * Vamos ter que criar uma funcao separada para poder utilizar ela
         * var functioDateDataCroacao = builder.function()
         *  1 - Parametro e o nome da funcao no banco "date"
         *  2 - Parametro quando executamos essa funcao que tipo de retorno queremos - LocalDate
         *  3 - Quais sao os argumentos a funcao, Podemos ter mas de 1 - nesse caso e dataCriacao
         * 2 - totalVendas -> Faz um count no id, utilizando builder.count - root e o pedido
         * entao pegamos o id dele
         * 3 - totalFaturado -> Faz o mesmo root.get("valorTotal")
         */

        var functioDateDataCriacao = builder.function("date",
                Date.class,
                root.get("dataCriacao"));

        var selection = builder.construct(VendaDiaria.class,
                functioDateDataCriacao,
                builder.count(root.get("id")),
                builder.sum(root.get("valorTotal")));

        /**
         * Vamos para proximo passo que e fazer o select no banco com o que queremos selecionar, que no caso e o retorno
         * do selection         *
         */
        query.select(selection);

        /**
         * Agora Fazemos o group By que utiliza a mesma funcao date(p.dataCriacao) igual do select, por isso fizemos
         * a funcao functioDateDataCriacao separada
         */
        query.groupBy(functioDateDataCriacao);

        /**
         * Damo um createQuery passando a query e pegando o resultList que ja nos retorna um tipo Venda diaria.
         */
        return manager.createQuery(query).getResultList();

    }
}
