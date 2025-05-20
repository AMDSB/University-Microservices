package com.universite.web.rest;

import static com.universite.domain.ReportDetailAsserts.*;
import static com.universite.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universite.IntegrationTest;
import com.universite.domain.ReportDetail;
import com.universite.repository.ReportDetailRepository;
import com.universite.service.dto.ReportDetailDTO;
import com.universite.service.mapper.ReportDetailMapper;
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
 * Integration tests for the {@link ReportDetailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportDetailResourceIT {

    private static final String DEFAULT_MODULE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MODULE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_STUDENTS = 1;
    private static final Integer UPDATED_NUMBER_OF_STUDENTS = 2;

    private static final Integer DEFAULT_NUMBER_PASSED = 1;
    private static final Integer UPDATED_NUMBER_PASSED = 2;

    private static final Integer DEFAULT_NUMBER_FAILED = 1;
    private static final Integer UPDATED_NUMBER_FAILED = 2;

    private static final Float DEFAULT_AVERAGE_GRADE = 1F;
    private static final Float UPDATED_AVERAGE_GRADE = 2F;

    private static final String ENTITY_API_URL = "/api/report-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportDetailRepository reportDetailRepository;

    @Autowired
    private ReportDetailMapper reportDetailMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportDetailMockMvc;

    private ReportDetail reportDetail;

    private ReportDetail insertedReportDetail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportDetail createEntity() {
        return new ReportDetail()
            .moduleName(DEFAULT_MODULE_NAME)
            .numberOfStudents(DEFAULT_NUMBER_OF_STUDENTS)
            .numberPassed(DEFAULT_NUMBER_PASSED)
            .numberFailed(DEFAULT_NUMBER_FAILED)
            .averageGrade(DEFAULT_AVERAGE_GRADE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportDetail createUpdatedEntity() {
        return new ReportDetail()
            .moduleName(UPDATED_MODULE_NAME)
            .numberOfStudents(UPDATED_NUMBER_OF_STUDENTS)
            .numberPassed(UPDATED_NUMBER_PASSED)
            .numberFailed(UPDATED_NUMBER_FAILED)
            .averageGrade(UPDATED_AVERAGE_GRADE);
    }

    @BeforeEach
    public void initTest() {
        reportDetail = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReportDetail != null) {
            reportDetailRepository.delete(insertedReportDetail);
            insertedReportDetail = null;
        }
    }

    @Test
    @Transactional
    void createReportDetail() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportDetail
        ReportDetailDTO reportDetailDTO = reportDetailMapper.toDto(reportDetail);
        var returnedReportDetailDTO = om.readValue(
            restReportDetailMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportDetailDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportDetailDTO.class
        );

        // Validate the ReportDetail in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportDetail = reportDetailMapper.toEntity(returnedReportDetailDTO);
        assertReportDetailUpdatableFieldsEquals(returnedReportDetail, getPersistedReportDetail(returnedReportDetail));

        insertedReportDetail = returnedReportDetail;
    }

    @Test
    @Transactional
    void createReportDetailWithExistingId() throws Exception {
        // Create the ReportDetail with an existing ID
        reportDetail.setId(1L);
        ReportDetailDTO reportDetailDTO = reportDetailMapper.toDto(reportDetail);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportDetailMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReportDetails() throws Exception {
        // Initialize the database
        insertedReportDetail = reportDetailRepository.saveAndFlush(reportDetail);

        // Get all the reportDetailList
        restReportDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].moduleName").value(hasItem(DEFAULT_MODULE_NAME)))
            .andExpect(jsonPath("$.[*].numberOfStudents").value(hasItem(DEFAULT_NUMBER_OF_STUDENTS)))
            .andExpect(jsonPath("$.[*].numberPassed").value(hasItem(DEFAULT_NUMBER_PASSED)))
            .andExpect(jsonPath("$.[*].numberFailed").value(hasItem(DEFAULT_NUMBER_FAILED)))
            .andExpect(jsonPath("$.[*].averageGrade").value(hasItem(DEFAULT_AVERAGE_GRADE.doubleValue())));
    }

    @Test
    @Transactional
    void getReportDetail() throws Exception {
        // Initialize the database
        insertedReportDetail = reportDetailRepository.saveAndFlush(reportDetail);

        // Get the reportDetail
        restReportDetailMockMvc
            .perform(get(ENTITY_API_URL_ID, reportDetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportDetail.getId().intValue()))
            .andExpect(jsonPath("$.moduleName").value(DEFAULT_MODULE_NAME))
            .andExpect(jsonPath("$.numberOfStudents").value(DEFAULT_NUMBER_OF_STUDENTS))
            .andExpect(jsonPath("$.numberPassed").value(DEFAULT_NUMBER_PASSED))
            .andExpect(jsonPath("$.numberFailed").value(DEFAULT_NUMBER_FAILED))
            .andExpect(jsonPath("$.averageGrade").value(DEFAULT_AVERAGE_GRADE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingReportDetail() throws Exception {
        // Get the reportDetail
        restReportDetailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportDetail() throws Exception {
        // Initialize the database
        insertedReportDetail = reportDetailRepository.saveAndFlush(reportDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportDetail
        ReportDetail updatedReportDetail = reportDetailRepository.findById(reportDetail.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportDetail are not directly saved in db
        em.detach(updatedReportDetail);
        updatedReportDetail
            .moduleName(UPDATED_MODULE_NAME)
            .numberOfStudents(UPDATED_NUMBER_OF_STUDENTS)
            .numberPassed(UPDATED_NUMBER_PASSED)
            .numberFailed(UPDATED_NUMBER_FAILED)
            .averageGrade(UPDATED_AVERAGE_GRADE);
        ReportDetailDTO reportDetailDTO = reportDetailMapper.toDto(updatedReportDetail);

        restReportDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportDetailDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportDetailDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportDetailToMatchAllProperties(updatedReportDetail);
    }

    @Test
    @Transactional
    void putNonExistingReportDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDetail.setId(longCount.incrementAndGet());

        // Create the ReportDetail
        ReportDetailDTO reportDetailDTO = reportDetailMapper.toDto(reportDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportDetailDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDetail.setId(longCount.incrementAndGet());

        // Create the ReportDetail
        ReportDetailDTO reportDetailDTO = reportDetailMapper.toDto(reportDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDetail.setId(longCount.incrementAndGet());

        // Create the ReportDetail
        ReportDetailDTO reportDetailDTO = reportDetailMapper.toDto(reportDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportDetailMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportDetailDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportDetailWithPatch() throws Exception {
        // Initialize the database
        insertedReportDetail = reportDetailRepository.saveAndFlush(reportDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportDetail using partial update
        ReportDetail partialUpdatedReportDetail = new ReportDetail();
        partialUpdatedReportDetail.setId(reportDetail.getId());

        partialUpdatedReportDetail
            .numberOfStudents(UPDATED_NUMBER_OF_STUDENTS)
            .numberFailed(UPDATED_NUMBER_FAILED)
            .averageGrade(UPDATED_AVERAGE_GRADE);

        restReportDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportDetail.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportDetail))
            )
            .andExpect(status().isOk());

        // Validate the ReportDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportDetailUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportDetail, reportDetail),
            getPersistedReportDetail(reportDetail)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportDetailWithPatch() throws Exception {
        // Initialize the database
        insertedReportDetail = reportDetailRepository.saveAndFlush(reportDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportDetail using partial update
        ReportDetail partialUpdatedReportDetail = new ReportDetail();
        partialUpdatedReportDetail.setId(reportDetail.getId());

        partialUpdatedReportDetail
            .moduleName(UPDATED_MODULE_NAME)
            .numberOfStudents(UPDATED_NUMBER_OF_STUDENTS)
            .numberPassed(UPDATED_NUMBER_PASSED)
            .numberFailed(UPDATED_NUMBER_FAILED)
            .averageGrade(UPDATED_AVERAGE_GRADE);

        restReportDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportDetail.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportDetail))
            )
            .andExpect(status().isOk());

        // Validate the ReportDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportDetailUpdatableFieldsEquals(partialUpdatedReportDetail, getPersistedReportDetail(partialUpdatedReportDetail));
    }

    @Test
    @Transactional
    void patchNonExistingReportDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDetail.setId(longCount.incrementAndGet());

        // Create the ReportDetail
        ReportDetailDTO reportDetailDTO = reportDetailMapper.toDto(reportDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportDetailDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDetail.setId(longCount.incrementAndGet());

        // Create the ReportDetail
        ReportDetailDTO reportDetailDTO = reportDetailMapper.toDto(reportDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportDetail.setId(longCount.incrementAndGet());

        // Create the ReportDetail
        ReportDetailDTO reportDetailDTO = reportDetailMapper.toDto(reportDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportDetailMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportDetailDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportDetail() throws Exception {
        // Initialize the database
        insertedReportDetail = reportDetailRepository.saveAndFlush(reportDetail);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportDetail
        restReportDetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportDetail.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportDetailRepository.count();
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

    protected ReportDetail getPersistedReportDetail(ReportDetail reportDetail) {
        return reportDetailRepository.findById(reportDetail.getId()).orElseThrow();
    }

    protected void assertPersistedReportDetailToMatchAllProperties(ReportDetail expectedReportDetail) {
        assertReportDetailAllPropertiesEquals(expectedReportDetail, getPersistedReportDetail(expectedReportDetail));
    }

    protected void assertPersistedReportDetailToMatchUpdatableProperties(ReportDetail expectedReportDetail) {
        assertReportDetailAllUpdatablePropertiesEquals(expectedReportDetail, getPersistedReportDetail(expectedReportDetail));
    }
}
