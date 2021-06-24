package br.com.caelum.carangobom.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    UsuarioRepository repository;

    public Usuario registerNewUser(UsuarioDTO usuarioDTO) {
        if (repository.findByName(usuarioDTO.getName()).isPresent()) {
            return null;
        }

        Usuario user = new Usuario();
        user.setName(usuarioDTO.getName());
        user.setPassword(usuarioDTO.getPassword());

        return repository.save(user);
    }
}
