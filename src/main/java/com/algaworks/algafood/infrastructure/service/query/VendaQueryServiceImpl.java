package com.algaworks.algafood.infrastructure.service.query;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.StatusPedido;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.service.VendaQueryService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {

    @PersistenceContext
    private EntityManager manager;//a partir do EntityManager conseguimos dar um criteriaBuilder

    @Override
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro,String timeOffset) {

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
         * Funcao para alterar a data para o horario de Brasilia Brazil com offset +3
         * convert_tz -> nome da funcao no Mysql
         * root.get("dataCriacao") -> Data Criacao do pedido
         * Date.class -> tipo esperado
         * builder.literal("+00:00") -> Primeiro argumento e um Literal de string, Pos espera uma Expression
         * Nossa Api tem que ser Flexivel, por isso nao vamos colocar o codigo do timezone de Brasilia,
         * Tera qye ser passado Pelo Controllador, que recebe como argumento pelo Responsavel Pela API
         *
         */
        var functionConvertTzDataCriacao = builder.function(
                "convert_tz",Date.class, root.get("dataCriacao"),  builder.literal("+00:00"), builder.literal(timeOffset));



        /**
         * O resultado da pesquisa ele vai ser usado para chamar o construtor de uma classe
         * Qual classe
         * Venda Diaria
         * Para cada linha queremos transformar em um venda diaria.
         * Temos que passar o construction da classe venda diaria
         * 1  - data_criacao -> ai ja e mas complicado visto que o nao tem a funcao date() do sql
         * Vamos ter que criar uma funcao separada para poder utilizar ela
         * var functioDateDataCroacao = builder.function()
         *  1 - Parametro e o nome da funcao no bÀ medida que a adoção da extensão cresce, poderemos adicionar suporte para WebStorm no futuro.anco "date"
         *  2 - Parametro quando executamos essa funcao que tipo de retorno queremos - LocalDate
         *  3 - Quais sao os argumentos a funcao, Podemos ter mas de 1 - nesse caso e dataCriacao
         * 2 - totalVendas -> Faz um count no id, utilizando builder.count - root e o pedido
         * entao pegamos o id dele
         * 3 - totalFaturado -> Faz o mesmo root.get("valorTotal")
         */

        var functioDateDataCriacao = builder.function("date",
                Date.class,
                functionConvertTzDataCriacao);


        var selection = builder.construct(VendaDiaria.class,
                functioDateDataCriacao,
                builder.count(root.get("id")),
                builder.sum(root.get("valorTotal")));

        /**
         * Criando os filtros
         * status = CONFIRMADO, ENTREGUE
         */

        var predicates = new ArrayList<Predicate>();



        if(filtro.getRestauranteId() != null){
            predicates.add(builder.equal(root.get("id"), filtro.getRestauranteId()));
        }
        if (filtro.getDataCriacaoInicio() != null){
            predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
        }
        if (filtro.getDataCriacaoFim() != null){
            predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoFim()));
        }

        predicates.add(root.get("status").in(
                StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE));


        query.where(predicates.toArray(new Predicate[0]));

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
