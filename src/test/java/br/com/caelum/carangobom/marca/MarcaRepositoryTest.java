package br.com.caelum.carangobom.marca;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@DataJpaTest
class MarcaRepositoryTest {

    @Autowired
    private MarcaRepository mr;

    @Test
    void deveRetornarListaDeMarcas() {
        // when
        List<Marca> marcas = mr.findAllByOrderByNome();

        // then
        assertThat(marcas, is(not(empty())));
        assertThat(marcas.size(), is(3));
    }
}
