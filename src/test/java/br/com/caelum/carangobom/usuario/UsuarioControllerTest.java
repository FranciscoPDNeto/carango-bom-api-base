package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.exception.UsuarioAlreadyRegisteredException;
import br.com.caelum.carangobom.usuario.dtos.UsuarioRequest;
import br.com.caelum.carangobom.usuario.dtos.UsuarioResponse;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
        // given
        URI uri = new URI("/cadastro-usuario");
        UsuarioRequest usuarioRequest = new UsuarioRequest("Joao", "123456");
        Usuario usuario = usuarioRequest.toModel();
        usuario.setId(1L);
        UsuarioResponse usuarioResponse = UsuarioResponse.fromModel(usuario);

        // when
        String json = objectMapper.writeValueAsString(usuarioRequest);
        String jsonResult = objectMapper.writeValueAsString(usuarioResponse);
        when(usuarioService.registerNewUser(usuarioRequest))
            .thenReturn(usuarioResponse);

        // then
        mvc
            .perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/usuarios/1"))
            .andExpect(MockMvcResultMatchers.content().json(jsonResult));
    }

    @Test
    void naoDeveCadastrarCasoJaExistaUsuario() throws Exception {
        // given
        URI uri = new URI("/cadastro-usuario");
        UsuarioRequest usuarioRequest = new UsuarioRequest("Joao", "123456");

        // when
        String json = objectMapper.writeValueAsString(usuarioRequest);
        doThrow(new UsuarioAlreadyRegisteredException()).when(usuarioService).registerNewUser(any());

        // then
        mvc
            .perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void naoDeveAceitarCadastroComUsuarioVazio() throws Exception {
        // given
        URI uri = new URI("/cadastro-usuario");
        UsuarioRequest usuarioRequest = new UsuarioRequest("", "123456");

        // when
        String json = objectMapper.writeValueAsString(usuarioRequest);
        when(usuarioService.registerNewUser(usuarioRequest))
            .thenReturn(null);

        // then
        mvc
            .perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void naoDeveAceitarCadastroComSenhaVazia() throws Exception {
        // given
        URI uri = new URI("/cadastro-usuario");
        UsuarioRequest usuarioDTO = new UsuarioRequest("Joao", "");

        // when
        String json = objectMapper.writeValueAsString(usuarioDTO);
        when(usuarioService.registerNewUser(usuarioDTO))
            .thenReturn(null);

        mvc
            .perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
