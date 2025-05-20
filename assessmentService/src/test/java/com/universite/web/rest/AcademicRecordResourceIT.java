package com.universite.web.rest;

import static com.universite.domain.AcademicRecordAsserts.*;
import static com.universite.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universite.IntegrationTest;
import com.universite.domain.AcademicRecord;
import com.universite.repository.AcademicRecordRepository;
import com.universite.service.dto.AcademicRecordDTO;
import com.universite.service.mapper.AcademicRecordMapper;
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
 * Integration tests for the {@link AcademicRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AcademicRecordResourceIT {

    private static final Integer DEFAULT_ID_ACADEMIC_RECORD = 1;
    private static final Integer UPDATED_ID_ACADEMIC_RECORD = 2;

    private static final Float DEFAULT_MOYENNE = 1F;
    private static final Float UPDATED_MOYENNE = 2F;

    private static final String DEFAULT_MENTION = "AAAAAAAAAA";
    private static final String UPDATED_MENTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/academic-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AcademicRecordRepository academicRecordRepository;

    @Autowired
    private AcademicRecordMapper academicRecordMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAcademicRecordMockMvc;

    private AcademicRecord academicRecord;

    private AcademicRecord insertedAcademicRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcademicRecord createEntity() {
        return new AcademicRecord().idAcademicRecord(DEFAULT_ID_ACADEMIC_RECORD).moyenne(DEFAULT_MOYENNE).mention(DEFAULT_MENTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcademicRecord createUpdatedEntity() {
        return new AcademicRecord().idAcademicRecord(UPDATED_ID_ACADEMIC_RECORD).moyenne(UPDATED_MOYENNE).mention(UPDATED_MENTION);
    }

    @BeforeEach
    public void initTest() {
        academicRecord = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAcademicRecord != null) {
            academicRecordRepository.delete(insertedAcademicRecord);
            insertedAcademicRecord = null;
        }
    }

    @Test
    @Transactional
    void createAcademicRecord() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AcademicRecord
        AcademicRecordDTO academicRecordDTO = academicRecordMapper.toDto(academicRecord);
        var returnedAcademicRecordDTO = om.readValue(
            restAcademicRecordMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(academicRecordDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AcademicRecordDTO.class
        );

        // Validate the AcademicRecord in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAcademicRecord = academicRecordMapper.toEntity(returnedAcademicRecordDTO);
        assertAcademicRecordUpdatableFieldsEquals(returnedAcademicRecord, getPersistedAcademicRecord(returnedAcademicRecord));

        insertedAcademicRecord = returnedAcademicRecord;
    }

    @Test
    @Transactional
    void createAcademicRecordWithExistingId() throws Exception {
        // Create the AcademicRecord with an existing ID
        academicRecord.setId(1L);
        AcademicRecordDTO academicRecordDTO = academicRecordMapper.toDto(academicRecord);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcademicRecordMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(academicRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademicRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdAcademicRecordIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        academicRecord.setIdAcademicRecord(null);

        // Create the AcademicRecord, which fails.
        AcademicRecordDTO academicRecordDTO = academicRecordMapper.toDto(academicRecord);

        restAcademicRecordMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(academicRecordDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAcademicRecords() throws Exception {
        // Initialize the database
        insertedAcademicRecord = academicRecordRepository.saveAndFlush(academicRecord);

        // Get all the academicRecordList
        restAcademicRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(academicRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].idAcademicRecord").value(hasItem(DEFAULT_ID_ACADEMIC_RECORD)))
            .andExpect(jsonPath("$.[*].moyenne").value(hasItem(DEFAULT_MOYENNE.doubleValue())))
            .andExpect(jsonPath("$.[*].mention").value(hasItem(DEFAULT_MENTION)));
    }

    @Test
    @Transactional
    void getAcademicRecord() throws Exception {
        // Initialize the database
        insertedAcademicRecord = academicRecordRepository.saveAndFlush(academicRecord);

        // Get the academicRecord
        restAcademicRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, academicRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(academicRecord.getId().intValue()))
            .andExpect(jsonPath("$.idAcademicRecord").value(DEFAULT_ID_ACADEMIC_RECORD))
            .andExpect(jsonPath("$.moyenne").value(DEFAULT_MOYENNE.doubleValue()))
            .andExpect(jsonPath("$.mention").value(DEFAULT_MENTION));
    }

    @Test
    @Transactional
    void getNonExistingAcademicRecord() throws Exception {
        // Get the academicRecord
        restAcademicRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAcademicRecord() throws Exception {
        // Initialize the database
        insertedAcademicRecord = academicRecordRepository.saveAndFlush(academicRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the academicRecord
        AcademicRecord updatedAcademicRecord = academicRecordRepository.findById(academicRecord.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAcademicRecord are not directly saved in db
        em.detach(updatedAcademicRecord);
        updatedAcademicRecord.idAcademicRecord(UPDATED_ID_ACADEMIC_RECORD).moyenne(UPDATED_MOYENNE).mention(UPDATED_MENTION);
        AcademicRecordDTO academicRecordDTO = academicRecordMapper.toDto(updatedAcademicRecord);

        restAcademicRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, academicRecordDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(academicRecordDTO))
            )
            .andExpect(status().isOk());

        // Validate the AcademicRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAcademicRecordToMatchAllProperties(updatedAcademicRecord);
    }

    @Test
    @Transactional
    void putNonExistingAcademicRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        academicRecord.setId(longCount.incrementAndGet());

        // Create the AcademicRecord
        AcademicRecordDTO academicRecordDTO = academicRecordMapper.toDto(academicRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcademicRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, academicRecordDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(academicRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademicRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAcademicRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        academicRecord.setId(longCount.incrementAndGet());

        // Create the AcademicRecord
        AcademicRecordDTO academicRecordDTO = academicRecordMapper.toDto(academicRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademicRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(academicRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademicRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAcademicRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        academicRecord.setId(longCount.incrementAndGet());

        // Create the AcademicRecord
        AcademicRecordDTO academicRecordDTO = academicRecordMapper.toDto(academicRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademicRecordMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(academicRecordDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AcademicRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAcademicRecordWithPatch() throws Exception {
        // Initialize the database
        insertedAcademicRecord = academicRecordRepository.saveAndFlush(academicRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the academicRecord using partial update
        AcademicRecord partialUpdatedAcademicRecord = new AcademicRecord();
        partialUpdatedAcademicRecord.setId(academicRecord.getId());

        partialUpdatedAcademicRecord.idAcademicRecord(UPDATED_ID_ACADEMIC_RECORD).moyenne(UPDATED_MOYENNE).mention(UPDATED_MENTION);

        restAcademicRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcademicRecord.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAcademicRecord))
            )
            .andExpect(status().isOk());

        // Validate the AcademicRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAcademicRecordUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAcademicRecord, academicRecord),
            getPersistedAcademicRecord(academicRecord)
        );
    }

    @Test
    @Transactional
    void fullUpdateAcademicRecordWithPatch() throws Exception {
        // Initialize the database
        insertedAcademicRecord = academicRecordRepository.saveAndFlush(academicRecord);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the academicRecord using partial update
        AcademicRecord partialUpdatedAcademicRecord = new AcademicRecord();
        partialUpdatedAcademicRecord.setId(academicRecord.getId());

        partialUpdatedAcademicRecord.idAcademicRecord(UPDATED_ID_ACADEMIC_RECORD).moyenne(UPDATED_MOYENNE).mention(UPDATED_MENTION);

        restAcademicRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcademicRecord.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAcademicRecord))
            )
            .andExpect(status().isOk());

        // Validate the AcademicRecord in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAcademicRecordUpdatableFieldsEquals(partialUpdatedAcademicRecord, getPersistedAcademicRecord(partialUpdatedAcademicRecord));
    }

    @Test
    @Transactional
    void patchNonExistingAcademicRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        academicRecord.setId(longCount.incrementAndGet());

        // Create the AcademicRecord
        AcademicRecordDTO academicRecordDTO = academicRecordMapper.toDto(academicRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcademicRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, academicRecordDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(academicRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademicRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAcademicRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        academicRecord.setId(longCount.incrementAndGet());

        // Create the AcademicRecord
        AcademicRecordDTO academicRecordDTO = academicRecordMapper.toDto(academicRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademicRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(academicRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademicRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAcademicRecord() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        academicRecord.setId(longCount.incrementAndGet());

        // Create the AcademicRecord
        AcademicRecordDTO academicRecordDTO = academicRecordMapper.toDto(academicRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademicRecordMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(academicRecordDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AcademicRecord in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAcademicRecord() throws Exception {
        // Initialize the database
        insertedAcademicRecord = academicRecordRepository.saveAndFlush(academicRecord);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the academicRecord
        restAcademicRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, academicRecord.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return academicRecordRepository.count();
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

    protected AcademicRecord getPersistedAcademicRecord(AcademicRecord academicRecord) {
        return academicRecordRepository.findById(academicRecord.getId()).orElseThrow();
    }

    protected void assertPersistedAcademicRecordToMatchAllProperties(AcademicRecord expectedAcademicRecord) {
        assertAcademicRecordAllPropertiesEquals(expectedAcademicRecord, getPersistedAcademicRecord(expectedAcademicRecord));
    }

    protected void assertPersistedAcademicRecordToMatchUpdatableProperties(AcademicRecord expectedAcademicRecord) {
        assertAcademicRecordAllUpdatablePropertiesEquals(expectedAcademicRecord, getPersistedAcademicRecord(expectedAcademicRecord));
    }
}
