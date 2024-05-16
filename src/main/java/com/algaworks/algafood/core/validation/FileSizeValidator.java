package com.algaworks.algafood.core.validation;

import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * Essa classe e quem vai fazer a validacao da anotacoa
 * Nessa constrain passamos por parametro a anotacao que criamos e o tipo do campo que queremos que seja validado.
 * NAO INPORTA SE MANDAMOS 500KB OU 500MG VAI SER FEITO A CONVERSAO EM BYTES DA ANOTACAO
 * Anotacao ficaria
 *@FileSize(max = "20KB") -> campo tem que ser do tipo MultipartFile
 */
public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

    /**
     * DataSize -> Uma classe do spring que representa o tamanho do dado
     * Atraves dele podemos fazer o parse, ou conversao da string 500KB para Tantos Bytes
     */
    private DataSize maxSize;


    @Override
    public void initialize(FileSize constraintAnnotation) {
        /**
         *pegamos o maxsize, passamos a string que vem da constraintAnnotation.max() que e da nossa anotation
         *  Passando no metodo parse, para transformar string em Bytes
         *  Virando um DataSize
         */
        this.maxSize = DataSize.parse(constraintAnnotation.max());

    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext constraintValidatorContext) {
        /**
         *`value == null` → se o que esta vindo no campo e null ok esta correto, nao fazemos a validacao de campo null aqui
         *`value.getSize()` → vem o tamanho em Bytes do arquivo que foi enviado
         * this.maxSize.toBytes()`  → Converto o valor que vem da anotacao para Bytes para fazer uma comparacao
         *`value.getSize() <= this.maxSize.toBytes();` → se o arquivo enviado convertido em bytes for menor ou igual ao valor da anotacao convertida em bytes, ok
         *
         */
        return value == null || value.getSize() <= this.maxSize.toBytes();
    }
}
