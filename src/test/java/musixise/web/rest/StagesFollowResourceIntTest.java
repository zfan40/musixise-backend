package musixise.web.rest;

import musixise.MusixiseApp;
import musixise.domain.StagesFollow;
import musixise.repository.StagesFollowRepository;
import musixise.service.StagesFollowService;
import musixise.repository.search.StagesFollowSearchRepository;
import musixise.web.rest.dto.StagesFollowDTO;
import musixise.web.rest.mapper.StagesFollowMapper;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the StagesFollowResource REST controller.
 *
 * @see StagesFollowResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MusixiseApp.class)
@WebAppConfiguration
@IntegrationTest
public class StagesFollowResourceIntTest {


    private static final Long DEFAULT_MUSIXISER_UID = 1L;
    private static final Long UPDATED_MUSIXISER_UID = 2L;

    private static final Long DEFAULT_AUDIENCE_UID = 1L;
    private static final Long UPDATED_AUDIENCE_UID = 2L;

    private static final LocalDate DEFAULT_TIMESTAMP = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TIMESTAMP = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_STAGES_ID = 1L;
    private static final Long UPDATED_STAGES_ID = 2L;

    private static final LocalDate DEFAULT_UPDTETIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDTETIME = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private StagesFollowRepository stagesFollowRepository;

    @Inject
    private StagesFollowMapper stagesFollowMapper;

    @Inject
    private StagesFollowService stagesFollowService;

    @Inject
    private StagesFollowSearchRepository stagesFollowSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStagesFollowMockMvc;

    private StagesFollow stagesFollow;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StagesFollowResource stagesFollowResource = new StagesFollowResource();
        ReflectionTestUtils.setField(stagesFollowResource, "stagesFollowService", stagesFollowService);
        ReflectionTestUtils.setField(stagesFollowResource, "stagesFollowMapper", stagesFollowMapper);
        this.restStagesFollowMockMvc = MockMvcBuilders.standaloneSetup(stagesFollowResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stagesFollowSearchRepository.deleteAll();
        stagesFollow = new StagesFollow();
        stagesFollow.setMusixiserUid(DEFAULT_MUSIXISER_UID);
        stagesFollow.setAudienceUid(DEFAULT_AUDIENCE_UID);
        stagesFollow.setTimestamp(DEFAULT_TIMESTAMP);
        stagesFollow.setStagesId(DEFAULT_STAGES_ID);
        stagesFollow.setUpdtetime(DEFAULT_UPDTETIME);
    }

