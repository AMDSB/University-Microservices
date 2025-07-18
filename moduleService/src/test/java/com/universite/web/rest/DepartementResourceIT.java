package com.universite.web.rest;

import static com.universite.domain.DepartementAsserts.*;
import static com.universite.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universite.IntegrationTest;
import com.universite.domain.Departement;
import com.universite.repository.DepartementRepository;
import com.universite.service.dto.DepartementDTO;
import com.universite.service.mapper.DepartementMapper;
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
 * Integration tests for the {@link DepartementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DepartementResourceIT {

    private static final Integer DEFAULT_ID_DEPARTEMENT = 1;
    private static final Integer UPDATED_ID_DEPARTEMENT = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/departements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private DepartementMapper departementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartementMockMvc;

    private Departement departement;

    private Departement insertedDepartement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departement createEntity() {
        return new Departement().idDepartement(DEFAULT_ID_DEPARTEMENT).name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departement createUpdatedEntity() {
        return new Departement().idDepartement(UPDATED_ID_DEPARTEMENT).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        departement = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDepartement != null) {
            departementRepository.delete(insertedDepartement);
            insertedDepartement = null;
        }
    }

    @Test
    @Transactional
    void createDepartement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);
        var returnedDepartementDTO = om.readValue(
            restDepartementMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departementDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DepartementDTO.class
        );

        // Validate the Departement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDepartement = departementMapper.toEntity(returnedDepartementDTO);
        assertDepartementUpdatableFieldsEquals(returnedDepartement, getPersistedDepartement(returnedDepartement));

        insertedDepartement = returnedDepartement;
    }

    @Test
    @Transactional
    void createDepartementWithExistingId() throws Exception {
        // Create the Departement with an existing ID
        departement.setId(1L);
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartementMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdDepartementIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        departement.setIdDepartement(null);

        // Create the Departement, which fails.
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        restDepartementMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepartements() throws Exception {
        // Initialize the database
        insertedDepartement = departementRepository.saveAndFlush(departement);

        // Get all the departementList
        restDepartementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departement.getId().intValue())))
            .andExpect(jsonPath("$.[*].idDepartement").value(hasItem(DEFAULT_ID_DEPARTEMENT)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getDepartement() throws Exception {
        // Initialize the database
        insertedDepartement = departementRepository.saveAndFlush(departement);

        // Get the departement
        restDepartementMockMvc
            .perform(get(ENTITY_API_URL_ID, departement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departement.getId().intValue()))
            .andExpect(jsonPath("$.idDepartement").value(DEFAULT_ID_DEPARTEMENT))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingDepartement() throws Exception {
        // Get the departement
        restDepartementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepartement() throws Exception {
        // Initialize the database
        insertedDepartement = departementRepository.saveAndFlush(departement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departement
        Departement updatedDepartement = departementRepository.findById(departement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDepartement are not directly saved in db
        em.detach(updatedDepartement);
        updatedDepartement.idDepartement(UPDATED_ID_DEPARTEMENT).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        DepartementDTO departementDTO = departementMapper.toDto(updatedDepartement);

        restDepartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departementDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(departementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Departement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDepartementToMatchAllProperties(updatedDepartement);
    }

    @Test
    @Transactional
    void putNonExistingDepartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departement.setId(longCount.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departementDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departement.setId(longCount.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departement.setId(longCount.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepartementWithPatch() throws Exception {
        // Initialize the database
        insertedDepartement = departementRepository.saveAndFlush(departement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departement using partial update
        Departement partialUpdatedDepartement = new Departement();
        partialUpdatedDepartement.setId(departement.getId());

        partialUpdatedDepartement.idDepartement(UPDATED_ID_DEPARTEMENT).name(UPDATED_NAME);

        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepartement))
            )
            .andExpect(status().isOk());

        // Validate the Departement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDepartement, departement),
            getPersistedDepartement(departement)
        );
    }

    @Test
    @Transactional
    void fullUpdateDepartementWithPatch() throws Exception {
        // Initialize the database
        insertedDepartement = departementRepository.saveAndFlush(departement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departement using partial update
        Departement partialUpdatedDepartement = new Departement();
        partialUpdatedDepartement.setId(departement.getId());

        partialUpdatedDepartement.idDepartement(UPDATED_ID_DEPARTEMENT).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepartement))
            )
            .andExpect(status().isOk());

        // Validate the Departement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartementUpdatableFieldsEquals(partialUpdatedDepartement, getPersistedDepartement(partialUpdatedDepartement));
    }

    @Test
    @Transactional
    void patchNonExistingDepartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departement.setId(longCount.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departementDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departement.setId(longCount.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(departementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departement.setId(longCount.incrementAndGet());

        // Create the Departement
        DepartementDTO departementDTO = departementMapper.toDto(departement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(departementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepartement() throws Exception {
        // Initialize the database
        insertedDepartement = departementRepository.saveAndFlush(departement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the departement
        restDepartementMockMvc
            .perform(delete(ENTITY_API_URL_ID, departement.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return departementRepository.count();
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

    protected Departement getPersistedDepartement(Departement departement) {
        return departementRepository.findById(departement.getId()).orElseThrow();
    }

    protected void assertPersistedDepartementToMatchAllProperties(Departement expectedDepartement) {
        assertDepartementAllPropertiesEquals(expectedDepartement, getPersistedDepartement(expectedDepartement));
    }

    protected void assertPersistedDepartementToMatchUpdatableProperties(Departement expectedDepartement) {
        assertDepartementAllUpdatablePropertiesEquals(expectedDepartement, getPersistedDepartement(expectedDepartement));
    }
}
