package musixise.web.rest;

import musixise.MusixiseApp;
import musixise.domain.Stages;
import musixise.repository.StagesRepository;
import musixise.service.StagesService;
import musixise.repository.search.StagesSearchRepository;
import musixise.web.rest.dto.StagesDTO;
import musixise.web.rest.mapper.StagesMapper;

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
 * Test class for the StagesResource REST controller.
 *
 * @see StagesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MusixiseApp.class)
@WebAppConfiguration
@IntegrationTest
public class StagesResourceIntTest {


    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final LocalDate DEFAULT_CREATETIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATETIME = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Integer DEFAULT_AUDIENCE_NUM = 1;
    private static final Integer UPDATED_AUDIENCE_NUM = 2;

    @Inject
    private StagesRepository stagesRepository;

    @Inject
    private StagesMapper stagesMapper;

    @Inject
    private StagesService stagesService;

    @Inject
    private StagesSearchRepository stagesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStagesMockMvc;

    private Stages stages;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StagesResource stagesResource = new StagesResource();
        ReflectionTestUtils.setField(stagesResource, "stagesService", stagesService);
        ReflectionTestUtils.setField(stagesResource, "stagesMapper", stagesMapper);
        this.restStagesMockMvc = MockMvcBuilders.standaloneSetup(stagesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stagesSearchRepository.deleteAll();
        stages = new Stages();
        stages.setStatus(DEFAULT_STATUS);
        stages.setCreatetime(DEFAULT_CREATETIME);
        stages.setUserId(DEFAULT_USER_ID);
        stages.setAudienceNum(DEFAULT_AUDIENCE_NUM);
    }

    @Test
    @Transactional
    public void createStages() throws Exception {
        int databaseSizeBeforeCreate = stagesRepository.findAll().size();

        // Create the Stages
        StagesDTO stagesDTO = stagesMapper.stagesToStagesDTO(stages);

        restStagesMockMvc.perform(post("/api/stages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stagesDTO)))
                .andExpect(status().isCreated());

        // Validate the Stages in the database
        List<Stages> stages = stagesRepository.findAll();
        assertThat(stages).hasSize(databaseSizeBeforeCreate + 1);
        Stages testStages = stages.get(stages.size() - 1);
        assertThat(testStages.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStages.getCreatetime()).isEqualTo(DEFAULT_CREATETIME);
        assertThat(testStages.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testStages.getAudienceNum()).isEqualTo(DEFAULT_AUDIENCE_NUM);

        // Validate the Stages in ElasticSearch
        Stages stagesEs = stagesSearchRepository.findOne(testStages.getId());
        assertThat(stagesEs).isEqualToComparingFieldByField(testStages);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stagesRepository.findAll().size();
        // set the field null
        stages.setStatus(null);

        // Create the Stages, which fails.
        StagesDTO stagesDTO = stagesMapper.stagesToStagesDTO(stages);

        restStagesMockMvc.perform(post("/api/stages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stagesDTO)))
                .andExpect(status().isBadRequest());

        List<Stages> stages = stagesRepository.findAll();
        assertThat(stages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatetimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stagesRepository.findAll().size();
        // set the field null
        stages.setCreatetime(null);

        // Create the Stages, which fails.
        StagesDTO stagesDTO = stagesMapper.stagesToStagesDTO(stages);

        restStagesMockMvc.perform(post("/api/stages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stagesDTO)))
                .andExpect(status().isBadRequest());

        List<Stages> stages = stagesRepository.findAll();
        assertThat(stages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStages() throws Exception {
        // Initialize the database
        stagesRepository.saveAndFlush(stages);

        // Get all the stages
        restStagesMockMvc.perform(get("/api/stages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stages.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
                .andExpect(jsonPath("$.[*].createtime").value(hasItem(DEFAULT_CREATETIME.toString())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
                .andExpect(jsonPath("$.[*].audienceNum").value(hasItem(DEFAULT_AUDIENCE_NUM)));
    }

    @Test
    @Transactional
    public void getStages() throws Exception {
        // Initialize the database
        stagesRepository.saveAndFlush(stages);

        // Get the stages
        restStagesMockMvc.perform(get("/api/stages/{id}", stages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stages.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createtime").value(DEFAULT_CREATETIME.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.audienceNum").value(DEFAULT_AUDIENCE_NUM));
    }

    @Test
    @Transactional
    public void getNonExistingStages() throws Exception {
        // Get the stages
        restStagesMockMvc.perform(get("/api/stages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStages() throws Exception {
        // Initialize the database
        stagesRepository.saveAndFlush(stages);
        stagesSearchRepository.save(stages);
        int databaseSizeBeforeUpdate = stagesRepository.findAll().size();

        // Update the stages
        Stages updatedStages = new Stages();
        updatedStages.setId(stages.getId());
        updatedStages.setStatus(UPDATED_STATUS);
        updatedStages.setCreatetime(UPDATED_CREATETIME);
        updatedStages.setUserId(UPDATED_USER_ID);
        updatedStages.setAudienceNum(UPDATED_AUDIENCE_NUM);
        StagesDTO stagesDTO = stagesMapper.stagesToStagesDTO(updatedStages);

        restStagesMockMvc.perform(put("/api/stages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stagesDTO)))
                .andExpect(status().isOk());

        // Validate the Stages in the database
        List<Stages> stages = stagesRepository.findAll();
        assertThat(stages).hasSize(databaseSizeBeforeUpdate);
        Stages testStages = stages.get(stages.size() - 1);
        assertThat(testStages.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStages.getCreatetime()).isEqualTo(UPDATED_CREATETIME);
        assertThat(testStages.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testStages.getAudienceNum()).isEqualTo(UPDATED_AUDIENCE_NUM);

        // Validate the Stages in ElasticSearch
        Stages stagesEs = stagesSearchRepository.findOne(testStages.getId());
        assertThat(stagesEs).isEqualToComparingFieldByField(testStages);
    }

    @Test
    @Transactional
    public void deleteStages() throws Exception {
        // Initialize the database
        stagesRepository.saveAndFlush(stages);
        stagesSearchRepository.save(stages);
        int databaseSizeBeforeDelete = stagesRepository.findAll().size();

        // Get the stages
        restStagesMockMvc.perform(delete("/api/stages/{id}", stages.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean stagesExistsInEs = stagesSearchRepository.exists(stages.getId());
        assertThat(stagesExistsInEs).isFalse();

        // Validate the database is empty
        List<Stages> stages = stagesRepository.findAll();
        assertThat(stages).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStages() throws Exception {
        // Initialize the database
        stagesRepository.saveAndFlush(stages);
        stagesSearchRepository.save(stages);

        // Search the stages
        restStagesMockMvc.perform(get("/api/_search/stages?query=id:" + stages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stages.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createtime").value(hasItem(DEFAULT_CREATETIME.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].audienceNum").value(hasItem(DEFAULT_AUDIENCE_NUM)));
    }
}
