package com.pkp.web.rest;

import com.pkp.ReBaseApp;
import com.pkp.domain.Classification;
import com.pkp.repository.ClassificationRepository;
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

import com.pkp.domain.enumeration.Hands;
/**
 * Integration tests for the {@link ClassificationResource} REST controller.
 */
@SpringBootTest(classes = ReBaseApp.class)
public class ClassificationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Hands DEFAULT_HANDED = Hands.LEFT;
    private static final Hands UPDATED_HANDED = Hands.RIGHT;

    @Autowired
    private ClassificationRepository classificationRepository;

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

    private MockMvc restClassificationMockMvc;

    private Classification classification;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClassificationResource classificationResource = new ClassificationResource(classificationRepository);
        this.restClassificationMockMvc = MockMvcBuilders.standaloneSetup(classificationResource)
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
    public static Classification createEntity(EntityManager em) {
        Classification classification = new Classification()
            .name(DEFAULT_NAME)
            .handed(DEFAULT_HANDED);
        return classification;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classification createUpdatedEntity(EntityManager em) {
        Classification classification = new Classification()
            .name(UPDATED_NAME)
            .handed(UPDATED_HANDED);
        return classification;
    }

    @BeforeEach
    public void initTest() {
        classification = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassification() throws Exception {
        int databaseSizeBeforeCreate = classificationRepository.findAll().size();

        // Create the Classification
        restClassificationMockMvc.perform(post("/api/classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classification)))
            .andExpect(status().isCreated());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeCreate + 1);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClassification.getHanded()).isEqualTo(DEFAULT_HANDED);
    }

    @Test
    @Transactional
    public void createClassificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classificationRepository.findAll().size();

        // Create the Classification with an existing ID
        classification.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassificationMockMvc.perform(post("/api/classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classification)))
            .andExpect(status().isBadRequest());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = classificationRepository.findAll().size();
        // set the field null
        classification.setName(null);

        // Create the Classification, which fails.

        restClassificationMockMvc.perform(post("/api/classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classification)))
            .andExpect(status().isBadRequest());

        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHandedIsRequired() throws Exception {
        int databaseSizeBeforeTest = classificationRepository.findAll().size();
        // set the field null
        classification.setHanded(null);

        // Create the Classification, which fails.

        restClassificationMockMvc.perform(post("/api/classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classification)))
            .andExpect(status().isBadRequest());

        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClassifications() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        // Get all the classificationList
        restClassificationMockMvc.perform(get("/api/classifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classification.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].handed").value(hasItem(DEFAULT_HANDED.toString())));
    }
    
    @Test
    @Transactional
    public void getClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        // Get the classification
        restClassificationMockMvc.perform(get("/api/classifications/{id}", classification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(classification.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.handed").value(DEFAULT_HANDED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClassification() throws Exception {
        // Get the classification
        restClassificationMockMvc.perform(get("/api/classifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();

        // Update the classification
        Classification updatedClassification = classificationRepository.findById(classification.getId()).get();
        // Disconnect from session so that the updates on updatedClassification are not directly saved in db
        em.detach(updatedClassification);
        updatedClassification
            .name(UPDATED_NAME)
            .handed(UPDATED_HANDED);

        restClassificationMockMvc.perform(put("/api/classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClassification)))
            .andExpect(status().isOk());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClassification.getHanded()).isEqualTo(UPDATED_HANDED);
    }

    @Test
    @Transactional
    public void updateNonExistingClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();

        // Create the Classification

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassificationMockMvc.perform(put("/api/classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classification)))
            .andExpect(status().isBadRequest());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        int databaseSizeBeforeDelete = classificationRepository.findAll().size();

        // Delete the classification
        restClassificationMockMvc.perform(delete("/api/classifications/{id}", classification.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Classification.class);
        Classification classification1 = new Classification();
        classification1.setId(1L);
        Classification classification2 = new Classification();
        classification2.setId(classification1.getId());
        assertThat(classification1).isEqualTo(classification2);
        classification2.setId(2L);
        assertThat(classification1).isNotEqualTo(classification2);
        classification1.setId(null);
        assertThat(classification1).isNotEqualTo(classification2);
    }
}
