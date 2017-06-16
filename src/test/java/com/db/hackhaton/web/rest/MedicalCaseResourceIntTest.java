package com.db.hackhaton.web.rest;

import com.db.hackhaton.AppApp;
import com.db.hackhaton.domain.MedicalCase;
import com.db.hackhaton.repository.MedicalCaseRepository;
import com.db.hackhaton.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MedicalCaseResource REST controller.
 *
 * @see MedicalCaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class MedicalCaseResourceIntTest {

    private static final Boolean DEFAULT_APPROVAL_STATUS = false;
    private static final Boolean UPDATED_APPROVAL_STATUS = true;

    private static final LocalDate DEFAULT_CREATEDON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATEDON = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ARCHIVED = false;
    private static final Boolean UPDATED_ARCHIVED = true;

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    @Autowired
    private MedicalCaseRepository medicalCaseRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMedicalCaseMockMvc;

    private MedicalCase medicalCase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MedicalCaseResource medicalCaseResource = new MedicalCaseResource(medicalCaseRepository, null, null, null, null);
        this.restMedicalCaseMockMvc = MockMvcBuilders.standaloneSetup(medicalCaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedicalCase createEntity(EntityManager em) {
        MedicalCase medicalCase = new MedicalCase()
            .approvalStatus(DEFAULT_APPROVAL_STATUS)
            .createdon(DEFAULT_CREATEDON)
            .archived(DEFAULT_ARCHIVED)
            .version(DEFAULT_VERSION);
        return medicalCase;
    }

    @Before
    public void initTest() {
        medicalCase = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedicalCase() throws Exception {
        int databaseSizeBeforeCreate = medicalCaseRepository.findAll().size();

        // Create the MedicalCase
        restMedicalCaseMockMvc.perform(post("/api/medical-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalCase)))
            .andExpect(status().isCreated());

        // Validate the MedicalCase in the database
        List<MedicalCase> medicalCaseList = medicalCaseRepository.findAll();
        assertThat(medicalCaseList).hasSize(databaseSizeBeforeCreate + 1);
        MedicalCase testMedicalCase = medicalCaseList.get(medicalCaseList.size() - 1);
        assertThat(testMedicalCase.isApprovalStatus()).isEqualTo(DEFAULT_APPROVAL_STATUS);
        assertThat(testMedicalCase.getCreatedon()).isEqualTo(DEFAULT_CREATEDON);
        assertThat(testMedicalCase.isArchived()).isEqualTo(DEFAULT_ARCHIVED);
        assertThat(testMedicalCase.getVersion()).isEqualTo(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    public void createMedicalCaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medicalCaseRepository.findAll().size();

        // Create the MedicalCase with an existing ID
        medicalCase.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalCaseMockMvc.perform(post("/api/medical-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalCase)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MedicalCase> medicalCaseList = medicalCaseRepository.findAll();
        assertThat(medicalCaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMedicalCases() throws Exception {
        // Initialize the database
        medicalCaseRepository.saveAndFlush(medicalCase);

        // Get all the medicalCaseList
        restMedicalCaseMockMvc.perform(get("/api/medical-cases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalCase.getId().intValue())))
            .andExpect(jsonPath("$.[*].approvalStatus").value(hasItem(DEFAULT_APPROVAL_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].createdon").value(hasItem(DEFAULT_CREATEDON.toString())))
            .andExpect(jsonPath("$.[*].archived").value(hasItem(DEFAULT_ARCHIVED.booleanValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)));
    }

    @Test
    @Transactional
    public void getMedicalCase() throws Exception {
        // Initialize the database
        medicalCaseRepository.saveAndFlush(medicalCase);

        // Get the medicalCase
        restMedicalCaseMockMvc.perform(get("/api/medical-cases/{id}", medicalCase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(medicalCase.getId().intValue()))
            .andExpect(jsonPath("$.approvalStatus").value(DEFAULT_APPROVAL_STATUS.booleanValue()))
            .andExpect(jsonPath("$.createdon").value(DEFAULT_CREATEDON.toString()))
            .andExpect(jsonPath("$.archived").value(DEFAULT_ARCHIVED.booleanValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION));
    }

    @Test
    @Transactional
    public void getNonExistingMedicalCase() throws Exception {
        // Get the medicalCase
        restMedicalCaseMockMvc.perform(get("/api/medical-cases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedicalCase() throws Exception {
        // Initialize the database
        medicalCaseRepository.saveAndFlush(medicalCase);
        int databaseSizeBeforeUpdate = medicalCaseRepository.findAll().size();

        // Update the medicalCase
        MedicalCase updatedMedicalCase = medicalCaseRepository.findOne(medicalCase.getId());
        updatedMedicalCase
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .createdon(UPDATED_CREATEDON)
            .archived(UPDATED_ARCHIVED)
            .version(UPDATED_VERSION);

        restMedicalCaseMockMvc.perform(put("/api/medical-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMedicalCase)))
            .andExpect(status().isOk());

        // Validate the MedicalCase in the database
        List<MedicalCase> medicalCaseList = medicalCaseRepository.findAll();
        assertThat(medicalCaseList).hasSize(databaseSizeBeforeUpdate);
        MedicalCase testMedicalCase = medicalCaseList.get(medicalCaseList.size() - 1);
        assertThat(testMedicalCase.isApprovalStatus()).isEqualTo(UPDATED_APPROVAL_STATUS);
        assertThat(testMedicalCase.getCreatedon()).isEqualTo(UPDATED_CREATEDON);
        assertThat(testMedicalCase.isArchived()).isEqualTo(UPDATED_ARCHIVED);
        assertThat(testMedicalCase.getVersion()).isEqualTo(UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void updateNonExistingMedicalCase() throws Exception {
        int databaseSizeBeforeUpdate = medicalCaseRepository.findAll().size();

        // Create the MedicalCase

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMedicalCaseMockMvc.perform(put("/api/medical-cases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalCase)))
            .andExpect(status().isCreated());

        // Validate the MedicalCase in the database
        List<MedicalCase> medicalCaseList = medicalCaseRepository.findAll();
        assertThat(medicalCaseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMedicalCase() throws Exception {
        // Initialize the database
        medicalCaseRepository.saveAndFlush(medicalCase);
        int databaseSizeBeforeDelete = medicalCaseRepository.findAll().size();

        // Get the medicalCase
        restMedicalCaseMockMvc.perform(delete("/api/medical-cases/{id}", medicalCase.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MedicalCase> medicalCaseList = medicalCaseRepository.findAll();
        assertThat(medicalCaseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalCase.class);
        MedicalCase medicalCase1 = new MedicalCase();
        medicalCase1.setId(1L);
        MedicalCase medicalCase2 = new MedicalCase();
        medicalCase2.setId(medicalCase1.getId());
        assertThat(medicalCase1).isEqualTo(medicalCase2);
        medicalCase2.setId(2L);
        assertThat(medicalCase1).isNotEqualTo(medicalCase2);
        medicalCase1.setId(null);
        assertThat(medicalCase1).isNotEqualTo(medicalCase2);
    }
}
