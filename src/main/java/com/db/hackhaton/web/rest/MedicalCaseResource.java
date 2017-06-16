package com.db.hackhaton.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.db.hackhaton.domain.CaseFieldValue;
import com.db.hackhaton.domain.MedicalCase;
import com.db.hackhaton.domain.RegistryField;
import com.db.hackhaton.domain.User;
import com.db.hackhaton.repository.CaseFieldValueRepository;
import com.db.hackhaton.repository.MedicalCaseRepository;
import com.db.hackhaton.repository.RegistryRepository;
import com.db.hackhaton.repository.UserRepository;
import com.db.hackhaton.security.SecurityUtils;
import com.db.hackhaton.service.MailService;
import com.db.hackhaton.web.rest.dto.MedicalCaseRetrieveDTO;
import com.db.hackhaton.web.rest.dto.MedicalCaseValue;
import com.db.hackhaton.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.*;

/**
 * REST controller for managing MedicalCase.
 */
@RestController
@RequestMapping("/api")
public class MedicalCaseResource {

    private final Logger log = LoggerFactory.getLogger(MedicalCaseResource.class);

    private static final String ENTITY_NAME = "medicalCase";

    private final MedicalCaseRepository medicalCaseRepository;
    private final CaseFieldValueRepository caseFieldValueRepository;
    private final RegistryRepository registryRepository;
    private final MailService mailService;
    private final UserRepository userRepository;

    public MedicalCaseResource(MedicalCaseRepository medicalCaseRepository,
                               CaseFieldValueRepository caseFieldValueRepository,
                               RegistryRepository registryRepository,
                               MailService mailService,
                               UserRepository userRepository) {
        this.medicalCaseRepository = medicalCaseRepository;
        this.caseFieldValueRepository = caseFieldValueRepository;
        this.registryRepository = registryRepository;
        this.mailService = mailService;
        this.userRepository = userRepository;
    }

    /**
     * POST  /medical-cases : Create a new medicalCase.
     *
     * @param medicalCase the medicalCase to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medicalCase, or with status 400 (Bad Request) if the medicalCase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/medical-cases")
    @Timed
    public ResponseEntity<MedicalCase> createMedicalCase(@RequestBody MedicalCase medicalCase) throws URISyntaxException {
        log.debug("REST request to save MedicalCase : {}", medicalCase);
        if (medicalCase.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new medicalCase cannot already have an ID")).body(null);
        }

        medicalCase.setCreatedon(LocalDate.now());
        setFieldIds(medicalCase);

        MedicalCase result = medicalCaseRepository.save(medicalCase);

        sendNewCaseEmailToAdmins(result);

        return ResponseEntity.created(new URI("/api/medical-cases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private void sendNewCaseEmailToAdmins(MedicalCase result) {
        if(result.isApprovalStatus() == null) {
            mailService.sendNewPatientCreatedCaseEmailToAdmins(result);
        }
    }

    /**
     * PUT  /medical-cases : Updates an existing medicalCase.
     *
     * @param medicalCase the medicalCase to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated medicalCase,
     * or with status 400 (Bad Request) if the medicalCase is not valid,
     * or with status 500 (Internal Server Error) if the medicalCase couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/medical-cases")
    @Timed
    public ResponseEntity<MedicalCase> updateMedicalCase(@RequestBody MedicalCase medicalCase) throws URISyntaxException {
        log.debug("REST request to update MedicalCase : {}", medicalCase);

        //setFieldIds(medicalCase);

        if (medicalCase.getId() == null) {
            return createMedicalCase(medicalCase);
        }
        MedicalCase result = medicalCaseRepository.save(medicalCase);

        // update case field values
        for(CaseFieldValue updateCaseField : medicalCase.getCaseFields()) {
            caseFieldValueRepository.updateValueById(updateCaseField.getValue(), updateCaseField.getId());
        }

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, medicalCase.getId().toString()))
            .body(result);
    }

    private void setFieldIds(MedicalCase medicalCase) {
        Set<CaseFieldValue> caseFields = medicalCase.getCaseFields();

        for(CaseFieldValue v : caseFields) {
            Long fieldId = v.getId();
            v.setId(null);
            RegistryField field = new RegistryField();
            field.setId(fieldId);
            v.setRegistryField(field);
        }
    }

    /**
     * GET  /medical-cases : get all the medicalCases.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of medicalCases in body
     */
    @GetMapping("/medical-cases")
    @Timed
    public List<MedicalCaseRetrieveDTO> getAllMedicalCases() {
        log.debug("REST request to get all MedicalCases");
        List<MedicalCase> medicalCases = null;
        List<MedicalCaseRetrieveDTO> medicalCaseDTOS = new ArrayList<>();

        if (SecurityUtils.isAboveUser()) {
            medicalCases = medicalCaseRepository.findAll();
        } else {
            Optional<User> user = userRepository.findOneWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin());
            String cnp = user.get().getCnp();
            medicalCases = medicalCaseRepository.getMedicalCasesByUserCnp(cnp);
        }

