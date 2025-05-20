package com.universite.service;

import com.universite.domain.ReportDetail;
import com.universite.repository.ReportDetailRepository;
import com.universite.service.dto.ReportDetailDTO;
import com.universite.service.mapper.ReportDetailMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.universite.domain.ReportDetail}.
 */
@Service
@Transactional
public class ReportDetailService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportDetailService.class);

    private final ReportDetailRepository reportDetailRepository;

    private final ReportDetailMapper reportDetailMapper;

    public ReportDetailService(ReportDetailRepository reportDetailRepository, ReportDetailMapper reportDetailMapper) {
        this.reportDetailRepository = reportDetailRepository;
        this.reportDetailMapper = reportDetailMapper;
    }

    /**
     * Save a reportDetail.
     *
     * @param reportDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportDetailDTO save(ReportDetailDTO reportDetailDTO) {
        LOG.debug("Request to save ReportDetail : {}", reportDetailDTO);
        ReportDetail reportDetail = reportDetailMapper.toEntity(reportDetailDTO);
        reportDetail = reportDetailRepository.save(reportDetail);
        return reportDetailMapper.toDto(reportDetail);
    }

    /**
     * Update a reportDetail.
     *
     * @param reportDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportDetailDTO update(ReportDetailDTO reportDetailDTO) {
        LOG.debug("Request to update ReportDetail : {}", reportDetailDTO);
        ReportDetail reportDetail = reportDetailMapper.toEntity(reportDetailDTO);
        reportDetail = reportDetailRepository.save(reportDetail);
        return reportDetailMapper.toDto(reportDetail);
    }

    /**
     * Partially update a reportDetail.
     *
     * @param reportDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportDetailDTO> partialUpdate(ReportDetailDTO reportDetailDTO) {
        LOG.debug("Request to partially update ReportDetail : {}", reportDetailDTO);

        return reportDetailRepository
            .findById(reportDetailDTO.getId())
            .map(existingReportDetail -> {
                reportDetailMapper.partialUpdate(existingReportDetail, reportDetailDTO);

                return existingReportDetail;
            })
            .map(reportDetailRepository::save)
            .map(reportDetailMapper::toDto);
    }

    /**
     * Get all the reportDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportDetailDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ReportDetails");
        return reportDetailRepository.findAll(pageable).map(reportDetailMapper::toDto);
    }

    /**
     * Get one reportDetail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportDetailDTO> findOne(Long id) {
        LOG.debug("Request to get ReportDetail : {}", id);
        return reportDetailRepository.findById(id).map(reportDetailMapper::toDto);
    }

    /**
     * Delete the reportDetail by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReportDetail : {}", id);
        reportDetailRepository.deleteById(id);
    }
}
