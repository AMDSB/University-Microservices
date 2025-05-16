package com.universite.assessment.service;

import com.universite.assessment.domain.AcademicRecord;
import com.universite.assessment.repository.AcademicRecordRepository;
import com.universite.assessment.service.dto.AcademicRecordDTO;
import com.universite.assessment.service.mapper.AcademicRecordMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.universite.assessment.domain.AcademicRecord}.
 */
@Service
@Transactional
public class AcademicRecordService {

    private static final Logger LOG = LoggerFactory.getLogger(AcademicRecordService.class);

    private final AcademicRecordRepository academicRecordRepository;

    private final AcademicRecordMapper academicRecordMapper;

    public AcademicRecordService(AcademicRecordRepository academicRecordRepository, AcademicRecordMapper academicRecordMapper) {
        this.academicRecordRepository = academicRecordRepository;
        this.academicRecordMapper = academicRecordMapper;
    }

    /**
     * Save a academicRecord.
     *
     * @param academicRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public AcademicRecordDTO save(AcademicRecordDTO academicRecordDTO) {
        LOG.debug("Request to save AcademicRecord : {}", academicRecordDTO);
        AcademicRecord academicRecord = academicRecordMapper.toEntity(academicRecordDTO);
        academicRecord = academicRecordRepository.save(academicRecord);
        return academicRecordMapper.toDto(academicRecord);
    }

    /**
     * Update a academicRecord.
     *
     * @param academicRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public AcademicRecordDTO update(AcademicRecordDTO academicRecordDTO) {
        LOG.debug("Request to update AcademicRecord : {}", academicRecordDTO);
        AcademicRecord academicRecord = academicRecordMapper.toEntity(academicRecordDTO);
        academicRecord = academicRecordRepository.save(academicRecord);
        return academicRecordMapper.toDto(academicRecord);
    }

    /**
     * Partially update a academicRecord.
     *
     * @param academicRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AcademicRecordDTO> partialUpdate(AcademicRecordDTO academicRecordDTO) {
        LOG.debug("Request to partially update AcademicRecord : {}", academicRecordDTO);

        return academicRecordRepository
            .findById(academicRecordDTO.getId())
            .map(existingAcademicRecord -> {
                academicRecordMapper.partialUpdate(existingAcademicRecord, academicRecordDTO);

                return existingAcademicRecord;
            })
            .map(academicRecordRepository::save)
            .map(academicRecordMapper::toDto);
    }

    /**
     * Get all the academicRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AcademicRecordDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AcademicRecords");
        return academicRecordRepository.findAll(pageable).map(academicRecordMapper::toDto);
    }

    /**
     * Get one academicRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AcademicRecordDTO> findOne(Long id) {
        LOG.debug("Request to get AcademicRecord : {}", id);
        return academicRecordRepository.findById(id).map(academicRecordMapper::toDto);
    }

    /**
     * Delete the academicRecord by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AcademicRecord : {}", id);
        academicRecordRepository.deleteById(id);
    }
}
