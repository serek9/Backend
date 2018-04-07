package com.proyecto.serflix.web.rest;

import com.proyecto.serflix.SerflixApp;

import com.proyecto.serflix.domain.MovieRecomendation;
import com.proyecto.serflix.repository.MovieRecomendationRepository;

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
 * Test class for the MovieRecomendationResource REST controller.
 *
 * @see MovieRecomendationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SerflixApp.class)
public class MovieRecomendationResourceIntTest {

    private static final RecomendationResult DEFAULT_RECOMENDATION_RESULT = RecomendationResult.PRESELECTED;
    private static final RecomendationResult UPDATED_RECOMENDATION_RESULT = RecomendationResult.ACCEPTED;

    @Inject
    private MovieRecomendationRepository movieRecomendationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMovieRecomendationMockMvc;

    private MovieRecomendation movieRecomendation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MovieRecomendationResource movieRecomendationResource = new MovieRecomendationResource();
        ReflectionTestUtils.setField(movieRecomendationResource, "movieRecomendationRepository", movieRecomendationRepository);
        this.restMovieRecomendationMockMvc = MockMvcBuilders.standaloneSetup(movieRecomendationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MovieRecomendation createEntity(EntityManager em) {
        MovieRecomendation movieRecomendation = new MovieRecomendation()
                .recomendationResult(DEFAULT_RECOMENDATION_RESULT);
        return movieRecomendation;
    }

    @Before
    public void initTest() {
        movieRecomendation = createEntity(em);
    }

    @Test
    @Transactional
    public void createMovieRecomendation() throws Exception {
        int databaseSizeBeforeCreate = movieRecomendationRepository.findAll().size();

        // Create the MovieRecomendation

        restMovieRecomendationMockMvc.perform(post("/api/movie-recomendations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(movieRecomendation)))
            .andExpect(status().isCreated());

        // Validate the MovieRecomendation in the database
        List<MovieRecomendation> movieRecomendations = movieRecomendationRepository.findAll();
        assertThat(movieRecomendations).hasSize(databaseSizeBeforeCreate + 1);
        MovieRecomendation testMovieRecomendation = movieRecomendations.get(movieRecomendations.size() - 1);
        assertThat(testMovieRecomendation.getRecomendationResult()).isEqualTo(DEFAULT_RECOMENDATION_RESULT);
    }

    @Test
    @Transactional
    public void getAllMovieRecomendations() throws Exception {
        // Initialize the database
        movieRecomendationRepository.saveAndFlush(movieRecomendation);

        // Get all the movieRecomendations
        restMovieRecomendationMockMvc.perform(get("/api/movie-recomendations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movieRecomendation.getId().intValue())))
            .andExpect(jsonPath("$.[*].recomendationResult").value(hasItem(DEFAULT_RECOMENDATION_RESULT.toString())));
    }

    @Test
    @Transactional
    public void getMovieRecomendation() throws Exception {
        // Initialize the database
        movieRecomendationRepository.saveAndFlush(movieRecomendation);

        // Get the movieRecomendation
        restMovieRecomendationMockMvc.perform(get("/api/movie-recomendations/{id}", movieRecomendation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(movieRecomendation.getId().intValue()))
            .andExpect(jsonPath("$.recomendationResult").value(DEFAULT_RECOMENDATION_RESULT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMovieRecomendation() throws Exception {
        // Get the movieRecomendation
        restMovieRecomendationMockMvc.perform(get("/api/movie-recomendations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMovieRecomendation() throws Exception {
        // Initialize the database
        movieRecomendationRepository.saveAndFlush(movieRecomendation);
        int databaseSizeBeforeUpdate = movieRecomendationRepository.findAll().size();

        // Update the movieRecomendation
        MovieRecomendation updatedMovieRecomendation = movieRecomendationRepository.findOne(movieRecomendation.getId());
        updatedMovieRecomendation
                .recomendationResult(UPDATED_RECOMENDATION_RESULT);

        restMovieRecomendationMockMvc.perform(put("/api/movie-recomendations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMovieRecomendation)))
            .andExpect(status().isOk());

        // Validate the MovieRecomendation in the database
        List<MovieRecomendation> movieRecomendations = movieRecomendationRepository.findAll();
        assertThat(movieRecomendations).hasSize(databaseSizeBeforeUpdate);
        MovieRecomendation testMovieRecomendation = movieRecomendations.get(movieRecomendations.size() - 1);
        assertThat(testMovieRecomendation.getRecomendationResult()).isEqualTo(UPDATED_RECOMENDATION_RESULT);
    }

    @Test
    @Transactional
    public void deleteMovieRecomendation() throws Exception {
        // Initialize the database
        movieRecomendationRepository.saveAndFlush(movieRecomendation);
        int databaseSizeBeforeDelete = movieRecomendationRepository.findAll().size();

        // Get the movieRecomendation
        restMovieRecomendationMockMvc.perform(delete("/api/movie-recomendations/{id}", movieRecomendation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MovieRecomendation> movieRecomendations = movieRecomendationRepository.findAll();
        assertThat(movieRecomendations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
