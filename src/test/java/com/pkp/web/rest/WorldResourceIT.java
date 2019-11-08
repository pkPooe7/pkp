package com.pkp.web.rest;

import com.pkp.ReBaseApp;
import com.pkp.domain.World;
import com.pkp.repository.WorldRepository;
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
 * Integration tests for the {@link WorldResource} REST controller.
 */
@SpringBootTest(classes = ReBaseApp.class)
public class WorldResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SYSTEM = "AAAAAAAAAA";
    private static final String UPDATED_SYSTEM = "BBBBBBBBBB";

    @Autowired
    private WorldRepository worldRepository;

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

    private MockMvc restWorldMockMvc;

    private World world;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WorldResource worldResource = new WorldResource(worldRepository);
        this.restWorldMockMvc = MockMvcBuilders.standaloneSetup(worldResource)
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
    public static World createEntity(EntityManager em) {
        World world = new World()
            .name(DEFAULT_NAME)
            .system(DEFAULT_SYSTEM);
        return world;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static World createUpdatedEntity(EntityManager em) {
        World world = new World()
            .name(UPDATED_NAME)
            .system(UPDATED_SYSTEM);
        return world;
    }

    @BeforeEach
    public void initTest() {
        world = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorld() throws Exception {
        int databaseSizeBeforeCreate = worldRepository.findAll().size();

        // Create the World
        restWorldMockMvc.perform(post("/api/worlds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(world)))
            .andExpect(status().isCreated());

        // Validate the World in the database
        List<World> worldList = worldRepository.findAll();
        assertThat(worldList).hasSize(databaseSizeBeforeCreate + 1);
        World testWorld = worldList.get(worldList.size() - 1);
        assertThat(testWorld.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorld.getSystem()).isEqualTo(DEFAULT_SYSTEM);
    }

    @Test
    @Transactional
    public void createWorldWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = worldRepository.findAll().size();

        // Create the World with an existing ID
        world.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorldMockMvc.perform(post("/api/worlds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(world)))
            .andExpect(status().isBadRequest());

        // Validate the World in the database
        List<World> worldList = worldRepository.findAll();
        assertThat(worldList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = worldRepository.findAll().size();
        // set the field null
        world.setName(null);

        // Create the World, which fails.

        restWorldMockMvc.perform(post("/api/worlds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(world)))
            .andExpect(status().isBadRequest());

        List<World> worldList = worldRepository.findAll();
        assertThat(worldList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSystemIsRequired() throws Exception {
        int databaseSizeBeforeTest = worldRepository.findAll().size();
        // set the field null
        world.setSystem(null);

        // Create the World, which fails.

        restWorldMockMvc.perform(post("/api/worlds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(world)))
            .andExpect(status().isBadRequest());

        List<World> worldList = worldRepository.findAll();
        assertThat(worldList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorlds() throws Exception {
        // Initialize the database
        worldRepository.saveAndFlush(world);

        // Get all the worldList
        restWorldMockMvc.perform(get("/api/worlds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(world.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].system").value(hasItem(DEFAULT_SYSTEM)));
    }
    
    @Test
    @Transactional
    public void getWorld() throws Exception {
        // Initialize the database
        worldRepository.saveAndFlush(world);

        // Get the world
        restWorldMockMvc.perform(get("/api/worlds/{id}", world.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(world.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.system").value(DEFAULT_SYSTEM));
    }

    @Test
    @Transactional
    public void getNonExistingWorld() throws Exception {
        // Get the world
        restWorldMockMvc.perform(get("/api/worlds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorld() throws Exception {
        // Initialize the database
        worldRepository.saveAndFlush(world);

        int databaseSizeBeforeUpdate = worldRepository.findAll().size();

        // Update the world
        World updatedWorld = worldRepository.findById(world.getId()).get();
        // Disconnect from session so that the updates on updatedWorld are not directly saved in db
        em.detach(updatedWorld);
        updatedWorld
            .name(UPDATED_NAME)
            .system(UPDATED_SYSTEM);

        restWorldMockMvc.perform(put("/api/worlds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWorld)))
            .andExpect(status().isOk());

        // Validate the World in the database
        List<World> worldList = worldRepository.findAll();
        assertThat(worldList).hasSize(databaseSizeBeforeUpdate);
        World testWorld = worldList.get(worldList.size() - 1);
        assertThat(testWorld.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorld.getSystem()).isEqualTo(UPDATED_SYSTEM);
    }

    @Test
    @Transactional
    public void updateNonExistingWorld() throws Exception {
        int databaseSizeBeforeUpdate = worldRepository.findAll().size();

        // Create the World

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorldMockMvc.perform(put("/api/worlds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(world)))
            .andExpect(status().isBadRequest());

        // Validate the World in the database
        List<World> worldList = worldRepository.findAll();
        assertThat(worldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWorld() throws Exception {
        // Initialize the database
        worldRepository.saveAndFlush(world);

        int databaseSizeBeforeDelete = worldRepository.findAll().size();

        // Delete the world
        restWorldMockMvc.perform(delete("/api/worlds/{id}", world.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<World> worldList = worldRepository.findAll();
        assertThat(worldList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(World.class);
        World world1 = new World();
        world1.setId(1L);
        World world2 = new World();
        world2.setId(world1.getId());
        assertThat(world1).isEqualTo(world2);
        world2.setId(2L);
        assertThat(world1).isNotEqualTo(world2);
        world1.setId(null);
        assertThat(world1).isNotEqualTo(world2);
    }
}
