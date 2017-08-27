package musixise.service;

import com.alibaba.fastjson.JSON;
import musixise.MusixiseApp;
import musixise.web.rest.dto.HomeDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by zhaowei on 17/5/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MusixiseApp.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class HomeServiceTest {

    @Resource HomeService homeService;
    @Test
    public void getHome() throws Exception {
        HomeDTO home = homeService.getHome(3l);
        System.out.println(JSON.toJSONString(home));
    }

}
