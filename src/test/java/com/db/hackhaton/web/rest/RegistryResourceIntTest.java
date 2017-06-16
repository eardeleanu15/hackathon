package com.db.hackhaton.web.rest;

import com.db.hackhaton.AppApp;

import com.db.hackhaton.domain.Registry;
import com.db.hackhaton.repository.RegistryRepository;
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
 * Test class for the RegistryResource REST controller.
 *
 * @see RegistryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class RegistryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATEDON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATEDON = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_ARCHIVED = false;
    private static final Boolean UPDATED_ARCHIVED = true;

    @Autowired
    private RegistryRepository registryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRegistryMockMvc;

    private Registry registry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RegistryResource registryResource = new RegistryResource(registryRepository);
        this.restRegistryMockMvc = MockMvcBuilders.standaloneSetup(registryResource)
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
    public static Registry createEntity(EntityManager em) {
        Registry registry = new Registry()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdon(DEFAULT_CREATEDON)
            .active(DEFAULT_ACTIVE)
            .archived(DEFAULT_ARCHIVED);
        return registry;
    }

    @Before
    public void initTest() {
        registry = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegistry() throws Exception {
        int databaseSizeBeforeCreate = registryRepository.findAll().size();

        // Create the Registry
        restRegistryMockMvc.perform(post("/api/registries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registry)))
            .andExpect(status().isCreated());

        // Validate the Registry in the database
        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeCreate + 1);
        Registry testRegistry = registryList.get(registryList.size() - 1);
        assertThat(testRegistry.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRegistry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRegistry.getCreatedon()).isEqualTo(DEFAULT_CREATEDON);
        assertThat(testRegistry.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testRegistry.isArchived()).isEqualTo(DEFAULT_ARCHIVED);
    }

    @Test
    @Transactional
    public void createRegistryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = registryRepository.findAll().size();

        // Create the Registry with an existing ID
        registry.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistryMockMvc.perform(post("/api/registries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registry)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRegistries() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get all the registryList
        restRegistryMockMvc.perform(get("/api/registries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registry.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].createdon").value(hasItem(DEFAULT_CREATEDON.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].archived").value(hasItem(DEFAULT_ARCHIVED.booleanValue())));
    }

    @Test
    @Transactional
    public void getRegistry() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);

        // Get the registry
        restRegistryMockMvc.perform(get("/api/registries/{id}", registry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(registry.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdon").value(DEFAULT_CREATEDON.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.archived").value(DEFAULT_ARCHIVED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRegistry() throws Exception {
        // Get the registry
        restRegistryMockMvc.perform(get("/api/registries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegistry() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);
        int databaseSizeBeforeUpdate = registryRepository.findAll().size();

        // Update the registry
        Registry updatedRegistry = registryRepository.findOne(registry.getId());
        updatedRegistry
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdon(UPDATED_CREATEDON)
            .active(UPDATED_ACTIVE)
            .archived(UPDATED_ARCHIVED);

        restRegistryMockMvc.perform(put("/api/registries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRegistry)))
            .andExpect(status().isOk());

        // Validate the Registry in the database
        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeUpdate);
        Registry testRegistry = registryList.get(registryList.size() - 1);
        assertThat(testRegistry.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRegistry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRegistry.getCreatedon()).isEqualTo(UPDATED_CREATEDON);
        assertThat(testRegistry.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testRegistry.isArchived()).isEqualTo(UPDATED_ARCHIVED);
    }

    @Test
    @Transactional
    public void updateNonExistingRegistry() throws Exception {
        int databaseSizeBeforeUpdate = registryRepository.findAll().size();

        // Create the Registry

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRegistryMockMvc.perform(put("/api/registries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(registry)))
            .andExpect(status().isCreated());

        // Validate the Registry in the database
        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRegistry() throws Exception {
        // Initialize the database
        registryRepository.saveAndFlush(registry);
        int databaseSizeBeforeDelete = registryRepository.findAll().size();

        // Get the registry
        restRegistryMockMvc.perform(delete("/api/registries/{id}", registry.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Registry> registryList = registryRepository.findAll();
        assertThat(registryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Registry.class);
        Registry registry1 = new Registry();
        registry1.setId(1L);
        Registry registry2 = new Registry();
        registry2.setId(registry1.getId());
        assertThat(registry1).isEqualTo(registry2);
        registry2.setId(2L);
        assertThat(registry1).isNotEqualTo(registry2);
        registry1.setId(null);
        assertThat(registry1).isNotEqualTo(registry2);
    }
}
