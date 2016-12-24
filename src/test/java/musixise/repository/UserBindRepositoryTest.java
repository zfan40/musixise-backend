package musixise.repository;

import musixise.MusixiseApp;
import musixise.domain.User;
import musixise.domain.UserBind;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Created by zhaowei on 16/12/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MusixiseApp.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class UserBindRepositoryTest {

    @Inject UserBindRepository userBindRepository;

    @Test
    public void testInsert() {
        UserBind userBind = new UserBind();
        userBind.setOpenId("2");
        userBind.setLogin("33");
        UserBind userBind1 = userBindRepository.save(userBind);
    }

    @Test
    public void testFindByOpenIdAndProvider() {
        UserBind userBind = new UserBind();
        userBind.setOpenId("111");
        userBind.setLogin("ZZZ");
        userBind.setProvider("sina");
        UserBind userBind1 = userBindRepository.save(userBind);

        UserBind getUserBind = userBindRepository.findByOpenIdAndProvider(userBind.getOpenId(),
            userBind.getProvider());
    }
}