    @Test
    @Transactional
    public void createStagesFollow() throws Exception {
        int databaseSizeBeforeCreate = stagesFollowRepository.findAll().size();

        // Create the StagesFollow
        StagesFollowDTO stagesFollowDTO = stagesFollowMapper.stagesFollowToStagesFollowDTO(stagesFollow);

        restStagesFollowMockMvc.perform(post("/api/stages-follows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stagesFollowDTO)))
                .andExpect(status().isCreated());

        // Validate the StagesFollow in the database
        List<StagesFollow> stagesFollows = stagesFollowRepository.findAll();
        assertThat(stagesFollows).hasSize(databaseSizeBeforeCreate + 1);
        StagesFollow testStagesFollow = stagesFollows.get(stagesFollows.size() - 1);
        assertThat(testStagesFollow.getMusixiserUid()).isEqualTo(DEFAULT_MUSIXISER_UID);
        assertThat(testStagesFollow.getAudienceUid()).isEqualTo(DEFAULT_AUDIENCE_UID);
        assertThat(testStagesFollow.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testStagesFollow.getStagesId()).isEqualTo(DEFAULT_STAGES_ID);
        assertThat(testStagesFollow.getUpdtetime()).isEqualTo(DEFAULT_UPDTETIME);

        // Validate the StagesFollow in ElasticSearch
        StagesFollow stagesFollowEs = stagesFollowSearchRepository.findOne(testStagesFollow.getId());
        assertThat(stagesFollowEs).isEqualToComparingFieldByField(testStagesFollow);
    }

    @Test
    @Transactional
    public void getAllStagesFollows() throws Exception {
        // Initialize the database
        stagesFollowRepository.saveAndFlush(stagesFollow);

        // Get all the stagesFollows
        restStagesFollowMockMvc.perform(get("/api/stages-follows?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stagesFollow.getId().intValue())))
                .andExpect(jsonPath("$.[*].musixiserUid").value(hasItem(DEFAULT_MUSIXISER_UID.intValue())))
                .andExpect(jsonPath("$.[*].audienceUid").value(hasItem(DEFAULT_AUDIENCE_UID.intValue())))
                .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
                .andExpect(jsonPath("$.[*].stagesId").value(hasItem(DEFAULT_STAGES_ID.intValue())))
                .andExpect(jsonPath("$.[*].updtetime").value(hasItem(DEFAULT_UPDTETIME.toString())));
    }

    @Test
    @Transactional
    public void getStagesFollow() throws Exception {
        // Initialize the database
        stagesFollowRepository.saveAndFlush(stagesFollow);

        // Get the stagesFollow
        restStagesFollowMockMvc.perform(get("/api/stages-follows/{id}", stagesFollow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stagesFollow.getId().intValue()))
            .andExpect(jsonPath("$.musixiserUid").value(DEFAULT_MUSIXISER_UID.intValue()))
            .andExpect(jsonPath("$.audienceUid").value(DEFAULT_AUDIENCE_UID.intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.stagesId").value(DEFAULT_STAGES_ID.intValue()))
            .andExpect(jsonPath("$.updtetime").value(DEFAULT_UPDTETIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStagesFollow() throws Exception {
        // Get the stagesFollow
        restStagesFollowMockMvc.perform(get("/api/stages-follows/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStagesFollow() throws Exception {
        // Initialize the database
        stagesFollowRepository.saveAndFlush(stagesFollow);
        stagesFollowSearchRepository.save(stagesFollow);
        int databaseSizeBeforeUpdate = stagesFollowRepository.findAll().size();

        // Update the stagesFollow
        StagesFollow updatedStagesFollow = new StagesFollow();
        updatedStagesFollow.setId(stagesFollow.getId());
        updatedStagesFollow.setMusixiserUid(UPDATED_MUSIXISER_UID);
        updatedStagesFollow.setAudienceUid(UPDATED_AUDIENCE_UID);
        updatedStagesFollow.setTimestamp(UPDATED_TIMESTAMP);
        updatedStagesFollow.setStagesId(UPDATED_STAGES_ID);
        updatedStagesFollow.setUpdtetime(UPDATED_UPDTETIME);
        StagesFollowDTO stagesFollowDTO = stagesFollowMapper.stagesFollowToStagesFollowDTO(updatedStagesFollow);

        restStagesFollowMockMvc.perform(put("/api/stages-follows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stagesFollowDTO)))
                .andExpect(status().isOk());

        // Validate the StagesFollow in the database
        List<StagesFollow> stagesFollows = stagesFollowRepository.findAll();
        assertThat(stagesFollows).hasSize(databaseSizeBeforeUpdate);
        StagesFollow testStagesFollow = stagesFollows.get(stagesFollows.size() - 1);
        assertThat(testStagesFollow.getMusixiserUid()).isEqualTo(UPDATED_MUSIXISER_UID);
        assertThat(testStagesFollow.getAudienceUid()).isEqualTo(UPDATED_AUDIENCE_UID);
        assertThat(testStagesFollow.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testStagesFollow.getStagesId()).isEqualTo(UPDATED_STAGES_ID);
        assertThat(testStagesFollow.getUpdtetime()).isEqualTo(UPDATED_UPDTETIME);

        // Validate the StagesFollow in ElasticSearch
        StagesFollow stagesFollowEs = stagesFollowSearchRepository.findOne(testStagesFollow.getId());
        assertThat(stagesFollowEs).isEqualToComparingFieldByField(testStagesFollow);
    }

    @Test
    @Transactional
    public void deleteStagesFollow() throws Exception {
        // Initialize the database
        stagesFollowRepository.saveAndFlush(stagesFollow);
        stagesFollowSearchRepository.save(stagesFollow);
        int databaseSizeBeforeDelete = stagesFollowRepository.findAll().size();

        // Get the stagesFollow
        restStagesFollowMockMvc.perform(delete("/api/stages-follows/{id}", stagesFollow.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean stagesFollowExistsInEs = stagesFollowSearchRepository.exists(stagesFollow.getId());
        assertThat(stagesFollowExistsInEs).isFalse();

        // Validate the database is empty
        List<StagesFollow> stagesFollows = stagesFollowRepository.findAll();
        assertThat(stagesFollows).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStagesFollow() throws Exception {
        // Initialize the database
        stagesFollowRepository.saveAndFlush(stagesFollow);
        stagesFollowSearchRepository.save(stagesFollow);

        // Search the stagesFollow
        restStagesFollowMockMvc.perform(get("/api/_search/stages-follows?query=id:" + stagesFollow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stagesFollow.getId().intValue())))
            .andExpect(jsonPath("$.[*].musixiserUid").value(hasItem(DEFAULT_MUSIXISER_UID.intValue())))
            .andExpect(jsonPath("$.[*].audienceUid").value(hasItem(DEFAULT_AUDIENCE_UID.intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].stagesId").value(hasItem(DEFAULT_STAGES_ID.intValue())))
            .andExpect(jsonPath("$.[*].updtetime").value(hasItem(DEFAULT_UPDTETIME.toString())));
    }
}
