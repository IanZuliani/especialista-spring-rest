package com.algaworks.algafood.core.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Classe que converte de Page para pageable
 * @param <T>
 */
public class PageWrapper<T> extends PageImpl<T> {

    private Pageable pageable;
    /**
     * Vamos criar um construtor onde nos passamos uma lista do conteudo do Tipo T
     * page.getContent() -> conteudo
     * pageable → depos um pageable, que vamos receber como parametro
     *page.getTotalElements() → vamos receber o total de elementos do page
     * @param page
     */
    public PageWrapper(Page<T> page, Pageable pageable) {
        super(page.getContent(), pageable, page.getTotalElements());

        this.pageable = pageable;
    }

    /**
     * Vamos subistituir o pegeable que recebemos como parametro na funcao acima
     * @return
     */
    @Override
    public Pageable getPageable() {
        return this.pageable;
    }
}
