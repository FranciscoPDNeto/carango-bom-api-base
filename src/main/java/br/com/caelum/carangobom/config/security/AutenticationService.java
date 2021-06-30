package br.com.caelum.carangobom.config.security;

import br.com.caelum.carangobom.exception.UsuarioNotFoundException;
import br.com.caelum.carangobom.usuario.Usuario;
import br.com.caelum.carangobom.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticationService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByUsername(username);

        return optionalUsuario.orElseThrow(UsuarioNotFoundException::new);
    }
}
