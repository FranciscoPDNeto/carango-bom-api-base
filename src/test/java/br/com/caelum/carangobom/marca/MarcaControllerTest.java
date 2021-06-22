package br.com.caelum.carangobom.marca;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MarcaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MarcaRepository marcaRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final URI baseUri = new URI("/marcas");

    MarcaControllerTest() throws URISyntaxException {
    }

    @Test
    void deveRetornarListaQuandoHouverResultados() throws Exception {
        List<Marca> marcas = List.of(
            new Marca(1L, "Audi"),
            new Marca(2L, "BMW"),
            new Marca(3L, "Fiat")
        );

        String json = objectMapper.writeValueAsString(marcas);

        when(marcaRepository.findAllByOrderByNome())
            .thenReturn(marcas);

        mvc.perform(MockMvcRequestBuilders.get(baseUri)).andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    void deveRetornarMarcaPeloId() throws Exception {
        Marca audi = new Marca(1L, "Audi");
        URI uri = new URI(baseUri.getPath() + "/1");

        when(marcaRepository.findById(1L))
            .thenReturn(Optional.of(audi));

        String json = objectMapper.writeValueAsString(audi);

        mvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    void deveRetornarNotFoundQuandoRecuperarMarcaComIdInexistente() throws Exception {
        URI uri = new URI(baseUri.getPath() + "/1");
        when(marcaRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deveResponderCreatedELocationQuandoCadastrarMarca() throws Exception {
        Marca nova = new Marca("Ferrari");
        Marca expected = new Marca(1L, "Ferrari");

        String json = objectMapper.writeValueAsString(nova);

        String jsonExpected = objectMapper.writeValueAsString(expected);

        when(marcaRepository.save(any()))
            .then(invocation -> {
                Marca marcaSalva = invocation.getArgument(0, Marca.class);
                marcaSalva.setId(1L);

                return marcaSalva;
            });

        mvc
        .perform(MockMvcRequestBuilders.post(baseUri)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/marcas/1"))
        .andExpect(MockMvcResultMatchers.content().json(jsonExpected));
    }

    @Test
    void deveAlterarNomeQuandoMarcaExistir() throws Exception {
        URI uri = new URI(baseUri.getPath() + "/1");
        Marca audi = new Marca(1L, "Audi");
        Marca novaAudi = new Marca(1L, "NOVA Audi");

        String json = objectMapper.writeValueAsString(novaAudi);

        when(marcaRepository.findById(1L))
            .thenReturn(Optional.of(audi));

        mvc
        .perform(MockMvcRequestBuilders.put(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    void naoDeveAlterarMarcaInexistente() throws Exception {
        URI uri = new URI(baseUri.getPath() + "/1");
        Marca novaAudi = new Marca(1L, "NOVA Audi");

        String json = objectMapper.writeValueAsString(novaAudi);

        when(marcaRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.put(uri)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deveDeletarMarcaExistente() throws Exception {
        URI uri = new URI(baseUri.getPath() + "/1");
        Marca audi = new Marca(1l, "Audi");

        String json = objectMapper.writeValueAsString(audi);

        when(marcaRepository.findById(1L))
            .thenReturn(Optional.of(audi));

        mvc.perform(MockMvcRequestBuilders.delete(uri)).andExpect(MockMvcResultMatchers.status().isOk());

        verify(marcaRepository).delete(audi);
    }

    @Test
    void naoDeveDeletarMarcaInexistente() throws Exception {
        URI uri = new URI(baseUri.getPath() + "/1");

        when(marcaRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.delete(uri)).andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(marcaRepository, never()).delete(any());
    }
}