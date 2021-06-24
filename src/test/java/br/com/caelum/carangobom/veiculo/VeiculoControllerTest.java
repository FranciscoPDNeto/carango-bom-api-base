package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.exception.VeiculoNotFoundException;
import br.com.caelum.carangobom.marca.dtos.MarcaResponse;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoRequest;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoResponse;
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
import java.net.URISyntaxException;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VeiculoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VeiculoService veiculoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final URI baseUri = new URI("/veiculos");

    VeiculoControllerTest() throws URISyntaxException {
    }

    @Test
    void deveRetornarListaDeVeiculos() throws Exception {
        // given
        List<VeiculoResponse> veiculos = List.of(
            new VeiculoResponse(1L),
            new VeiculoResponse(2L),
            new VeiculoResponse(3L)
        );

        // when
        String json = objectMapper.writeValueAsString(veiculos);
        when(veiculoService.findAll()).thenReturn(veiculos);

        // then
        mvc.perform(MockMvcRequestBuilders.get(baseUri)).andExpect(MockMvcResultMatchers.content().json(json));
    }

    @Test
    void deveCriarVeiculoERetornarBody() throws Exception {
        // given
        var veiculoRequest = new VeiculoRequest("Uno", 2000, 40000L, 1L);
        var veiculoResponse = new VeiculoResponse(1L, "Uno", 2000, 40000L, new MarcaResponse(1L, "Fiat"));

        // when
        String jsonResponse = objectMapper.writeValueAsString(veiculoResponse);
        String jsonRequest = objectMapper.writeValueAsString(veiculoRequest);
        when(veiculoService.save(veiculoRequest)).thenReturn(veiculoResponse);

        // then
        mvc.perform(
            MockMvcRequestBuilders
                .post(baseUri)
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().json(jsonResponse));
    }

    @Test
    void deveRetornarNoContentAoDeletarVeiculo() throws Exception {
        // given
        Long veiculoId = 42L;
        URI uri = new URI(baseUri.getPath() + "/42");
        Veiculo veiculo = new Veiculo(veiculoId);

        // when
        String json = objectMapper.writeValueAsString(veiculo);
        doNothing().when(veiculoService).delete(veiculoId);

        // then
        mvc.perform(
            MockMvcRequestBuilders.delete(uri)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoTentarDeletarVeiculoNaoExistente() throws Exception {
        // given
        URI uri = new URI(baseUri.getPath() + "/1");

        // when
        doThrow(new VeiculoNotFoundException()).when(veiculoService).delete(anyLong());

        // then
        mvc.perform(MockMvcRequestBuilders.delete(uri)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
