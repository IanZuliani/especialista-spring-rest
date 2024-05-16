package com.algaworks.algafood.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {FileSizeValidator.class}
)
/**
 * Anotacao para validar o tamanho de arquivo passada na API
 * @FileSize(max = "20KB") -> campo tem que ser do tipo MultipartFile
 */
public @interface FileSize {

    /**
     * Mensagem Padrao
     */
    String message() default "tamanho do arquivo invalido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     *Vamos passar o tamanho do arquivo que queremos, e vai cair nessa propriedade
     * Appos isso vamos fazer toda a validacao dentro da classe FileSizeValidator
     */
   String max();
}
