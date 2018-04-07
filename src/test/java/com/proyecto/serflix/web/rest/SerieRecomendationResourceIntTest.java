package com.proyecto.serflix.web.rest;

import com.proyecto.serflix.SerflixApp;

import com.proyecto.serflix.domain.SerieRecomendation;
import com.proyecto.serflix.repository.SerieRecomendationRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.proyecto.serflix.domain.enumeration.RecomendationResult;
/**
 * Test class for the SerieRecomendationResource REST controller.
 *
 * @see SerieRecomendationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SerflixApp.class)
public class SerieRecomendationResourceIntTest {

    private static final RecomendationResult DEFAULT_RECOMENDATION_RESULT = RecomendationResult.PRESELECTED;
    private static final RecomendationResult UPDATED_RECOMENDATION_RESULT = RecomendationResult.ACCEPTED;

    @Inject
    private SerieRecomendationRepository serieRecomendationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSerieRecomendationMockMvc;

    private SerieRecomendation serieRecomendation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SerieRecomendationResource serieRecomendationResource = new SerieRecomendationResource();
        ReflectionTestUtils.setField(serieRecomendationResource, "serieRecomendationRepository", serieRecomendationRepository);
        this.restSerieRecomendationMockMvc = MockMvcBuilders.standaloneSetup(serieRecomendationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SerieRecomendation createEntity(EntityManager em) {
        SerieRecomendation serieRecomendation = new SerieRecomendation()
                .recomendationResult(DEFAULT_RECOMENDATION_RESULT);
        return serieRecomendation;
    }

    @Before
    public void initTest() {
        serieRecomendation = createEntity(em);
    }

    @Test
    @Transactional
    public void createSerieRecomendation() throws Exception {
        int databaseSizeBeforeCreate = serieRecomendationRepository.findAll().size();

        // Create the SerieRecomendation

        restSerieRecomendationMockMvc.perform(post("/api/serie-recomendations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serieRecomendation)))
            .andExpect(status().isCreated());

        // Validate the SerieRecomendation in the database
        List<SerieRecomendation> serieRecomendations = serieRecomendationRepository.findAll();
        assertThat(serieRecomendations).hasSize(databaseSizeBeforeCreate + 1);
        SerieRecomendation testSerieRecomendation = serieRecomendations.get(serieRecomendations.size() - 1);
        assertThat(testSerieRecomendation.getRecomendationResult()).isEqualTo(DEFAULT_RECOMENDATION_RESULT);
    }

    @Test
    @Transactional
    public void getAllSerieRecomendations() throws Exception {
        // Initialize the database
        serieRecomendationRepository.saveAndFlush(serieRecomendation);

        // Get all the serieRecomendations
        restSerieRecomendationMockMvc.perform(get("/api/serie-recomendations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serieRecomendation.getId().intValue())))
            .andExpect(jsonPath("$.[*].recomendationResult").value(hasItem(DEFAULT_RECOMENDATION_RESULT.toString())));
    }

    @Test
    @Transactional
    public void getSerieRecomendation() throws Exception {
        // Initialize the database
        serieRecomendationRepository.saveAndFlush(serieRecomendation);

        // Get the serieRecomendation
        restSerieRecomendationMockMvc.perform(get("/api/serie-recomendations/{id}", serieRecomendation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serieRecomendation.getId().intValue()))
            .andExpect(jsonPath("$.recomendationResult").value(DEFAULT_RECOMENDATION_RESULT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSerieRecomendation() throws Exception {
        // Get the serieRecomendation
        restSerieRecomendationMockMvc.perform(get("/api/serie-recomendations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSerieRecomendation() throws Exception {
        // Initialize the database
        serieRecomendationRepository.saveAndFlush(serieRecomendation);
        int databaseSizeBeforeUpdate = serieRecomendationRepository.findAll().size();

        // Update the serieRecomendation
        SerieRecomendation updatedSerieRecomendation = serieRecomendationRepository.findOne(serieRecomendation.getId());
        updatedSerieRecomendation
                .recomendationResult(UPDATED_RECOMENDATION_RESULT);

        restSerieRecomendationMockMvc.perform(put("/api/serie-recomendations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSerieRecomendation)))
            .andExpect(status().isOk());

        // Validate the SerieRecomendation in the database
        List<SerieRecomendation> serieRecomendations = serieRecomendationRepository.findAll();
        assertThat(serieRecomendations).hasSize(databaseSizeBeforeUpdate);
        SerieRecomendation testSerieRecomendation = serieRecomendations.get(serieRecomendations.size() - 1);
        assertThat(testSerieRecomendation.getRecomendationResult()).isEqualTo(UPDATED_RECOMENDATION_RESULT);
    }

    @Test
    @Transactional
    public void deleteSerieRecomendation() throws Exception {
        // Initialize the database
        serieRecomendationRepository.saveAndFlush(serieRecomendation);
        int databaseSizeBeforeDelete = serieRecomendationRepository.findAll().size();

        // Get the serieRecomendation
        restSerieRecomendationMockMvc.perform(delete("/api/serie-recomendations/{id}", serieRecomendation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SerieRecomendation> serieRecomendations = serieRecomendationRepository.findAll();
        assertThat(serieRecomendations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
