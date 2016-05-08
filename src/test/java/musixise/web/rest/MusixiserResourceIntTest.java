package musixise.web.rest;

import musixise.MusixiseApp;
import musixise.domain.Musixiser;
import musixise.repository.MusixiserRepository;
import musixise.repository.search.MusixiserSearchRepository;

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
 * Test class for the MusixiserResource REST controller.
 *
 * @see MusixiserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MusixiseApp.class)
@WebAppConfiguration
@IntegrationTest
public class MusixiserResourceIntTest {

    private static final String DEFAULT_REALNAME = "AAAAA";
    private static final String UPDATED_REALNAME = "BBBBB";
    private static final String DEFAULT_TEL = "AAAAA";
    private static final String UPDATED_TEL = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_BIRTH = "AAAAA";
    private static final String UPDATED_BIRTH = "BBBBB";
    private static final String DEFAULT_GENDER = "AAAAA";
    private static final String UPDATED_GENDER = "BBBBB";
    private static final String DEFAULT_SMALL_AVATAR = "AAAAA";
    private static final String UPDATED_SMALL_AVATAR = "BBBBB";
    private static final String DEFAULT_LARGE_AVATAR = "AAAAA";
    private static final String UPDATED_LARGE_AVATAR = "BBBBB";
    private static final String DEFAULT_NATION = "AAAAA";
    private static final String UPDATED_NATION = "BBBBB";
    private static final String DEFAULT_N = "AAAAA";
    private static final String UPDATED_N = "BBBBB";

    @Inject
    private MusixiserRepository musixiserRepository;

    @Inject
    private MusixiserSearchRepository musixiserSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMusixiserMockMvc;

    private Musixiser musixiser;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MusixiserResource musixiserResource = new MusixiserResource();
        ReflectionTestUtils.setField(musixiserResource, "musixiserSearchRepository", musixiserSearchRepository);
        ReflectionTestUtils.setField(musixiserResource, "musixiserRepository", musixiserRepository);
        this.restMusixiserMockMvc = MockMvcBuilders.standaloneSetup(musixiserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        musixiserSearchRepository.deleteAll();
        musixiser = new Musixiser();
        musixiser.setRealname(DEFAULT_REALNAME);
        musixiser.setTel(DEFAULT_TEL);
        musixiser.setEmail(DEFAULT_EMAIL);
        musixiser.setBirth(DEFAULT_BIRTH);
        musixiser.setGender(DEFAULT_GENDER);
        musixiser.setSmallAvatar(DEFAULT_SMALL_AVATAR);
        musixiser.setLargeAvatar(DEFAULT_LARGE_AVATAR);
        musixiser.setNation(DEFAULT_NATION);
        musixiser.setN(DEFAULT_N);
    }

