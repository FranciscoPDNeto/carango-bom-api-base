package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.exception.UsuarioAlreadyRegisteredException;
import br.com.caelum.carangobom.exception.UsuarioNotFoundException;
import br.com.caelum.carangobom.usuario.dtos.PasswordRequest;
import br.com.caelum.carangobom.usuario.dtos.UsuarioRequest;
import br.com.caelum.carangobom.usuario.dtos.UsuarioResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UsuarioControllerUnitTest {

    private UsuarioController usuarioController;

    private UriComponentsBuilder uriBuilder;

    @Mock
    private UsuarioService usuarioService;

    @BeforeEach
    public void configure() {
        openMocks(this);

        usuarioController = new UsuarioController(usuarioService);
        uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080");
    }

    @Test
    void deveCadastrarComSucesso() {
        var usuarioResponse = new UsuarioResponse(1L, "Jose");
        when(usuarioService.registerNewUser(any())).thenReturn(usuarioResponse);

        ResponseEntity<UsuarioResponse> response =
            usuarioController.save(new UsuarioRequest("Jose", "1234"), uriBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("http://localhost:8080/usuarios/1", response.getHeaders().getLocation().toString());
    }

    @Test
    void deveRetornarBadRequestAoTentarCadastrarUsuarioQueJaExista() {
        doThrow(UsuarioAlreadyRegisteredException.class).when(usuarioService).registerNewUser(any());

        ResponseEntity<UsuarioResponse> response =
            usuarioController.save(new UsuarioRequest("Jose", "1234"), uriBuilder);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deveRetornarListaDeUsuariosCadastrados() {
        var usuarios = List.of(
            new UsuarioResponse(1L, "João"),
            new UsuarioResponse(2L, "Maria"),
            new UsuarioResponse(3L, "José"),
            new UsuarioResponse(4L, "Ana")
        );

        when(usuarioService.findAll()).thenReturn(usuarios);

        var response = usuarioController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarios, response.getBody());
    }

    @Test
    void deveExcluirUsuarioExistente() {
        doNothing().when(usuarioService).delete(anyLong());

        var response = usuarioController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deveRetornarNotFoundAoTentarExcluirUsuarioInexistente() {
        doThrow(UsuarioNotFoundException.class).when(usuarioService).delete(anyLong());

        var response = usuarioController.delete(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveRetornarNotFoundAoTentarAlterarSenhaComTokenInvalido() throws Exception {
        doThrow(UsuarioNotFoundException.class).when(usuarioService).updatePassword(anyString(), anyString());

        ResponseEntity<Void> response = usuarioController.updatePassword(new PasswordRequest("1234"), "Bearer wrongToken");

        verify(usuarioService).updatePassword(anyString(), anyString());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveRetornarOkAoTentarAlterarSenhaComTokenValido() throws Exception {

        final String token =
            ".eyJpc3MiOiJBUEkgZG8gQ2FyYW5nb0JvbSIsInN1YiI6IjgiLCJpYXQiOjE2MjUyNDgwMjksImV4cCI6MTYyNTMzNDQyOX0.tlX9KM1aLUjA_3w7oChzjqsUzDh4rh2vUR-rmYwuyZs";

        ResponseEntity<Void> response = usuarioController.updatePassword(new PasswordRequest("1234"), "Bearer " + token);

        verify(usuarioService).updatePassword(anyString(), anyString());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
