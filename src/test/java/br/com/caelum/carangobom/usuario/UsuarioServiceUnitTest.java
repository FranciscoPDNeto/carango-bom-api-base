package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.config.security.TokenService;
import br.com.caelum.carangobom.exception.UsuarioAlreadyRegisteredException;
import br.com.caelum.carangobom.exception.UsuarioNotFoundException;
import br.com.caelum.carangobom.usuario.dtos.UsuarioRequest;
import br.com.caelum.carangobom.usuario.dtos.UsuarioResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalToObject;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UsuarioServiceUnitTest {

    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private TokenService tokenService;

    @BeforeEach
    public void configure() {
        openMocks(this);

        usuarioService = new UsuarioService(repository, tokenService);

        when(tokenService.isValidToken(anyString())).thenReturn(true);
    }

    @Test
    void deveAtualizarSenhaDoUsuario() {
        final var oldPassword = "1234";
        final var usuario = new Usuario(1L, "Joao", oldPassword);
        final var newPassword = "123456";
        final var token =
            ".eyJpc3MiOiJBUEkgZG8gQ2FyYW5nb0JvbSIsInN1YiI6IjgiLCJpYXQiOjE2MjUyNDgwMjksImV4cCI6MTYyNTMzNDQyOX0.tlX9KM1aLUjA_3w7oChzjqsUzDh4rh2vUR-rmYwuyZs";

        when(tokenService.getUserId(token)).thenReturn(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.updatePassword(newPassword, token);

        assertFalse(BCrypt.checkpw(oldPassword, usuario.getPassword()));
        assertTrue(BCrypt.checkpw(newPassword, usuario.getPassword()));
    }

    @Test
    void naoDeveAtualizarSenhaDeUsuarioInexistente() {
        final var token =
            ".eyJpc3MiOiJBUEkgZG8gQ2FyYW5nb0JvbSIsInN1YiI6IjgiLCJpYXQiOjE2MjUyNDgwMjksImV4cCI6MTYyNTMzNDQyOX0.tlX9KM1aLUjA_3w7oChzjqsUzDh4rh2vUR-rmYwuyZs";

        when(tokenService.getUserId(token)).thenReturn(1L);
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNotFoundException.class, () -> usuarioService.updatePassword("123456", token));
    }
}
