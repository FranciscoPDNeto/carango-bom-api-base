package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.usuario.dtos.UsuarioRequest;
import br.com.caelum.carangobom.usuario.dtos.UsuarioResponse;
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
        UsuarioRequest usuarioRequest = new UsuarioRequest("nomeTeste", "12345");
        when(repository.findByNome(usuarioRequest.getNome())).thenReturn(Optional.empty());

        Usuario usuarioFromRequest = usuarioRequest.toModel();
        usuarioFromRequest.setId(1L);
        when(repository.save(any())).thenReturn(usuarioFromRequest);

        UsuarioResponse usuarioResponse = usuarioService.registerNewUser(usuarioRequest);

        assertEquals(usuarioResponse.getId(), 1L);
        assertEquals(usuarioResponse.getNome(), usuarioRequest.getNome());
    }

    @Test
    void naoDeveCadastrarUsuarioJaExistente() {
        UsuarioRequest usuarioRequest = new UsuarioRequest("nomeTeste", "12345");
        when(repository.findByNome(usuarioRequest.getNome())).thenReturn(Optional.of(usuarioRequest.toModel()));

        UsuarioResponse usuarioResponse = usuarioService.registerNewUser(usuarioRequest);
        assertNull(usuarioResponse);
    }
}
