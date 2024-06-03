package com.algaworks.algafood.infrastructure.email;

import com.algaworks.algafood.core.email.EmailProperties;
import com.algaworks.algafood.domain.service.EnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * Implementacao de envio de email atraves da interface EnvioEmailService
 */
@Service
public class SmtpEnvioEmailService implements EnvioEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailProperties emailProperties;

    @Override
    public void enviar(Mensagem mensagem) {
        try {
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
            helper.setText(mensagem.getCorpo(), true);


            mailSender.send(mimeMessage);
        }catch (Exception e){
            throw new EmailException("Nao foi possivel enviar e-mail", e);
        }

    }
}
