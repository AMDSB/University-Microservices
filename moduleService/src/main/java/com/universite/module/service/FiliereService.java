package com.universite.module.service;

import com.universite.module.domain.Filiere;
import com.universite.module.repository.FiliereRepository;
import com.universite.module.service.dto.FiliereDTO;
import com.universite.module.service.mapper.FiliereMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.universite.module.domain.Filiere}.
 */
@Service
@Transactional
public class FiliereService {

    private static final Logger LOG = LoggerFactory.getLogger(FiliereService.class);

    private final FiliereRepository filiereRepository;

    private final FiliereMapper filiereMapper;

    public FiliereService(FiliereRepository filiereRepository, FiliereMapper filiereMapper) {
        this.filiereRepository = filiereRepository;
        this.filiereMapper = filiereMapper;
    }

    /**
     * Save a filiere.
     *
     * @param filiereDTO the entity to save.
     * @return the persisted entity.
     */
    public FiliereDTO save(FiliereDTO filiereDTO) {
        LOG.debug("Request to save Filiere : {}", filiereDTO);
        Filiere filiere = filiereMapper.toEntity(filiereDTO);
        filiere = filiereRepository.save(filiere);
        return filiereMapper.toDto(filiere);
    }

    /**
     * Update a filiere.
     *
     * @param filiereDTO the entity to save.
     * @return the persisted entity.
     */
    public FiliereDTO update(FiliereDTO filiereDTO) {
        LOG.debug("Request to update Filiere : {}", filiereDTO);
        Filiere filiere = filiereMapper.toEntity(filiereDTO);
        filiere = filiereRepository.save(filiere);
        return filiereMapper.toDto(filiere);
    }

    /**
     * Partially update a filiere.
     *
     * @param filiereDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FiliereDTO> partialUpdate(FiliereDTO filiereDTO) {
        LOG.debug("Request to partially update Filiere : {}", filiereDTO);

        return filiereRepository
            .findById(filiereDTO.getId())
            .map(existingFiliere -> {
                filiereMapper.partialUpdate(existingFiliere, filiereDTO);

                return existingFiliere;
            })
            .map(filiereRepository::save)
            .map(filiereMapper::toDto);
    }

    /**
     * Get all the filieres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FiliereDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Filieres");
        return filiereRepository.findAll(pageable).map(filiereMapper::toDto);
    }

    /**
     * Get one filiere by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FiliereDTO> findOne(Long id) {
        LOG.debug("Request to get Filiere : {}", id);
        return filiereRepository.findById(id).map(filiereMapper::toDto);
    }

    /**
     * Delete the filiere by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Filiere : {}", id);
        filiereRepository.deleteById(id);
    }
}
