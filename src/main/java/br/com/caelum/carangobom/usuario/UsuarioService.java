package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.usuario.dtos.UsuarioRequest;
import br.com.caelum.carangobom.usuario.dtos.UsuarioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    UsuarioRepository repository;

    public UsuarioResponse registerNewUser(UsuarioRequest usuarioRequest) {
        if (repository.findByNome(usuarioRequest.getNome()).isPresent()) {
            return null;
        }

        return UsuarioResponse.fromModel(repository.save(usuarioRequest.toModel()));
    }
}
