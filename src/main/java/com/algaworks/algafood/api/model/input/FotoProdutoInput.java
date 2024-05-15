package com.algaworks.algafood.api.model.input;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Classe quer representa nossa classe de upload
 *
 */
@Getter
@Setter
public class FotoProdutoInput {

    private MultipartFile arquivo;
    private String descricao;
}
