package musixise.repository;

import musixise.MusixiseApp;
import musixise.domain.Config;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by zhaowei on 17/5/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MusixiseApp.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class ConfigRepositoryTest {
    @Resource ConfigRepository configRepository;

    @Test
    public void testInsert() {

        Config config = new Config();
        config.setCkey("test");
        config.setCval("testv");
        configRepository.save(config);

        Config test = configRepository.findOneByCkey("test");
        System.out.println(test.toString());
    }
}
