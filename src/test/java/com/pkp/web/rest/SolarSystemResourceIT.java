package com.pkp.web.rest;

import com.pkp.ReBaseApp;
import com.pkp.domain.SolarSystem;
import com.pkp.repository.SolarSystemRepository;
import com.pkp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.pkp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SolarSystemResource} REST controller.
 */
@SpringBootTest(classes = ReBaseApp.class)
public class SolarSystemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GALAXY = "AAAAAAAAAA";
    private static final String UPDATED_GALAXY = "BBBBBBBBBB";

    @Autowired
    private SolarSystemRepository solarSystemRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSolarSystemMockMvc;

    private SolarSystem solarSystem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SolarSystemResource solarSystemResource = new SolarSystemResource(solarSystemRepository);
        this.restSolarSystemMockMvc = MockMvcBuilders.standaloneSetup(solarSystemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SolarSystem createEntity(EntityManager em) {
        SolarSystem solarSystem = new SolarSystem()
            .name(DEFAULT_NAME)
            .galaxy(DEFAULT_GALAXY);
        return solarSystem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SolarSystem createUpdatedEntity(EntityManager em) {
        SolarSystem solarSystem = new SolarSystem()
            .name(UPDATED_NAME)
            .galaxy(UPDATED_GALAXY);
        return solarSystem;
    }

    @BeforeEach
    public void initTest() {
        solarSystem = createEntity(em);
    }

    @Test
    @Transactional
    public void createSolarSystem() throws Exception {
        int databaseSizeBeforeCreate = solarSystemRepository.findAll().size();

        // Create the SolarSystem
        restSolarSystemMockMvc.perform(post("/api/solar-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solarSystem)))
            .andExpect(status().isCreated());

        // Validate the SolarSystem in the database
        List<SolarSystem> solarSystemList = solarSystemRepository.findAll();
        assertThat(solarSystemList).hasSize(databaseSizeBeforeCreate + 1);
        SolarSystem testSolarSystem = solarSystemList.get(solarSystemList.size() - 1);
        assertThat(testSolarSystem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSolarSystem.getGalaxy()).isEqualTo(DEFAULT_GALAXY);
    }

    @Test
    @Transactional
    public void createSolarSystemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = solarSystemRepository.findAll().size();

        // Create the SolarSystem with an existing ID
        solarSystem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSolarSystemMockMvc.perform(post("/api/solar-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solarSystem)))
            .andExpect(status().isBadRequest());

        // Validate the SolarSystem in the database
        List<SolarSystem> solarSystemList = solarSystemRepository.findAll();
        assertThat(solarSystemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = solarSystemRepository.findAll().size();
        // set the field null
        solarSystem.setName(null);

        // Create the SolarSystem, which fails.

        restSolarSystemMockMvc.perform(post("/api/solar-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solarSystem)))
            .andExpect(status().isBadRequest());

        List<SolarSystem> solarSystemList = solarSystemRepository.findAll();
        assertThat(solarSystemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGalaxyIsRequired() throws Exception {
        int databaseSizeBeforeTest = solarSystemRepository.findAll().size();
        // set the field null
        solarSystem.setGalaxy(null);

        // Create the SolarSystem, which fails.

        restSolarSystemMockMvc.perform(post("/api/solar-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solarSystem)))
            .andExpect(status().isBadRequest());

        List<SolarSystem> solarSystemList = solarSystemRepository.findAll();
        assertThat(solarSystemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSolarSystems() throws Exception {
        // Initialize the database
        solarSystemRepository.saveAndFlush(solarSystem);

        // Get all the solarSystemList
        restSolarSystemMockMvc.perform(get("/api/solar-systems?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solarSystem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].galaxy").value(hasItem(DEFAULT_GALAXY)));
    }
    
    @Test
    @Transactional
    public void getSolarSystem() throws Exception {
        // Initialize the database
        solarSystemRepository.saveAndFlush(solarSystem);

        // Get the solarSystem
        restSolarSystemMockMvc.perform(get("/api/solar-systems/{id}", solarSystem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(solarSystem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.galaxy").value(DEFAULT_GALAXY));
    }

    @Test
    @Transactional
    public void getNonExistingSolarSystem() throws Exception {
        // Get the solarSystem
        restSolarSystemMockMvc.perform(get("/api/solar-systems/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSolarSystem() throws Exception {
        // Initialize the database
        solarSystemRepository.saveAndFlush(solarSystem);

        int databaseSizeBeforeUpdate = solarSystemRepository.findAll().size();

        // Update the solarSystem
        SolarSystem updatedSolarSystem = solarSystemRepository.findById(solarSystem.getId()).get();
        // Disconnect from session so that the updates on updatedSolarSystem are not directly saved in db
        em.detach(updatedSolarSystem);
        updatedSolarSystem
            .name(UPDATED_NAME)
            .galaxy(UPDATED_GALAXY);

        restSolarSystemMockMvc.perform(put("/api/solar-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSolarSystem)))
            .andExpect(status().isOk());

        // Validate the SolarSystem in the database
        List<SolarSystem> solarSystemList = solarSystemRepository.findAll();
        assertThat(solarSystemList).hasSize(databaseSizeBeforeUpdate);
        SolarSystem testSolarSystem = solarSystemList.get(solarSystemList.size() - 1);
        assertThat(testSolarSystem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSolarSystem.getGalaxy()).isEqualTo(UPDATED_GALAXY);
    }

    @Test
    @Transactional
    public void updateNonExistingSolarSystem() throws Exception {
        int databaseSizeBeforeUpdate = solarSystemRepository.findAll().size();

        // Create the SolarSystem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSolarSystemMockMvc.perform(put("/api/solar-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solarSystem)))
            .andExpect(status().isBadRequest());

        // Validate the SolarSystem in the database
        List<SolarSystem> solarSystemList = solarSystemRepository.findAll();
        assertThat(solarSystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSolarSystem() throws Exception {
        // Initialize the database
        solarSystemRepository.saveAndFlush(solarSystem);

        int databaseSizeBeforeDelete = solarSystemRepository.findAll().size();

        // Delete the solarSystem
        restSolarSystemMockMvc.perform(delete("/api/solar-systems/{id}", solarSystem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SolarSystem> solarSystemList = solarSystemRepository.findAll();
        assertThat(solarSystemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SolarSystem.class);
        SolarSystem solarSystem1 = new SolarSystem();
        solarSystem1.setId(1L);
        SolarSystem solarSystem2 = new SolarSystem();
        solarSystem2.setId(solarSystem1.getId());
        assertThat(solarSystem1).isEqualTo(solarSystem2);
        solarSystem2.setId(2L);
        assertThat(solarSystem1).isNotEqualTo(solarSystem2);
        solarSystem1.setId(null);
        assertThat(solarSystem1).isNotEqualTo(solarSystem2);
    }
}
