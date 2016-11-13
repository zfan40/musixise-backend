package musixise.web.rest;

import musixise.MusixiseApp;
import musixise.domain.WorkListFollow;
import musixise.repository.WorkListFollowRepository;
import musixise.repository.search.WorkListFollowSearchRepository;

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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the WorkListFollowResource REST controller.
 *
 * @see WorkListFollowResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MusixiseApp.class)
@WebAppConfiguration
@IntegrationTest
public class WorkListFollowResourceIntTest {


    private static final LocalDate DEFAULT_CREATETIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATETIME = LocalDate.now(ZoneId.systemDefault());
    //private static final LocalDateTime DEFAULT_CREATETIME = LocalDateTime.now();
    //private static final LocalDateTime UPDATED_CREATETIME = LocalDateTime.now();

    @Inject
    private WorkListFollowRepository workListFollowRepository;

    @Inject
    private WorkListFollowSearchRepository workListFollowSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWorkListFollowMockMvc;

    private WorkListFollow workListFollow;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        musixise.web.rest.WorkListFollowResource workListFollowResource = new musixise.web.rest.WorkListFollowResource();
        ReflectionTestUtils.setField(workListFollowResource, "workListFollowSearchRepository", workListFollowSearchRepository);
        ReflectionTestUtils.setField(workListFollowResource, "workListFollowRepository", workListFollowRepository);
        this.restWorkListFollowMockMvc = MockMvcBuilders.standaloneSetup(workListFollowResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        workListFollowSearchRepository.deleteAll();
        workListFollow = new WorkListFollow();
        //workListFollow.setCreatetime(DEFAULT_CREATETIME);
    }

    @Test
    @Transactional
    public void createWorkListFollow() throws Exception {
        int databaseSizeBeforeCreate = workListFollowRepository.findAll().size();

        // Create the WorkListFollow

        restWorkListFollowMockMvc.perform(post("/api/work-list-follows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workListFollow)))
                .andExpect(status().isCreated());

        // Validate the WorkListFollow in the database
        List<WorkListFollow> workListFollows = workListFollowRepository.findAll();
        assertThat(workListFollows).hasSize(databaseSizeBeforeCreate + 1);
        WorkListFollow testWorkListFollow = workListFollows.get(workListFollows.size() - 1);
        //assertThat(testWorkListFollow.getCreatetime()).isEqualTo(DEFAULT_CREATETIME);

        // Validate the WorkListFollow in ElasticSearch
        WorkListFollow workListFollowEs = workListFollowSearchRepository.findOne(testWorkListFollow.getId());
        assertThat(workListFollowEs).isEqualToComparingFieldByField(testWorkListFollow);
    }

    @Test
    @Transactional
    public void checkCreatetimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = workListFollowRepository.findAll().size();
        // set the field null
        //workListFollow.setCreatetime(null);

        // Create the WorkListFollow, which fails.

        restWorkListFollowMockMvc.perform(post("/api/work-list-follows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workListFollow)))
                .andExpect(status().isBadRequest());

        List<WorkListFollow> workListFollows = workListFollowRepository.findAll();
        assertThat(workListFollows).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorkListFollows() throws Exception {
        // Initialize the database
        workListFollowRepository.saveAndFlush(workListFollow);

        // Get all the workListFollows
        restWorkListFollowMockMvc.perform(get("/api/work-list-follows?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(workListFollow.getId().intValue())))
                .andExpect(jsonPath("$.[*].createtime").value(hasItem(DEFAULT_CREATETIME.toString())));
    }

    @Test
    @Transactional
    public void getWorkListFollow() throws Exception {
        // Initialize the database
        workListFollowRepository.saveAndFlush(workListFollow);

        // Get the workListFollow
        restWorkListFollowMockMvc.perform(get("/api/work-list-follows/{id}", workListFollow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(workListFollow.getId().intValue()))
            .andExpect(jsonPath("$.createtime").value(DEFAULT_CREATETIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkListFollow() throws Exception {
        // Get the workListFollow
        restWorkListFollowMockMvc.perform(get("/api/work-list-follows/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkListFollow() throws Exception {
        // Initialize the database
        workListFollowRepository.saveAndFlush(workListFollow);
        workListFollowSearchRepository.save(workListFollow);
        int databaseSizeBeforeUpdate = workListFollowRepository.findAll().size();

        // Update the workListFollow
        WorkListFollow updatedWorkListFollow = new WorkListFollow();
        updatedWorkListFollow.setId(workListFollow.getId());
        //updatedWorkListFollow.setCreatetime(UPDATED_CREATETIME);

        restWorkListFollowMockMvc.perform(put("/api/work-list-follows")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedWorkListFollow)))
                .andExpect(status().isOk());

        // Validate the WorkListFollow in the database
        List<WorkListFollow> workListFollows = workListFollowRepository.findAll();
        assertThat(workListFollows).hasSize(databaseSizeBeforeUpdate);
        WorkListFollow testWorkListFollow = workListFollows.get(workListFollows.size() - 1);
        //assertThat(testWorkListFollow.getCreatetime()).isEqualTo(UPDATED_CREATETIME);

        // Validate the WorkListFollow in ElasticSearch
        WorkListFollow workListFollowEs = workListFollowSearchRepository.findOne(testWorkListFollow.getId());
        assertThat(workListFollowEs).isEqualToComparingFieldByField(testWorkListFollow);
    }

    @Test
    @Transactional
    public void deleteWorkListFollow() throws Exception {
        // Initialize the database
        workListFollowRepository.saveAndFlush(workListFollow);
        workListFollowSearchRepository.save(workListFollow);
        int databaseSizeBeforeDelete = workListFollowRepository.findAll().size();

        // Get the workListFollow
        restWorkListFollowMockMvc.perform(delete("/api/work-list-follows/{id}", workListFollow.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean workListFollowExistsInEs = workListFollowSearchRepository.exists(workListFollow.getId());
        assertThat(workListFollowExistsInEs).isFalse();

        // Validate the database is empty
        List<WorkListFollow> workListFollows = workListFollowRepository.findAll();
        assertThat(workListFollows).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWorkListFollow() throws Exception {
        // Initialize the database
        workListFollowRepository.saveAndFlush(workListFollow);
        workListFollowSearchRepository.save(workListFollow);

        // Search the workListFollow
        restWorkListFollowMockMvc.perform(get("/api/_search/work-list-follows?query=id:" + workListFollow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workListFollow.getId().intValue())))
            .andExpect(jsonPath("$.[*].createtime").value(hasItem(DEFAULT_CREATETIME.toString())));
    }
}
