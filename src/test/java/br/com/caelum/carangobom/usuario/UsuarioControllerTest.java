package br.com.caelum.carangobom.usuario;

import br.com.caelum.carangobom.exception.UsuarioAlreadyRegisteredException;
import br.com.caelum.carangobom.exception.UsuarioNotFoundException;
import br.com.caelum.carangobom.usuario.dtos.UsuarioRequest;
import br.com.caelum.carangobom.usuario.dtos.UsuarioResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

    @Autowired
    private ObjectMapper objectMapper;

    private final URI baseUri = new URI("/usuarios");

    UsuarioControllerTest() throws URISyntaxException {
    }

    @Test
    void deveCadastrarComSucesso() throws Exception {
        // given
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
            .perform(MockMvcRequestBuilders.post(baseUri).content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/usuarios/1"))
            .andExpect(MockMvcResultMatchers.content().json(jsonResult));
    }

    @Test
    void naoDeveCadastrarCasoJaExistaUsuario() throws Exception {
        // given
        UsuarioRequest usuarioRequest = new UsuarioRequest("Joao", "123456");

        // when
        String json = objectMapper.writeValueAsString(usuarioRequest);
        doThrow(new UsuarioAlreadyRegisteredException()).when(usuarioService).registerNewUser(any());

        // then
        mvc
            .perform(MockMvcRequestBuilders.post(baseUri).content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void naoDeveAceitarCadastroComUsuarioVazio() throws Exception {
        // given
        UsuarioRequest usuarioRequest = new UsuarioRequest("", "123456");

        // when
        String json = objectMapper.writeValueAsString(usuarioRequest);
        when(usuarioService.registerNewUser(usuarioRequest))
            .thenReturn(null);

        // then
        mvc
            .perform(MockMvcRequestBuilders.post(baseUri).content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void naoDeveAceitarCadastroComSenhaVazia() throws Exception {
        // given
        UsuarioRequest usuarioDTO = new UsuarioRequest("Joao", "");

        // when
        String json = objectMapper.writeValueAsString(usuarioDTO);
        when(usuarioService.registerNewUser(usuarioDTO))
            .thenReturn(null);

        mvc
            .perform(MockMvcRequestBuilders.post(baseUri).content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test", password = "123456")
    void deveRetornarListaDeUsuarios() throws Exception {
        // given
        var usuarios = List.of(
                new UsuarioResponse(1L, "João"),
                new UsuarioResponse(2L, "Maria"),
                new UsuarioResponse(3L, "José"),
                new UsuarioResponse(4L, "Ana")
        );

        // when
        var json = objectMapper.writeValueAsString(usuarios);
        when(usuarioService.findAll()).thenReturn(usuarios);

        // then
        mvc.perform(MockMvcRequestBuilders.get(baseUri))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    @WithMockUser(username = "test", password = "123456")
    void deveExcluirUsuarioExistente() throws Exception {
        // given
        var uri = new URI(baseUri.getPath() + "/1");
        var usuario = new Usuario(1L, "João", "1234");

        // when
        var json = objectMapper.writeValueAsString(usuario);
        doNothing().when(usuarioService).delete(usuario.getId());

        //then
        mvc.perform(MockMvcRequestBuilders.delete(uri))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(username = "test", password = "123456")
    void deveRetornarNotFoundAoTentarExcluirUsuarioInexistente() throws Exception {
        // given
        var uri = new URI(baseUri.getPath() + "/1");

        // when
        doThrow(new UsuarioNotFoundException()).when(usuarioService).delete(1L);

        //then
        mvc.perform(MockMvcRequestBuilders.delete(uri))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
