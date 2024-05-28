package com.algaworks.algafood.infrastructure.storage;

import com.algaworks.algafood.domain.service.FotoStorageService;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class Se3FotoStorageService implements FotoStorageService {
    @Override
    public InputStream recuperar(String momeArquivo) {
        return null;
    }

    @Override
    public void armazenar(NovaFoto novaFoto) {

    }

    @Override
    public void remover(String nomeArquivo) {

    }
}
