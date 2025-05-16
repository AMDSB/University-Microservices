package com.universite.module.service;

import com.universite.module.domain.Specialisation;
import com.universite.module.repository.SpecialisationRepository;
import com.universite.module.service.dto.SpecialisationDTO;
import com.universite.module.service.mapper.SpecialisationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.universite.module.domain.Specialisation}.
 */
@Service
@Transactional
public class SpecialisationService {

    private static final Logger LOG = LoggerFactory.getLogger(SpecialisationService.class);

    private final SpecialisationRepository specialisationRepository;

    private final SpecialisationMapper specialisationMapper;

    public SpecialisationService(SpecialisationRepository specialisationRepository, SpecialisationMapper specialisationMapper) {
        this.specialisationRepository = specialisationRepository;
        this.specialisationMapper = specialisationMapper;
    }

    /**
     * Save a specialisation.
     *
     * @param specialisationDTO the entity to save.
     * @return the persisted entity.
     */
    public SpecialisationDTO save(SpecialisationDTO specialisationDTO) {
        LOG.debug("Request to save Specialisation : {}", specialisationDTO);
        Specialisation specialisation = specialisationMapper.toEntity(specialisationDTO);
        specialisation = specialisationRepository.save(specialisation);
        return specialisationMapper.toDto(specialisation);
    }

    /**
     * Update a specialisation.
     *
     * @param specialisationDTO the entity to save.
     * @return the persisted entity.
     */
    public SpecialisationDTO update(SpecialisationDTO specialisationDTO) {
        LOG.debug("Request to update Specialisation : {}", specialisationDTO);
        Specialisation specialisation = specialisationMapper.toEntity(specialisationDTO);
        specialisation = specialisationRepository.save(specialisation);
        return specialisationMapper.toDto(specialisation);
    }

    /**
     * Partially update a specialisation.
     *
     * @param specialisationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SpecialisationDTO> partialUpdate(SpecialisationDTO specialisationDTO) {
        LOG.debug("Request to partially update Specialisation : {}", specialisationDTO);

        return specialisationRepository
            .findById(specialisationDTO.getId())
            .map(existingSpecialisation -> {
                specialisationMapper.partialUpdate(existingSpecialisation, specialisationDTO);

                return existingSpecialisation;
            })
            .map(specialisationRepository::save)
            .map(specialisationMapper::toDto);
    }

    /**
     * Get all the specialisations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SpecialisationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Specialisations");
        return specialisationRepository.findAll(pageable).map(specialisationMapper::toDto);
    }

    /**
     * Get one specialisation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SpecialisationDTO> findOne(Long id) {
        LOG.debug("Request to get Specialisation : {}", id);
        return specialisationRepository.findById(id).map(specialisationMapper::toDto);
    }

    /**
     * Delete the specialisation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Specialisation : {}", id);
        specialisationRepository.deleteById(id);
    }
}
