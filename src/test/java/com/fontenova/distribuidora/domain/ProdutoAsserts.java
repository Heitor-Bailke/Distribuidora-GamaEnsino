package com.fontenova.distribuidora.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ProdutoAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProdutoAllPropertiesEquals(Produto expected, Produto actual) {
        assertProdutoAutoGeneratedPropertiesEquals(expected, actual);
        assertProdutoAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProdutoAllUpdatablePropertiesEquals(Produto expected, Produto actual) {
        assertProdutoUpdatableFieldsEquals(expected, actual);
        assertProdutoUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProdutoAutoGeneratedPropertiesEquals(Produto expected, Produto actual) {
        assertThat(expected)
            .as("Verify Produto auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProdutoUpdatableFieldsEquals(Produto expected, Produto actual) {
        assertThat(expected)
            .as("Verify Produto relevant properties")
            .satisfies(e -> assertThat(e.getNome()).as("check nome").isEqualTo(actual.getNome()))
            .satisfies(e -> assertThat(e.getValor()).as("check valor").isEqualTo(actual.getValor()))
            .satisfies(e -> assertThat(e.getQuantidade()).as("check quantidade").isEqualTo(actual.getQuantidade()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProdutoUpdatableRelationshipsEquals(Produto expected, Produto actual) {
        assertThat(expected)
            .as("Verify Produto relationships")
            .satisfies(e -> assertThat(e.getTipo()).as("check tipo").isEqualTo(actual.getTipo()))
            .satisfies(e -> assertThat(e.getDistribuidora()).as("check distribuidora").isEqualTo(actual.getDistribuidora()));
    }
}
