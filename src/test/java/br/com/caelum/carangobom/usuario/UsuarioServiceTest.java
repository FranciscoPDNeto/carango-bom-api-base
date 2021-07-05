package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.config.security.TokenService;
import br.com.caelum.carangobom.exception.UsuarioAlreadyRegisteredException;
import br.com.caelum.carangobom.exception.UsuarioNotFoundException;
import br.com.caelum.carangobom.usuario.dtos.UsuarioRequest;
import br.com.caelum.carangobom.usuario.dtos.UsuarioResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalToObject;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UsuarioServiceTest {

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
    void deveCadastrarNovoUsuario() {
        // given
        var usuarioRequest = new UsuarioRequest("nomeTeste", "12345");

        // when
        when(repository.findByUsername(usuarioRequest.getUsername())).thenReturn(Optional.empty());
        when(repository.save(any(Usuario.class))).then(invocation -> {
            var usuario = invocation.getArgument(0, Usuario.class);
            usuario.setId(1L);

            return usuario;
        });

        // then
        var usuarioResponse = usuarioService.registerNewUser(usuarioRequest);
        assertEquals(1L, usuarioResponse.getId());
        assertEquals(usuarioResponse.getUsername(), usuarioRequest.getUsername());
    }

    @Test
    void naoDeveCadastrarUsuarioJaExistente() {
        // given
        UsuarioRequest usuarioRequest = new UsuarioRequest("nomeTeste", "12345");

        // when
        when(repository.findByUsername(usuarioRequest.getUsername())).thenReturn(Optional.of(usuarioRequest.toModel()));

        // then
        assertThrows(UsuarioAlreadyRegisteredException.class, () -> usuarioService.registerNewUser(usuarioRequest));
    }

    @Test
    void deveRetornarListaDeUsuarios() {
        // given
        var usuarios = List.of(
                new Usuario(1L, "João", "12345"),
                new Usuario(2L, "Maria", "12345"),
                new Usuario(3L, "José", "12345"),
                new Usuario(4L, "Ana", "12345")
        );

        // when
        when(repository.findAll()).thenReturn(usuarios);
        var expectedUsers = usuarioService.findAll();

        // then
        assertEquals(4, usuarios.size());
        assertThat(expectedUsers, contains(
                equalToObject(UsuarioResponse.fromModel(usuarios.get(0))),
                equalToObject(UsuarioResponse.fromModel(usuarios.get(1))),
                equalToObject(UsuarioResponse.fromModel(usuarios.get(2))),
                equalToObject(UsuarioResponse.fromModel(usuarios.get(3)))
        ));
    }

    @Test
    void deveDeletarUsuarioExistente() {
        // given
        var usuario = new Usuario(1L, "João", "12345");

        // when
        when(repository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        usuarioService.delete(usuario.getId());

        // then
        verify(repository).delete(usuario);
    }

    @Test
    void deveLancarExceptionQuandoTentarDeletarUsuarioInexistente() {
        // then
        assertThrows(UsuarioNotFoundException.class, () -> usuarioService.delete(1L));
        verify(repository, never()).delete(any());
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
