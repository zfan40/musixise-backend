package musixise.web.rest;

import musixise.MusixiseApp;
import musixise.domain.MusixiserFollow;
import musixise.repository.MusixiserFollowRepository;
import musixise.service.MusixiserFollowService;
import musixise.repository.search.MusixiserFollowSearchRepository;
import musixise.web.rest.admin.MusixiserFollowResource;
import musixise.web.rest.dto.MusixiserFollowDTO;
import musixise.web.rest.mapper.MusixiserFollowMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MusixiserFollowResource REST controller.
 *
 * @see MusixiserFollowResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MusixiseApp.class)
@WebAppConfiguration
@IntegrationTest
public class MusixiserFollowResourceIntTest {


    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_FOLLOW_UID = 1L;
    private static final Long UPDATED_FOLLOW_UID = 2L;

    @Inject
    private MusixiserFollowRepository musixiserFollowRepository;

    @Inject
    private MusixiserFollowMapper musixiserFollowMapper;

    @Inject
    private MusixiserFollowService musixiserFollowService;

    @Inject
    private MusixiserFollowSearchRepository musixiserFollowSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMusixiserFollowMockMvc;

    private MusixiserFollow musixiserFollow;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MusixiserFollowResource musixiserFollowResource = new MusixiserFollowResource();
        ReflectionTestUtils.setField(musixiserFollowResource, "musixiserFollowService", musixiserFollowService);
        ReflectionTestUtils.setField(musixiserFollowResource, "musixiserFollowMapper", musixiserFollowMapper);
        this.restMusixiserFollowMockMvc = MockMvcBuilders.standaloneSetup(musixiserFollowResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        musixiserFollowSearchRepository.deleteAll();
        musixiserFollow = new MusixiserFollow();
        musixiserFollow.setUserId(DEFAULT_USER_ID);
        //musixiserFollow.setFollowUid(DEFAULT_FOLLOW_UID);
    }

