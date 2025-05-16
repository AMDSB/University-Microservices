package com.universite.module.service;

import com.universite.module.domain.Semestre;
import com.universite.module.repository.SemestreRepository;
import com.universite.module.service.dto.SemestreDTO;
import com.universite.module.service.mapper.SemestreMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.universite.module.domain.Semestre}.
 */
@Service
@Transactional
public class SemestreService {

    private static final Logger LOG = LoggerFactory.getLogger(SemestreService.class);

    private final SemestreRepository semestreRepository;

    private final SemestreMapper semestreMapper;

    public SemestreService(SemestreRepository semestreRepository, SemestreMapper semestreMapper) {
        this.semestreRepository = semestreRepository;
        this.semestreMapper = semestreMapper;
    }

    /**
     * Save a semestre.
     *
     * @param semestreDTO the entity to save.
     * @return the persisted entity.
     */
    public SemestreDTO save(SemestreDTO semestreDTO) {
        LOG.debug("Request to save Semestre : {}", semestreDTO);
        Semestre semestre = semestreMapper.toEntity(semestreDTO);
        semestre = semestreRepository.save(semestre);
        return semestreMapper.toDto(semestre);
    }

    /**
     * Update a semestre.
     *
     * @param semestreDTO the entity to save.
     * @return the persisted entity.
     */
    public SemestreDTO update(SemestreDTO semestreDTO) {
        LOG.debug("Request to update Semestre : {}", semestreDTO);
        Semestre semestre = semestreMapper.toEntity(semestreDTO);
        semestre = semestreRepository.save(semestre);
        return semestreMapper.toDto(semestre);
    }

    /**
     * Partially update a semestre.
     *
     * @param semestreDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SemestreDTO> partialUpdate(SemestreDTO semestreDTO) {
        LOG.debug("Request to partially update Semestre : {}", semestreDTO);

        return semestreRepository
            .findById(semestreDTO.getId())
            .map(existingSemestre -> {
                semestreMapper.partialUpdate(existingSemestre, semestreDTO);

                return existingSemestre;
            })
            .map(semestreRepository::save)
            .map(semestreMapper::toDto);
    }

    /**
     * Get all the semestres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SemestreDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Semestres");
        return semestreRepository.findAll(pageable).map(semestreMapper::toDto);
    }

    /**
     * Get one semestre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SemestreDTO> findOne(Long id) {
        LOG.debug("Request to get Semestre : {}", id);
        return semestreRepository.findById(id).map(semestreMapper::toDto);
    }

    /**
     * Delete the semestre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Semestre : {}", id);
        semestreRepository.deleteById(id);
    }
}
