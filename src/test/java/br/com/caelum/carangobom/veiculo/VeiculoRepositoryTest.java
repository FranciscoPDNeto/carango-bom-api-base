package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.usuario.UsuarioRepository;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoDashboard;
import org.junit.jupiter.api.Test;
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
}
