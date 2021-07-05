package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.exception.MarcaNotFoundException;
import br.com.caelum.carangobom.marca.dtos.MarcaRequest;
import br.com.caelum.carangobom.marca.dtos.MarcaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class MarcaControllerUnitTest {

    private MarcaController marcaController;

    private UriComponentsBuilder uriBuilder;

    @Mock
    private MarcaService marcaService;

    @BeforeEach
    void configure() {
        openMocks(this);

        marcaController = new MarcaController(marcaService);
        uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080");
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
        when(marcaService.findAllByNameOrder())
            .thenReturn(marcas);

        // then
        var response = marcaController.getAllByNameOrder();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(marcas, response.getBody());
    }

    @Test
    void deveRetornarMarcaPeloId() throws Exception {
        // given
        var audi = new MarcaResponse(1L, "Audi");

        // when
        when(marcaService.findById(1L))
            .thenReturn(audi);

        // then
        var response = marcaController.getById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(audi, response.getBody());
    }

    @Test
    void deveRetornarNotFoundQuandoRecuperarMarcaComIdInexistente() throws Exception {
        // when
        doThrow(new MarcaNotFoundException()).when(marcaService).findById(anyLong());

        // then
        var response = marcaController.getById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveResponderCreatedELocationQuandoCadastrarMarca() throws Exception {
        // given
        var nova = new MarcaRequest("Ferrari");

        // when
        when(marcaService.save(any(MarcaRequest.class)))
            .then(invocation -> {
                var marcaSalva = invocation.getArgument(0, MarcaRequest.class);

                return new MarcaResponse(1L, marcaSalva.getNome());
            });

        // then
        var response = marcaController.save(nova, uriBuilder);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("http://localhost:8080/marcas/1", response.getHeaders().getLocation().toString());
        assertEquals(1L, response.getBody().getId());
        assertEquals(nova.getNome(), response.getBody().getNome());
    }

    @Test
    void deveAlterarNomeQuandoMarcaExistir() throws Exception {
        // given
        var novaAudi = new MarcaRequest("NOVA Audi");
        var novaAudiResponse = new MarcaResponse(1L, "NOVA Audi");

        // when
        when(marcaService.update(eq(1L), any(MarcaRequest.class)))
            .thenReturn(novaAudiResponse);

        // then
        var response = marcaController.update(1L, novaAudi);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(novaAudiResponse, response.getBody());
    }

    @Test
    void naoDeveAlterarMarcaInexistente() throws Exception {
        // when
        doThrow(new MarcaNotFoundException()).when(marcaService).update(anyLong(), any(MarcaRequest.class));

        // then
        var response = marcaController.update(2L, new MarcaRequest("Ferrari"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deveDeletarMarcaExistente() throws Exception {
        // when
        doNothing().when(marcaService).delete(1L);

        // then
        var response = marcaController.delete(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void naoDeveDeletarMarcaInexistente() throws Exception {
        // when
        doThrow(new MarcaNotFoundException()).when(marcaService).delete(anyLong());

        // then
        var response = marcaController.delete(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
