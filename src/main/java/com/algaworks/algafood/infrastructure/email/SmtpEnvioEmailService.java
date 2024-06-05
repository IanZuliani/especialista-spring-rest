package com.algaworks.algafood.infrastructure.email;

import com.algaworks.algafood.core.email.EmailProperties;
import com.algaworks.algafood.domain.service.EnvioEmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.io.IOException;

/**
 * Implementacao de envio de email atraves da interface EnvioEmailService
 */
@Service
public class SmtpEnvioEmailService implements EnvioEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailProperties emailProperties;

    /**
     * Classe de configuracao para trabalha com o templates de envio de email
     */
    @Autowired
    private Configuration freemarkerConfig;

    @Override
    public void enviar(Mensagem mensagem) {
        try {

            /**
             *
             */
            String corpo = processarTemplate(mensagem);


            /**
             * O metodo sender precisa de um tipo MimeMessage
             * O mailSender que estamos injetando cria uma instancia de mailSender
             */
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            /**
             * Apos criarmos essa instandia de mimemessage, poderiamos setar as informacoes
             * Utilizando a propria instancia
             * Exemplo mimeMessage.addFrom();
             * Mas o Spring possui um Helper chamado MimeMessageHelper que nos ajuda na criacao desse objeto para o envio de email
             * De uma forma muito mas facil
             * Vamos passar o encoding "UTF-8" para nao dar erro de caracter
             */
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            /**
             * Criando o Remetente
             * Todas as classes vai utilziar o Mesmo remetente vinda do application.properties
             * algafood.email.remetente=Algafood <naoresponder@algafood.com.br>
             * Para pegar o email, criamos a classe de configuracao EmailProperties
             */
            helper.setFrom(emailProperties.getRemetente());

            /**
             * Vamos expcificar tambem os destinatarios
             * Vamos utilizar o setTo() que recebe um array de string
             *Vamos pegar o mensagem.getDestinatario(), mas esse retorna um Set, conjunto de String nao um array
             * Pra gente converter em um array basta colocarmos .toArray(new String[0]) passando um array de string vazio.
             */
            helper.setTo(mensagem.getDestinatarios().toArray(new String[0]));

            /**
             * Assunto do email
             */
            helper.setSubject(mensagem.getAssunto());
            /**
             * Corpo do email,
             * True Que possibilita Html
             */
            helper.setText(corpo, true);


            mailSender.send(mimeMessage);
        }catch (Exception e){
            throw new EmailException("Nao foi possivel enviar e-mail", e);
        }

    }

    /**
     * Agora que estamos utilizando a biblioteca FREEMaKER
     * Nap passamos mas texto como envio no corpo
     * Agora passamos HTML e Vamos criar um metodo para processar esse HTML
     *
     * Dado uma mensagem ele deve retornar o corpo do email que vai ser enviado
     * A PARTIR DE AGORA GETCORPO E O NOME DO ARQUIVO HTML
     */
    private String processarTemplate(Mensagem mensagem){

        try {
            /**
             * Pegamos o template
             */
            Template template = freemarkerConfig.getTemplate(mensagem.getCorpo());

            /**
             * Processamos ele
             *Agora que temos o template vamos pegar a classe de Utils do FreeMarker
             * e utilizamos o metodo processTemplateIntoString para transformar tudo em string
             * primeiro parametro e o template
             * segundo argumento e o objeto java que vai ser utilizado para gerar as informacoes dentro do template
             * Vamos criar o objeto em Mensagem
             * @Singular("variavel") Po
             * private Map<String, Object> variaveis;
             */
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, mensagem.getVariaveis());

        } catch (Exception e) {
            throw new EmailException("Nao foi possivel montar o template do e-mail", e);
        }
    }
}
