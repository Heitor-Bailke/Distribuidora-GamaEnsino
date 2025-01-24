package com.fontenova.distribuidora.domain;

import static com.fontenova.distribuidora.domain.DistribuidoraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fontenova.distribuidora.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DistribuidoraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Distribuidora.class);
        Distribuidora distribuidora1 = getDistribuidoraSample1();
        Distribuidora distribuidora2 = new Distribuidora();
        assertThat(distribuidora1).isNotEqualTo(distribuidora2);

        distribuidora2.setId(distribuidora1.getId());
        assertThat(distribuidora1).isEqualTo(distribuidora2);

        distribuidora2 = getDistribuidoraSample2();
        assertThat(distribuidora1).isNotEqualTo(distribuidora2);
    }
}
