package com.fontenova.distribuidora.domain;

import static com.fontenova.distribuidora.domain.DistribuidoraTestSamples.*;
import static com.fontenova.distribuidora.domain.ProdutoTestSamples.*;
import static com.fontenova.distribuidora.domain.TipoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.fontenova.distribuidora.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProdutoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Produto.class);
        Produto produto1 = getProdutoSample1();
        Produto produto2 = new Produto();
        assertThat(produto1).isNotEqualTo(produto2);

        produto2.setId(produto1.getId());
        assertThat(produto1).isEqualTo(produto2);

        produto2 = getProdutoSample2();
        assertThat(produto1).isNotEqualTo(produto2);
    }

    @Test
    void tipoTest() {
        Produto produto = getProdutoRandomSampleGenerator();
        Tipo tipoBack = getTipoRandomSampleGenerator();

        produto.setTipo(tipoBack);
        assertThat(produto.getTipo()).isEqualTo(tipoBack);

        produto.tipo(null);
        assertThat(produto.getTipo()).isNull();
    }

    @Test
    void distribuidoraTest() {
        Produto produto = getProdutoRandomSampleGenerator();
        Distribuidora distribuidoraBack = getDistribuidoraRandomSampleGenerator();

        produto.setDistribuidora(distribuidoraBack);
        assertThat(produto.getDistribuidora()).isEqualTo(distribuidoraBack);

        produto.distribuidora(null);
        assertThat(produto.getDistribuidora()).isNull();
    }
}
