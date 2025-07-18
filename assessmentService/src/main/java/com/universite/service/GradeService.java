package com.universite.service;

import com.universite.domain.Grade;
import com.universite.repository.GradeRepository;
import com.universite.service.dto.GradeDTO;
import com.universite.service.mapper.GradeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.universite.domain.Grade}.
 */
@Service
@Transactional
public class GradeService {

    private static final Logger LOG = LoggerFactory.getLogger(GradeService.class);

    private final GradeRepository gradeRepository;

    private final GradeMapper gradeMapper;

    public GradeService(GradeRepository gradeRepository, GradeMapper gradeMapper) {
        this.gradeRepository = gradeRepository;
        this.gradeMapper = gradeMapper;
    }

    /**
     * Save a grade.
     *
     * @param gradeDTO the entity to save.
     * @return the persisted entity.
     */
    public GradeDTO save(GradeDTO gradeDTO) {
        LOG.debug("Request to save Grade : {}", gradeDTO);
        Grade grade = gradeMapper.toEntity(gradeDTO);
        grade = gradeRepository.save(grade);
        return gradeMapper.toDto(grade);
    }

    /**
     * Update a grade.
     *
     * @param gradeDTO the entity to save.
     * @return the persisted entity.
     */
    public GradeDTO update(GradeDTO gradeDTO) {
        LOG.debug("Request to update Grade : {}", gradeDTO);
        Grade grade = gradeMapper.toEntity(gradeDTO);
        grade = gradeRepository.save(grade);
        return gradeMapper.toDto(grade);
    }

    /**
     * Partially update a grade.
     *
     * @param gradeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GradeDTO> partialUpdate(GradeDTO gradeDTO) {
        LOG.debug("Request to partially update Grade : {}", gradeDTO);

        return gradeRepository
            .findById(gradeDTO.getId())
            .map(existingGrade -> {
                gradeMapper.partialUpdate(existingGrade, gradeDTO);

                return existingGrade;
            })
            .map(gradeRepository::save)
            .map(gradeMapper::toDto);
    }

    /**
     * Get all the grades.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GradeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Grades");
        return gradeRepository.findAll(pageable).map(gradeMapper::toDto);
    }

    /**
     * Get one grade by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GradeDTO> findOne(Long id) {
        LOG.debug("Request to get Grade : {}", id);
        return gradeRepository.findById(id).map(gradeMapper::toDto);
    }

    /**
     * Delete the grade by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Grade : {}", id);
        gradeRepository.deleteById(id);
    }
}
