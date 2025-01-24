package com.fontenova.distribuidora.web.rest;

import static com.fontenova.distribuidora.domain.ProdutoAsserts.*;
import static com.fontenova.distribuidora.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fontenova.distribuidora.IntegrationTest;
import com.fontenova.distribuidora.domain.Produto;
import com.fontenova.distribuidora.repository.ProdutoRepository;
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
 * Integration tests for the {@link ProdutoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProdutoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Float DEFAULT_VALOR = 1F;
    private static final Float UPDATED_VALOR = 2F;

    private static final Integer DEFAULT_QUANTIDADE = 1;
    private static final Integer UPDATED_QUANTIDADE = 2;

    private static final String ENTITY_API_URL = "/api/produtos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProdutoMockMvc;

    private Produto produto;

    private Produto insertedProduto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Produto createEntity() {
        return new Produto().nome(DEFAULT_NOME).valor(DEFAULT_VALOR).quantidade(DEFAULT_QUANTIDADE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Produto createUpdatedEntity() {
        return new Produto().nome(UPDATED_NOME).valor(UPDATED_VALOR).quantidade(UPDATED_QUANTIDADE);
    }

    @BeforeEach
    public void initTest() {
        produto = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProduto != null) {
            produtoRepository.delete(insertedProduto);
            insertedProduto = null;
        }
    }

    @Test
    @Transactional
    void createProduto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Produto
        var returnedProduto = om.readValue(
            restProdutoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Produto.class
        );

        // Validate the Produto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProdutoUpdatableFieldsEquals(returnedProduto, getPersistedProduto(returnedProduto));

        insertedProduto = returnedProduto;
    }

    @Test
    @Transactional
    void createProdutoWithExistingId() throws Exception {
        // Create the Produto with an existing ID
        produto.setId(1);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProdutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produto)))
            .andExpect(status().isBadRequest());

        // Validate the Produto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        produto.setNome(null);

        // Create the Produto, which fails.

        restProdutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        produto.setValor(null);

        // Create the Produto, which fails.

        restProdutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantidadeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        produto.setQuantidade(null);

        // Create the Produto, which fails.

        restProdutoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produto)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProdutos() throws Exception {
        // Initialize the database
        insertedProduto = produtoRepository.saveAndFlush(produto);

        // Get all the produtoList
        restProdutoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE)));
    }

    @Test
    @Transactional
    void getProduto() throws Exception {
        // Initialize the database
        insertedProduto = produtoRepository.saveAndFlush(produto);

        // Get the produto
        restProdutoMockMvc
            .perform(get(ENTITY_API_URL_ID, produto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(produto.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.quantidade").value(DEFAULT_QUANTIDADE));
    }

    @Test
    @Transactional
    void getNonExistingProduto() throws Exception {
        // Get the produto
        restProdutoMockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduto() throws Exception {
        // Initialize the database
        insertedProduto = produtoRepository.saveAndFlush(produto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the produto
        Produto updatedProduto = produtoRepository.findById(produto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProduto are not directly saved in db
        em.detach(updatedProduto);
        updatedProduto.nome(UPDATED_NOME).valor(UPDATED_VALOR).quantidade(UPDATED_QUANTIDADE);

        restProdutoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProduto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProduto))
            )
            .andExpect(status().isOk());

        // Validate the Produto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProdutoToMatchAllProperties(updatedProduto);
    }

    @Test
    @Transactional
    void putNonExistingProduto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produto.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(put(ENTITY_API_URL_ID, produto.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produto)))
            .andExpect(status().isBadRequest());

        // Validate the Produto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produto.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(produto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produto.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(produto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Produto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProdutoWithPatch() throws Exception {
        // Initialize the database
        insertedProduto = produtoRepository.saveAndFlush(produto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the produto using partial update
        Produto partialUpdatedProduto = new Produto();
        partialUpdatedProduto.setId(produto.getId());

        partialUpdatedProduto.valor(UPDATED_VALOR).quantidade(UPDATED_QUANTIDADE);

        restProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduto))
            )
            .andExpect(status().isOk());

        // Validate the Produto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProdutoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProduto, produto), getPersistedProduto(produto));
    }

    @Test
    @Transactional
    void fullUpdateProdutoWithPatch() throws Exception {
        // Initialize the database
        insertedProduto = produtoRepository.saveAndFlush(produto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the produto using partial update
        Produto partialUpdatedProduto = new Produto();
        partialUpdatedProduto.setId(produto.getId());

        partialUpdatedProduto.nome(UPDATED_NOME).valor(UPDATED_VALOR).quantidade(UPDATED_QUANTIDADE);

        restProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduto))
            )
            .andExpect(status().isOk());

        // Validate the Produto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProdutoUpdatableFieldsEquals(partialUpdatedProduto, getPersistedProduto(partialUpdatedProduto));
    }

    @Test
    @Transactional
    void patchNonExistingProduto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produto.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, produto.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(produto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produto.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(produto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Produto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        produto.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProdutoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(produto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Produto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduto() throws Exception {
        // Initialize the database
        insertedProduto = produtoRepository.saveAndFlush(produto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the produto
        restProdutoMockMvc
            .perform(delete(ENTITY_API_URL_ID, produto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return produtoRepository.count();
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

    protected Produto getPersistedProduto(Produto produto) {
        return produtoRepository.findById(produto.getId()).orElseThrow();
    }

    protected void assertPersistedProdutoToMatchAllProperties(Produto expectedProduto) {
        assertProdutoAllPropertiesEquals(expectedProduto, getPersistedProduto(expectedProduto));
    }

    protected void assertPersistedProdutoToMatchUpdatableProperties(Produto expectedProduto) {
        assertProdutoAllUpdatablePropertiesEquals(expectedProduto, getPersistedProduto(expectedProduto));
    }
}
