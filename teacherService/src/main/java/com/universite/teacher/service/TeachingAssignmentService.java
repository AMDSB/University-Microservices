package com.universite.teacher.service;

import com.universite.teacher.domain.TeachingAssignment;
import com.universite.teacher.repository.TeachingAssignmentRepository;
import com.universite.teacher.service.dto.TeachingAssignmentDTO;
import com.universite.teacher.service.mapper.TeachingAssignmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.universite.teacher.domain.TeachingAssignment}.
 */
@Service
@Transactional
public class TeachingAssignmentService {

    private static final Logger LOG = LoggerFactory.getLogger(TeachingAssignmentService.class);

    private final TeachingAssignmentRepository teachingAssignmentRepository;

    private final TeachingAssignmentMapper teachingAssignmentMapper;

    public TeachingAssignmentService(
        TeachingAssignmentRepository teachingAssignmentRepository,
        TeachingAssignmentMapper teachingAssignmentMapper
    ) {
        this.teachingAssignmentRepository = teachingAssignmentRepository;
        this.teachingAssignmentMapper = teachingAssignmentMapper;
    }

    /**
     * Save a teachingAssignment.
     *
     * @param teachingAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public TeachingAssignmentDTO save(TeachingAssignmentDTO teachingAssignmentDTO) {
        LOG.debug("Request to save TeachingAssignment : {}", teachingAssignmentDTO);
        TeachingAssignment teachingAssignment = teachingAssignmentMapper.toEntity(teachingAssignmentDTO);
        teachingAssignment = teachingAssignmentRepository.save(teachingAssignment);
        return teachingAssignmentMapper.toDto(teachingAssignment);
    }

    /**
     * Update a teachingAssignment.
     *
     * @param teachingAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public TeachingAssignmentDTO update(TeachingAssignmentDTO teachingAssignmentDTO) {
        LOG.debug("Request to update TeachingAssignment : {}", teachingAssignmentDTO);
        TeachingAssignment teachingAssignment = teachingAssignmentMapper.toEntity(teachingAssignmentDTO);
        teachingAssignment = teachingAssignmentRepository.save(teachingAssignment);
        return teachingAssignmentMapper.toDto(teachingAssignment);
    }

    /**
     * Partially update a teachingAssignment.
     *
     * @param teachingAssignmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TeachingAssignmentDTO> partialUpdate(TeachingAssignmentDTO teachingAssignmentDTO) {
        LOG.debug("Request to partially update TeachingAssignment : {}", teachingAssignmentDTO);

        return teachingAssignmentRepository
            .findById(teachingAssignmentDTO.getId())
            .map(existingTeachingAssignment -> {
                teachingAssignmentMapper.partialUpdate(existingTeachingAssignment, teachingAssignmentDTO);

                return existingTeachingAssignment;
            })
            .map(teachingAssignmentRepository::save)
            .map(teachingAssignmentMapper::toDto);
    }

    /**
     * Get all the teachingAssignments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TeachingAssignmentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TeachingAssignments");
        return teachingAssignmentRepository.findAll(pageable).map(teachingAssignmentMapper::toDto);
    }

    /**
     * Get one teachingAssignment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TeachingAssignmentDTO> findOne(Long id) {
        LOG.debug("Request to get TeachingAssignment : {}", id);
        return teachingAssignmentRepository.findById(id).map(teachingAssignmentMapper::toDto);
    }

    /**
     * Delete the teachingAssignment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TeachingAssignment : {}", id);
        teachingAssignmentRepository.deleteById(id);
    }
}
