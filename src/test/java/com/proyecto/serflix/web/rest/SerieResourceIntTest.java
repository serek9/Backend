package com.proyecto.serflix.web.rest;

import com.proyecto.serflix.SerflixApp;

import com.proyecto.serflix.domain.Serie;
import com.proyecto.serflix.repository.SerieRepository;

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

/**
 * Test class for the SerieResource REST controller.
 *
 * @see SerieResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SerflixApp.class)
public class SerieResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_ID_EXTERNAL_API = 1L;
    private static final Long UPDATED_ID_EXTERNAL_API = 2L;

    @Inject
    private SerieRepository serieRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSerieMockMvc;

    private Serie serie;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SerieResource serieResource = new SerieResource();
        ReflectionTestUtils.setField(serieResource, "serieRepository", serieRepository);
        this.restSerieMockMvc = MockMvcBuilders.standaloneSetup(serieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Serie createEntity(EntityManager em) {
        Serie serie = new Serie()
                .name(DEFAULT_NAME)
                .idExternalApi(DEFAULT_ID_EXTERNAL_API);
        return serie;
    }

    @Before
    public void initTest() {
        serie = createEntity(em);
    }

    @Test
    @Transactional
    public void createSerie() throws Exception {
        int databaseSizeBeforeCreate = serieRepository.findAll().size();

        // Create the Serie

        restSerieMockMvc.perform(post("/api/series")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serie)))
            .andExpect(status().isCreated());

        // Validate the Serie in the database
        List<Serie> series = serieRepository.findAll();
        assertThat(series).hasSize(databaseSizeBeforeCreate + 1);
        Serie testSerie = series.get(series.size() - 1);
        assertThat(testSerie.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSerie.getIdExternalApi()).isEqualTo(DEFAULT_ID_EXTERNAL_API);
    }

    @Test
    @Transactional
    public void getAllSeries() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

        // Get all the series
        restSerieMockMvc.perform(get("/api/series?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serie.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].idExternalApi").value(hasItem(DEFAULT_ID_EXTERNAL_API.intValue())));
    }

    @Test
    @Transactional
    public void getSerie() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

        // Get the serie
        restSerieMockMvc.perform(get("/api/series/{id}", serie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serie.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.idExternalApi").value(DEFAULT_ID_EXTERNAL_API.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSerie() throws Exception {
        // Get the serie
        restSerieMockMvc.perform(get("/api/series/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSerie() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);
        int databaseSizeBeforeUpdate = serieRepository.findAll().size();

        // Update the serie
        Serie updatedSerie = serieRepository.findOne(serie.getId());
        updatedSerie
                .name(UPDATED_NAME)
                .idExternalApi(UPDATED_ID_EXTERNAL_API);

        restSerieMockMvc.perform(put("/api/series")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSerie)))
            .andExpect(status().isOk());

        // Validate the Serie in the database
        List<Serie> series = serieRepository.findAll();
        assertThat(series).hasSize(databaseSizeBeforeUpdate);
        Serie testSerie = series.get(series.size() - 1);
        assertThat(testSerie.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSerie.getIdExternalApi()).isEqualTo(UPDATED_ID_EXTERNAL_API);
    }

    @Test
    @Transactional
    public void deleteSerie() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);
        int databaseSizeBeforeDelete = serieRepository.findAll().size();

        // Get the serie
        restSerieMockMvc.perform(delete("/api/series/{id}", serie.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Serie> series = serieRepository.findAll();
        assertThat(series).hasSize(databaseSizeBeforeDelete - 1);
    }
}
