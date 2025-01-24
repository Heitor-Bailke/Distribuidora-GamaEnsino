package com.fontenova.distribuidora.domain;

import static com.fontenova.distribuidora.domain.TipoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fontenova.distribuidora.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tipo.class);
        Tipo tipo1 = getTipoSample1();
        Tipo tipo2 = new Tipo();
        assertThat(tipo1).isNotEqualTo(tipo2);

        tipo2.setId(tipo1.getId());
        assertThat(tipo1).isEqualTo(tipo2);

        tipo2 = getTipoSample2();
        assertThat(tipo1).isNotEqualTo(tipo2);
    }
}
