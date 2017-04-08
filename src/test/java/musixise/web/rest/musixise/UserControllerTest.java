package musixise.web.rest.musixise;

import com.alibaba.fastjson.JSON;
import musixise.MusixiseApp;
import musixise.domain.Musixiser;
import musixise.domain.User;
import musixise.repository.MusixiserRepository;
import musixise.repository.UserRepository;
import musixise.repository.search.MusixiserSearchRepository;
import musixise.service.MusixiserService;
import musixise.service.UserService;
import musixise.web.rest.TestUtil;
import musixise.web.rest.UserController;
import musixise.web.rest.dto.user.RegisterDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by zhaowei on 17/4/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MusixiseApp.class)
@WebAppConfiguration
@IntegrationTest
public class UserControllerTest {

    private MockMvc restMockMvc;
    @Inject private MusixiserRepository musixiserRepository;

    @Inject private UserRepository userRepository;

    @Inject private UserService userService;

    @Inject private MusixiserService musixiserService;

    @Inject     private MusixiserSearchRepository musixiserSearchRepository;

    @Before
    public void setup() {

        UserController userController = new UserController();

        this.restMockMvc = MockMvcBuilders.standaloneSetup(userController).setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();

        ReflectionTestUtils.setField(userController, "userRepository", userRepository);
        ReflectionTestUtils.setField(userController, "musixiserRepository", musixiserRepository);
        ReflectionTestUtils.setField(userController, "userService", userService);
        ReflectionTestUtils.setField(userController, "musixiserService", musixiserService);
        ReflectionTestUtils.setField(userController, "musixiserSearchRepository", musixiserSearchRepository);

        //Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn("admin");

    }


    @Test
    public void testRegister() throws Exception {
       // int datasizeBeforeCreate = workListRepository.findAll().size();

        String login = "iamaunittestdata";
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername(login);
        registerDTO.setPassword("mypassword");
        registerDTO.setRealname("hello");

        ResultActions perform = restMockMvc.perform(
            post("/api/user/register")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONBytes(registerDTO)))
            .andExpect(status().isOk());


        //get id by username
        //clean all
        Optional<User> oneByLogin = userRepository.findOneByLogin(login);
        if (oneByLogin.isPresent()) {
            Long uid = oneByLogin.get().getId();
            userService.deleteUserInformation(uid);
            Musixiser oneByUserId = musixiserRepository.findOneByUserId(uid);
            musixiserService.delete(oneByUserId.getId());
        }
    }
}
