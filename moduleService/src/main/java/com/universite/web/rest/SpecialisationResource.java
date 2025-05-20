package com.universite.web.rest;

import com.universite.repository.SpecialisationRepository;
import com.universite.service.SpecialisationService;
import com.universite.service.dto.SpecialisationDTO;
import com.universite.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.universite.domain.Specialisation}.
 */
@RestController
@RequestMapping("/api/specialisations")
public class SpecialisationResource {

    private static final Logger LOG = LoggerFactory.getLogger(SpecialisationResource.class);

    private static final String ENTITY_NAME = "moduleserviceSpecialisation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialisationService specialisationService;

    private final SpecialisationRepository specialisationRepository;

    public SpecialisationResource(SpecialisationService specialisationService, SpecialisationRepository specialisationRepository) {
        this.specialisationService = specialisationService;
        this.specialisationRepository = specialisationRepository;
    }

    /**
     * {@code POST  /specialisations} : Create a new specialisation.
     *
     * @param specialisationDTO the specialisationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialisationDTO, or with status {@code 400 (Bad Request)} if the specialisation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SpecialisationDTO> createSpecialisation(@Valid @RequestBody SpecialisationDTO specialisationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Specialisation : {}", specialisationDTO);
        if (specialisationDTO.getId() != null) {
            throw new BadRequestAlertException("A new specialisation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        specialisationDTO = specialisationService.save(specialisationDTO);
        return ResponseEntity.created(new URI("/api/specialisations/" + specialisationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, specialisationDTO.getId().toString()))
            .body(specialisationDTO);
    }

    /**
     * {@code PUT  /specialisations/:id} : Updates an existing specialisation.
     *
     * @param id the id of the specialisationDTO to save.
     * @param specialisationDTO the specialisationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialisationDTO,
     * or with status {@code 400 (Bad Request)} if the specialisationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialisationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpecialisationDTO> updateSpecialisation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SpecialisationDTO specialisationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Specialisation : {}, {}", id, specialisationDTO);
        if (specialisationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialisationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialisationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        specialisationDTO = specialisationService.update(specialisationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialisationDTO.getId().toString()))
            .body(specialisationDTO);
    }

    /**
     * {@code PATCH  /specialisations/:id} : Partial updates given fields of an existing specialisation, field will ignore if it is null
     *
     * @param id the id of the specialisationDTO to save.
     * @param specialisationDTO the specialisationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialisationDTO,
     * or with status {@code 400 (Bad Request)} if the specialisationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the specialisationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the specialisationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpecialisationDTO> partialUpdateSpecialisation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SpecialisationDTO specialisationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Specialisation partially : {}, {}", id, specialisationDTO);
        if (specialisationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialisationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialisationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpecialisationDTO> result = specialisationService.partialUpdate(specialisationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialisationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /specialisations} : get all the specialisations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialisations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SpecialisationDTO>> getAllSpecialisations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of Specialisations");
        Page<SpecialisationDTO> page = specialisationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /specialisations/:id} : get the "id" specialisation.
     *
     * @param id the id of the specialisationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialisationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpecialisationDTO> getSpecialisation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Specialisation : {}", id);
        Optional<SpecialisationDTO> specialisationDTO = specialisationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specialisationDTO);
    }

    /**
     * {@code DELETE  /specialisations/:id} : delete the "id" specialisation.
     *
     * @param id the id of the specialisationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialisation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Specialisation : {}", id);
        specialisationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
