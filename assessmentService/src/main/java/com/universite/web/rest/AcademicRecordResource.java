package com.universite.web.rest;

import com.universite.repository.AcademicRecordRepository;
import com.universite.service.AcademicRecordService;
import com.universite.service.dto.AcademicRecordDTO;
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
 * REST controller for managing {@link com.universite.domain.AcademicRecord}.
 */
@RestController
@RequestMapping("/api/academic-records")
public class AcademicRecordResource {

    private static final Logger LOG = LoggerFactory.getLogger(AcademicRecordResource.class);

    private static final String ENTITY_NAME = "assessmentserviceAcademicRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcademicRecordService academicRecordService;

    private final AcademicRecordRepository academicRecordRepository;

    public AcademicRecordResource(AcademicRecordService academicRecordService, AcademicRecordRepository academicRecordRepository) {
        this.academicRecordService = academicRecordService;
        this.academicRecordRepository = academicRecordRepository;
    }

    /**
     * {@code POST  /academic-records} : Create a new academicRecord.
     *
     * @param academicRecordDTO the academicRecordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new academicRecordDTO, or with status {@code 400 (Bad Request)} if the academicRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AcademicRecordDTO> createAcademicRecord(@Valid @RequestBody AcademicRecordDTO academicRecordDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AcademicRecord : {}", academicRecordDTO);
        if (academicRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new academicRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        academicRecordDTO = academicRecordService.save(academicRecordDTO);
        return ResponseEntity.created(new URI("/api/academic-records/" + academicRecordDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, academicRecordDTO.getId().toString()))
            .body(academicRecordDTO);
    }

    /**
     * {@code PUT  /academic-records/:id} : Updates an existing academicRecord.
     *
     * @param id the id of the academicRecordDTO to save.
     * @param academicRecordDTO the academicRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated academicRecordDTO,
     * or with status {@code 400 (Bad Request)} if the academicRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the academicRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AcademicRecordDTO> updateAcademicRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AcademicRecordDTO academicRecordDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AcademicRecord : {}, {}", id, academicRecordDTO);
        if (academicRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, academicRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!academicRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        academicRecordDTO = academicRecordService.update(academicRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, academicRecordDTO.getId().toString()))
            .body(academicRecordDTO);
    }

    /**
     * {@code PATCH  /academic-records/:id} : Partial updates given fields of an existing academicRecord, field will ignore if it is null
     *
     * @param id the id of the academicRecordDTO to save.
     * @param academicRecordDTO the academicRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated academicRecordDTO,
     * or with status {@code 400 (Bad Request)} if the academicRecordDTO is not valid,
     * or with status {@code 404 (Not Found)} if the academicRecordDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the academicRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AcademicRecordDTO> partialUpdateAcademicRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AcademicRecordDTO academicRecordDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AcademicRecord partially : {}, {}", id, academicRecordDTO);
        if (academicRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, academicRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!academicRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AcademicRecordDTO> result = academicRecordService.partialUpdate(academicRecordDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, academicRecordDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /academic-records} : get all the academicRecords.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of academicRecords in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AcademicRecordDTO>> getAllAcademicRecords(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of AcademicRecords");
        Page<AcademicRecordDTO> page = academicRecordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /academic-records/:id} : get the "id" academicRecord.
     *
     * @param id the id of the academicRecordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the academicRecordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AcademicRecordDTO> getAcademicRecord(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AcademicRecord : {}", id);
        Optional<AcademicRecordDTO> academicRecordDTO = academicRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(academicRecordDTO);
    }

    /**
     * {@code DELETE  /academic-records/:id} : delete the "id" academicRecord.
     *
     * @param id the id of the academicRecordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAcademicRecord(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AcademicRecord : {}", id);
        academicRecordService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
