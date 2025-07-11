package com.universite.web.rest;

import static com.universite.domain.SemestreAsserts.*;
import static com.universite.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universite.IntegrationTest;
import com.universite.domain.Semestre;
import com.universite.repository.SemestreRepository;
import com.universite.service.dto.SemestreDTO;
import com.universite.service.mapper.SemestreMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SemestreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SemestreResourceIT {

    private static final Integer DEFAULT_ID_SEMESTRE = 1;
    private static final Integer UPDATED_ID_SEMESTRE = 2;

    private static final LocalDate DEFAULT_STARTED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STARTED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ENDED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENDED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/semestres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SemestreRepository semestreRepository;

    @Autowired
    private SemestreMapper semestreMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSemestreMockMvc;

    private Semestre semestre;

    private Semestre insertedSemestre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semestre createEntity() {
        return new Semestre().idSemestre(DEFAULT_ID_SEMESTRE).startedAt(DEFAULT_STARTED_AT).endedAt(DEFAULT_ENDED_AT).name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semestre createUpdatedEntity() {
        return new Semestre().idSemestre(UPDATED_ID_SEMESTRE).startedAt(UPDATED_STARTED_AT).endedAt(UPDATED_ENDED_AT).name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        semestre = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSemestre != null) {
            semestreRepository.delete(insertedSemestre);
            insertedSemestre = null;
        }
    }

    @Test
    @Transactional
    void createSemestre() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Semestre
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);
        var returnedSemestreDTO = om.readValue(
            restSemestreMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(semestreDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SemestreDTO.class
        );

        // Validate the Semestre in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSemestre = semestreMapper.toEntity(returnedSemestreDTO);
        assertSemestreUpdatableFieldsEquals(returnedSemestre, getPersistedSemestre(returnedSemestre));

        insertedSemestre = returnedSemestre;
    }

    @Test
    @Transactional
    void createSemestreWithExistingId() throws Exception {
        // Create the Semestre with an existing ID
        semestre.setId(1L);
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSemestreMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(semestreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Semestre in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdSemestreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        semestre.setIdSemestre(null);

        // Create the Semestre, which fails.
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);

        restSemestreMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(semestreDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSemestres() throws Exception {
        // Initialize the database
        insertedSemestre = semestreRepository.saveAndFlush(semestre);

        // Get all the semestreList
        restSemestreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(semestre.getId().intValue())))
            .andExpect(jsonPath("$.[*].idSemestre").value(hasItem(DEFAULT_ID_SEMESTRE)))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].endedAt").value(hasItem(DEFAULT_ENDED_AT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSemestre() throws Exception {
        // Initialize the database
        insertedSemestre = semestreRepository.saveAndFlush(semestre);

        // Get the semestre
        restSemestreMockMvc
            .perform(get(ENTITY_API_URL_ID, semestre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(semestre.getId().intValue()))
            .andExpect(jsonPath("$.idSemestre").value(DEFAULT_ID_SEMESTRE))
            .andExpect(jsonPath("$.startedAt").value(DEFAULT_STARTED_AT.toString()))
            .andExpect(jsonPath("$.endedAt").value(DEFAULT_ENDED_AT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSemestre() throws Exception {
        // Get the semestre
        restSemestreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSemestre() throws Exception {
        // Initialize the database
        insertedSemestre = semestreRepository.saveAndFlush(semestre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the semestre
        Semestre updatedSemestre = semestreRepository.findById(semestre.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSemestre are not directly saved in db
        em.detach(updatedSemestre);
        updatedSemestre.idSemestre(UPDATED_ID_SEMESTRE).startedAt(UPDATED_STARTED_AT).endedAt(UPDATED_ENDED_AT).name(UPDATED_NAME);
        SemestreDTO semestreDTO = semestreMapper.toDto(updatedSemestre);

        restSemestreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, semestreDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(semestreDTO))
            )
            .andExpect(status().isOk());

        // Validate the Semestre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSemestreToMatchAllProperties(updatedSemestre);
    }

    @Test
    @Transactional
    void putNonExistingSemestre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        semestre.setId(longCount.incrementAndGet());

        // Create the Semestre
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, semestreDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(semestreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semestre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSemestre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        semestre.setId(longCount.incrementAndGet());

        // Create the Semestre
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(semestreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semestre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSemestre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        semestre.setId(longCount.incrementAndGet());

        // Create the Semestre
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(semestreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Semestre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSemestreWithPatch() throws Exception {
        // Initialize the database
        insertedSemestre = semestreRepository.saveAndFlush(semestre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the semestre using partial update
        Semestre partialUpdatedSemestre = new Semestre();
        partialUpdatedSemestre.setId(semestre.getId());

        partialUpdatedSemestre.idSemestre(UPDATED_ID_SEMESTRE).startedAt(UPDATED_STARTED_AT).endedAt(UPDATED_ENDED_AT).name(UPDATED_NAME);

        restSemestreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSemestre.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSemestre))
            )
            .andExpect(status().isOk());

        // Validate the Semestre in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSemestreUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSemestre, semestre), getPersistedSemestre(semestre));
    }

    @Test
    @Transactional
    void fullUpdateSemestreWithPatch() throws Exception {
        // Initialize the database
        insertedSemestre = semestreRepository.saveAndFlush(semestre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the semestre using partial update
        Semestre partialUpdatedSemestre = new Semestre();
        partialUpdatedSemestre.setId(semestre.getId());

        partialUpdatedSemestre.idSemestre(UPDATED_ID_SEMESTRE).startedAt(UPDATED_STARTED_AT).endedAt(UPDATED_ENDED_AT).name(UPDATED_NAME);

        restSemestreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSemestre.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSemestre))
            )
            .andExpect(status().isOk());

        // Validate the Semestre in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSemestreUpdatableFieldsEquals(partialUpdatedSemestre, getPersistedSemestre(partialUpdatedSemestre));
    }

    @Test
    @Transactional
    void patchNonExistingSemestre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        semestre.setId(longCount.incrementAndGet());

        // Create the Semestre
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, semestreDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(semestreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semestre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSemestre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        semestre.setId(longCount.incrementAndGet());

        // Create the Semestre
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(semestreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semestre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSemestre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        semestre.setId(longCount.incrementAndGet());

        // Create the Semestre
        SemestreDTO semestreDTO = semestreMapper.toDto(semestre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(semestreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Semestre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSemestre() throws Exception {
        // Initialize the database
        insertedSemestre = semestreRepository.saveAndFlush(semestre);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the semestre
        restSemestreMockMvc
            .perform(delete(ENTITY_API_URL_ID, semestre.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return semestreRepository.count();
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

    protected Semestre getPersistedSemestre(Semestre semestre) {
        return semestreRepository.findById(semestre.getId()).orElseThrow();
    }

    protected void assertPersistedSemestreToMatchAllProperties(Semestre expectedSemestre) {
        assertSemestreAllPropertiesEquals(expectedSemestre, getPersistedSemestre(expectedSemestre));
    }

    protected void assertPersistedSemestreToMatchUpdatableProperties(Semestre expectedSemestre) {
        assertSemestreAllUpdatablePropertiesEquals(expectedSemestre, getPersistedSemestre(expectedSemestre));
    }
}
