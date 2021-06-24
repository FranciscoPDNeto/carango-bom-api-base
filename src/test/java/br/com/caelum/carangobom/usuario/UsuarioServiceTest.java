package br.com.caelum.carangobom.usuario;

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
        UsuarioDTO usuarioDTO = new UsuarioDTO("nomeTeste", "12345");
        when(repository.findByName(usuarioDTO.getName())).thenReturn(Optional.empty());

        Usuario usuarioFromDTO = usuarioDTO.convert();
        usuarioFromDTO.setId(1L);
        when(repository.save(any())).thenReturn(usuarioFromDTO);

        Usuario usuario = usuarioService.registerNewUser(usuarioDTO);

        assertEquals(usuario.getName(), usuarioDTO.getName());
        assertEquals(usuario.getPassword(), usuarioDTO.getPassword());
    }

    @Test
    void naoDeveCadastrarUsuarioJaExistente() {
        UsuarioDTO usuarioDTO = new UsuarioDTO("nomeTeste", "12345");
        when(repository.findByName(usuarioDTO.getName())).thenReturn(Optional.of(usuarioDTO.convert()));

        Usuario usuario = usuarioService.registerNewUser(usuarioDTO);
        assertNull(usuario);
    }
}