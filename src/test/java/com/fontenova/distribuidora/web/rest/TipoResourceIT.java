package com.fontenova.distribuidora.web.rest;

import static com.fontenova.distribuidora.domain.TipoAsserts.*;
import static com.fontenova.distribuidora.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fontenova.distribuidora.IntegrationTest;
import com.fontenova.distribuidora.domain.Tipo;
import com.fontenova.distribuidora.repository.TipoRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TipoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tipos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TipoRepository tipoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoMockMvc;

    private Tipo tipo;

    private Tipo insertedTipo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tipo createEntity() {
        return new Tipo().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tipo createUpdatedEntity() {
        return new Tipo().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        tipo = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTipo != null) {
            tipoRepository.delete(insertedTipo);
            insertedTipo = null;
        }
    }

    @Test
    @Transactional
    void createTipo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Tipo
        var returnedTipo = om.readValue(
            restTipoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Tipo.class
        );

        // Validate the Tipo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTipoUpdatableFieldsEquals(returnedTipo, getPersistedTipo(returnedTipo));

        insertedTipo = returnedTipo;
    }

    @Test
    @Transactional
    void createTipoWithExistingId() throws Exception {
        // Create the Tipo with an existing ID
        tipo.setId(1);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipo)))
            .andExpect(status().isBadRequest());

        // Validate the Tipo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipo.setName(null);

        // Create the Tipo, which fails.

        restTipoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipo)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipos() throws Exception {
        // Initialize the database
        insertedTipo = tipoRepository.saveAndFlush(tipo);

        // Get all the tipoList
        restTipoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTipo() throws Exception {
        // Initialize the database
        insertedTipo = tipoRepository.saveAndFlush(tipo);

        // Get the tipo
        restTipoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTipo() throws Exception {
        // Get the tipo
        restTipoMockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipo() throws Exception {
        // Initialize the database
        insertedTipo = tipoRepository.saveAndFlush(tipo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipo
        Tipo updatedTipo = tipoRepository.findById(tipo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTipo are not directly saved in db
        em.detach(updatedTipo);
        updatedTipo.name(UPDATED_NAME);

        restTipoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTipo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTipo))
            )
            .andExpect(status().isOk());

        // Validate the Tipo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTipoToMatchAllProperties(updatedTipo);
    }

    @Test
    @Transactional
    void putNonExistingTipo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipo.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(put(ENTITY_API_URL_ID, tipo.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipo)))
            .andExpect(status().isBadRequest());

        // Validate the Tipo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipo.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tipo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipo.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tipo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoWithPatch() throws Exception {
        // Initialize the database
        insertedTipo = tipoRepository.saveAndFlush(tipo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipo using partial update
        Tipo partialUpdatedTipo = new Tipo();
        partialUpdatedTipo.setId(tipo.getId());

        restTipoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipo))
            )
            .andExpect(status().isOk());

        // Validate the Tipo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTipo, tipo), getPersistedTipo(tipo));
    }

    @Test
    @Transactional
    void fullUpdateTipoWithPatch() throws Exception {
        // Initialize the database
        insertedTipo = tipoRepository.saveAndFlush(tipo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipo using partial update
        Tipo partialUpdatedTipo = new Tipo();
        partialUpdatedTipo.setId(tipo.getId());

        partialUpdatedTipo.name(UPDATED_NAME);

        restTipoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipo))
            )
            .andExpect(status().isOk());

        // Validate the Tipo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoUpdatableFieldsEquals(partialUpdatedTipo, getPersistedTipo(partialUpdatedTipo));
    }

    @Test
    @Transactional
    void patchNonExistingTipo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipo.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(patch(ENTITY_API_URL_ID, tipo.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tipo)))
            .andExpect(status().isBadRequest());

        // Validate the Tipo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipo.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tipo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipo.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tipo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tipo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipo() throws Exception {
        // Initialize the database
        insertedTipo = tipoRepository.saveAndFlush(tipo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tipo
        restTipoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tipoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Tipo getPersistedTipo(Tipo tipo) {
        return tipoRepository.findById(tipo.getId()).orElseThrow();
    }

    protected void assertPersistedTipoToMatchAllProperties(Tipo expectedTipo) {
        assertTipoAllPropertiesEquals(expectedTipo, getPersistedTipo(expectedTipo));
    }

    protected void assertPersistedTipoToMatchUpdatableProperties(Tipo expectedTipo) {
        assertTipoAllUpdatablePropertiesEquals(expectedTipo, getPersistedTipo(expectedTipo));
    }
}
