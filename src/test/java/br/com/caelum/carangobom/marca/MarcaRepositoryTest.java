package br.com.caelum.carangobom.marca;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MarcaRepositoryTest {

    @Autowired
    private MarcaRepository mr;

    @Autowired
    private TestEntityManager em;

    @Test
    void deveRetornarListaDeMarcas() {
        // given
        String marcaFiat = "Fiat";
        String marcaAudi = "Audi";
        Marca m1 = new Marca();
        Marca m2 = new Marca();
        m1.setNome(marcaFiat);
        m2.setNome(marcaAudi);

        em.persist(m1);
        em.persist(m2);

        // when
        List<Marca> marcas = mr.findAllByOrderByNome();

        // then
        assertThat(marcas, is(not(empty())));
        assertThat(marcas.size(), is(2));
    }

    @Test
    void deveRetornarListaVazia() {
        // when
        List<Marca> marcas = mr.findAllByOrderByNome();

        // then
        assertThat(marcas, is(empty()));
        assertThat(marcas.size(), is(0));
    }
}
