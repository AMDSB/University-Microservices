package com.universite.module.web.rest;

import com.universite.module.repository.EdtRepository;
import com.universite.module.service.EdtService;
import com.universite.module.service.dto.EdtDTO;
import com.universite.module.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.universite.module.domain.Edt}.
 */
@RestController
@RequestMapping("/api/edts")
public class EdtResource {

    private static final Logger LOG = LoggerFactory.getLogger(EdtResource.class);

    private static final String ENTITY_NAME = "moduleserviceEdt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EdtService edtService;

    private final EdtRepository edtRepository;

    public EdtResource(EdtService edtService, EdtRepository edtRepository) {
        this.edtService = edtService;
        this.edtRepository = edtRepository;
    }

    /**
     * {@code POST  /edts} : Create a new edt.
     *
     * @param edtDTO the edtDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new edtDTO, or with status {@code 400 (Bad Request)} if the edt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EdtDTO> createEdt(@Valid @RequestBody EdtDTO edtDTO) throws URISyntaxException {
        LOG.debug("REST request to save Edt : {}", edtDTO);
        if (edtDTO.getId() != null) {
            throw new BadRequestAlertException("A new edt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        edtDTO = edtService.save(edtDTO);
        return ResponseEntity.created(new URI("/api/edts/" + edtDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, edtDTO.getId().toString()))
            .body(edtDTO);
    }

    /**
     * {@code PUT  /edts/:id} : Updates an existing edt.
     *
     * @param id the id of the edtDTO to save.
     * @param edtDTO the edtDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated edtDTO,
     * or with status {@code 400 (Bad Request)} if the edtDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the edtDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EdtDTO> updateEdt(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody EdtDTO edtDTO)
        throws URISyntaxException {
        LOG.debug("REST request to update Edt : {}, {}", id, edtDTO);
        if (edtDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, edtDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!edtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        edtDTO = edtService.update(edtDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, edtDTO.getId().toString()))
            .body(edtDTO);
    }

    /**
     * {@code PATCH  /edts/:id} : Partial updates given fields of an existing edt, field will ignore if it is null
     *
     * @param id the id of the edtDTO to save.
     * @param edtDTO the edtDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated edtDTO,
     * or with status {@code 400 (Bad Request)} if the edtDTO is not valid,
     * or with status {@code 404 (Not Found)} if the edtDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the edtDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EdtDTO> partialUpdateEdt(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EdtDTO edtDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Edt partially : {}, {}", id, edtDTO);
        if (edtDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, edtDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!edtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EdtDTO> result = edtService.partialUpdate(edtDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, edtDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /edts} : get all the edts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of edts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EdtDTO>> getAllEdts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Edts");
        Page<EdtDTO> page = edtService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /edts/:id} : get the "id" edt.
     *
     * @param id the id of the edtDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the edtDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EdtDTO> getEdt(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Edt : {}", id);
        Optional<EdtDTO> edtDTO = edtService.findOne(id);
        return ResponseUtil.wrapOrNotFound(edtDTO);
    }

    /**
     * {@code DELETE  /edts/:id} : delete the "id" edt.
     *
     * @param id the id of the edtDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEdt(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Edt : {}", id);
        edtService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
