package com.db.hackhaton.web.rest;

import com.db.hackhaton.AppApp;

import com.db.hackhaton.domain.RegistryField;
import com.db.hackhaton.repository.RegistryFieldRepository;
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

import com.db.hackhaton.domain.enumeration.FieldType;
import com.db.hackhaton.domain.enumeration.FieldGroups;
/**
 * Test class for the RegistryFieldResource REST controller.
 *
 * @see RegistryFieldResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class RegistryFieldResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_MANDATORY = false;
    private static final Boolean UPDATED_MANDATORY = true;

    private static final FieldType DEFAULT_TYPE = FieldType.TEXT;
    private static final FieldType UPDATED_TYPE = FieldType.NUMERIC;

    private static final Integer DEFAULT_MAX_LENGTH = 1;
    private static final Integer UPDATED_MAX_LENGTH = 2;

    private static final String DEFAULT_VALUES = "AAAAAAAAAA";
    private static final String UPDATED_VALUES = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_VALUE = "BBBBBBBBBB";

    private static final FieldGroups DEFAULT_GROUP = FieldGroups.ADMINISTRATIVE;
    private static final FieldGroups UPDATED_GROUP = FieldGroups.CLINICAL;

    @Autowired
    private RegistryFieldRepository registryFieldRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRegistryFieldMockMvc;

    private RegistryField registryField;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RegistryFieldResource registryFieldResource = new RegistryFieldResource(registryFieldRepository);
        this.restRegistryFieldMockMvc = MockMvcBuilders.standaloneSetup(registryFieldResource)
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
    public static RegistryField createEntity(EntityManager em) {
        RegistryField registryField = new RegistryField()
            .name(DEFAULT_NAME)
            .active(DEFAULT_ACTIVE)
            .mandatory(DEFAULT_MANDATORY)
            .type(DEFAULT_TYPE)
            .maxLength(DEFAULT_MAX_LENGTH)
            .values(DEFAULT_VALUES)
            .defaultValue(DEFAULT_DEFAULT_VALUE)
            .group(DEFAULT_GROUP);
        return registryField;
    }

    @Before
    public void initTest() {
        registryField = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegistryField() throws Exception {
        int databaseSizeBeforeCreate = registryFieldRepository.findAll().size();

        // Create the RegistryField
        restRegistryFieldMockMvc.perform(post("/api/registry-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registryField)))
            .andExpect(status().isCreated());

        // Validate the RegistryField in the database
        List<RegistryField> registryFieldList = registryFieldRepository.findAll();
        assertThat(registryFieldList).hasSize(databaseSizeBeforeCreate + 1);
        RegistryField testRegistryField = registryFieldList.get(registryFieldList.size() - 1);
        assertThat(testRegistryField.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRegistryField.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testRegistryField.isMandatory()).isEqualTo(DEFAULT_MANDATORY);
        assertThat(testRegistryField.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRegistryField.getMaxLength()).isEqualTo(DEFAULT_MAX_LENGTH);
        assertThat(testRegistryField.getValues()).isEqualTo(DEFAULT_VALUES);
        assertThat(testRegistryField.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
        assertThat(testRegistryField.getGroup()).isEqualTo(DEFAULT_GROUP);
    }

    @Test
    @Transactional
    public void createRegistryFieldWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = registryFieldRepository.findAll().size();

        // Create the RegistryField with an existing ID
        registryField.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistryFieldMockMvc.perform(post("/api/registry-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registryField)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<RegistryField> registryFieldList = registryFieldRepository.findAll();
        assertThat(registryFieldList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRegistryFields() throws Exception {
        // Initialize the database
        registryFieldRepository.saveAndFlush(registryField);

        // Get all the registryFieldList
        restRegistryFieldMockMvc.perform(get("/api/registry-fields?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registryField.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].mandatory").value(hasItem(DEFAULT_MANDATORY.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].maxLength").value(hasItem(DEFAULT_MAX_LENGTH)))
            .andExpect(jsonPath("$.[*].values").value(hasItem(DEFAULT_VALUES.toString())))
            .andExpect(jsonPath("$.[*].defaultValue").value(hasItem(DEFAULT_DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.toString())));
    }

    @Test
    @Transactional
    public void getRegistryField() throws Exception {
        // Initialize the database
        registryFieldRepository.saveAndFlush(registryField);

        // Get the registryField
        restRegistryFieldMockMvc.perform(get("/api/registry-fields/{id}", registryField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(registryField.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.mandatory").value(DEFAULT_MANDATORY.booleanValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.maxLength").value(DEFAULT_MAX_LENGTH))
            .andExpect(jsonPath("$.values").value(DEFAULT_VALUES.toString()))
            .andExpect(jsonPath("$.defaultValue").value(DEFAULT_DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.group").value(DEFAULT_GROUP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRegistryField() throws Exception {
        // Get the registryField
        restRegistryFieldMockMvc.perform(get("/api/registry-fields/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegistryField() throws Exception {
        // Initialize the database
        registryFieldRepository.saveAndFlush(registryField);
        int databaseSizeBeforeUpdate = registryFieldRepository.findAll().size();

        // Update the registryField
        RegistryField updatedRegistryField = registryFieldRepository.findOne(registryField.getId());
        updatedRegistryField
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE)
            .mandatory(UPDATED_MANDATORY)
            .type(UPDATED_TYPE)
            .maxLength(UPDATED_MAX_LENGTH)
            .values(UPDATED_VALUES)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .group(UPDATED_GROUP);

        restRegistryFieldMockMvc.perform(put("/api/registry-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRegistryField)))
            .andExpect(status().isOk());

        // Validate the RegistryField in the database
        List<RegistryField> registryFieldList = registryFieldRepository.findAll();
        assertThat(registryFieldList).hasSize(databaseSizeBeforeUpdate);
        RegistryField testRegistryField = registryFieldList.get(registryFieldList.size() - 1);
        assertThat(testRegistryField.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRegistryField.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testRegistryField.isMandatory()).isEqualTo(UPDATED_MANDATORY);
        assertThat(testRegistryField.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRegistryField.getMaxLength()).isEqualTo(UPDATED_MAX_LENGTH);
        assertThat(testRegistryField.getValues()).isEqualTo(UPDATED_VALUES);
        assertThat(testRegistryField.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
        assertThat(testRegistryField.getGroup()).isEqualTo(UPDATED_GROUP);
    }

    @Test
    @Transactional
    public void updateNonExistingRegistryField() throws Exception {
        int databaseSizeBeforeUpdate = registryFieldRepository.findAll().size();

        // Create the RegistryField

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRegistryFieldMockMvc.perform(put("/api/registry-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registryField)))
            .andExpect(status().isCreated());

        // Validate the RegistryField in the database
        List<RegistryField> registryFieldList = registryFieldRepository.findAll();
        assertThat(registryFieldList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRegistryField() throws Exception {
        // Initialize the database
        registryFieldRepository.saveAndFlush(registryField);
        int databaseSizeBeforeDelete = registryFieldRepository.findAll().size();

        // Get the registryField
        restRegistryFieldMockMvc.perform(delete("/api/registry-fields/{id}", registryField.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RegistryField> registryFieldList = registryFieldRepository.findAll();
        assertThat(registryFieldList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegistryField.class);
        RegistryField registryField1 = new RegistryField();
        registryField1.setId(1L);
        RegistryField registryField2 = new RegistryField();
        registryField2.setId(registryField1.getId());
        assertThat(registryField1).isEqualTo(registryField2);
        registryField2.setId(2L);
        assertThat(registryField1).isNotEqualTo(registryField2);
        registryField1.setId(null);
        assertThat(registryField1).isNotEqualTo(registryField2);
    }
}
