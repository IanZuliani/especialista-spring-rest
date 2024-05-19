package com.algaworks.algafood.domain.model;

import com.algaworks.algafood.core.validation.Groups;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class FotoProduto {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "produto_id")//nosso id da tabela fotoProduto se chama produto_id
    private Long id;

    /**
     * Facilita para gente ter o produto, caso queremos buscar a foto e saber qual e o produto
     * Nesse caso nao colocamos @JoinColumn(name = "produto_id")
     * pos ja foi feito o mapeamento no atributo private Long id; da nossa entidade
     * Oque colocamos e a anotacao @MapsId  Estamos dizendo que a propriedade produto e mapeada atraves do id da entidade FotoProduto
     * e colocamos tambem fetch = FetchType.LAZY -> A maioria das vezes que buscarmos uma Foto Produto nao vamos precisar do produto
     * Lazy para evitar selects disnecessarios
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Produto produto;

    @NotNull
    private String nomeArquivo;
    private String descricao;
    @NotNull
    private String contentType;
    private Long tamanho;


}
