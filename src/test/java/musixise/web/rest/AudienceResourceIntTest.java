package musixise.web.rest;

import musixise.MusixiseApp;
import musixise.domain.Audience;
import musixise.repository.AudienceRepository;
import musixise.service.AudienceService;
import musixise.repository.search.AudienceSearchRepository;
import musixise.web.rest.dto.AudienceDTO;
import musixise.web.rest.mapper.AudienceMapper;

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
 * Test class for the AudienceResource REST controller.
 *
 * @see AudienceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MusixiseApp.class)
@WebAppConfiguration
@IntegrationTest
public class AudienceResourceIntTest {

    private static final String DEFAULT_NICKNAME = "AAAAA";
    private static final String UPDATED_NICKNAME = "BBBBB";
    private static final String DEFAULT_REALNAME = "AAAAA";
    private static final String UPDATED_REALNAME = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_TEL = "AAAAA";
    private static final String UPDATED_TEL = "BBBBB";

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    @Inject
    private AudienceRepository audienceRepository;

    @Inject
    private AudienceMapper audienceMapper;

    @Inject
    private AudienceService audienceService;

    @Inject
    private AudienceSearchRepository audienceSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAudienceMockMvc;

    private Audience audience;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AudienceResource audienceResource = new AudienceResource();
        ReflectionTestUtils.setField(audienceResource, "audienceService", audienceService);
        ReflectionTestUtils.setField(audienceResource, "audienceMapper", audienceMapper);
        this.restAudienceMockMvc = MockMvcBuilders.standaloneSetup(audienceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        audienceSearchRepository.deleteAll();
        audience = new Audience();
        audience.setNickname(DEFAULT_NICKNAME);
        audience.setRealname(DEFAULT_REALNAME);
        audience.setEmail(DEFAULT_EMAIL);
        audience.setTel(DEFAULT_TEL);
        audience.setUserId(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    public void createAudience() throws Exception {
        int databaseSizeBeforeCreate = audienceRepository.findAll().size();

        // Create the Audience
        AudienceDTO audienceDTO = audienceMapper.audienceToAudienceDTO(audience);

        restAudienceMockMvc.perform(post("/api/audiences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(audienceDTO)))
                .andExpect(status().isCreated());

        // Validate the Audience in the database
        List<Audience> audiences = audienceRepository.findAll();
        assertThat(audiences).hasSize(databaseSizeBeforeCreate + 1);
        Audience testAudience = audiences.get(audiences.size() - 1);
        assertThat(testAudience.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testAudience.getRealname()).isEqualTo(DEFAULT_REALNAME);
        assertThat(testAudience.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAudience.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testAudience.getUserId()).isEqualTo(DEFAULT_USER_ID);

        // Validate the Audience in ElasticSearch
        Audience audienceEs = audienceSearchRepository.findOne(testAudience.getId());
        assertThat(audienceEs).isEqualToComparingFieldByField(testAudience);
    }

    @Test
    @Transactional
    public void getAllAudiences() throws Exception {
        // Initialize the database
        audienceRepository.saveAndFlush(audience);

        // Get all the audiences
        restAudienceMockMvc.perform(get("/api/audiences?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(audience.getId().intValue())))
                .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME.toString())))
                .andExpect(jsonPath("$.[*].realname").value(hasItem(DEFAULT_REALNAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    public void getAudience() throws Exception {
        // Initialize the database
        audienceRepository.saveAndFlush(audience);

        // Get the audience
        restAudienceMockMvc.perform(get("/api/audiences/{id}", audience.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(audience.getId().intValue()))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME.toString()))
            .andExpect(jsonPath("$.realname").value(DEFAULT_REALNAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAudience() throws Exception {
        // Get the audience
        restAudienceMockMvc.perform(get("/api/audiences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAudience() throws Exception {
        // Initialize the database
        audienceRepository.saveAndFlush(audience);
        audienceSearchRepository.save(audience);
        int databaseSizeBeforeUpdate = audienceRepository.findAll().size();

        // Update the audience
        Audience updatedAudience = new Audience();
        updatedAudience.setId(audience.getId());
        updatedAudience.setNickname(UPDATED_NICKNAME);
        updatedAudience.setRealname(UPDATED_REALNAME);
        updatedAudience.setEmail(UPDATED_EMAIL);
        updatedAudience.setTel(UPDATED_TEL);
        updatedAudience.setUserId(UPDATED_USER_ID);
        AudienceDTO audienceDTO = audienceMapper.audienceToAudienceDTO(updatedAudience);

        restAudienceMockMvc.perform(put("/api/audiences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(audienceDTO)))
                .andExpect(status().isOk());

        // Validate the Audience in the database
        List<Audience> audiences = audienceRepository.findAll();
        assertThat(audiences).hasSize(databaseSizeBeforeUpdate);
        Audience testAudience = audiences.get(audiences.size() - 1);
        assertThat(testAudience.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testAudience.getRealname()).isEqualTo(UPDATED_REALNAME);
        assertThat(testAudience.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAudience.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testAudience.getUserId()).isEqualTo(UPDATED_USER_ID);

        // Validate the Audience in ElasticSearch
        Audience audienceEs = audienceSearchRepository.findOne(testAudience.getId());
        assertThat(audienceEs).isEqualToComparingFieldByField(testAudience);
    }

    @Test
    @Transactional
    public void deleteAudience() throws Exception {
        // Initialize the database
        audienceRepository.saveAndFlush(audience);
        audienceSearchRepository.save(audience);
        int databaseSizeBeforeDelete = audienceRepository.findAll().size();

        // Get the audience
        restAudienceMockMvc.perform(delete("/api/audiences/{id}", audience.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean audienceExistsInEs = audienceSearchRepository.exists(audience.getId());
        assertThat(audienceExistsInEs).isFalse();

        // Validate the database is empty
        List<Audience> audiences = audienceRepository.findAll();
        assertThat(audiences).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAudience() throws Exception {
        // Initialize the database
        audienceRepository.saveAndFlush(audience);
        audienceSearchRepository.save(audience);

        // Search the audience
        restAudienceMockMvc.perform(get("/api/_search/audiences?query=id:" + audience.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(audience.getId().intValue())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME.toString())))
            .andExpect(jsonPath("$.[*].realname").value(hasItem(DEFAULT_REALNAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }
}
