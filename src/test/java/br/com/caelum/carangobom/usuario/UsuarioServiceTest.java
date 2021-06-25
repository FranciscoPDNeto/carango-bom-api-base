package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.exception.UsuarioAlreadyRegisteredException;
import br.com.caelum.carangobom.usuario.dtos.UsuarioRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository repository;

    @Test
    void deveCadastrarNovoUsuario() {
        // given
        var usuarioRequest = new UsuarioRequest("nomeTeste", "12345");
        var usuarioFromRequest = usuarioRequest.toModel();
        usuarioFromRequest.setId(1L);

        // when
        when(repository.findByUsername(usuarioRequest.getUsername())).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(usuarioFromRequest);

        // then
        var usuarioResponse = usuarioService.registerNewUser(usuarioRequest);
        assertEquals(usuarioResponse.getId(), 1L);
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
}
