package com.pkp.web.rest;

import com.pkp.ReBaseApp;
import com.pkp.domain.Alien;
import com.pkp.repository.AlienRepository;
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
 * Integration tests for the {@link AlienResource} REST controller.
 */
@SpringBootTest(classes = ReBaseApp.class)
public class AlienResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIES = "AAAAAAAAAA";
    private static final String UPDATED_SPECIES = "BBBBBBBBBB";

    private static final String DEFAULT_PLANET = "AAAAAAAAAA";
    private static final String UPDATED_PLANET = "BBBBBBBBBB";

    @Autowired
    private AlienRepository alienRepository;

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

    private MockMvc restAlienMockMvc;

    private Alien alien;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AlienResource alienResource = new AlienResource(alienRepository);
        this.restAlienMockMvc = MockMvcBuilders.standaloneSetup(alienResource)
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
    public static Alien createEntity(EntityManager em) {
        Alien alien = new Alien()
            .name(DEFAULT_NAME)
            .species(DEFAULT_SPECIES)
            .planet(DEFAULT_PLANET);
        return alien;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alien createUpdatedEntity(EntityManager em) {
        Alien alien = new Alien()
            .name(UPDATED_NAME)
            .species(UPDATED_SPECIES)
            .planet(UPDATED_PLANET);
        return alien;
    }

    @BeforeEach
    public void initTest() {
        alien = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlien() throws Exception {
        int databaseSizeBeforeCreate = alienRepository.findAll().size();

        // Create the Alien
        restAlienMockMvc.perform(post("/api/aliens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alien)))
            .andExpect(status().isCreated());

        // Validate the Alien in the database
        List<Alien> alienList = alienRepository.findAll();
        assertThat(alienList).hasSize(databaseSizeBeforeCreate + 1);
        Alien testAlien = alienList.get(alienList.size() - 1);
        assertThat(testAlien.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAlien.getSpecies()).isEqualTo(DEFAULT_SPECIES);
        assertThat(testAlien.getPlanet()).isEqualTo(DEFAULT_PLANET);
    }

    @Test
    @Transactional
    public void createAlienWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = alienRepository.findAll().size();

        // Create the Alien with an existing ID
        alien.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlienMockMvc.perform(post("/api/aliens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alien)))
            .andExpect(status().isBadRequest());

        // Validate the Alien in the database
        List<Alien> alienList = alienRepository.findAll();
        assertThat(alienList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = alienRepository.findAll().size();
        // set the field null
        alien.setName(null);

        // Create the Alien, which fails.

        restAlienMockMvc.perform(post("/api/aliens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alien)))
            .andExpect(status().isBadRequest());

        List<Alien> alienList = alienRepository.findAll();
        assertThat(alienList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSpeciesIsRequired() throws Exception {
        int databaseSizeBeforeTest = alienRepository.findAll().size();
        // set the field null
        alien.setSpecies(null);

        // Create the Alien, which fails.

        restAlienMockMvc.perform(post("/api/aliens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alien)))
            .andExpect(status().isBadRequest());

        List<Alien> alienList = alienRepository.findAll();
        assertThat(alienList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPlanetIsRequired() throws Exception {
        int databaseSizeBeforeTest = alienRepository.findAll().size();
        // set the field null
        alien.setPlanet(null);

        // Create the Alien, which fails.

        restAlienMockMvc.perform(post("/api/aliens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alien)))
            .andExpect(status().isBadRequest());

        List<Alien> alienList = alienRepository.findAll();
        assertThat(alienList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAliens() throws Exception {
        // Initialize the database
        alienRepository.saveAndFlush(alien);

        // Get all the alienList
        restAlienMockMvc.perform(get("/api/aliens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alien.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].species").value(hasItem(DEFAULT_SPECIES)))
            .andExpect(jsonPath("$.[*].planet").value(hasItem(DEFAULT_PLANET)));
    }
    
    @Test
    @Transactional
    public void getAlien() throws Exception {
        // Initialize the database
        alienRepository.saveAndFlush(alien);

        // Get the alien
        restAlienMockMvc.perform(get("/api/aliens/{id}", alien.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(alien.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.species").value(DEFAULT_SPECIES))
            .andExpect(jsonPath("$.planet").value(DEFAULT_PLANET));
    }

    @Test
    @Transactional
    public void getNonExistingAlien() throws Exception {
        // Get the alien
        restAlienMockMvc.perform(get("/api/aliens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlien() throws Exception {
        // Initialize the database
        alienRepository.saveAndFlush(alien);

        int databaseSizeBeforeUpdate = alienRepository.findAll().size();

        // Update the alien
        Alien updatedAlien = alienRepository.findById(alien.getId()).get();
        // Disconnect from session so that the updates on updatedAlien are not directly saved in db
        em.detach(updatedAlien);
        updatedAlien
            .name(UPDATED_NAME)
            .species(UPDATED_SPECIES)
            .planet(UPDATED_PLANET);

        restAlienMockMvc.perform(put("/api/aliens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAlien)))
            .andExpect(status().isOk());

        // Validate the Alien in the database
        List<Alien> alienList = alienRepository.findAll();
        assertThat(alienList).hasSize(databaseSizeBeforeUpdate);
        Alien testAlien = alienList.get(alienList.size() - 1);
        assertThat(testAlien.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAlien.getSpecies()).isEqualTo(UPDATED_SPECIES);
        assertThat(testAlien.getPlanet()).isEqualTo(UPDATED_PLANET);
    }

    @Test
    @Transactional
    public void updateNonExistingAlien() throws Exception {
        int databaseSizeBeforeUpdate = alienRepository.findAll().size();

        // Create the Alien

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlienMockMvc.perform(put("/api/aliens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alien)))
            .andExpect(status().isBadRequest());

        // Validate the Alien in the database
        List<Alien> alienList = alienRepository.findAll();
        assertThat(alienList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAlien() throws Exception {
        // Initialize the database
        alienRepository.saveAndFlush(alien);

        int databaseSizeBeforeDelete = alienRepository.findAll().size();

        // Delete the alien
        restAlienMockMvc.perform(delete("/api/aliens/{id}", alien.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Alien> alienList = alienRepository.findAll();
        assertThat(alienList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alien.class);
        Alien alien1 = new Alien();
        alien1.setId(1L);
        Alien alien2 = new Alien();
        alien2.setId(alien1.getId());
        assertThat(alien1).isEqualTo(alien2);
        alien2.setId(2L);
        assertThat(alien1).isNotEqualTo(alien2);
        alien1.setId(null);
        assertThat(alien1).isNotEqualTo(alien2);
    }
}
