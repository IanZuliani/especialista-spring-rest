package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.NegocioExceptional;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoExption;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Service
public class CadastroUsuarioService {

    @Autowired
    private UsuarioRepository repository;


    //adicionar
    @Transactional
    public Usuario save(Usuario usuario){

       repository.detach(usuario);

        Optional<Usuario> usuarioExistente = repository.findByEmail(usuario.getEmail());

        if(usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)){
            throw new NegocioExceptional(
                    String.format("Ja existe um usuario cadastrado com o e-mail %w", usuario.getEmail()));
        }
        return repository.save(usuario);
    }

    //Buscar ou falhar
    public Usuario buscarOuFalhar(Long id){
        return repository.findById(id).orElseThrow(()-> new UsuarioNaoEncontradoExption(id));
    }

    @Transactional
    public void alterarSenha(Long id, String senhaAtual, String novaSenha) {
        var user = buscarOuFalhar(id);

        if(user.senhaNaoCoincideCom(senhaAtual)){
            throw new NegocioExceptional("Senha atual informada não coincide com a senha do usuário.");
        }

        user.setSenha(novaSenha);
    }
}
