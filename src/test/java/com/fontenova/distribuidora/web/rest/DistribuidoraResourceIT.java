package com.fontenova.distribuidora.web.rest;

import static com.fontenova.distribuidora.domain.DistribuidoraAsserts.*;
import static com.fontenova.distribuidora.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fontenova.distribuidora.IntegrationTest;
import com.fontenova.distribuidora.domain.Distribuidora;
import com.fontenova.distribuidora.repository.DistribuidoraRepository;
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
 * Integration tests for the {@link DistribuidoraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DistribuidoraResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_CNPJ = "BBBBBBBBBB";

    private static final Integer DEFAULT_CONTATO = 1;
    private static final Integer UPDATED_CONTATO = 2;

    private static final String DEFAULT_CEP = "AAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBB";

    private static final String DEFAULT_RUA = "AAAAAAAAAA";
    private static final String UPDATED_RUA = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    private static final String DEFAULT_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AA";
    private static final String UPDATED_ESTADO = "BB";

    private static final String DEFAULT_DETALHES = "AAAAAAAAAA";
    private static final String UPDATED_DETALHES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/distribuidoras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DistribuidoraRepository distribuidoraRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDistribuidoraMockMvc;

    private Distribuidora distribuidora;

    private Distribuidora insertedDistribuidora;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Distribuidora createEntity() {
        return new Distribuidora()
            .nome(DEFAULT_NOME)
            .cnpj(DEFAULT_CNPJ)
            .contato(DEFAULT_CONTATO)
            .cep(DEFAULT_CEP)
            .cidade(DEFAULT_CIDADE)
            .bairro(DEFAULT_BAIRRO)
            .rua(DEFAULT_RUA)
            .numero(DEFAULT_NUMERO)
            .referencia(DEFAULT_REFERENCIA)
            .estado(DEFAULT_ESTADO)
            .detalhes(DEFAULT_DETALHES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Distribuidora createUpdatedEntity() {
        return new Distribuidora()
            .nome(UPDATED_NOME)
            .cnpj(UPDATED_CNPJ)
            .contato(UPDATED_CONTATO)
            .cep(UPDATED_CEP)
            .cidade(UPDATED_CIDADE)
            .bairro(UPDATED_BAIRRO)
            .rua(UPDATED_RUA)
            .numero(UPDATED_NUMERO)
            .referencia(UPDATED_REFERENCIA)
            .estado(UPDATED_ESTADO)
            .detalhes(UPDATED_DETALHES);
    }

    @BeforeEach
    public void initTest() {
        distribuidora = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDistribuidora != null) {
            distribuidoraRepository.delete(insertedDistribuidora);
            insertedDistribuidora = null;
        }
    }

    @Test
    @Transactional
    void createDistribuidora() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Distribuidora
        var returnedDistribuidora = om.readValue(
            restDistribuidoraMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuidora)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Distribuidora.class
        );

        // Validate the Distribuidora in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDistribuidoraUpdatableFieldsEquals(returnedDistribuidora, getPersistedDistribuidora(returnedDistribuidora));

        insertedDistribuidora = returnedDistribuidora;
    }

    @Test
    @Transactional
    void createDistribuidoraWithExistingId() throws Exception {
        // Create the Distribuidora with an existing ID
        distribuidora.setId(1);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDistribuidoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuidora)))
            .andExpect(status().isBadRequest());

        // Validate the Distribuidora in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        distribuidora.setNome(null);

        // Create the Distribuidora, which fails.

        restDistribuidoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuidora)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCnpjIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        distribuidora.setCnpj(null);

        // Create the Distribuidora, which fails.

        restDistribuidoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuidora)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContatoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        distribuidora.setContato(null);

        // Create the Distribuidora, which fails.

        restDistribuidoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuidora)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCepIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        distribuidora.setCep(null);

        // Create the Distribuidora, which fails.

        restDistribuidoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuidora)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCidadeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        distribuidora.setCidade(null);

        // Create the Distribuidora, which fails.

        restDistribuidoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuidora)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBairroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        distribuidora.setBairro(null);

        // Create the Distribuidora, which fails.

        restDistribuidoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuidora)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRuaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        distribuidora.setRua(null);

        // Create the Distribuidora, which fails.

        restDistribuidoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuidora)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        distribuidora.setNumero(null);

        // Create the Distribuidora, which fails.

        restDistribuidoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuidora)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        distribuidora.setEstado(null);

        // Create the Distribuidora, which fails.

        restDistribuidoraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuidora)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDistribuidoras() throws Exception {
        // Initialize the database
        insertedDistribuidora = distribuidoraRepository.saveAndFlush(distribuidora);

        // Get all the distribuidoraList
        restDistribuidoraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(distribuidora.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].contato").value(hasItem(DEFAULT_CONTATO)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].rua").value(hasItem(DEFAULT_RUA)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].referencia").value(hasItem(DEFAULT_REFERENCIA)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].detalhes").value(hasItem(DEFAULT_DETALHES)));
    }

    @Test
    @Transactional
    void getDistribuidora() throws Exception {
        // Initialize the database
        insertedDistribuidora = distribuidoraRepository.saveAndFlush(distribuidora);

        // Get the distribuidora
        restDistribuidoraMockMvc
            .perform(get(ENTITY_API_URL_ID, distribuidora.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(distribuidora.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ))
            .andExpect(jsonPath("$.contato").value(DEFAULT_CONTATO))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO))
            .andExpect(jsonPath("$.rua").value(DEFAULT_RUA))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.referencia").value(DEFAULT_REFERENCIA))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.detalhes").value(DEFAULT_DETALHES));
    }

    @Test
    @Transactional
    void getNonExistingDistribuidora() throws Exception {
        // Get the distribuidora
        restDistribuidoraMockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDistribuidora() throws Exception {
        // Initialize the database
        insertedDistribuidora = distribuidoraRepository.saveAndFlush(distribuidora);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the distribuidora
        Distribuidora updatedDistribuidora = distribuidoraRepository.findById(distribuidora.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDistribuidora are not directly saved in db
        em.detach(updatedDistribuidora);
        updatedDistribuidora
            .nome(UPDATED_NOME)
            .cnpj(UPDATED_CNPJ)
            .contato(UPDATED_CONTATO)
            .cep(UPDATED_CEP)
            .cidade(UPDATED_CIDADE)
            .bairro(UPDATED_BAIRRO)
            .rua(UPDATED_RUA)
            .numero(UPDATED_NUMERO)
            .referencia(UPDATED_REFERENCIA)
            .estado(UPDATED_ESTADO)
            .detalhes(UPDATED_DETALHES);

        restDistribuidoraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDistribuidora.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDistribuidora))
            )
            .andExpect(status().isOk());

        // Validate the Distribuidora in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDistribuidoraToMatchAllProperties(updatedDistribuidora);
    }

    @Test
    @Transactional
    void putNonExistingDistribuidora() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distribuidora.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistribuidoraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, distribuidora.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(distribuidora))
            )
            .andExpect(status().isBadRequest());

        // Validate the Distribuidora in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDistribuidora() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distribuidora.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistribuidoraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(distribuidora))
            )
            .andExpect(status().isBadRequest());

        // Validate the Distribuidora in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDistribuidora() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distribuidora.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistribuidoraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(distribuidora)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Distribuidora in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDistribuidoraWithPatch() throws Exception {
        // Initialize the database
        insertedDistribuidora = distribuidoraRepository.saveAndFlush(distribuidora);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the distribuidora using partial update
        Distribuidora partialUpdatedDistribuidora = new Distribuidora();
        partialUpdatedDistribuidora.setId(distribuidora.getId());

        partialUpdatedDistribuidora
            .nome(UPDATED_NOME)
            .cnpj(UPDATED_CNPJ)
            .contato(UPDATED_CONTATO)
            .cep(UPDATED_CEP)
            .bairro(UPDATED_BAIRRO)
            .numero(UPDATED_NUMERO)
            .referencia(UPDATED_REFERENCIA)
            .estado(UPDATED_ESTADO)
            .detalhes(UPDATED_DETALHES);

        restDistribuidoraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDistribuidora.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDistribuidora))
            )
            .andExpect(status().isOk());

        // Validate the Distribuidora in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDistribuidoraUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDistribuidora, distribuidora),
            getPersistedDistribuidora(distribuidora)
        );
    }

    @Test
    @Transactional
    void fullUpdateDistribuidoraWithPatch() throws Exception {
        // Initialize the database
        insertedDistribuidora = distribuidoraRepository.saveAndFlush(distribuidora);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the distribuidora using partial update
        Distribuidora partialUpdatedDistribuidora = new Distribuidora();
        partialUpdatedDistribuidora.setId(distribuidora.getId());

        partialUpdatedDistribuidora
            .nome(UPDATED_NOME)
            .cnpj(UPDATED_CNPJ)
            .contato(UPDATED_CONTATO)
            .cep(UPDATED_CEP)
            .cidade(UPDATED_CIDADE)
            .bairro(UPDATED_BAIRRO)
            .rua(UPDATED_RUA)
            .numero(UPDATED_NUMERO)
            .referencia(UPDATED_REFERENCIA)
            .estado(UPDATED_ESTADO)
            .detalhes(UPDATED_DETALHES);

        restDistribuidoraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDistribuidora.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDistribuidora))
            )
            .andExpect(status().isOk());

        // Validate the Distribuidora in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDistribuidoraUpdatableFieldsEquals(partialUpdatedDistribuidora, getPersistedDistribuidora(partialUpdatedDistribuidora));
    }

    @Test
    @Transactional
    void patchNonExistingDistribuidora() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distribuidora.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDistribuidoraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, distribuidora.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(distribuidora))
            )
            .andExpect(status().isBadRequest());

        // Validate the Distribuidora in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDistribuidora() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distribuidora.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistribuidoraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(distribuidora))
            )
            .andExpect(status().isBadRequest());

        // Validate the Distribuidora in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDistribuidora() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        distribuidora.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDistribuidoraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(distribuidora)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Distribuidora in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDistribuidora() throws Exception {
        // Initialize the database
        insertedDistribuidora = distribuidoraRepository.saveAndFlush(distribuidora);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the distribuidora
        restDistribuidoraMockMvc
            .perform(delete(ENTITY_API_URL_ID, distribuidora.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return distribuidoraRepository.count();
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

    protected Distribuidora getPersistedDistribuidora(Distribuidora distribuidora) {
        return distribuidoraRepository.findById(distribuidora.getId()).orElseThrow();
    }

    protected void assertPersistedDistribuidoraToMatchAllProperties(Distribuidora expectedDistribuidora) {
        assertDistribuidoraAllPropertiesEquals(expectedDistribuidora, getPersistedDistribuidora(expectedDistribuidora));
    }

    protected void assertPersistedDistribuidoraToMatchUpdatableProperties(Distribuidora expectedDistribuidora) {
        assertDistribuidoraAllUpdatablePropertiesEquals(expectedDistribuidora, getPersistedDistribuidora(expectedDistribuidora));
    }
}
