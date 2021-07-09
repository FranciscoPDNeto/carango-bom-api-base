package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.basecrud.BaseCrudService;
import br.com.caelum.carangobom.config.security.TokenService;
import br.com.caelum.carangobom.exception.UsuarioAlreadyRegisteredException;
import br.com.caelum.carangobom.exception.UsuarioNotFoundException;
import br.com.caelum.carangobom.usuario.dtos.UsuarioRequest;
import br.com.caelum.carangobom.usuario.dtos.UsuarioResponse;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsuarioService implements BaseCrudService<UsuarioResponse, UsuarioRequest> {

    UsuarioRepository repository;

    private TokenService tokenService;

    @Override
    public List<UsuarioResponse> findAll() {
        return repository.findAll().stream().map(UsuarioResponse::fromModel).collect(Collectors.toList());
    }

    @Override
    public UsuarioResponse findById(Long id) {
        var usuarioOptional = repository.findById(id);
        var usuario = usuarioOptional.orElseThrow(UsuarioNotFoundException::new);

        return UsuarioResponse.fromModel(usuario);
    }

    @Override
    public UsuarioResponse save(UsuarioRequest request) {
        repository.findByUsername(request.getUsername()).ifPresent(user -> { throw new UsuarioAlreadyRegisteredException(); } );

        return UsuarioResponse.fromModel(repository.save(request.toModel()));
    }

    @Override
    public void delete(Long id) {
        var optionalUsuario = repository.findById(id);
        var usuario = optionalUsuario.orElseThrow(UsuarioNotFoundException::new);

        repository.delete(usuario);
    }

    @Override
    public UsuarioResponse update(Long id, UsuarioRequest request) {
        throw new RuntimeException("Update for user by id is not available.");
    }

    public void updatePassword(String password, String token) {
        Assert.isTrue(tokenService.isValidToken(token), "The token must be valid");

        Long userId = tokenService.getUserId(token);
        Usuario user = repository.findById(userId).orElseThrow(UsuarioNotFoundException::new);

        user.setPassword(new BCryptPasswordEncoder().encode(password));
    }
}
