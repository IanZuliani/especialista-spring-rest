package com.algaworks.algafood.infrastructure.email;

import com.algaworks.algafood.domain.service.EnvioEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementando o envio Fake de e-mail
 * Ao inves de enviar o email, ele printa no console o email que seria enviado.
 */
@Slf4j
public class FakeEnvioEmailService extends SmtpEnvioEmailService{


    @Override
    public void enviar(Mensagem mensagem) {
        String corpo = processarTemplate(mensagem);

        log.info("[FAKE E-MAIL] Para: {}\n{}", mensagem.getDestinatarios(), corpo);
    }
}
