package com.universite.web.rest;

import static com.universite.domain.SpecialisationAsserts.*;
import static com.universite.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universite.IntegrationTest;
import com.universite.domain.Specialisation;
import com.universite.repository.SpecialisationRepository;
import com.universite.service.dto.SpecialisationDTO;
import com.universite.service.mapper.SpecialisationMapper;
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
 * Integration tests for the {@link SpecialisationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecialisationResourceIT {

    private static final Integer DEFAULT_ID_SPECIALISATION = 1;
    private static final Integer UPDATED_ID_SPECIALISATION = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/specialisations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SpecialisationRepository specialisationRepository;

    @Autowired
    private SpecialisationMapper specialisationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialisationMockMvc;

    private Specialisation specialisation;

    private Specialisation insertedSpecialisation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialisation createEntity() {
        return new Specialisation().idSpecialisation(DEFAULT_ID_SPECIALISATION).name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialisation createUpdatedEntity() {
        return new Specialisation().idSpecialisation(UPDATED_ID_SPECIALISATION).name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        specialisation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSpecialisation != null) {
            specialisationRepository.delete(insertedSpecialisation);
            insertedSpecialisation = null;
        }
    }

    @Test
    @Transactional
    void createSpecialisation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Specialisation
        SpecialisationDTO specialisationDTO = specialisationMapper.toDto(specialisation);
        var returnedSpecialisationDTO = om.readValue(
            restSpecialisationMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(specialisationDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SpecialisationDTO.class
        );

        // Validate the Specialisation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSpecialisation = specialisationMapper.toEntity(returnedSpecialisationDTO);
        assertSpecialisationUpdatableFieldsEquals(returnedSpecialisation, getPersistedSpecialisation(returnedSpecialisation));

        insertedSpecialisation = returnedSpecialisation;
    }

    @Test
    @Transactional
    void createSpecialisationWithExistingId() throws Exception {
        // Create the Specialisation with an existing ID
        specialisation.setId(1L);
        SpecialisationDTO specialisationDTO = specialisationMapper.toDto(specialisation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialisationMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialisation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdSpecialisationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        specialisation.setIdSpecialisation(null);

        // Create the Specialisation, which fails.
        SpecialisationDTO specialisationDTO = specialisationMapper.toDto(specialisation);

        restSpecialisationMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialisationDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecialisations() throws Exception {
        // Initialize the database
        insertedSpecialisation = specialisationRepository.saveAndFlush(specialisation);

        // Get all the specialisationList
        restSpecialisationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialisation.getId().intValue())))
            .andExpect(jsonPath("$.[*].idSpecialisation").value(hasItem(DEFAULT_ID_SPECIALISATION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSpecialisation() throws Exception {
        // Initialize the database
        insertedSpecialisation = specialisationRepository.saveAndFlush(specialisation);

        // Get the specialisation
        restSpecialisationMockMvc
            .perform(get(ENTITY_API_URL_ID, specialisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialisation.getId().intValue()))
            .andExpect(jsonPath("$.idSpecialisation").value(DEFAULT_ID_SPECIALISATION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSpecialisation() throws Exception {
        // Get the specialisation
        restSpecialisationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecialisation() throws Exception {
        // Initialize the database
        insertedSpecialisation = specialisationRepository.saveAndFlush(specialisation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialisation
        Specialisation updatedSpecialisation = specialisationRepository.findById(specialisation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpecialisation are not directly saved in db
        em.detach(updatedSpecialisation);
        updatedSpecialisation.idSpecialisation(UPDATED_ID_SPECIALISATION).name(UPDATED_NAME);
        SpecialisationDTO specialisationDTO = specialisationMapper.toDto(updatedSpecialisation);

        restSpecialisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialisationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialisationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Specialisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSpecialisationToMatchAllProperties(updatedSpecialisation);
    }

    @Test
    @Transactional
    void putNonExistingSpecialisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialisation.setId(longCount.incrementAndGet());

        // Create the Specialisation
        SpecialisationDTO specialisationDTO = specialisationMapper.toDto(specialisation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialisationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialisation.setId(longCount.incrementAndGet());

        // Create the Specialisation
        SpecialisationDTO specialisationDTO = specialisationMapper.toDto(specialisation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialisation.setId(longCount.incrementAndGet());

        // Create the Specialisation
        SpecialisationDTO specialisationDTO = specialisationMapper.toDto(specialisation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialisationMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialisationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecialisationWithPatch() throws Exception {
        // Initialize the database
        insertedSpecialisation = specialisationRepository.saveAndFlush(specialisation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialisation using partial update
        Specialisation partialUpdatedSpecialisation = new Specialisation();
        partialUpdatedSpecialisation.setId(specialisation.getId());

        partialUpdatedSpecialisation.idSpecialisation(UPDATED_ID_SPECIALISATION);

        restSpecialisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialisation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpecialisation))
            )
            .andExpect(status().isOk());

        // Validate the Specialisation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpecialisationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSpecialisation, specialisation),
            getPersistedSpecialisation(specialisation)
        );
    }

    @Test
    @Transactional
    void fullUpdateSpecialisationWithPatch() throws Exception {
        // Initialize the database
        insertedSpecialisation = specialisationRepository.saveAndFlush(specialisation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialisation using partial update
        Specialisation partialUpdatedSpecialisation = new Specialisation();
        partialUpdatedSpecialisation.setId(specialisation.getId());

        partialUpdatedSpecialisation.idSpecialisation(UPDATED_ID_SPECIALISATION).name(UPDATED_NAME);

        restSpecialisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialisation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpecialisation))
            )
            .andExpect(status().isOk());

        // Validate the Specialisation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpecialisationUpdatableFieldsEquals(partialUpdatedSpecialisation, getPersistedSpecialisation(partialUpdatedSpecialisation));
    }

    @Test
    @Transactional
    void patchNonExistingSpecialisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialisation.setId(longCount.incrementAndGet());

        // Create the Specialisation
        SpecialisationDTO specialisationDTO = specialisationMapper.toDto(specialisation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specialisationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(specialisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialisation.setId(longCount.incrementAndGet());

        // Create the Specialisation
        SpecialisationDTO specialisationDTO = specialisationMapper.toDto(specialisation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(specialisationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialisation.setId(longCount.incrementAndGet());

        // Create the Specialisation
        SpecialisationDTO specialisationDTO = specialisationMapper.toDto(specialisation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialisationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(specialisationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecialisation() throws Exception {
        // Initialize the database
        insertedSpecialisation = specialisationRepository.saveAndFlush(specialisation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the specialisation
        restSpecialisationMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialisation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return specialisationRepository.count();
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

    protected Specialisation getPersistedSpecialisation(Specialisation specialisation) {
        return specialisationRepository.findById(specialisation.getId()).orElseThrow();
    }

    protected void assertPersistedSpecialisationToMatchAllProperties(Specialisation expectedSpecialisation) {
        assertSpecialisationAllPropertiesEquals(expectedSpecialisation, getPersistedSpecialisation(expectedSpecialisation));
    }

    protected void assertPersistedSpecialisationToMatchUpdatableProperties(Specialisation expectedSpecialisation) {
        assertSpecialisationAllUpdatablePropertiesEquals(expectedSpecialisation, getPersistedSpecialisation(expectedSpecialisation));
    }
}
