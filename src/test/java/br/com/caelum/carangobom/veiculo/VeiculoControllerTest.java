package br.com.caelum.carangobom.veiculo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
class VeiculoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VeiculoRepository veiculoRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final URI baseUri = new URI("/veiculos");

    VeiculoControllerTest() throws URISyntaxException {
    }

    @Test
    void deveRetornarListaDeVeiculos() throws Exception {
        List<Veiculo> veiculos = List.of(
            new Veiculo(1L),
            new Veiculo(2L),
            new Veiculo(3L)
        );

        String json = objectMapper.writeValueAsString(veiculos);

        when(veiculoRepository.findAll()).thenReturn(veiculos);

        mvc.perform(MockMvcRequestBuilders.get(baseUri)).andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    void deveDeletarVeiculoExistente() throws Exception {
        Long veiculoId = 42L;
        URI uri = new URI(baseUri.getPath() + "/42");
        Veiculo veiculo = new Veiculo(veiculoId);

        String json = objectMapper.writeValueAsString(veiculo);

        when(veiculoRepository.findById(veiculoId))
            .thenReturn(Optional.of(veiculo));

        mvc.perform(
            MockMvcRequestBuilders.delete(uri)).andExpect(MockMvcResultMatchers.status().isNoContent()
        );

        verify(veiculoRepository).delete(veiculo);
    }

    @Test
    void naoDeveDeletarVeiculoInexistente() throws Exception {
        URI uri = new URI(baseUri.getPath() + "/1");

        when(veiculoRepository.findById(anyLong()))
            .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.delete(uri)).andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(veiculoRepository, never()).delete(any());
    }
}
