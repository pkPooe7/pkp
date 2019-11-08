package com.pkp.web.rest;

import com.pkp.ReBaseApp;
import com.pkp.domain.Technology;
import com.pkp.repository.TechnologyRepository;
import com.pkp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.pkp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TechnologyResource} REST controller.
 */
@SpringBootTest(classes = ReBaseApp.class)
public class TechnologyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_AQUIRED = false;
    private static final Boolean UPDATED_AQUIRED = true;

    private static final String DEFAULT_SPECI = "AAAAAAAAAA";
    private static final String UPDATED_SPECI = "BBBBBBBBBB";

    @Autowired
    private TechnologyRepository technologyRepository;

    @Mock
    private TechnologyRepository technologyRepositoryMock;

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

    private MockMvc restTechnologyMockMvc;

    private Technology technology;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TechnologyResource technologyResource = new TechnologyResource(technologyRepository);
        this.restTechnologyMockMvc = MockMvcBuilders.standaloneSetup(technologyResource)
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
    public static Technology createEntity(EntityManager em) {
        Technology technology = new Technology()
            .name(DEFAULT_NAME)
            .aquired(DEFAULT_AQUIRED)
            .speci(DEFAULT_SPECI);
        return technology;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Technology createUpdatedEntity(EntityManager em) {
        Technology technology = new Technology()
            .name(UPDATED_NAME)
            .aquired(UPDATED_AQUIRED)
            .speci(UPDATED_SPECI);
        return technology;
    }

    @BeforeEach
    public void initTest() {
        technology = createEntity(em);
    }

    @Test
    @Transactional
    public void createTechnology() throws Exception {
        int databaseSizeBeforeCreate = technologyRepository.findAll().size();

        // Create the Technology
        restTechnologyMockMvc.perform(post("/api/technologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(technology)))
            .andExpect(status().isCreated());

        // Validate the Technology in the database
        List<Technology> technologyList = technologyRepository.findAll();
        assertThat(technologyList).hasSize(databaseSizeBeforeCreate + 1);
        Technology testTechnology = technologyList.get(technologyList.size() - 1);
        assertThat(testTechnology.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTechnology.isAquired()).isEqualTo(DEFAULT_AQUIRED);
        assertThat(testTechnology.getSpeci()).isEqualTo(DEFAULT_SPECI);
    }

    @Test
    @Transactional
    public void createTechnologyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = technologyRepository.findAll().size();

        // Create the Technology with an existing ID
        technology.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechnologyMockMvc.perform(post("/api/technologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(technology)))
            .andExpect(status().isBadRequest());

        // Validate the Technology in the database
        List<Technology> technologyList = technologyRepository.findAll();
        assertThat(technologyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = technologyRepository.findAll().size();
        // set the field null
        technology.setName(null);

        // Create the Technology, which fails.

        restTechnologyMockMvc.perform(post("/api/technologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(technology)))
            .andExpect(status().isBadRequest());

        List<Technology> technologyList = technologyRepository.findAll();
        assertThat(technologyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAquiredIsRequired() throws Exception {
        int databaseSizeBeforeTest = technologyRepository.findAll().size();
        // set the field null
        technology.setAquired(null);

        // Create the Technology, which fails.

        restTechnologyMockMvc.perform(post("/api/technologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(technology)))
            .andExpect(status().isBadRequest());

        List<Technology> technologyList = technologyRepository.findAll();
        assertThat(technologyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSpeciIsRequired() throws Exception {
        int databaseSizeBeforeTest = technologyRepository.findAll().size();
        // set the field null
        technology.setSpeci(null);

        // Create the Technology, which fails.

        restTechnologyMockMvc.perform(post("/api/technologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(technology)))
            .andExpect(status().isBadRequest());

        List<Technology> technologyList = technologyRepository.findAll();
        assertThat(technologyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTechnologies() throws Exception {
        // Initialize the database
        technologyRepository.saveAndFlush(technology);

        // Get all the technologyList
        restTechnologyMockMvc.perform(get("/api/technologies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(technology.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].aquired").value(hasItem(DEFAULT_AQUIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].speci").value(hasItem(DEFAULT_SPECI)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllTechnologiesWithEagerRelationshipsIsEnabled() throws Exception {
        TechnologyResource technologyResource = new TechnologyResource(technologyRepositoryMock);
        when(technologyRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restTechnologyMockMvc = MockMvcBuilders.standaloneSetup(technologyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTechnologyMockMvc.perform(get("/api/technologies?eagerload=true"))
        .andExpect(status().isOk());

        verify(technologyRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTechnologiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        TechnologyResource technologyResource = new TechnologyResource(technologyRepositoryMock);
            when(technologyRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restTechnologyMockMvc = MockMvcBuilders.standaloneSetup(technologyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTechnologyMockMvc.perform(get("/api/technologies?eagerload=true"))
        .andExpect(status().isOk());

            verify(technologyRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getTechnology() throws Exception {
        // Initialize the database
        technologyRepository.saveAndFlush(technology);

        // Get the technology
        restTechnologyMockMvc.perform(get("/api/technologies/{id}", technology.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(technology.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.aquired").value(DEFAULT_AQUIRED.booleanValue()))
            .andExpect(jsonPath("$.speci").value(DEFAULT_SPECI));
    }

    @Test
    @Transactional
    public void getNonExistingTechnology() throws Exception {
        // Get the technology
        restTechnologyMockMvc.perform(get("/api/technologies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTechnology() throws Exception {
        // Initialize the database
        technologyRepository.saveAndFlush(technology);

        int databaseSizeBeforeUpdate = technologyRepository.findAll().size();

        // Update the technology
        Technology updatedTechnology = technologyRepository.findById(technology.getId()).get();
        // Disconnect from session so that the updates on updatedTechnology are not directly saved in db
        em.detach(updatedTechnology);
        updatedTechnology
            .name(UPDATED_NAME)
            .aquired(UPDATED_AQUIRED)
            .speci(UPDATED_SPECI);

        restTechnologyMockMvc.perform(put("/api/technologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTechnology)))
            .andExpect(status().isOk());

        // Validate the Technology in the database
        List<Technology> technologyList = technologyRepository.findAll();
        assertThat(technologyList).hasSize(databaseSizeBeforeUpdate);
        Technology testTechnology = technologyList.get(technologyList.size() - 1);
        assertThat(testTechnology.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTechnology.isAquired()).isEqualTo(UPDATED_AQUIRED);
        assertThat(testTechnology.getSpeci()).isEqualTo(UPDATED_SPECI);
    }

    @Test
    @Transactional
    public void updateNonExistingTechnology() throws Exception {
        int databaseSizeBeforeUpdate = technologyRepository.findAll().size();

        // Create the Technology

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechnologyMockMvc.perform(put("/api/technologies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(technology)))
            .andExpect(status().isBadRequest());

        // Validate the Technology in the database
        List<Technology> technologyList = technologyRepository.findAll();
        assertThat(technologyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTechnology() throws Exception {
        // Initialize the database
        technologyRepository.saveAndFlush(technology);

        int databaseSizeBeforeDelete = technologyRepository.findAll().size();

        // Delete the technology
        restTechnologyMockMvc.perform(delete("/api/technologies/{id}", technology.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Technology> technologyList = technologyRepository.findAll();
        assertThat(technologyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Technology.class);
        Technology technology1 = new Technology();
        technology1.setId(1L);
        Technology technology2 = new Technology();
        technology2.setId(technology1.getId());
        assertThat(technology1).isEqualTo(technology2);
        technology2.setId(2L);
        assertThat(technology1).isNotEqualTo(technology2);
        technology1.setId(null);
        assertThat(technology1).isNotEqualTo(technology2);
    }
}
