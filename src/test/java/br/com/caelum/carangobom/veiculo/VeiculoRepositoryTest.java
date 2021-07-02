package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.usuario.UsuarioRepository;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoDashboard;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoFilterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest
class VeiculoRepositoryTest {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void groupVeiculosByMarca() {
        // when
        List<VeiculoDashboard> dashboards = veiculoRepository.groupVeiculosByMarca();

        // then
        assertThat(dashboards.get(0).getNumVeiculos(), is(1L));
        assertThat(dashboards.get(0).getSomaValor(), is(42000L));
        assertThat(dashboards.get(0).getMarca(), is("Fiat"));
        assertThat(dashboards.get(1).getNumVeiculos(), is(2L));
        assertThat(dashboards.get(1).getSomaValor(), is(83000L));
        assertThat(dashboards.get(1).getMarca(), is("Volkswagen"));
    }

    @ParameterizedTest
    @CsvSource({
        "Gol, 0, 0, 2",
        "Uno, 0, 0, 1",
        "'', 2020, 0, 3",
        "'', 2018, 0, 1",
        "'', 0, 2, 2"
    })
    void deveRetornarTodosOsVeiculosQuandoNaoHaFiltros(
        String modelo, Integer ano, Long marcaId, Integer expectedSize
    ) {
        // given
        var veiculoFilter = new VeiculoFilterRequest(modelo, ano, marcaId);

        // when
        List<Veiculo> veiculos = veiculoRepository.findAll(
            new VeiculoFilterSpecification(veiculoFilter)
        );

        // then
        assertThat(veiculos.size(), is(expectedSize));
        veiculos.forEach((Veiculo v) -> {
            if(!modelo.isEmpty())
                assertThat(v.getModelo(), is(modelo));
            else if(ano > 0)
                assertThat(v.getAno(), is(ano));
            else
                assertThat(v.getMarca().getId(), is(marcaId));
        });
    }
}
