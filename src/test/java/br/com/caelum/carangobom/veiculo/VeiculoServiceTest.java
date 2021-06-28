package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.exception.VeiculoNotFoundException;
import br.com.caelum.carangobom.marca.Marca;
import br.com.caelum.carangobom.marca.dtos.MarcaResponse;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoRequest;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoResponse;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class VeiculoServiceTest {

    @MockBean
    private VeiculoRepository veiculoRepository;

    @Autowired
    private VeiculoService veiculoService;

    @Test
    void deveRetornarListaDeVeiculosResponse() {
        // given
        List<Veiculo> veiculos = List.of(
            new Veiculo(1L, "Fiesta", 2000, 100000L, new Marca(1L, "Ford"))
        );

        // when
        when(veiculoRepository.findAll()).thenReturn(veiculos);
        var expectedVeiculos = veiculoService.findAll();

        // then
        assertThat(expectedVeiculos, hasSize(1));
        assertThat(expectedVeiculos, contains(
            allOf(
                hasProperty("id", is(veiculos.get(0).getId())),
                hasProperty("modelo", is(veiculos.get(0).getModelo())),
                hasProperty("ano", is(veiculos.get(0).getAno())),
                hasProperty("valor", is(veiculos.get(0).getValor())),
                hasProperty("marca",
                    hasProperty("id", is(veiculos.get(0).getMarca().getId()))
                )
            )
        ));
    }

    @Test
    void deveCriarVeiculo() {
        // given
        var veiculo = new Veiculo(1L, "Fiesta", 2000, 100000L, new Marca(1L, "Ford"));
        var veiculoRequest = new VeiculoRequest(
            veiculo.getModelo(), veiculo.getAno(), veiculo.getValor(), veiculo.getMarca().getId()
        );
        var veiculoResponse = new VeiculoResponse(
            veiculo.getId(), veiculo.getModelo(), veiculo.getAno(), veiculo.getValor(),
            new MarcaResponse(veiculo.getMarca().getId(), veiculo.getMarca().getNome())
        );

        // when
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);
        var expectedVeiculo = veiculoService.save(veiculoRequest);

        // then
        assertThat(expectedVeiculo, is(veiculoResponse));
    }

    @Test
    void deveAtualizarVeiculo() {
        // given
        Veiculo veiculo = new Veiculo(1L, "modelo", 1991, 12000L, new Marca(1L));
        VeiculoUpdateRequest veiculoRequest = new VeiculoUpdateRequest();
        veiculoRequest.setModelo("novoModelo");

        // when
        when(veiculoRepository.findById(veiculo.getId())).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        veiculoService.update(veiculo.getId(), veiculoRequest);

        // then
        verify(veiculoRepository).save(veiculo);
        assertThat(veiculo.getModelo(), is("novoModelo"));
    }

    @Test
    void deveDeletarVeiculoExistente() {
        // given
        Veiculo veiculo = new Veiculo(1L);

        // when
        when(veiculoRepository.findById(veiculo.getId())).thenReturn(Optional.of(veiculo));
        veiculoService.delete(veiculo.getId());

        // then
        verify(veiculoRepository).delete(veiculo);
    }

    @Test
    void deveLancarExceptionQuandoVeiculoNaoExiste() {
        // then
        assertThrows(VeiculoNotFoundException.class, () -> veiculoService.delete(1L));
        verify(veiculoRepository, never()).delete(any());
    }
}
