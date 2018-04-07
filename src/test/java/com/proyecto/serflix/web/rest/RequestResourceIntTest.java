package com.proyecto.serflix.web.rest;

import com.proyecto.serflix.SerflixApp;

import com.proyecto.serflix.domain.Request;
import com.proyecto.serflix.repository.RequestRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.proyecto.serflix.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.proyecto.serflix.domain.enumeration.Type;
import com.proyecto.serflix.domain.enumeration.Company;
/**
 * Test class for the RequestResource REST controller.
 *
 * @see RequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SerflixApp.class)
public class RequestResourceIntTest {

    private static final Type DEFAULT_TYPE = Type.MOVIE;
    private static final Type UPDATED_TYPE = Type.SERIES;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_VIEW_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VIEW_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Company DEFAULT_COMPANY = Company.ALONE;
    private static final Company UPDATED_COMPANY = Company.FRIENDS;

    @Inject
    private RequestRepository requestRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRequestMockMvc;

    private Request request;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RequestResource requestResource = new RequestResource();
        ReflectionTestUtils.setField(requestResource, "requestRepository", requestRepository);
        this.restRequestMockMvc = MockMvcBuilders.standaloneSetup(requestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Request createEntity(EntityManager em) {
        Request request = new Request()
                .type(DEFAULT_TYPE)
                .name(DEFAULT_NAME)
                .viewDate(DEFAULT_VIEW_DATE)
                .creationDate(DEFAULT_CREATION_DATE)
                .company(DEFAULT_COMPANY);
        return request;
    }

    @Before
    public void initTest() {
        request = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequest() throws Exception {
        int databaseSizeBeforeCreate = requestRepository.findAll().size();

        // Create the Request

        restRequestMockMvc.perform(post("/api/requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isCreated());

        // Validate the Request in the database
        List<Request> requests = requestRepository.findAll();
        assertThat(requests).hasSize(databaseSizeBeforeCreate + 1);
        Request testRequest = requests.get(requests.size() - 1);
        assertThat(testRequest.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRequest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRequest.getViewDate()).isEqualTo(DEFAULT_VIEW_DATE);
        assertThat(testRequest.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testRequest.getCompany()).isEqualTo(DEFAULT_COMPANY);
    }

    @Test
    @Transactional
    public void getAllRequests() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requests
        restRequestMockMvc.perform(get("/api/requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(request.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].viewDate").value(hasItem(sameInstant(DEFAULT_VIEW_DATE))))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY.toString())));
    }

    @Test
    @Transactional
    public void getRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get the request
        restRequestMockMvc.perform(get("/api/requests/{id}", request.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(request.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.viewDate").value(sameInstant(DEFAULT_VIEW_DATE)))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRequest() throws Exception {
        // Get the request
        restRequestMockMvc.perform(get("/api/requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);
        int databaseSizeBeforeUpdate = requestRepository.findAll().size();

        // Update the request
        Request updatedRequest = requestRepository.findOne(request.getId());
        updatedRequest
                .type(UPDATED_TYPE)
                .name(UPDATED_NAME)
                .viewDate(UPDATED_VIEW_DATE)
                .creationDate(UPDATED_CREATION_DATE)
                .company(UPDATED_COMPANY);

        restRequestMockMvc.perform(put("/api/requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRequest)))
            .andExpect(status().isOk());

        // Validate the Request in the database
        List<Request> requests = requestRepository.findAll();
        assertThat(requests).hasSize(databaseSizeBeforeUpdate);
        Request testRequest = requests.get(requests.size() - 1);
        assertThat(testRequest.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRequest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRequest.getViewDate()).isEqualTo(UPDATED_VIEW_DATE);
        assertThat(testRequest.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testRequest.getCompany()).isEqualTo(UPDATED_COMPANY);
    }

    @Test
    @Transactional
    public void deleteRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);
        int databaseSizeBeforeDelete = requestRepository.findAll().size();

        // Get the request
        restRequestMockMvc.perform(delete("/api/requests/{id}", request.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Request> requests = requestRepository.findAll();
        assertThat(requests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