    @Test
    @Transactional
    public void createMusixiser() throws Exception {
        int databaseSizeBeforeCreate = musixiserRepository.findAll().size();

        // Create the Musixiser

        restMusixiserMockMvc.perform(post("/api/musixisers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(musixiser)))
                .andExpect(status().isCreated());

        // Validate the Musixiser in the database
        List<Musixiser> musixisers = musixiserRepository.findAll();
        assertThat(musixisers).hasSize(databaseSizeBeforeCreate + 1);
        Musixiser testMusixiser = musixisers.get(musixisers.size() - 1);
        assertThat(testMusixiser.getRealname()).isEqualTo(DEFAULT_REALNAME);
        assertThat(testMusixiser.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testMusixiser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMusixiser.getBirth()).isEqualTo(DEFAULT_BIRTH);
        assertThat(testMusixiser.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testMusixiser.getSmallAvatar()).isEqualTo(DEFAULT_SMALL_AVATAR);
        assertThat(testMusixiser.getLargeAvatar()).isEqualTo(DEFAULT_LARGE_AVATAR);
        assertThat(testMusixiser.getNation()).isEqualTo(DEFAULT_NATION);
        assertThat(testMusixiser.getN()).isEqualTo(DEFAULT_N);

        // Validate the Musixiser in ElasticSearch
        Musixiser musixiserEs = musixiserSearchRepository.findOne(testMusixiser.getId());
        assertThat(musixiserEs).isEqualToComparingFieldByField(testMusixiser);
    }

    @Test
    @Transactional
    public void getAllMusixisers() throws Exception {
        // Initialize the database
        musixiserRepository.saveAndFlush(musixiser);

        // Get all the musixisers
        restMusixiserMockMvc.perform(get("/api/musixisers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(musixiser.getId().intValue())))
                .andExpect(jsonPath("$.[*].realname").value(hasItem(DEFAULT_REALNAME.toString())))
                .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].birth").value(hasItem(DEFAULT_BIRTH.toString())))
                .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
                .andExpect(jsonPath("$.[*].smallAvatar").value(hasItem(DEFAULT_SMALL_AVATAR.toString())))
                .andExpect(jsonPath("$.[*].largeAvatar").value(hasItem(DEFAULT_LARGE_AVATAR.toString())))
                .andExpect(jsonPath("$.[*].nation").value(hasItem(DEFAULT_NATION.toString())))
                .andExpect(jsonPath("$.[*].n").value(hasItem(DEFAULT_N.toString())));
    }

    @Test
    @Transactional
    public void getMusixiser() throws Exception {
        // Initialize the database
        musixiserRepository.saveAndFlush(musixiser);

        // Get the musixiser
        restMusixiserMockMvc.perform(get("/api/musixisers/{id}", musixiser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(musixiser.getId().intValue()))
            .andExpect(jsonPath("$.realname").value(DEFAULT_REALNAME.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.birth").value(DEFAULT_BIRTH.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.smallAvatar").value(DEFAULT_SMALL_AVATAR.toString()))
            .andExpect(jsonPath("$.largeAvatar").value(DEFAULT_LARGE_AVATAR.toString()))
            .andExpect(jsonPath("$.nation").value(DEFAULT_NATION.toString()))
            .andExpect(jsonPath("$.n").value(DEFAULT_N.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMusixiser() throws Exception {
        // Get the musixiser
        restMusixiserMockMvc.perform(get("/api/musixisers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMusixiser() throws Exception {
        // Initialize the database
        musixiserRepository.saveAndFlush(musixiser);
        musixiserSearchRepository.save(musixiser);
        int databaseSizeBeforeUpdate = musixiserRepository.findAll().size();

        // Update the musixiser
        Musixiser updatedMusixiser = new Musixiser();
        updatedMusixiser.setId(musixiser.getId());
        updatedMusixiser.setRealname(UPDATED_REALNAME);
        updatedMusixiser.setTel(UPDATED_TEL);
        updatedMusixiser.setEmail(UPDATED_EMAIL);
        updatedMusixiser.setBirth(UPDATED_BIRTH);
        updatedMusixiser.setGender(UPDATED_GENDER);
        updatedMusixiser.setSmallAvatar(UPDATED_SMALL_AVATAR);
        updatedMusixiser.setLargeAvatar(UPDATED_LARGE_AVATAR);
        updatedMusixiser.setNation(UPDATED_NATION);
        updatedMusixiser.setN(UPDATED_N);

        restMusixiserMockMvc.perform(put("/api/musixisers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMusixiser)))
                .andExpect(status().isOk());

        // Validate the Musixiser in the database
        List<Musixiser> musixisers = musixiserRepository.findAll();
        assertThat(musixisers).hasSize(databaseSizeBeforeUpdate);
        Musixiser testMusixiser = musixisers.get(musixisers.size() - 1);
        assertThat(testMusixiser.getRealname()).isEqualTo(UPDATED_REALNAME);
        assertThat(testMusixiser.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testMusixiser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMusixiser.getBirth()).isEqualTo(UPDATED_BIRTH);
        assertThat(testMusixiser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testMusixiser.getSmallAvatar()).isEqualTo(UPDATED_SMALL_AVATAR);
        assertThat(testMusixiser.getLargeAvatar()).isEqualTo(UPDATED_LARGE_AVATAR);
        assertThat(testMusixiser.getNation()).isEqualTo(UPDATED_NATION);
        assertThat(testMusixiser.getN()).isEqualTo(UPDATED_N);

        // Validate the Musixiser in ElasticSearch
        Musixiser musixiserEs = musixiserSearchRepository.findOne(testMusixiser.getId());
        assertThat(musixiserEs).isEqualToComparingFieldByField(testMusixiser);
    }

    @Test
    @Transactional
    public void deleteMusixiser() throws Exception {
        // Initialize the database
        musixiserRepository.saveAndFlush(musixiser);
        musixiserSearchRepository.save(musixiser);
        int databaseSizeBeforeDelete = musixiserRepository.findAll().size();

        // Get the musixiser
        restMusixiserMockMvc.perform(delete("/api/musixisers/{id}", musixiser.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean musixiserExistsInEs = musixiserSearchRepository.exists(musixiser.getId());
        assertThat(musixiserExistsInEs).isFalse();

        // Validate the database is empty
        List<Musixiser> musixisers = musixiserRepository.findAll();
        assertThat(musixisers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMusixiser() throws Exception {
        // Initialize the database
        musixiserRepository.saveAndFlush(musixiser);
        musixiserSearchRepository.save(musixiser);

        // Search the musixiser
        restMusixiserMockMvc.perform(get("/api/_search/musixisers?query=id:" + musixiser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(musixiser.getId().intValue())))
            .andExpect(jsonPath("$.[*].realname").value(hasItem(DEFAULT_REALNAME.toString())))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].birth").value(hasItem(DEFAULT_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].smallAvatar").value(hasItem(DEFAULT_SMALL_AVATAR.toString())))
            .andExpect(jsonPath("$.[*].largeAvatar").value(hasItem(DEFAULT_LARGE_AVATAR.toString())))
            .andExpect(jsonPath("$.[*].nation").value(hasItem(DEFAULT_NATION.toString())))
            .andExpect(jsonPath("$.[*].n").value(hasItem(DEFAULT_N.toString())));
    }
}
