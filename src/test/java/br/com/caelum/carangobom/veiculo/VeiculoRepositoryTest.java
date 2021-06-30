package br.com.caelum.carangobom.veiculo;

import br.com.caelum.carangobom.marca.Marca;
import br.com.caelum.carangobom.veiculo.dtos.VeiculoDashboard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class VeiculoRepositoryTest {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void groupVeiculosByMarca() {
        // given
        Marca m1 = new Marca("Fiat");

        Veiculo v1 = new Veiculo("Uno", 2001, 30000L, m1);
        Veiculo v2 = new Veiculo("Elba", 1990, 10000L, m1);

        em.persist(m1);
        em.persist(v1);
        em.persist(v2);

        // when
        List<VeiculoDashboard> dashboards = veiculoRepository.groupVeiculosByMarca();

        // then
        assertThat(dashboards.get(0).getNumVeiculos(), is(2L));
        assertThat(dashboards.get(0).getSomaValor(), is(40000L));
        assertThat(dashboards.get(0).getMarca(), is("Fiat"));
    }
}
