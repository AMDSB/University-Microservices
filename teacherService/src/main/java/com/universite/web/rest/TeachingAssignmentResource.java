package com.universite.web.rest;

import com.universite.repository.TeachingAssignmentRepository;
import com.universite.service.TeachingAssignmentService;
import com.universite.service.dto.TeachingAssignmentDTO;
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
 * REST controller for managing {@link com.universite.domain.TeachingAssignment}.
 */
@RestController
@RequestMapping("/api/teaching-assignments")
public class TeachingAssignmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(TeachingAssignmentResource.class);

    private static final String ENTITY_NAME = "teacherserviceTeachingAssignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeachingAssignmentService teachingAssignmentService;

    private final TeachingAssignmentRepository teachingAssignmentRepository;

    public TeachingAssignmentResource(
        TeachingAssignmentService teachingAssignmentService,
        TeachingAssignmentRepository teachingAssignmentRepository
    ) {
        this.teachingAssignmentService = teachingAssignmentService;
        this.teachingAssignmentRepository = teachingAssignmentRepository;
    }

    /**
     * {@code POST  /teaching-assignments} : Create a new teachingAssignment.
     *
     * @param teachingAssignmentDTO the teachingAssignmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teachingAssignmentDTO, or with status {@code 400 (Bad Request)} if the teachingAssignment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TeachingAssignmentDTO> createTeachingAssignment(@Valid @RequestBody TeachingAssignmentDTO teachingAssignmentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TeachingAssignment : {}", teachingAssignmentDTO);
        if (teachingAssignmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new teachingAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        teachingAssignmentDTO = teachingAssignmentService.save(teachingAssignmentDTO);
        return ResponseEntity.created(new URI("/api/teaching-assignments/" + teachingAssignmentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, teachingAssignmentDTO.getId().toString()))
            .body(teachingAssignmentDTO);
    }

    /**
     * {@code PUT  /teaching-assignments/:id} : Updates an existing teachingAssignment.
     *
     * @param id the id of the teachingAssignmentDTO to save.
     * @param teachingAssignmentDTO the teachingAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teachingAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the teachingAssignmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teachingAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeachingAssignmentDTO> updateTeachingAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TeachingAssignmentDTO teachingAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TeachingAssignment : {}, {}", id, teachingAssignmentDTO);
        if (teachingAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teachingAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teachingAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        teachingAssignmentDTO = teachingAssignmentService.update(teachingAssignmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teachingAssignmentDTO.getId().toString()))
            .body(teachingAssignmentDTO);
    }

    /**
     * {@code PATCH  /teaching-assignments/:id} : Partial updates given fields of an existing teachingAssignment, field will ignore if it is null
     *
     * @param id the id of the teachingAssignmentDTO to save.
     * @param teachingAssignmentDTO the teachingAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teachingAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the teachingAssignmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the teachingAssignmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the teachingAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TeachingAssignmentDTO> partialUpdateTeachingAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TeachingAssignmentDTO teachingAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TeachingAssignment partially : {}, {}", id, teachingAssignmentDTO);
        if (teachingAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teachingAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teachingAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TeachingAssignmentDTO> result = teachingAssignmentService.partialUpdate(teachingAssignmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teachingAssignmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /teaching-assignments} : get all the teachingAssignments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teachingAssignments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TeachingAssignmentDTO>> getAllTeachingAssignments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of TeachingAssignments");
        Page<TeachingAssignmentDTO> page = teachingAssignmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /teaching-assignments/:id} : get the "id" teachingAssignment.
     *
     * @param id the id of the teachingAssignmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teachingAssignmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeachingAssignmentDTO> getTeachingAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TeachingAssignment : {}", id);
        Optional<TeachingAssignmentDTO> teachingAssignmentDTO = teachingAssignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teachingAssignmentDTO);
    }

    /**
     * {@code DELETE  /teaching-assignments/:id} : delete the "id" teachingAssignment.
     *
     * @param id the id of the teachingAssignmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeachingAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TeachingAssignment : {}", id);
        teachingAssignmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
