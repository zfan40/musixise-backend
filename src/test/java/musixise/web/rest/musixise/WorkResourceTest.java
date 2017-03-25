package musixise.web.rest.musixise;

import com.alibaba.fastjson.JSON;
import musixise.MusixiseApp;
import musixise.domain.WorkList;
import musixise.repository.UserRepository;
import musixise.repository.WorkListRepository;
import musixise.repository.search.WorkListSearchRepository;
import musixise.service.WorkListService;
import musixise.web.rest.TestUtil;
import musixise.web.rest.WorkController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by zhaowei on 17/3/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MusixiseApp.class)
@WebAppConfiguration
@IntegrationTest
public class WorkResourceTest {

    private static final Long DEFAULT_USER_ID = 3L;

    private MockMvc restWorkMockMvc;

    private static final LocalDateTime DEFAULT_CREATETIME = LocalDateTime.now();

    @Inject WorkListRepository workListRepository;

    @Inject UserRepository userRepository;

    @Inject WorkListSearchRepository workListSearchRepository;

    @Inject WorkListService workListService;

    @Before
    public void setup() {

        WorkController workController = new WorkController();
        this.restWorkMockMvc = MockMvcBuilders.standaloneSetup(workController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();

        ReflectionTestUtils.setField(workController, "workListRepository", workListRepository);
        ReflectionTestUtils.setField(workController, "userRepository", userRepository);
        ReflectionTestUtils.setField(workController, "workListSearchRepository", workListSearchRepository);
        ReflectionTestUtils.setField(workController, "workListService", workListService);

        Authentication authentication = Mockito.mock(Authentication.class);
// Mockito.whens() for your authorization object
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn("admin");

    }


    @Test
    public void testCreateWork() throws Exception {

        int datasizeBeforeCreate = workListRepository.findAll().size();

        WorkList w = new WorkList();

        w.setContent("盲造数据，现>在是北京时间3/18/2017,4:03AM，下了机场大巴，安贞麦当劳，又看到了形形色");
        w.setTitle("这是标题");
        w.setCover("http://oaeyej2ty.bkt.clouddn.com/CcwtD1PN_dfg.jpg");
        w.setUrl("http://oiqvdjk3s.bkt.clouddn.com/kuNziglJ_test.txt");

        ResultActions perform = restWorkMockMvc.perform(
            post("/api/work/create")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONBytes(w)))
            .andExpect(status().isOk());


        List<WorkList> workListRepositoryAll = workListRepository.findAll();

        //数据是否写入
        assertThat(workListRepositoryAll).hasSize(datasizeBeforeCreate + 1);

        //检测用户ID
        WorkList workList = workListRepositoryAll.get(workListRepositoryAll.size() - 1);
        assertThat(workList.getUserId()).isEqualTo(DEFAULT_USER_ID);

    }

    @Test
    public void testWorkList() throws Exception {

        PageRequest pageable = new PageRequest(1, 10);

        ResultActions perform = restWorkMockMvc.perform(
            post("/api/work/getList?page=1&size=10")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isOk());
    }
}
