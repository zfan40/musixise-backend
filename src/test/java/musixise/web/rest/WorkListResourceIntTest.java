package musixise.web.rest;

import musixise.MusixiseApp;
import musixise.domain.WorkList;
import musixise.repository.WorkListRepository;
import musixise.service.WorkListService;
import musixise.repository.search.WorkListSearchRepository;
import musixise.web.rest.dto.WorkListDTO;
import musixise.web.rest.mapper.WorkListMapper;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the WorkListResource REST controller.
 *
 * @see WorkListResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MusixiseApp.class)
@WebAppConfiguration
@IntegrationTest
public class WorkListResourceIntTest {


    private static final String DEFAULT_CONTENT = "";
    private static final String UPDATED_CONTENT = "";
    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    private static final LocalDate DEFAULT_CREATETIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATETIME = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    @Inject
    private WorkListRepository workListRepository;

    @Inject
    private WorkListMapper workListMapper;

    @Inject
    private WorkListService workListService;

    @Inject
    private WorkListSearchRepository workListSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWorkListMockMvc;

    private WorkList workList;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkListResource workListResource = new WorkListResource();
        ReflectionTestUtils.setField(workListResource, "workListService", workListService);
        ReflectionTestUtils.setField(workListResource, "workListMapper", workListMapper);
        this.restWorkListMockMvc = MockMvcBuilders.standaloneSetup(workListResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        workListSearchRepository.deleteAll();
        workList = new WorkList();
        workList.setContent(DEFAULT_CONTENT);
        workList.setUrl(DEFAULT_URL);
        workList.setCreatetime(DEFAULT_CREATETIME);
        workList.setUserId(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    public void createWorkList() throws Exception {
        int databaseSizeBeforeCreate = workListRepository.findAll().size();

        // Create the WorkList
        WorkListDTO workListDTO = workListMapper.workListToWorkListDTO(workList);

        restWorkListMockMvc.perform(post("/api/work-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workListDTO)))
                .andExpect(status().isCreated());

        // Validate the WorkList in the database
        List<WorkList> workLists = workListRepository.findAll();
        assertThat(workLists).hasSize(databaseSizeBeforeCreate + 1);
        WorkList testWorkList = workLists.get(workLists.size() - 1);
        assertThat(testWorkList.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testWorkList.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testWorkList.getCreatetime()).isEqualTo(DEFAULT_CREATETIME);
        assertThat(testWorkList.getUserId()).isEqualTo(DEFAULT_USER_ID);

        // Validate the WorkList in ElasticSearch
        WorkList workListEs = workListSearchRepository.findOne(testWorkList.getId());
        assertThat(workListEs).isEqualToComparingFieldByField(testWorkList);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = workListRepository.findAll().size();
        // set the field null
        workList.setContent(null);

        // Create the WorkList, which fails.
        WorkListDTO workListDTO = workListMapper.workListToWorkListDTO(workList);

        restWorkListMockMvc.perform(post("/api/work-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workListDTO)))
                .andExpect(status().isBadRequest());

        List<WorkList> workLists = workListRepository.findAll();
        assertThat(workLists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatetimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = workListRepository.findAll().size();
        // set the field null
        workList.setCreatetime(null);

        // Create the WorkList, which fails.
        WorkListDTO workListDTO = workListMapper.workListToWorkListDTO(workList);

        restWorkListMockMvc.perform(post("/api/work-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workListDTO)))
                .andExpect(status().isBadRequest());

        List<WorkList> workLists = workListRepository.findAll();
        assertThat(workLists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorkLists() throws Exception {
        // Initialize the database
        workListRepository.saveAndFlush(workList);

        // Get all the workLists
        restWorkListMockMvc.perform(get("/api/work-lists?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(workList.getId().intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].createtime").value(hasItem(DEFAULT_CREATETIME.toString())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    public void getWorkList() throws Exception {
        // Initialize the database
        workListRepository.saveAndFlush(workList);

        // Get the workList
        restWorkListMockMvc.perform(get("/api/work-lists/{id}", workList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(workList.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.createtime").value(DEFAULT_CREATETIME.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkList() throws Exception {
        // Get the workList
        restWorkListMockMvc.perform(get("/api/work-lists/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkList() throws Exception {
        // Initialize the database
        workListRepository.saveAndFlush(workList);
        workListSearchRepository.save(workList);
        int databaseSizeBeforeUpdate = workListRepository.findAll().size();

        // Update the workList
        WorkList updatedWorkList = new WorkList();
        updatedWorkList.setId(workList.getId());
        updatedWorkList.setContent(UPDATED_CONTENT);
        updatedWorkList.setUrl(UPDATED_URL);
        updatedWorkList.setCreatetime(UPDATED_CREATETIME);
        updatedWorkList.setUserId(UPDATED_USER_ID);
        WorkListDTO workListDTO = workListMapper.workListToWorkListDTO(updatedWorkList);

        restWorkListMockMvc.perform(put("/api/work-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workListDTO)))
                .andExpect(status().isOk());

        // Validate the WorkList in the database
        List<WorkList> workLists = workListRepository.findAll();
        assertThat(workLists).hasSize(databaseSizeBeforeUpdate);
        WorkList testWorkList = workLists.get(workLists.size() - 1);
        assertThat(testWorkList.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testWorkList.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testWorkList.getCreatetime()).isEqualTo(UPDATED_CREATETIME);
        assertThat(testWorkList.getUserId()).isEqualTo(UPDATED_USER_ID);

        // Validate the WorkList in ElasticSearch
        WorkList workListEs = workListSearchRepository.findOne(testWorkList.getId());
        assertThat(workListEs).isEqualToComparingFieldByField(testWorkList);
    }

    @Test
    @Transactional
    public void deleteWorkList() throws Exception {
        // Initialize the database
        workListRepository.saveAndFlush(workList);
        workListSearchRepository.save(workList);
        int databaseSizeBeforeDelete = workListRepository.findAll().size();

        // Get the workList
        restWorkListMockMvc.perform(delete("/api/work-lists/{id}", workList.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean workListExistsInEs = workListSearchRepository.exists(workList.getId());
        assertThat(workListExistsInEs).isFalse();

        // Validate the database is empty
        List<WorkList> workLists = workListRepository.findAll();
        assertThat(workLists).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWorkList() throws Exception {
        // Initialize the database
        workListRepository.saveAndFlush(workList);
        workListSearchRepository.save(workList);

        // Search the workList
        restWorkListMockMvc.perform(get("/api/_search/work-lists?query=id:" + workList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workList.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].createtime").value(hasItem(DEFAULT_CREATETIME.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }
}
