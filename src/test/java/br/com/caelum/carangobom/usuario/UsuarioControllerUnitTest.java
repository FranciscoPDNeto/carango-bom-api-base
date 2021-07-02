package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.exception.UsuarioNotFoundException;
import br.com.caelum.carangobom.usuario.dtos.PasswordRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UsuarioControllerUnitTest {

    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    @BeforeEach
    public void configure() {
        openMocks(this);

        usuarioController = new UsuarioController(usuarioService);
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