        for (MedicalCase medicalCase : medicalCases) {
            MedicalCaseRetrieveDTO dto = convertToDTO(medicalCase);
            medicalCaseDTOS.add(dto);
        }
        return medicalCaseDTOS;
    }

    @GetMapping("/medical-cases/{id}/values")
    @Timed
    public List<MedicalCaseValue> getMedicalCaseValues(@PathVariable("id") Long medicalCaseId) {
        CaseFieldValue sample = new CaseFieldValue();
        MedicalCase medicalCase = new MedicalCase();
        medicalCase.setId(medicalCaseId);
        sample.setMedicalCase(medicalCase);
        Example<CaseFieldValue> sampleFieldValue = Example.of(sample);
        List<CaseFieldValue> all = caseFieldValueRepository.findAll(sampleFieldValue);

        return convertToCaseFieldValues(all);
    }

    private List<MedicalCaseValue> convertToCaseFieldValues(List<CaseFieldValue> all) {
        List<MedicalCaseValue> result = new ArrayList<>();

        for(CaseFieldValue v : all) {
            MedicalCaseValue m = new MedicalCaseValue();
            m.setId(v.getId());
            m.setValue(v.getValue());
            m.setRegistryField(v.getRegistryField());

            result.add(m);
        }

        return result;
    }

    /**
     * GET  /medical-cases/:id : get the "id" medicalCase.
     *
     * @param id the id of the medicalCase to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medicalCase, or with status 404 (Not Found)
     */
    @GetMapping("/medical-cases/{id}")
    @Timed
    public ResponseEntity<MedicalCaseRetrieveDTO> getMedicalCase(@PathVariable Long id) {
        log.debug("REST request to get MedicalCase : {}", id);
        MedicalCase medicalCase = medicalCaseRepository.findOne(id);
        MedicalCaseRetrieveDTO dto = convertToDTO(medicalCase);
        return ResponseEntity.ok().body(dto);
    }

    private MedicalCaseRetrieveDTO convertToDTO(MedicalCase medicalCase) {
        MedicalCaseRetrieveDTO dto = new MedicalCaseRetrieveDTO();

        dto.setId(medicalCase.getId());
        dto.setArchived(medicalCase.isArchived());
        dto.setApprovalStatus(medicalCase.isApprovalStatus());
        dto.setCaseFields(medicalCase.getCaseFields());
        dto.setCreatedon(medicalCase.getCreatedon());
        dto.setVersion(medicalCase.getVersion());
        dto.setRegistry(medicalCase.getRegistry());

        return dto;
    }

    private MedicalCaseRetrieveDTO convertToDTO(MedicalCase medicalCase, User user) {

        MedicalCaseRetrieveDTO dto = convertToDTO(medicalCase);

        dto.setUser(user);

        return dto;
    }

    /**
     * DELETE  /medical-cases/:id : delete the "id" medicalCase.
     *
     * @param id the id of the medicalCase to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/medical-cases/{id}")
    @Timed
    public ResponseEntity<Void> deleteMedicalCase(@PathVariable Long id) {
        log.debug("REST request to delete MedicalCase : {}", id);
        medicalCaseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
