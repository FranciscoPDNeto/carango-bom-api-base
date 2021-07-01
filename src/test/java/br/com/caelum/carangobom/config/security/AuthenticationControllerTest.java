package br.com.caelum.carangobom.config.security;

import br.com.caelum.carangobom.usuario.UsuarioRepository;
import br.com.caelum.carangobom.usuario.dtos.UsuarioRequest;
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

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRetornarBadRequestQuandoAutenticateUsuarioInvalido() throws Exception {
        // given
        var uri = new URI("/auth");
        UsuarioRequest usuarioRequest = new UsuarioRequest("luiz", "123456");

        // when
        String json = objectMapper.writeValueAsString(usuarioRequest);
        when(usuarioRepository.findByUsername(usuarioRequest.getUsername())).thenReturn(Optional.empty());

        // then
        mvc
            .perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deveRetornarOkQuandoAutenticateUsuario() throws Exception {
        // given
        var uri = new URI("/auth");
        UsuarioRequest usuarioRequest = new UsuarioRequest("luiz", "123456");

        // when
        String json = objectMapper.writeValueAsString(usuarioRequest);
        var usuario = usuarioRequest.toModel();
        usuario.setId(1L);
        when(usuarioRepository.findByUsername(usuarioRequest.getUsername())).thenReturn(Optional.of(usuario));

        // then
        mvc
            .perform(MockMvcRequestBuilders.post(uri).content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}