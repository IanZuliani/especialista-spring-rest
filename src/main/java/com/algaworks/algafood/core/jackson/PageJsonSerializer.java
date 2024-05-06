package com.algaworks.algafood.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import java.io.IOException;

/**
 *Classe que faz a serealizacao da resposta da Paginacao
 */

//Componemte do spring que fornece uma impplementacao de um serealizador ou desserealizador deve sr registrado no Json
@JsonComponent
public class PageJsonSerializer extends JsonSerializer<Page<?>> {
    @Override
    public void serialize(Page<?> page, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        /**
        *Vamos serealizar o Objeto Page do 0
        */

        //Comeca para min um objeto Novo
        gen.writeStartObject();


        /**
         * escreve uma propriedade content e coloca o conteudo de page.getContent() dentro dela
         * Quando fazemos paginacao nos colocamos o conteudo dentro do Page
         */
        gen.writeObjectField("content", page.getContent());

        /**
         * Vamos colocar as propriedades de paginacao
         * Para pegarmos as outras propriedades basta darmos
         * page.
         */
        gen.writeObjectField("size", page.getSize());
        gen.writeObjectField("TotalElements", page.getTotalElements());
        gen.writeObjectField("TotalPages", page.getTotalPages());
        gen.writeObjectField("number", page.getNumber());
        gen.writeObjectField("last", page.isLast());

        //Fechamos o objeto
        gen.writeEndObject();
    }
}
