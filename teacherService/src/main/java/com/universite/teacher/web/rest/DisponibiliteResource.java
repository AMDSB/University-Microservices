package com.universite.teacher.web.rest;

import com.universite.teacher.repository.DisponibiliteRepository;
import com.universite.teacher.service.DisponibiliteService;
import com.universite.teacher.service.dto.DisponibiliteDTO;
import com.universite.teacher.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.universite.teacher.domain.Disponibilite}.
 */
@RestController
@RequestMapping("/api/disponibilites")
public class DisponibiliteResource {

    private static final Logger LOG = LoggerFactory.getLogger(DisponibiliteResource.class);

    private static final String ENTITY_NAME = "teacherserviceDisponibilite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DisponibiliteService disponibiliteService;

    private final DisponibiliteRepository disponibiliteRepository;

    public DisponibiliteResource(DisponibiliteService disponibiliteService, DisponibiliteRepository disponibiliteRepository) {
        this.disponibiliteService = disponibiliteService;
        this.disponibiliteRepository = disponibiliteRepository;
    }

    /**
     * {@code POST  /disponibilites} : Create a new disponibilite.
     *
     * @param disponibiliteDTO the disponibiliteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new disponibiliteDTO, or with status {@code 400 (Bad Request)} if the disponibilite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DisponibiliteDTO> createDisponibilite(@Valid @RequestBody DisponibiliteDTO disponibiliteDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Disponibilite : {}", disponibiliteDTO);
        if (disponibiliteDTO.getId() != null) {
            throw new BadRequestAlertException("A new disponibilite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        disponibiliteDTO = disponibiliteService.save(disponibiliteDTO);
        return ResponseEntity.created(new URI("/api/disponibilites/" + disponibiliteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, disponibiliteDTO.getId().toString()))
            .body(disponibiliteDTO);
    }

    /**
     * {@code PUT  /disponibilites/:id} : Updates an existing disponibilite.
     *
     * @param id the id of the disponibiliteDTO to save.
     * @param disponibiliteDTO the disponibiliteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disponibiliteDTO,
     * or with status {@code 400 (Bad Request)} if the disponibiliteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the disponibiliteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DisponibiliteDTO> updateDisponibilite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DisponibiliteDTO disponibiliteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Disponibilite : {}, {}", id, disponibiliteDTO);
        if (disponibiliteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disponibiliteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disponibiliteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        disponibiliteDTO = disponibiliteService.update(disponibiliteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, disponibiliteDTO.getId().toString()))
            .body(disponibiliteDTO);
    }

    /**
     * {@code PATCH  /disponibilites/:id} : Partial updates given fields of an existing disponibilite, field will ignore if it is null
     *
     * @param id the id of the disponibiliteDTO to save.
     * @param disponibiliteDTO the disponibiliteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disponibiliteDTO,
     * or with status {@code 400 (Bad Request)} if the disponibiliteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the disponibiliteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the disponibiliteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DisponibiliteDTO> partialUpdateDisponibilite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DisponibiliteDTO disponibiliteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Disponibilite partially : {}, {}", id, disponibiliteDTO);
        if (disponibiliteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disponibiliteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disponibiliteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DisponibiliteDTO> result = disponibiliteService.partialUpdate(disponibiliteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, disponibiliteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /disponibilites} : get all the disponibilites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of disponibilites in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DisponibiliteDTO>> getAllDisponibilites(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Disponibilites");
        Page<DisponibiliteDTO> page = disponibiliteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /disponibilites/:id} : get the "id" disponibilite.
     *
     * @param id the id of the disponibiliteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the disponibiliteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DisponibiliteDTO> getDisponibilite(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Disponibilite : {}", id);
        Optional<DisponibiliteDTO> disponibiliteDTO = disponibiliteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(disponibiliteDTO);
    }

    /**
     * {@code DELETE  /disponibilites/:id} : delete the "id" disponibilite.
     *
     * @param id the id of the disponibiliteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisponibilite(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Disponibilite : {}", id);
        disponibiliteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
