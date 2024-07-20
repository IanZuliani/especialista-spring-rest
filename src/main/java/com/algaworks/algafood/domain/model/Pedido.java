package com.algaworks.algafood.domain.model;

import com.algaworks.algafood.domain.event.PedidoCalceladoEvent;
import com.algaworks.algafood.domain.event.PedidoConfirmadoEvent;
import com.algaworks.algafood.domain.exception.NegocioException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 1. Para dispararmos eventos vamos extender uma classe dentro da entidade Pedido chamada
 * 2. `AbstractAggregateRoot<Pedido>` → no argumento Passamos a propria classe.
 * Caso nao queremos gerar um Equals And Hash code da Super classe apenas da classe que estamos colocamos
 * a anotacao callSuper = false
 *
 */

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
public class Pedido extends AbstractAggregateRoot<Pedido> {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private BigDecimal subtotal;
    private BigDecimal taxaFrete;
    private BigDecimal valorTotal;

    @Embedded
    private Endereco enderecoEntrega;

    @Enumerated(EnumType.STRING)
    private StatusPedido status = StatusPedido.CRIADO;

    @CreationTimestamp
    private OffsetDateTime dataCriacao;
    private OffsetDateTime dataConfirmacao;
    private OffsetDateTime dataCancelamento;
    private OffsetDateTime dataEntrega;




    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private FormaPagamento formaPagamento;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Restaurante restaurante;

    @ManyToOne
    @JoinColumn(name = "usuario_cliente_id", nullable = false)
    private Usuario cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens = new ArrayList<>();

    public void calcularValorTotal() {
        getItens().forEach(ItemPedido::calcularPrecoTotal);

        this.subtotal = getItens().stream()
                .map(itens -> itens.getPrecoTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.valorTotal = this.subtotal.add(this.taxaFrete);
    }

    /**
     * Mesmo nao chamando o metodo save, o metodo confirmar faz alteracoes de uma instancia que esta sendo gerenciada
     * pelo Entity Manager do JPA
     * Mas precisamos do metodo save para disparar o evento
     */
    public void confirmar(){
        setStatus(StatusPedido.CONFIRMADO);
        setDataConfirmacao(OffsetDateTime.now());

        /**
         * Criando uma instancia de evento, com todas as informacoes do evento que acabou de acontecer
         * Caso e confirmar o pedido
         * Para utilziar, temos que ter uma classe que representa o Evento
         * this -> instancia atual do pedido
         * Nesse momento nao estamos disparando o evento, estamos registrando um evento que deve ser disparado
         * assim que o objeto dessa entidade Pedido for salvo no repositorio
         */
        registerEvent(new PedidoConfirmadoEvent(this));
    }
    public void entregar(){
        setStatus(StatusPedido.ENTREGUE);
        setDataEntrega(OffsetDateTime.now());
    }
    public void cancelar(){
        setStatus(StatusPedido.CANCELADO);
        setDataCancelamento(OffsetDateTime.now());

        registerEvent(new PedidoCalceladoEvent(this));
    }

    private void setStatus(StatusPedido novoStatus) {
        if (getStatus().naoPodeAlterarPara(novoStatus)) {
            throw new NegocioException(
                    String.format("Status do pedido %s não pode ser alterado de %s para %s",
                            getCodigo(), getStatus().getDescricao(),
                            novoStatus.getDescricao()));
        }

        this.status = novoStatus;
    }


    /**
     * Metodos que verifica se o pedido pode ser confirmado entegue ou cancelado.
     * @return
     */
    public boolean podeSerConfirmado(){
        return getStatus().podeAlterarPara(StatusPedido.CONFIRMADO);
    }

    public boolean podeSerEntregue(){
        return getStatus().podeAlterarPara(StatusPedido.ENTREGUE);
    }

    public boolean podeSerCancelado(){
        return getStatus().podeAlterarPara(StatusPedido.CANCELADO);
    }

    @PrePersist
    private void gerarCodigo(){
        setCodigo(UUID.randomUUID().toString());
    }
   }



