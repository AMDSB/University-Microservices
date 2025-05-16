package com.universite.module.service;

import com.universite.module.domain.Module;
import com.universite.module.repository.ModuleRepository;
import com.universite.module.service.dto.ModuleDTO;
import com.universite.module.service.mapper.ModuleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.universite.module.domain.Module}.
 */
@Service
@Transactional
public class ModuleService {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleService.class);

    private final ModuleRepository moduleRepository;

    private final ModuleMapper moduleMapper;

    public ModuleService(ModuleRepository moduleRepository, ModuleMapper moduleMapper) {
        this.moduleRepository = moduleRepository;
        this.moduleMapper = moduleMapper;
    }

    /**
     * Save a module.
     *
     * @param moduleDTO the entity to save.
     * @return the persisted entity.
     */
    public ModuleDTO save(ModuleDTO moduleDTO) {
        LOG.debug("Request to save Module : {}", moduleDTO);
        Module module = moduleMapper.toEntity(moduleDTO);
        module = moduleRepository.save(module);
        return moduleMapper.toDto(module);
    }

    /**
     * Update a module.
     *
     * @param moduleDTO the entity to save.
     * @return the persisted entity.
     */
    public ModuleDTO update(ModuleDTO moduleDTO) {
        LOG.debug("Request to update Module : {}", moduleDTO);
        Module module = moduleMapper.toEntity(moduleDTO);
        module = moduleRepository.save(module);
        return moduleMapper.toDto(module);
    }

    /**
     * Partially update a module.
     *
     * @param moduleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ModuleDTO> partialUpdate(ModuleDTO moduleDTO) {
        LOG.debug("Request to partially update Module : {}", moduleDTO);

        return moduleRepository
            .findById(moduleDTO.getId())
            .map(existingModule -> {
                moduleMapper.partialUpdate(existingModule, moduleDTO);

                return existingModule;
            })
            .map(moduleRepository::save)
            .map(moduleMapper::toDto);
    }

    /**
     * Get all the modules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ModuleDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Modules");
        return moduleRepository.findAll(pageable).map(moduleMapper::toDto);
    }

    /**
     * Get one module by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ModuleDTO> findOne(Long id) {
        LOG.debug("Request to get Module : {}", id);
        return moduleRepository.findById(id).map(moduleMapper::toDto);
    }

    /**
     * Delete the module by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Module : {}", id);
        moduleRepository.deleteById(id);
    }
}
