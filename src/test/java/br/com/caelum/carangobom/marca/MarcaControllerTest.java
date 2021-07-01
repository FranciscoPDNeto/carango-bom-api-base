package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.exception.MarcaNotFoundException;
import br.com.caelum.carangobom.marca.dtos.MarcaRequest;
import br.com.caelum.carangobom.marca.dtos.MarcaResponse;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MarcaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MarcaService marcaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final URI baseUri = new URI("/marcas");

    MarcaControllerTest() throws URISyntaxException {
    }

    @Test
    void deveRetornarListaQuandoHouverResultados() throws Exception {
        // given
        var marcas = List.of(
            new MarcaResponse(1L, "Audi"),
            new MarcaResponse(2L, "BMW"),
            new MarcaResponse(3L, "Fiat")
        );

        // when
        String json = objectMapper.writeValueAsString(marcas);
        when(marcaService.findAllByNameOrder())
            .thenReturn(marcas);

        // then
        mvc.perform(MockMvcRequestBuilders.get(baseUri)).andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    @WithMockUser(username = "test", password = "123456")
    void deveRetornarMarcaPeloId() throws Exception {
        // given
        var audi = new MarcaResponse(1L, "Audi");
        URI uri = new URI(baseUri.getPath() + "/1");

        // when
        String json = objectMapper.writeValueAsString(audi);
        when(marcaService.findById(1L))
            .thenReturn(audi);

        // then
        mvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    @WithMockUser(username = "test", password = "123456")
    void deveRetornarNotFoundQuandoRecuperarMarcaComIdInexistente() throws Exception {
        // given
        URI uri = new URI(baseUri.getPath() + "/1");

        // when
        doThrow(new MarcaNotFoundException()).when(marcaService).findById(anyLong());

        // then
        mvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test", password = "123456")
    void deveResponderCreatedELocationQuandoCadastrarMarca() throws Exception {
        // given
        var nova = new MarcaRequest("Ferrari");
        var expected = new MarcaResponse(1L, "Ferrari");

        // when
        String json = objectMapper.writeValueAsString(nova);
        String jsonExpected = objectMapper.writeValueAsString(expected);
        when(marcaService.save(any(MarcaRequest.class)))
            .then(invocation -> {
                var marcaSalva = invocation.getArgument(0, MarcaRequest.class);

                return new MarcaResponse(1L, marcaSalva.getNome());
            });

        // then
        mvc
        .perform(MockMvcRequestBuilders.post(baseUri)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/marcas/1"))
        .andExpect(MockMvcResultMatchers.content().json(jsonExpected));
    }

    @Test
    @WithMockUser(username = "test", password = "123456")
    void deveAlterarNomeQuandoMarcaExistir() throws Exception {
        // given
        URI uri = new URI(baseUri.getPath() + "/1");
        var novaAudi = new MarcaRequest("NOVA Audi");
        var novaAudiResponse = new MarcaResponse(1L, "NOVA Audi");

        // when
        String json = objectMapper.writeValueAsString(novaAudi);
        String expectedJsonReturn = objectMapper.writeValueAsString(novaAudiResponse);
        when(marcaService.update(eq(1L), any(MarcaRequest.class)))
            .thenReturn(novaAudiResponse);

        // then
        mvc
        .perform(MockMvcRequestBuilders.put(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(expectedJsonReturn));
    }

    @Test
    @WithMockUser(username = "test", password = "123456")
    void naoDeveAlterarMarcaInexistente() throws Exception {
        // given
        URI uri = new URI(baseUri.getPath() + "/1");
        Marca novaAudi = new Marca(1L, "NOVA Audi");

        // when
        String json = objectMapper.writeValueAsString(novaAudi);
        doThrow(new MarcaNotFoundException()).when(marcaService).update(anyLong(), any());

        // then
        mvc.perform(MockMvcRequestBuilders.put(uri)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test", password = "123456")
    void deveDeletarMarcaExistente() throws Exception {
        // given
        URI uri = new URI(baseUri.getPath() + "/1");
        Marca audi = new Marca(1l, "Audi");

        // when
        doNothing().when(marcaService).delete(1L);

        // then
        mvc.perform(MockMvcRequestBuilders.delete(uri)).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(username = "test", password = "123456")
    void naoDeveDeletarMarcaInexistente() throws Exception {
        // given
        URI uri = new URI(baseUri.getPath() + "/1");

        // when
        doThrow(new MarcaNotFoundException()).when(marcaService).delete(anyLong());

        // then
        mvc.perform(MockMvcRequestBuilders.delete(uri)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test", password = "123456")
    void deveRetornarErroDeValidacaoQuandoMandadoParametroInvalido() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(baseUri)
                .content("{\"nome\": null}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"erros\":[{\"parametro\":\"nome\",\"mensagem\":\"Deve ser preenchido.\"}]}"));
    }
}