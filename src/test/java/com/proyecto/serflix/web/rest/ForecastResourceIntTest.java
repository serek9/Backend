package com.proyecto.serflix.web.rest;

import com.proyecto.serflix.SerflixApp;

import com.proyecto.serflix.domain.Forecast;
import com.proyecto.serflix.repository.ForecastRepository;

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

import com.proyecto.serflix.domain.enumeration.Weather;
/**
 * Test class for the ForecastResource REST controller.
 *
 * @see ForecastResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SerflixApp.class)
public class ForecastResourceIntTest {

    private static final Double DEFAULT_TEMPERATURE = 1D;
    private static final Double UPDATED_TEMPERATURE = 2D;

    private static final Weather DEFAULT_WEATHER = Weather.CLEAR;
    private static final Weather UPDATED_WEATHER = Weather.RAIN;

    @Inject
    private ForecastRepository forecastRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restForecastMockMvc;

    private Forecast forecast;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ForecastResource forecastResource = new ForecastResource();
        ReflectionTestUtils.setField(forecastResource, "forecastRepository", forecastRepository);
        this.restForecastMockMvc = MockMvcBuilders.standaloneSetup(forecastResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Forecast createEntity(EntityManager em) {
        Forecast forecast = new Forecast()
                .temperature(DEFAULT_TEMPERATURE)
                .weather(DEFAULT_WEATHER);
        return forecast;
    }

    @Before
    public void initTest() {
        forecast = createEntity(em);
    }

    @Test
    @Transactional
    public void createForecast() throws Exception {
        int databaseSizeBeforeCreate = forecastRepository.findAll().size();

        // Create the Forecast

        restForecastMockMvc.perform(post("/api/forecasts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(forecast)))
            .andExpect(status().isCreated());

        // Validate the Forecast in the database
        List<Forecast> forecasts = forecastRepository.findAll();
        assertThat(forecasts).hasSize(databaseSizeBeforeCreate + 1);
        Forecast testForecast = forecasts.get(forecasts.size() - 1);
        assertThat(testForecast.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testForecast.getWeather()).isEqualTo(DEFAULT_WEATHER);
    }

    @Test
    @Transactional
    public void getAllForecasts() throws Exception {
        // Initialize the database
        forecastRepository.saveAndFlush(forecast);

        // Get all the forecasts
        restForecastMockMvc.perform(get("/api/forecasts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(forecast.getId().intValue())))
            .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE.doubleValue())))
            .andExpect(jsonPath("$.[*].weather").value(hasItem(DEFAULT_WEATHER.toString())));
    }

    @Test
    @Transactional
    public void getForecast() throws Exception {
        // Initialize the database
        forecastRepository.saveAndFlush(forecast);

        // Get the forecast
        restForecastMockMvc.perform(get("/api/forecasts/{id}", forecast.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(forecast.getId().intValue()))
            .andExpect(jsonPath("$.temperature").value(DEFAULT_TEMPERATURE.doubleValue()))
            .andExpect(jsonPath("$.weather").value(DEFAULT_WEATHER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingForecast() throws Exception {
        // Get the forecast
        restForecastMockMvc.perform(get("/api/forecasts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateForecast() throws Exception {
        // Initialize the database
        forecastRepository.saveAndFlush(forecast);
        int databaseSizeBeforeUpdate = forecastRepository.findAll().size();

        // Update the forecast
        Forecast updatedForecast = forecastRepository.findOne(forecast.getId());
        updatedForecast
                .temperature(UPDATED_TEMPERATURE)
                .weather(UPDATED_WEATHER);

        restForecastMockMvc.perform(put("/api/forecasts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedForecast)))
            .andExpect(status().isOk());

        // Validate the Forecast in the database
        List<Forecast> forecasts = forecastRepository.findAll();
        assertThat(forecasts).hasSize(databaseSizeBeforeUpdate);
        Forecast testForecast = forecasts.get(forecasts.size() - 1);
        assertThat(testForecast.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testForecast.getWeather()).isEqualTo(UPDATED_WEATHER);
    }

    @Test
    @Transactional
    public void deleteForecast() throws Exception {
        // Initialize the database
        forecastRepository.saveAndFlush(forecast);
        int databaseSizeBeforeDelete = forecastRepository.findAll().size();

        // Get the forecast
        restForecastMockMvc.perform(delete("/api/forecasts/{id}", forecast.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Forecast> forecasts = forecastRepository.findAll();
        assertThat(forecasts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
