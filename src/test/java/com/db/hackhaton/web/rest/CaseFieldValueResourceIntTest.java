package com.db.hackhaton.web.rest;

import com.db.hackhaton.AppApp;

import com.db.hackhaton.domain.CaseFieldValue;
import com.db.hackhaton.repository.CaseFieldValueRepository;
import com.db.hackhaton.repository.MedicalCaseRepository;
import com.db.hackhaton.repository.RegistryFieldRepository;
import com.db.hackhaton.service.FileService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CaseFieldValueResource REST controller.
 *
 * @see CaseFieldValueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class CaseFieldValueResourceIntTest {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private CaseFieldValueRepository caseFieldValueRepository;

    @Autowired
    private RegistryFieldRepository registryFieldRepository;

    @Autowired
    private MedicalCaseRepository medicalCaseRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCaseFieldValueMockMvc;

    private CaseFieldValue caseFieldValue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CaseFieldValueResource caseFieldValueResource =
            new CaseFieldValueResource(caseFieldValueRepository, registryFieldRepository, medicalCaseRepository, fileService);
        this.restCaseFieldValueMockMvc = MockMvcBuilders.standaloneSetup(caseFieldValueResource)
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
    public static CaseFieldValue createEntity(EntityManager em) {
        CaseFieldValue caseFieldValue = new CaseFieldValue()
            .value(DEFAULT_VALUE);
        return caseFieldValue;
    }

    @Before
    public void initTest() {
        caseFieldValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createCaseFieldValue() throws Exception {
        int databaseSizeBeforeCreate = caseFieldValueRepository.findAll().size();

        // Create the CaseFieldValue
        restCaseFieldValueMockMvc.perform(post("/api/case-field-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(caseFieldValue)))
            .andExpect(status().isCreated());

        // Validate the CaseFieldValue in the database
        List<CaseFieldValue> caseFieldValueList = caseFieldValueRepository.findAll();
        assertThat(caseFieldValueList).hasSize(databaseSizeBeforeCreate + 1);
        CaseFieldValue testCaseFieldValue = caseFieldValueList.get(caseFieldValueList.size() - 1);
        assertThat(testCaseFieldValue.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createCaseFieldValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = caseFieldValueRepository.findAll().size();

        // Create the CaseFieldValue with an existing ID
        caseFieldValue.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaseFieldValueMockMvc.perform(post("/api/case-field-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(caseFieldValue)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CaseFieldValue> caseFieldValueList = caseFieldValueRepository.findAll();
        assertThat(caseFieldValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCaseFieldValues() throws Exception {
        // Initialize the database
        caseFieldValueRepository.saveAndFlush(caseFieldValue);

        // Get all the caseFieldValueList
        restCaseFieldValueMockMvc.perform(get("/api/case-field-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caseFieldValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getCaseFieldValue() throws Exception {
        // Initialize the database
        caseFieldValueRepository.saveAndFlush(caseFieldValue);

        // Get the caseFieldValue
        restCaseFieldValueMockMvc.perform(get("/api/case-field-values/{id}", caseFieldValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(caseFieldValue.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCaseFieldValue() throws Exception {
        // Get the caseFieldValue
        restCaseFieldValueMockMvc.perform(get("/api/case-field-values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCaseFieldValue() throws Exception {
        // Initialize the database
        caseFieldValueRepository.saveAndFlush(caseFieldValue);
        int databaseSizeBeforeUpdate = caseFieldValueRepository.findAll().size();

        // Update the caseFieldValue
        CaseFieldValue updatedCaseFieldValue = caseFieldValueRepository.findOne(caseFieldValue.getId());
        updatedCaseFieldValue
            .value(UPDATED_VALUE);

        restCaseFieldValueMockMvc.perform(put("/api/case-field-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCaseFieldValue)))
            .andExpect(status().isOk());

        // Validate the CaseFieldValue in the database
        List<CaseFieldValue> caseFieldValueList = caseFieldValueRepository.findAll();
        assertThat(caseFieldValueList).hasSize(databaseSizeBeforeUpdate);
        CaseFieldValue testCaseFieldValue = caseFieldValueList.get(caseFieldValueList.size() - 1);
        assertThat(testCaseFieldValue.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingCaseFieldValue() throws Exception {
        int databaseSizeBeforeUpdate = caseFieldValueRepository.findAll().size();

        // Create the CaseFieldValue

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCaseFieldValueMockMvc.perform(put("/api/case-field-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(caseFieldValue)))
            .andExpect(status().isCreated());

        // Validate the CaseFieldValue in the database
        List<CaseFieldValue> caseFieldValueList = caseFieldValueRepository.findAll();
        assertThat(caseFieldValueList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCaseFieldValue() throws Exception {
        // Initialize the database
        caseFieldValueRepository.saveAndFlush(caseFieldValue);
        int databaseSizeBeforeDelete = caseFieldValueRepository.findAll().size();

        // Get the caseFieldValue
        restCaseFieldValueMockMvc.perform(delete("/api/case-field-values/{id}", caseFieldValue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CaseFieldValue> caseFieldValueList = caseFieldValueRepository.findAll();
        assertThat(caseFieldValueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CaseFieldValue.class);
        CaseFieldValue caseFieldValue1 = new CaseFieldValue();
        caseFieldValue1.setId(1L);
        CaseFieldValue caseFieldValue2 = new CaseFieldValue();
        caseFieldValue2.setId(caseFieldValue1.getId());
        assertThat(caseFieldValue1).isEqualTo(caseFieldValue2);
        caseFieldValue2.setId(2L);
        assertThat(caseFieldValue1).isNotEqualTo(caseFieldValue2);
        caseFieldValue1.setId(null);
        assertThat(caseFieldValue1).isNotEqualTo(caseFieldValue2);
    }
}
