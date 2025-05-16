package com.universite.module.web.rest;

import static com.universite.module.domain.EdtAsserts.*;
import static com.universite.module.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universite.module.IntegrationTest;
import com.universite.module.domain.Edt;
import com.universite.module.repository.EdtRepository;
import com.universite.module.service.dto.EdtDTO;
import com.universite.module.service.mapper.EdtMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link EdtResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EdtResourceIT {

    private static final Integer DEFAULT_ID_EDT = 1;
    private static final Integer UPDATED_ID_EDT = 2;

    private static final String DEFAULT_DURATION = "AAAAAAAAAA";
    private static final String UPDATED_DURATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/edts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EdtRepository edtRepository;

    @Autowired
    private EdtMapper edtMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEdtMockMvc;

    private Edt edt;

    private Edt insertedEdt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Edt createEntity() {
        return new Edt().idEdt(DEFAULT_ID_EDT).duration(DEFAULT_DURATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Edt createUpdatedEntity() {
        return new Edt().idEdt(UPDATED_ID_EDT).duration(UPDATED_DURATION);
    }

    @BeforeEach
    public void initTest() {
        edt = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEdt != null) {
            edtRepository.delete(insertedEdt);
            insertedEdt = null;
        }
    }

    @Test
    @Transactional
    void createEdt() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Edt
        EdtDTO edtDTO = edtMapper.toDto(edt);
        var returnedEdtDTO = om.readValue(
            restEdtMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edtDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EdtDTO.class
        );

        // Validate the Edt in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEdt = edtMapper.toEntity(returnedEdtDTO);
        assertEdtUpdatableFieldsEquals(returnedEdt, getPersistedEdt(returnedEdt));

        insertedEdt = returnedEdt;
    }

    @Test
    @Transactional
    void createEdtWithExistingId() throws Exception {
        // Create the Edt with an existing ID
        edt.setId(1L);
        EdtDTO edtDTO = edtMapper.toDto(edt);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEdtMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edtDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Edt in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdEdtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        edt.setIdEdt(null);

        // Create the Edt, which fails.
        EdtDTO edtDTO = edtMapper.toDto(edt);

        restEdtMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edtDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEdts() throws Exception {
        // Initialize the database
        insertedEdt = edtRepository.saveAndFlush(edt);

        // Get all the edtList
        restEdtMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(edt.getId().intValue())))
            .andExpect(jsonPath("$.[*].idEdt").value(hasItem(DEFAULT_ID_EDT)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)));
    }

    @Test
    @Transactional
    void getEdt() throws Exception {
        // Initialize the database
        insertedEdt = edtRepository.saveAndFlush(edt);

        // Get the edt
        restEdtMockMvc
            .perform(get(ENTITY_API_URL_ID, edt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(edt.getId().intValue()))
            .andExpect(jsonPath("$.idEdt").value(DEFAULT_ID_EDT))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION));
    }

    @Test
    @Transactional
    void getNonExistingEdt() throws Exception {
        // Get the edt
        restEdtMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEdt() throws Exception {
        // Initialize the database
        insertedEdt = edtRepository.saveAndFlush(edt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the edt
        Edt updatedEdt = edtRepository.findById(edt.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEdt are not directly saved in db
        em.detach(updatedEdt);
        updatedEdt.idEdt(UPDATED_ID_EDT).duration(UPDATED_DURATION);
        EdtDTO edtDTO = edtMapper.toDto(updatedEdt);

        restEdtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, edtDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(edtDTO))
            )
            .andExpect(status().isOk());

        // Validate the Edt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEdtToMatchAllProperties(updatedEdt);
    }

    @Test
    @Transactional
    void putNonExistingEdt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edt.setId(longCount.incrementAndGet());

        // Create the Edt
        EdtDTO edtDTO = edtMapper.toDto(edt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEdtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, edtDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(edtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Edt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEdt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edt.setId(longCount.incrementAndGet());

        // Create the Edt
        EdtDTO edtDTO = edtMapper.toDto(edt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(edtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Edt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEdt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edt.setId(longCount.incrementAndGet());

        // Create the Edt
        EdtDTO edtDTO = edtMapper.toDto(edt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdtMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(edtDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Edt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEdtWithPatch() throws Exception {
        // Initialize the database
        insertedEdt = edtRepository.saveAndFlush(edt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the edt using partial update
        Edt partialUpdatedEdt = new Edt();
        partialUpdatedEdt.setId(edt.getId());

        partialUpdatedEdt.idEdt(UPDATED_ID_EDT).duration(UPDATED_DURATION);

        restEdtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEdt.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEdt))
            )
            .andExpect(status().isOk());

        // Validate the Edt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEdtUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEdt, edt), getPersistedEdt(edt));
    }

    @Test
    @Transactional
    void fullUpdateEdtWithPatch() throws Exception {
        // Initialize the database
        insertedEdt = edtRepository.saveAndFlush(edt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the edt using partial update
        Edt partialUpdatedEdt = new Edt();
        partialUpdatedEdt.setId(edt.getId());

        partialUpdatedEdt.idEdt(UPDATED_ID_EDT).duration(UPDATED_DURATION);

        restEdtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEdt.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEdt))
            )
            .andExpect(status().isOk());

        // Validate the Edt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEdtUpdatableFieldsEquals(partialUpdatedEdt, getPersistedEdt(partialUpdatedEdt));
    }

    @Test
    @Transactional
    void patchNonExistingEdt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edt.setId(longCount.incrementAndGet());

        // Create the Edt
        EdtDTO edtDTO = edtMapper.toDto(edt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEdtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, edtDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(edtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Edt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEdt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edt.setId(longCount.incrementAndGet());

        // Create the Edt
        EdtDTO edtDTO = edtMapper.toDto(edt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(edtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Edt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEdt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        edt.setId(longCount.incrementAndGet());

        // Create the Edt
        EdtDTO edtDTO = edtMapper.toDto(edt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEdtMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(edtDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Edt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEdt() throws Exception {
        // Initialize the database
        insertedEdt = edtRepository.saveAndFlush(edt);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the edt
        restEdtMockMvc
            .perform(delete(ENTITY_API_URL_ID, edt.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return edtRepository.count();
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

    protected Edt getPersistedEdt(Edt edt) {
        return edtRepository.findById(edt.getId()).orElseThrow();
    }

    protected void assertPersistedEdtToMatchAllProperties(Edt expectedEdt) {
        assertEdtAllPropertiesEquals(expectedEdt, getPersistedEdt(expectedEdt));
    }

    protected void assertPersistedEdtToMatchUpdatableProperties(Edt expectedEdt) {
        assertEdtAllUpdatablePropertiesEquals(expectedEdt, getPersistedEdt(expectedEdt));
    }
}
