package com.universite.teacher.web.rest;

import static com.universite.teacher.domain.DisponibiliteAsserts.*;
import static com.universite.teacher.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universite.teacher.IntegrationTest;
import com.universite.teacher.domain.Disponibilite;
import com.universite.teacher.repository.DisponibiliteRepository;
import com.universite.teacher.service.dto.DisponibiliteDTO;
import com.universite.teacher.service.mapper.DisponibiliteMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link DisponibiliteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DisponibiliteResourceIT {

    private static final Integer DEFAULT_ID_DISPONIBILITE = 1;
    private static final Integer UPDATED_ID_DISPONIBILITE = 2;

    private static final Instant DEFAULT_DEBUT_DISPONIBILITE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DEBUT_DISPONIBILITE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FIN_DISPONIBILITE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FIN_DISPONIBILITE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/disponibilites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DisponibiliteRepository disponibiliteRepository;

    @Autowired
    private DisponibiliteMapper disponibiliteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDisponibiliteMockMvc;

    private Disponibilite disponibilite;

    private Disponibilite insertedDisponibilite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disponibilite createEntity() {
        return new Disponibilite()
            .idDisponibilite(DEFAULT_ID_DISPONIBILITE)
            .debutDisponibilite(DEFAULT_DEBUT_DISPONIBILITE)
            .finDisponibilite(DEFAULT_FIN_DISPONIBILITE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disponibilite createUpdatedEntity() {
        return new Disponibilite()
            .idDisponibilite(UPDATED_ID_DISPONIBILITE)
            .debutDisponibilite(UPDATED_DEBUT_DISPONIBILITE)
            .finDisponibilite(UPDATED_FIN_DISPONIBILITE);
    }

    @BeforeEach
    public void initTest() {
        disponibilite = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDisponibilite != null) {
            disponibiliteRepository.delete(insertedDisponibilite);
            insertedDisponibilite = null;
        }
    }

    @Test
    @Transactional
    void createDisponibilite() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Disponibilite
        DisponibiliteDTO disponibiliteDTO = disponibiliteMapper.toDto(disponibilite);
        var returnedDisponibiliteDTO = om.readValue(
            restDisponibiliteMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(disponibiliteDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DisponibiliteDTO.class
        );

        // Validate the Disponibilite in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDisponibilite = disponibiliteMapper.toEntity(returnedDisponibiliteDTO);
        assertDisponibiliteUpdatableFieldsEquals(returnedDisponibilite, getPersistedDisponibilite(returnedDisponibilite));

        insertedDisponibilite = returnedDisponibilite;
    }

    @Test
    @Transactional
    void createDisponibiliteWithExistingId() throws Exception {
        // Create the Disponibilite with an existing ID
        disponibilite.setId(1L);
        DisponibiliteDTO disponibiliteDTO = disponibiliteMapper.toDto(disponibilite);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisponibiliteMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disponibiliteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilite in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdDisponibiliteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        disponibilite.setIdDisponibilite(null);

        // Create the Disponibilite, which fails.
        DisponibiliteDTO disponibiliteDTO = disponibiliteMapper.toDto(disponibilite);

        restDisponibiliteMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disponibiliteDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDisponibilites() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList
        restDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disponibilite.getId().intValue())))
            .andExpect(jsonPath("$.[*].idDisponibilite").value(hasItem(DEFAULT_ID_DISPONIBILITE)))
            .andExpect(jsonPath("$.[*].debutDisponibilite").value(hasItem(DEFAULT_DEBUT_DISPONIBILITE.toString())))
            .andExpect(jsonPath("$.[*].finDisponibilite").value(hasItem(DEFAULT_FIN_DISPONIBILITE.toString())));
    }

    @Test
    @Transactional
    void getDisponibilite() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        // Get the disponibilite
        restDisponibiliteMockMvc
            .perform(get(ENTITY_API_URL_ID, disponibilite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(disponibilite.getId().intValue()))
            .andExpect(jsonPath("$.idDisponibilite").value(DEFAULT_ID_DISPONIBILITE))
            .andExpect(jsonPath("$.debutDisponibilite").value(DEFAULT_DEBUT_DISPONIBILITE.toString()))
            .andExpect(jsonPath("$.finDisponibilite").value(DEFAULT_FIN_DISPONIBILITE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDisponibilite() throws Exception {
        // Get the disponibilite
        restDisponibiliteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDisponibilite() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disponibilite
        Disponibilite updatedDisponibilite = disponibiliteRepository.findById(disponibilite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDisponibilite are not directly saved in db
        em.detach(updatedDisponibilite);
        updatedDisponibilite
            .idDisponibilite(UPDATED_ID_DISPONIBILITE)
            .debutDisponibilite(UPDATED_DEBUT_DISPONIBILITE)
            .finDisponibilite(UPDATED_FIN_DISPONIBILITE);
        DisponibiliteDTO disponibiliteDTO = disponibiliteMapper.toDto(updatedDisponibilite);

        restDisponibiliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disponibiliteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disponibiliteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Disponibilite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDisponibiliteToMatchAllProperties(updatedDisponibilite);
    }

    @Test
    @Transactional
    void putNonExistingDisponibilite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilite.setId(longCount.incrementAndGet());

        // Create the Disponibilite
        DisponibiliteDTO disponibiliteDTO = disponibiliteMapper.toDto(disponibilite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disponibiliteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disponibiliteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDisponibilite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilite.setId(longCount.incrementAndGet());

        // Create the Disponibilite
        DisponibiliteDTO disponibiliteDTO = disponibiliteMapper.toDto(disponibilite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(disponibiliteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDisponibilite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilite.setId(longCount.incrementAndGet());

        // Create the Disponibilite
        DisponibiliteDTO disponibiliteDTO = disponibiliteMapper.toDto(disponibilite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(disponibiliteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disponibilite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDisponibiliteWithPatch() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disponibilite using partial update
        Disponibilite partialUpdatedDisponibilite = new Disponibilite();
        partialUpdatedDisponibilite.setId(disponibilite.getId());

        partialUpdatedDisponibilite.idDisponibilite(UPDATED_ID_DISPONIBILITE).finDisponibilite(UPDATED_FIN_DISPONIBILITE);

        restDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisponibilite.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDisponibilite))
            )
            .andExpect(status().isOk());

        // Validate the Disponibilite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDisponibiliteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDisponibilite, disponibilite),
            getPersistedDisponibilite(disponibilite)
        );
    }

    @Test
    @Transactional
    void fullUpdateDisponibiliteWithPatch() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the disponibilite using partial update
        Disponibilite partialUpdatedDisponibilite = new Disponibilite();
        partialUpdatedDisponibilite.setId(disponibilite.getId());

        partialUpdatedDisponibilite
            .idDisponibilite(UPDATED_ID_DISPONIBILITE)
            .debutDisponibilite(UPDATED_DEBUT_DISPONIBILITE)
            .finDisponibilite(UPDATED_FIN_DISPONIBILITE);

        restDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisponibilite.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDisponibilite))
            )
            .andExpect(status().isOk());

        // Validate the Disponibilite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDisponibiliteUpdatableFieldsEquals(partialUpdatedDisponibilite, getPersistedDisponibilite(partialUpdatedDisponibilite));
    }

    @Test
    @Transactional
    void patchNonExistingDisponibilite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilite.setId(longCount.incrementAndGet());

        // Create the Disponibilite
        DisponibiliteDTO disponibiliteDTO = disponibiliteMapper.toDto(disponibilite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, disponibiliteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(disponibiliteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDisponibilite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilite.setId(longCount.incrementAndGet());

        // Create the Disponibilite
        DisponibiliteDTO disponibiliteDTO = disponibiliteMapper.toDto(disponibilite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(disponibiliteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disponibilite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDisponibilite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        disponibilite.setId(longCount.incrementAndGet());

        // Create the Disponibilite
        DisponibiliteDTO disponibiliteDTO = disponibiliteMapper.toDto(disponibilite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisponibiliteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(disponibiliteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disponibilite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDisponibilite() throws Exception {
        // Initialize the database
        insertedDisponibilite = disponibiliteRepository.saveAndFlush(disponibilite);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the disponibilite
        restDisponibiliteMockMvc
            .perform(delete(ENTITY_API_URL_ID, disponibilite.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return disponibiliteRepository.count();
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

    protected Disponibilite getPersistedDisponibilite(Disponibilite disponibilite) {
        return disponibiliteRepository.findById(disponibilite.getId()).orElseThrow();
    }

    protected void assertPersistedDisponibiliteToMatchAllProperties(Disponibilite expectedDisponibilite) {
        assertDisponibiliteAllPropertiesEquals(expectedDisponibilite, getPersistedDisponibilite(expectedDisponibilite));
    }

    protected void assertPersistedDisponibiliteToMatchUpdatableProperties(Disponibilite expectedDisponibilite) {
        assertDisponibiliteAllUpdatablePropertiesEquals(expectedDisponibilite, getPersistedDisponibilite(expectedDisponibilite));
    }
}
