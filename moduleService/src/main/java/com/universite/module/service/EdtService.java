package com.universite.module.service;

import com.universite.module.domain.Edt;
import com.universite.module.repository.EdtRepository;
import com.universite.module.service.dto.EdtDTO;
import com.universite.module.service.mapper.EdtMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.universite.module.domain.Edt}.
 */
@Service
@Transactional
public class EdtService {

    private static final Logger LOG = LoggerFactory.getLogger(EdtService.class);

    private final EdtRepository edtRepository;

    private final EdtMapper edtMapper;

    public EdtService(EdtRepository edtRepository, EdtMapper edtMapper) {
        this.edtRepository = edtRepository;
        this.edtMapper = edtMapper;
    }

    /**
     * Save a edt.
     *
     * @param edtDTO the entity to save.
     * @return the persisted entity.
     */
    public EdtDTO save(EdtDTO edtDTO) {
        LOG.debug("Request to save Edt : {}", edtDTO);
        Edt edt = edtMapper.toEntity(edtDTO);
        edt = edtRepository.save(edt);
        return edtMapper.toDto(edt);
    }

    /**
     * Update a edt.
     *
     * @param edtDTO the entity to save.
     * @return the persisted entity.
     */
    public EdtDTO update(EdtDTO edtDTO) {
        LOG.debug("Request to update Edt : {}", edtDTO);
        Edt edt = edtMapper.toEntity(edtDTO);
        edt = edtRepository.save(edt);
        return edtMapper.toDto(edt);
    }

    /**
     * Partially update a edt.
     *
     * @param edtDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EdtDTO> partialUpdate(EdtDTO edtDTO) {
        LOG.debug("Request to partially update Edt : {}", edtDTO);

        return edtRepository
            .findById(edtDTO.getId())
            .map(existingEdt -> {
                edtMapper.partialUpdate(existingEdt, edtDTO);

                return existingEdt;
            })
            .map(edtRepository::save)
            .map(edtMapper::toDto);
    }

    /**
     * Get all the edts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EdtDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Edts");
        return edtRepository.findAll(pageable).map(edtMapper::toDto);
    }

    /**
     * Get one edt by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EdtDTO> findOne(Long id) {
        LOG.debug("Request to get Edt : {}", id);
        return edtRepository.findById(id).map(edtMapper::toDto);
    }

    /**
     * Delete the edt by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Edt : {}", id);
        edtRepository.deleteById(id);
    }
}
