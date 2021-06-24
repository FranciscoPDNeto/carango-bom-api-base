package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.marca.Marca;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UsuarioService usuarioService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveCadastrarComSucesso() throws Exception {
        URI uri = new URI("/cadastro-usuario");
        UsuarioDTO usuarioDTO = new UsuarioDTO("Joao", "123456");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setName(usuarioDTO.getName());
        usuario.setPassword(usuarioDTO.getPassword());

        String json = objectMapper.writeValueAsString(usuarioDTO);

        when(usuarioService.registerNewUser(usuarioDTO))
                .thenReturn(usuario);

        String jsonResult = objectMapper.writeValueAsString(usuario);

        mvc
            .perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/usuarios/1"))
            .andExpect(MockMvcResultMatchers.content().json(jsonResult));
    }

    @Test
    void naoDeveCadastrarCasoJaExistaUsuario() throws Exception {
        URI uri = new URI("/cadastro-usuario");
        UsuarioDTO usuarioDTO = new UsuarioDTO("Joao", "123456");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setName(usuarioDTO.getName());
        usuario.setPassword(usuarioDTO.getPassword());

        String json = objectMapper.writeValueAsString(usuarioDTO);

        when(usuarioService.registerNewUser(usuarioDTO))
                .thenReturn(null);

        String jsonResult = objectMapper.writeValueAsString(usuario);

        mvc
            .perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void naoDeveAceitarCadastroComUsuarioVazio() throws Exception {
        URI uri = new URI("/cadastro-usuario");
        UsuarioDTO usuarioDTO = new UsuarioDTO("", "123456");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setName(usuarioDTO.getName());
        usuario.setPassword(usuarioDTO.getPassword());

        String json = objectMapper.writeValueAsString(usuarioDTO);

        when(usuarioService.registerNewUser(usuarioDTO))
                .thenReturn(null);

        String jsonResult = objectMapper.writeValueAsString(usuario);

        mvc
                .perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void naoDeveAceitarCadastroComSenhaVazia() throws Exception {
        URI uri = new URI("/cadastro-usuario");
        UsuarioDTO usuarioDTO = new UsuarioDTO("Joao", "");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setName(usuarioDTO.getName());
        usuario.setPassword(usuarioDTO.getPassword());

        String json = objectMapper.writeValueAsString(usuarioDTO);

        when(usuarioService.registerNewUser(usuarioDTO))
                .thenReturn(null);

        String jsonResult = objectMapper.writeValueAsString(usuario);

        mvc
                .perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}