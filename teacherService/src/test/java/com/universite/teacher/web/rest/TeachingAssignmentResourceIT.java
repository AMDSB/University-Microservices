package com.universite.teacher.web.rest;

import static com.universite.teacher.domain.TeachingAssignmentAsserts.*;
import static com.universite.teacher.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universite.teacher.IntegrationTest;
import com.universite.teacher.domain.TeachingAssignment;
import com.universite.teacher.repository.TeachingAssignmentRepository;
import com.universite.teacher.service.dto.TeachingAssignmentDTO;
import com.universite.teacher.service.mapper.TeachingAssignmentMapper;
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
 * Integration tests for the {@link TeachingAssignmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TeachingAssignmentResourceIT {

    private static final Integer DEFAULT_ID_TEACHING_ASSIGNMENT = 1;
    private static final Integer UPDATED_ID_TEACHING_ASSIGNMENT = 2;

    private static final Integer DEFAULT_ACADEMIC_YEAR = 1;
    private static final Integer UPDATED_ACADEMIC_YEAR = 2;

    private static final String ENTITY_API_URL = "/api/teaching-assignments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TeachingAssignmentRepository teachingAssignmentRepository;

    @Autowired
    private TeachingAssignmentMapper teachingAssignmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeachingAssignmentMockMvc;

    private TeachingAssignment teachingAssignment;

    private TeachingAssignment insertedTeachingAssignment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeachingAssignment createEntity() {
        return new TeachingAssignment().idTeachingAssignment(DEFAULT_ID_TEACHING_ASSIGNMENT).academicYear(DEFAULT_ACADEMIC_YEAR);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeachingAssignment createUpdatedEntity() {
        return new TeachingAssignment().idTeachingAssignment(UPDATED_ID_TEACHING_ASSIGNMENT).academicYear(UPDATED_ACADEMIC_YEAR);
    }

    @BeforeEach
    public void initTest() {
        teachingAssignment = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTeachingAssignment != null) {
            teachingAssignmentRepository.delete(insertedTeachingAssignment);
            insertedTeachingAssignment = null;
        }
    }

    @Test
    @Transactional
    void createTeachingAssignment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TeachingAssignment
        TeachingAssignmentDTO teachingAssignmentDTO = teachingAssignmentMapper.toDto(teachingAssignment);
        var returnedTeachingAssignmentDTO = om.readValue(
            restTeachingAssignmentMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(teachingAssignmentDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TeachingAssignmentDTO.class
        );

        // Validate the TeachingAssignment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTeachingAssignment = teachingAssignmentMapper.toEntity(returnedTeachingAssignmentDTO);
        assertTeachingAssignmentUpdatableFieldsEquals(
            returnedTeachingAssignment,
            getPersistedTeachingAssignment(returnedTeachingAssignment)
        );

        insertedTeachingAssignment = returnedTeachingAssignment;
    }

    @Test
    @Transactional
    void createTeachingAssignmentWithExistingId() throws Exception {
        // Create the TeachingAssignment with an existing ID
        teachingAssignment.setId(1L);
        TeachingAssignmentDTO teachingAssignmentDTO = teachingAssignmentMapper.toDto(teachingAssignment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeachingAssignmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(teachingAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeachingAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdTeachingAssignmentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        teachingAssignment.setIdTeachingAssignment(null);

        // Create the TeachingAssignment, which fails.
        TeachingAssignmentDTO teachingAssignmentDTO = teachingAssignmentMapper.toDto(teachingAssignment);

        restTeachingAssignmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(teachingAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTeachingAssignments() throws Exception {
        // Initialize the database
        insertedTeachingAssignment = teachingAssignmentRepository.saveAndFlush(teachingAssignment);

        // Get all the teachingAssignmentList
        restTeachingAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teachingAssignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].idTeachingAssignment").value(hasItem(DEFAULT_ID_TEACHING_ASSIGNMENT)))
            .andExpect(jsonPath("$.[*].academicYear").value(hasItem(DEFAULT_ACADEMIC_YEAR)));
    }

    @Test
    @Transactional
    void getTeachingAssignment() throws Exception {
        // Initialize the database
        insertedTeachingAssignment = teachingAssignmentRepository.saveAndFlush(teachingAssignment);

        // Get the teachingAssignment
        restTeachingAssignmentMockMvc
            .perform(get(ENTITY_API_URL_ID, teachingAssignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(teachingAssignment.getId().intValue()))
            .andExpect(jsonPath("$.idTeachingAssignment").value(DEFAULT_ID_TEACHING_ASSIGNMENT))
            .andExpect(jsonPath("$.academicYear").value(DEFAULT_ACADEMIC_YEAR));
    }

    @Test
    @Transactional
    void getNonExistingTeachingAssignment() throws Exception {
        // Get the teachingAssignment
        restTeachingAssignmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTeachingAssignment() throws Exception {
        // Initialize the database
        insertedTeachingAssignment = teachingAssignmentRepository.saveAndFlush(teachingAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teachingAssignment
        TeachingAssignment updatedTeachingAssignment = teachingAssignmentRepository.findById(teachingAssignment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTeachingAssignment are not directly saved in db
        em.detach(updatedTeachingAssignment);
        updatedTeachingAssignment.idTeachingAssignment(UPDATED_ID_TEACHING_ASSIGNMENT).academicYear(UPDATED_ACADEMIC_YEAR);
        TeachingAssignmentDTO teachingAssignmentDTO = teachingAssignmentMapper.toDto(updatedTeachingAssignment);

        restTeachingAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teachingAssignmentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(teachingAssignmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the TeachingAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTeachingAssignmentToMatchAllProperties(updatedTeachingAssignment);
    }

    @Test
    @Transactional
    void putNonExistingTeachingAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachingAssignment.setId(longCount.incrementAndGet());

        // Create the TeachingAssignment
        TeachingAssignmentDTO teachingAssignmentDTO = teachingAssignmentMapper.toDto(teachingAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeachingAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teachingAssignmentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(teachingAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeachingAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTeachingAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachingAssignment.setId(longCount.incrementAndGet());

        // Create the TeachingAssignment
        TeachingAssignmentDTO teachingAssignmentDTO = teachingAssignmentMapper.toDto(teachingAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeachingAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(teachingAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeachingAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTeachingAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachingAssignment.setId(longCount.incrementAndGet());

        // Create the TeachingAssignment
        TeachingAssignmentDTO teachingAssignmentDTO = teachingAssignmentMapper.toDto(teachingAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeachingAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(teachingAssignmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeachingAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTeachingAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedTeachingAssignment = teachingAssignmentRepository.saveAndFlush(teachingAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teachingAssignment using partial update
        TeachingAssignment partialUpdatedTeachingAssignment = new TeachingAssignment();
        partialUpdatedTeachingAssignment.setId(teachingAssignment.getId());

        partialUpdatedTeachingAssignment.academicYear(UPDATED_ACADEMIC_YEAR);

        restTeachingAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeachingAssignment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTeachingAssignment))
            )
            .andExpect(status().isOk());

        // Validate the TeachingAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTeachingAssignmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTeachingAssignment, teachingAssignment),
            getPersistedTeachingAssignment(teachingAssignment)
        );
    }

    @Test
    @Transactional
    void fullUpdateTeachingAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedTeachingAssignment = teachingAssignmentRepository.saveAndFlush(teachingAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teachingAssignment using partial update
        TeachingAssignment partialUpdatedTeachingAssignment = new TeachingAssignment();
        partialUpdatedTeachingAssignment.setId(teachingAssignment.getId());

        partialUpdatedTeachingAssignment.idTeachingAssignment(UPDATED_ID_TEACHING_ASSIGNMENT).academicYear(UPDATED_ACADEMIC_YEAR);

        restTeachingAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeachingAssignment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTeachingAssignment))
            )
            .andExpect(status().isOk());

        // Validate the TeachingAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTeachingAssignmentUpdatableFieldsEquals(
            partialUpdatedTeachingAssignment,
            getPersistedTeachingAssignment(partialUpdatedTeachingAssignment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTeachingAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachingAssignment.setId(longCount.incrementAndGet());

        // Create the TeachingAssignment
        TeachingAssignmentDTO teachingAssignmentDTO = teachingAssignmentMapper.toDto(teachingAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeachingAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, teachingAssignmentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(teachingAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeachingAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTeachingAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachingAssignment.setId(longCount.incrementAndGet());

        // Create the TeachingAssignment
        TeachingAssignmentDTO teachingAssignmentDTO = teachingAssignmentMapper.toDto(teachingAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeachingAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(teachingAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeachingAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTeachingAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachingAssignment.setId(longCount.incrementAndGet());

        // Create the TeachingAssignment
        TeachingAssignmentDTO teachingAssignmentDTO = teachingAssignmentMapper.toDto(teachingAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeachingAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(teachingAssignmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeachingAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTeachingAssignment() throws Exception {
        // Initialize the database
        insertedTeachingAssignment = teachingAssignmentRepository.saveAndFlush(teachingAssignment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the teachingAssignment
        restTeachingAssignmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, teachingAssignment.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return teachingAssignmentRepository.count();
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

    protected TeachingAssignment getPersistedTeachingAssignment(TeachingAssignment teachingAssignment) {
        return teachingAssignmentRepository.findById(teachingAssignment.getId()).orElseThrow();
    }

    protected void assertPersistedTeachingAssignmentToMatchAllProperties(TeachingAssignment expectedTeachingAssignment) {
        assertTeachingAssignmentAllPropertiesEquals(expectedTeachingAssignment, getPersistedTeachingAssignment(expectedTeachingAssignment));
    }

    protected void assertPersistedTeachingAssignmentToMatchUpdatableProperties(TeachingAssignment expectedTeachingAssignment) {
        assertTeachingAssignmentAllUpdatablePropertiesEquals(
            expectedTeachingAssignment,
            getPersistedTeachingAssignment(expectedTeachingAssignment)
        );
    }
}
