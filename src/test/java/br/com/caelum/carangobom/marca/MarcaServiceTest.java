package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.exception.MarcaNotFoundException;
import br.com.caelum.carangobom.marca.dtos.MarcaRequest;
import br.com.caelum.carangobom.marca.dtos.MarcaResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalToObject;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class MarcaServiceTest {

    @MockBean
    private MarcaRepository marcaRepository;

    @Autowired
    private MarcaService marcaService;

    @Test
    void deveRetornarListaDeMarcasOrdenadas() {
        // given
        List<Marca> marcas = List.of(
                new Marca(2L, "Audi"),
                new Marca(1L, "Ford"),
                new Marca(3L, "Renault")
        );

        // when
        when(marcaRepository.findAllByOrderByNome()).thenReturn(marcas);
        var expectedMarcas = marcaService.findAllByNameOrder();

        // then
        assertEquals(3, expectedMarcas.size());
        assertThat(expectedMarcas, contains(
                equalToObject(MarcaResponse.fromModel(marcas.get(0))),
                equalToObject(MarcaResponse.fromModel(marcas.get(1))),
                equalToObject(MarcaResponse.fromModel(marcas.get(2)))
        ));
    }

    @Test
    void deveCriarMarca() {
        // given
        var marca = new Marca(1L, "Ford");
        var marcaRequest = new MarcaRequest(marca.getNome());
        var marcaResponse = new MarcaResponse(marca.getId(), marca.getNome());

        // when
        when(marcaRepository.save(any(Marca.class))).thenReturn(marca);
        var expectedMarca = marcaService.save(marcaRequest);

        // then
        assertThat(expectedMarca, is(marcaResponse));
    }

    @Test
    void deveDeletarMarcaExistente() {
        // given
        Marca marca = new Marca(1L);

        // when
        when(marcaRepository.findById(marca.getId())).thenReturn(Optional.of(marca));
        marcaService.delete(marca.getId());

        // then
        verify(marcaRepository).delete(marca);
    }

    @Test
    void deveLancarExceptionQuandoTentarDeletarMarcaInexistente() {
        // then
        assertThrows(MarcaNotFoundException.class, () -> marcaService.delete(1L));
        verify(marcaRepository, never()).delete(any());
    }
}