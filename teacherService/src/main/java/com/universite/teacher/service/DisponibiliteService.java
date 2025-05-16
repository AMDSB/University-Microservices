package com.universite.teacher.service;

import com.universite.teacher.domain.Disponibilite;
import com.universite.teacher.repository.DisponibiliteRepository;
import com.universite.teacher.service.dto.DisponibiliteDTO;
import com.universite.teacher.service.mapper.DisponibiliteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.universite.teacher.domain.Disponibilite}.
 */
@Service
@Transactional
public class DisponibiliteService {

    private static final Logger LOG = LoggerFactory.getLogger(DisponibiliteService.class);

    private final DisponibiliteRepository disponibiliteRepository;

    private final DisponibiliteMapper disponibiliteMapper;

    public DisponibiliteService(DisponibiliteRepository disponibiliteRepository, DisponibiliteMapper disponibiliteMapper) {
        this.disponibiliteRepository = disponibiliteRepository;
        this.disponibiliteMapper = disponibiliteMapper;
    }

    /**
     * Save a disponibilite.
     *
     * @param disponibiliteDTO the entity to save.
     * @return the persisted entity.
     */
    public DisponibiliteDTO save(DisponibiliteDTO disponibiliteDTO) {
        LOG.debug("Request to save Disponibilite : {}", disponibiliteDTO);
        Disponibilite disponibilite = disponibiliteMapper.toEntity(disponibiliteDTO);
        disponibilite = disponibiliteRepository.save(disponibilite);
        return disponibiliteMapper.toDto(disponibilite);
    }

    /**
     * Update a disponibilite.
     *
     * @param disponibiliteDTO the entity to save.
     * @return the persisted entity.
     */
    public DisponibiliteDTO update(DisponibiliteDTO disponibiliteDTO) {
        LOG.debug("Request to update Disponibilite : {}", disponibiliteDTO);
        Disponibilite disponibilite = disponibiliteMapper.toEntity(disponibiliteDTO);
        disponibilite = disponibiliteRepository.save(disponibilite);
        return disponibiliteMapper.toDto(disponibilite);
    }

    /**
     * Partially update a disponibilite.
     *
     * @param disponibiliteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DisponibiliteDTO> partialUpdate(DisponibiliteDTO disponibiliteDTO) {
        LOG.debug("Request to partially update Disponibilite : {}", disponibiliteDTO);

        return disponibiliteRepository
            .findById(disponibiliteDTO.getId())
            .map(existingDisponibilite -> {
                disponibiliteMapper.partialUpdate(existingDisponibilite, disponibiliteDTO);

                return existingDisponibilite;
            })
            .map(disponibiliteRepository::save)
            .map(disponibiliteMapper::toDto);
    }

    /**
     * Get all the disponibilites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DisponibiliteDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Disponibilites");
        return disponibiliteRepository.findAll(pageable).map(disponibiliteMapper::toDto);
    }

    /**
     * Get one disponibilite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DisponibiliteDTO> findOne(Long id) {
        LOG.debug("Request to get Disponibilite : {}", id);
        return disponibiliteRepository.findById(id).map(disponibiliteMapper::toDto);
    }

    /**
     * Delete the disponibilite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Disponibilite : {}", id);
        disponibiliteRepository.deleteById(id);
    }
}
