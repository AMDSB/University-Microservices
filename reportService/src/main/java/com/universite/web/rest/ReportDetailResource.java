package com.universite.web.rest;

import com.universite.repository.ReportDetailRepository;
import com.universite.service.ReportDetailService;
import com.universite.service.dto.ReportDetailDTO;
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
 * REST controller for managing {@link com.universite.domain.ReportDetail}.
 */
@RestController
@RequestMapping("/api/report-details")
public class ReportDetailResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReportDetailResource.class);

    private static final String ENTITY_NAME = "reportserviceReportDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportDetailService reportDetailService;

    private final ReportDetailRepository reportDetailRepository;

    public ReportDetailResource(ReportDetailService reportDetailService, ReportDetailRepository reportDetailRepository) {
        this.reportDetailService = reportDetailService;
        this.reportDetailRepository = reportDetailRepository;
    }

    /**
     * {@code POST  /report-details} : Create a new reportDetail.
     *
     * @param reportDetailDTO the reportDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportDetailDTO, or with status {@code 400 (Bad Request)} if the reportDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportDetailDTO> createReportDetail(@Valid @RequestBody ReportDetailDTO reportDetailDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ReportDetail : {}", reportDetailDTO);
        if (reportDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportDetailDTO = reportDetailService.save(reportDetailDTO);
        return ResponseEntity.created(new URI("/api/report-details/" + reportDetailDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportDetailDTO.getId().toString()))
            .body(reportDetailDTO);
    }

    /**
     * {@code PUT  /report-details/:id} : Updates an existing reportDetail.
     *
     * @param id the id of the reportDetailDTO to save.
     * @param reportDetailDTO the reportDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportDetailDTO,
     * or with status {@code 400 (Bad Request)} if the reportDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportDetailDTO> updateReportDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportDetailDTO reportDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReportDetail : {}, {}", id, reportDetailDTO);
        if (reportDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportDetailDTO = reportDetailService.update(reportDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportDetailDTO.getId().toString()))
            .body(reportDetailDTO);
    }

    /**
     * {@code PATCH  /report-details/:id} : Partial updates given fields of an existing reportDetail, field will ignore if it is null
     *
     * @param id the id of the reportDetailDTO to save.
     * @param reportDetailDTO the reportDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportDetailDTO,
     * or with status {@code 400 (Bad Request)} if the reportDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportDetailDTO> partialUpdateReportDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportDetailDTO reportDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReportDetail partially : {}, {}", id, reportDetailDTO);
        if (reportDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportDetailDTO> result = reportDetailService.partialUpdate(reportDetailDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportDetailDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-details} : get all the reportDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportDetails in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReportDetailDTO>> getAllReportDetails(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ReportDetails");
        Page<ReportDetailDTO> page = reportDetailService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /report-details/:id} : get the "id" reportDetail.
     *
     * @param id the id of the reportDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportDetailDTO> getReportDetail(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReportDetail : {}", id);
        Optional<ReportDetailDTO> reportDetailDTO = reportDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportDetailDTO);
    }

    /**
     * {@code DELETE  /report-details/:id} : delete the "id" reportDetail.
     *
     * @param id the id of the reportDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportDetail(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReportDetail : {}", id);
        reportDetailService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
