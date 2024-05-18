package com.algaworks.algafood.core.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = { FileSizeValidator.class })
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
