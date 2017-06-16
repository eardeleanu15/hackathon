package com.db.hackhaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackhaton.domain.CaseFieldValue;

import com.db.hackhaton.domain.MedicalCase;
import com.db.hackhaton.domain.RegistryField;
import com.db.hackhaton.repository.CaseFieldValueRepository;
import com.db.hackhaton.repository.MedicalCaseRepository;
import com.db.hackhaton.repository.RegistryFieldRepository;
import com.db.hackhaton.service.FileService;
import com.db.hackhaton.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CaseFieldValue.
 */
@RestController
@RequestMapping("/api")
public class CaseFieldValueResource {

    private final Logger log = LoggerFactory.getLogger(CaseFieldValueResource.class);

    private static final String ENTITY_NAME = "caseFieldValue";

    private final CaseFieldValueRepository caseFieldValueRepository;

    private final RegistryFieldRepository registryFieldRepository;

    private final MedicalCaseRepository medicalCaseRepository;

    private final FileService fileService;

    public CaseFieldValueResource(CaseFieldValueRepository caseFieldValueRepository,
                                  RegistryFieldRepository registryFieldRepository,
                                  MedicalCaseRepository medicalCaseRepository,
                                  FileService fileService) {
        this.caseFieldValueRepository = caseFieldValueRepository;
        this.registryFieldRepository = registryFieldRepository;
        this.medicalCaseRepository = medicalCaseRepository;
        this.fileService = fileService;
    }

    /**
     * POST  /case-field-values : Create a new caseFieldValue.
     *
     * @param caseFieldValue the caseFieldValue to create
     * @return the ResponseEntity with status 201 (Created) and with body the new caseFieldValue, or with status 400 (Bad Request) if the caseFieldValue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/case-field-values")
    @Timed
    public ResponseEntity<CaseFieldValue> createCaseFieldValue(@RequestBody CaseFieldValue caseFieldValue) throws URISyntaxException {
        log.debug("REST request to save CaseFieldValue : {}", caseFieldValue);
        if (caseFieldValue.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new caseFieldValue cannot already have an ID")).body(null);
        }
        CaseFieldValue result = caseFieldValueRepository.save(caseFieldValue);
        return ResponseEntity.created(new URI("/api/case-field-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /case-field-values : Updates an existing caseFieldValue.
     *
     * @param caseFieldValue the caseFieldValue to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated caseFieldValue,
     * or with status 400 (Bad Request) if the caseFieldValue is not valid,
     * or with status 500 (Internal Server Error) if the caseFieldValue couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/case-field-values")
    @Timed
    public ResponseEntity<CaseFieldValue> updateCaseFieldValue(@RequestBody CaseFieldValue caseFieldValue) throws URISyntaxException {
        log.debug("REST request to update CaseFieldValue : {}", caseFieldValue);
        if (caseFieldValue.getId() == null) {
            return createCaseFieldValue(caseFieldValue);
        }
        CaseFieldValue result = caseFieldValueRepository.save(caseFieldValue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, caseFieldValue.getId().toString()))
            .body(result);
    }

    /**
     * GET  /case-field-values : get all the caseFieldValues.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of caseFieldValues in body
     */
    @GetMapping("/case-field-values")
    @Timed
    public List<CaseFieldValue> getAllCaseFieldValues() {
        log.debug("REST request to get all CaseFieldValues");
        return caseFieldValueRepository.findAll();
    }

    /**
     * GET  /case-field-values/:id : get the "id" caseFieldValue.
     *
     * @param id the id of the caseFieldValue to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the caseFieldValue, or with status 404 (Not Found)
     */
    @GetMapping("/case-field-values/{id}")
    @Timed
    public ResponseEntity<CaseFieldValue> getCaseFieldValue(@PathVariable Long id) {
        log.debug("REST request to get CaseFieldValue : {}", id);
        CaseFieldValue caseFieldValue = caseFieldValueRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(caseFieldValue));
    }

    /**
     * DELETE  /case-field-values/:id : delete the "id" caseFieldValue.
     *
     * @param id the id of the caseFieldValue to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/case-field-values/{id}")
    @Timed
    public ResponseEntity<Void> deleteCaseFieldValue(@PathVariable Long id) {
        log.debug("REST request to delete CaseFieldValue : {}", id);
        caseFieldValueRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/case-field-values/attachment")
    public ResponseEntity<CaseFieldValue> uploadAttachmnet(@RequestParam("medicalCaseId") String medicalCaseId, @RequestParam("registryFieldId") String registryFieldId,
                                                   @RequestParam("fileName") String fileName, @RequestBody MultipartFile file) {
        log.debug("REST request to upload attachment for medical case : {}", medicalCaseId);
        if (fileName == null || file == null || file.isEmpty()) {
            return ResponseEntity
                .badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "malformedfile", "File attributes malformed")).body(null);
        }

        // Upload File on Server
        boolean uploaded = fileService.uploadFile(fileName, file);
        if (!uploaded){
            return ResponseEntity.badRequest().body(null);
        }

        // create a new case field value for attached file name
        RegistryField registryField = registryFieldRepository.findOne(Long.parseLong(registryFieldId));
        if (registryField == null) {
            log.debug("Could not identify registry field object by id {}", registryFieldId);
            return ResponseEntity.badRequest().body(null);
        }
        MedicalCase medicalCase = medicalCaseRepository.findOne(Long.parseLong(medicalCaseId));
        if (medicalCase == null) {
            log.debug("Could not identify medical case object by id {}", medicalCaseId);
            return ResponseEntity.badRequest().body(null);
        }

        CaseFieldValue result = caseFieldValueRepository.save(
            new CaseFieldValue().registryField(registryField).medicalCase(medicalCase).value(fileName));
        return ResponseEntity.ok().body(result);
    }
}
