package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.exception.UsuarioAlreadyRegisteredException;
import br.com.caelum.carangobom.usuario.dtos.UsuarioRequest;
import br.com.caelum.carangobom.usuario.dtos.UsuarioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository repository;

    public UsuarioResponse registerNewUser(UsuarioRequest usuarioRequest) {
        repository.findByUsername(usuarioRequest.getUsername()).ifPresent(user -> { throw new UsuarioAlreadyRegisteredException(); } );

        return UsuarioResponse.fromModel(repository.save(usuarioRequest.toModel()));
    }
}
