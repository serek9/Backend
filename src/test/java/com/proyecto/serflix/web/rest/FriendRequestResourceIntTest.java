package com.proyecto.serflix.web.rest;

import com.proyecto.serflix.SerflixApp;

import com.proyecto.serflix.domain.FriendRequest;
import com.proyecto.serflix.repository.FriendRequestRepository;

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

/**
 * Test class for the FriendRequestResource REST controller.
 *
 * @see FriendRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SerflixApp.class)
public class FriendRequestResourceIntTest {

    private static final ZonedDateTime DEFAULT_SENT_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SENT_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_RESOLVED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RESOLVED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ACCEPTED = false;
    private static final Boolean UPDATED_ACCEPTED = true;

    @Inject
    private FriendRequestRepository friendRequestRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFriendRequestMockMvc;

    private FriendRequest friendRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FriendRequestResource friendRequestResource = new FriendRequestResource();
        ReflectionTestUtils.setField(friendRequestResource, "friendRequestRepository", friendRequestRepository);
        this.restFriendRequestMockMvc = MockMvcBuilders.standaloneSetup(friendRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FriendRequest createEntity(EntityManager em) {
        FriendRequest friendRequest = new FriendRequest()
                .sentOn(DEFAULT_SENT_ON)
                .resolvedOn(DEFAULT_RESOLVED_ON)
                .accepted(DEFAULT_ACCEPTED);
        return friendRequest;
    }

    @Before
    public void initTest() {
        friendRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createFriendRequest() throws Exception {
        int databaseSizeBeforeCreate = friendRequestRepository.findAll().size();

        // Create the FriendRequest

        restFriendRequestMockMvc.perform(post("/api/friend-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(friendRequest)))
            .andExpect(status().isCreated());

        // Validate the FriendRequest in the database
        List<FriendRequest> friendRequests = friendRequestRepository.findAll();
        assertThat(friendRequests).hasSize(databaseSizeBeforeCreate + 1);
        FriendRequest testFriendRequest = friendRequests.get(friendRequests.size() - 1);
        assertThat(testFriendRequest.getSentOn()).isEqualTo(DEFAULT_SENT_ON);
        assertThat(testFriendRequest.getResolvedOn()).isEqualTo(DEFAULT_RESOLVED_ON);
        assertThat(testFriendRequest.isAccepted()).isEqualTo(DEFAULT_ACCEPTED);
    }

    @Test
    @Transactional
    public void getAllFriendRequests() throws Exception {
        // Initialize the database
        friendRequestRepository.saveAndFlush(friendRequest);

        // Get all the friendRequests
        restFriendRequestMockMvc.perform(get("/api/friend-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(friendRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].sentOn").value(hasItem(sameInstant(DEFAULT_SENT_ON))))
            .andExpect(jsonPath("$.[*].resolvedOn").value(hasItem(sameInstant(DEFAULT_RESOLVED_ON))))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())));
    }

    @Test
    @Transactional
    public void getFriendRequest() throws Exception {
        // Initialize the database
        friendRequestRepository.saveAndFlush(friendRequest);

        // Get the friendRequest
        restFriendRequestMockMvc.perform(get("/api/friend-requests/{id}", friendRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(friendRequest.getId().intValue()))
            .andExpect(jsonPath("$.sentOn").value(sameInstant(DEFAULT_SENT_ON)))
            .andExpect(jsonPath("$.resolvedOn").value(sameInstant(DEFAULT_RESOLVED_ON)))
            .andExpect(jsonPath("$.accepted").value(DEFAULT_ACCEPTED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFriendRequest() throws Exception {
        // Get the friendRequest
        restFriendRequestMockMvc.perform(get("/api/friend-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFriendRequest() throws Exception {
        // Initialize the database
        friendRequestRepository.saveAndFlush(friendRequest);
        int databaseSizeBeforeUpdate = friendRequestRepository.findAll().size();

        // Update the friendRequest
        FriendRequest updatedFriendRequest = friendRequestRepository.findOne(friendRequest.getId());
        updatedFriendRequest
                .sentOn(UPDATED_SENT_ON)
                .resolvedOn(UPDATED_RESOLVED_ON)
                .accepted(UPDATED_ACCEPTED);

        restFriendRequestMockMvc.perform(put("/api/friend-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFriendRequest)))
            .andExpect(status().isOk());

        // Validate the FriendRequest in the database
        List<FriendRequest> friendRequests = friendRequestRepository.findAll();
        assertThat(friendRequests).hasSize(databaseSizeBeforeUpdate);
        FriendRequest testFriendRequest = friendRequests.get(friendRequests.size() - 1);
        assertThat(testFriendRequest.getSentOn()).isEqualTo(UPDATED_SENT_ON);
        assertThat(testFriendRequest.getResolvedOn()).isEqualTo(UPDATED_RESOLVED_ON);
        assertThat(testFriendRequest.isAccepted()).isEqualTo(UPDATED_ACCEPTED);
    }

    @Test
    @Transactional
    public void deleteFriendRequest() throws Exception {
        // Initialize the database
        friendRequestRepository.saveAndFlush(friendRequest);
        int databaseSizeBeforeDelete = friendRequestRepository.findAll().size();

        // Get the friendRequest
        restFriendRequestMockMvc.perform(delete("/api/friend-requests/{id}", friendRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FriendRequest> friendRequests = friendRequestRepository.findAll();
        assertThat(friendRequests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
