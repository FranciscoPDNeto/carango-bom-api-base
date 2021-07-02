package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.config.security.TokenService;
import br.com.caelum.carangobom.exception.UsuarioAlreadyRegisteredException;
import br.com.caelum.carangobom.exception.UsuarioNotFoundException;
import br.com.caelum.carangobom.usuario.dtos.UsuarioRequest;
import br.com.caelum.carangobom.usuario.dtos.UsuarioResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsuarioService {

    UsuarioRepository repository;

    private TokenService tokenService;

    public UsuarioResponse registerNewUser(UsuarioRequest usuarioRequest) {
        repository.findByUsername(usuarioRequest.getUsername()).ifPresent(user -> { throw new UsuarioAlreadyRegisteredException(); } );

        return UsuarioResponse.fromModel(repository.save(usuarioRequest.toModel()));
    }

    public List<UsuarioResponse> findAll() {
        return repository.findAll().stream().map(UsuarioResponse::fromModel).collect(Collectors.toList());
    }

    public void delete(Long id) {
        var optionalUsuario = repository.findById(id);
        var usuario = optionalUsuario.orElseThrow(UsuarioNotFoundException::new);

        repository.delete(usuario);
    }

    public void updatePassword(String password, String token) {
        assert tokenService.isValidToken(token);

        Long userId = tokenService.getUserId(token);
        Usuario user = repository.findById(userId).orElseThrow(UsuarioNotFoundException::new);

        user.setPassword(new BCryptPasswordEncoder().encode(password));
    }
}