    @Test
    @Transactional
    public void createMusixiserFollow() throws Exception {
        int databaseSizeBeforeCreate = musixiserFollowRepository.findAll().size();

        // Create the MusixiserFollow
        MusixiserFollowDTO musixiserFollowDTO = musixiserFollowMapper.musixiserFollowToMusixiserFollowDTO(musixiserFollow);

        restMusixiserFollowMockMvc.perform(post("/api/musixiser-follows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(musixiserFollowDTO)))
                .andExpect(status().isCreated());

        // Validate the MusixiserFollow in the database
        List<MusixiserFollow> musixiserFollows = musixiserFollowRepository.findAll();
        assertThat(musixiserFollows).hasSize(databaseSizeBeforeCreate + 1);
        MusixiserFollow testMusixiserFollow = musixiserFollows.get(musixiserFollows.size() - 1);
        assertThat(testMusixiserFollow.getUserId()).isEqualTo(DEFAULT_USER_ID);
       // assertThat(testMusixiserFollow.getFollowUid()).isEqualTo(DEFAULT_FOLLOW_UID);

        // Validate the MusixiserFollow in ElasticSearch
        MusixiserFollow musixiserFollowEs = musixiserFollowSearchRepository.findOne(testMusixiserFollow.getId());
        assertThat(musixiserFollowEs).isEqualToComparingFieldByField(testMusixiserFollow);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = musixiserFollowRepository.findAll().size();
        // set the field null
        musixiserFollow.setUserId(null);

        // Create the MusixiserFollow, which fails.
        MusixiserFollowDTO musixiserFollowDTO = musixiserFollowMapper.musixiserFollowToMusixiserFollowDTO(musixiserFollow);

        restMusixiserFollowMockMvc.perform(post("/api/musixiser-follows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(musixiserFollowDTO)))
                .andExpect(status().isBadRequest());

        List<MusixiserFollow> musixiserFollows = musixiserFollowRepository.findAll();
        assertThat(musixiserFollows).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFollowUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = musixiserFollowRepository.findAll().size();
        // set the field null
       // musixiserFollow.setFollowUid(null);

        // Create the MusixiserFollow, which fails.
        MusixiserFollowDTO musixiserFollowDTO = musixiserFollowMapper.musixiserFollowToMusixiserFollowDTO(musixiserFollow);

        restMusixiserFollowMockMvc.perform(post("/api/musixiser-follows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(musixiserFollowDTO)))
                .andExpect(status().isBadRequest());

        List<MusixiserFollow> musixiserFollows = musixiserFollowRepository.findAll();
        assertThat(musixiserFollows).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMusixiserFollows() throws Exception {
        // Initialize the database
        musixiserFollowRepository.saveAndFlush(musixiserFollow);

        // Get all the musixiserFollows
        restMusixiserFollowMockMvc.perform(get("/api/musixiser-follows?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(musixiserFollow.getId().intValue())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
                .andExpect(jsonPath("$.[*].followUid").value(hasItem(DEFAULT_FOLLOW_UID.intValue())));
    }

    @Test
    @Transactional
    public void getMusixiserFollow() throws Exception {
        // Initialize the database
        musixiserFollowRepository.saveAndFlush(musixiserFollow);

        // Get the musixiserFollow
        restMusixiserFollowMockMvc.perform(get("/api/musixiser-follows/{id}", musixiserFollow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(musixiserFollow.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.followUid").value(DEFAULT_FOLLOW_UID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMusixiserFollow() throws Exception {
        // Get the musixiserFollow
        restMusixiserFollowMockMvc.perform(get("/api/musixiser-follows/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMusixiserFollow() throws Exception {
        // Initialize the database
        musixiserFollowRepository.saveAndFlush(musixiserFollow);
        musixiserFollowSearchRepository.save(musixiserFollow);
        int databaseSizeBeforeUpdate = musixiserFollowRepository.findAll().size();

        // Update the musixiserFollow
        MusixiserFollow updatedMusixiserFollow = new MusixiserFollow();
        updatedMusixiserFollow.setId(musixiserFollow.getId());
        updatedMusixiserFollow.setUserId(UPDATED_USER_ID);
       // updatedMusixiserFollow.setFollowUid(UPDATED_FOLLOW_UID);
        MusixiserFollowDTO musixiserFollowDTO = musixiserFollowMapper.musixiserFollowToMusixiserFollowDTO(updatedMusixiserFollow);

        restMusixiserFollowMockMvc.perform(put("/api/musixiser-follows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(musixiserFollowDTO)))
                .andExpect(status().isOk());

        // Validate the MusixiserFollow in the database
        List<MusixiserFollow> musixiserFollows = musixiserFollowRepository.findAll();
        assertThat(musixiserFollows).hasSize(databaseSizeBeforeUpdate);
        MusixiserFollow testMusixiserFollow = musixiserFollows.get(musixiserFollows.size() - 1);
        assertThat(testMusixiserFollow.getUserId()).isEqualTo(UPDATED_USER_ID);
       // assertThat(testMusixiserFollow.getFollowUid()).isEqualTo(UPDATED_FOLLOW_UID);

        // Validate the MusixiserFollow in ElasticSearch
        MusixiserFollow musixiserFollowEs = musixiserFollowSearchRepository.findOne(testMusixiserFollow.getId());
        assertThat(musixiserFollowEs).isEqualToComparingFieldByField(testMusixiserFollow);
    }

    @Test
    @Transactional
    public void deleteMusixiserFollow() throws Exception {
        // Initialize the database
        musixiserFollowRepository.saveAndFlush(musixiserFollow);
        musixiserFollowSearchRepository.save(musixiserFollow);
        int databaseSizeBeforeDelete = musixiserFollowRepository.findAll().size();

        // Get the musixiserFollow
        restMusixiserFollowMockMvc.perform(delete("/api/musixiser-follows/{id}", musixiserFollow.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean musixiserFollowExistsInEs = musixiserFollowSearchRepository.exists(musixiserFollow.getId());
        assertThat(musixiserFollowExistsInEs).isFalse();

        // Validate the database is empty
        List<MusixiserFollow> musixiserFollows = musixiserFollowRepository.findAll();
        assertThat(musixiserFollows).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMusixiserFollow() throws Exception {
        // Initialize the database
        musixiserFollowRepository.saveAndFlush(musixiserFollow);
        musixiserFollowSearchRepository.save(musixiserFollow);

        // Search the musixiserFollow
        restMusixiserFollowMockMvc.perform(get("/api/_search/musixiser-follows?query=id:" + musixiserFollow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(musixiserFollow.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].followUid").value(hasItem(DEFAULT_FOLLOW_UID.intValue())));
    }
}
