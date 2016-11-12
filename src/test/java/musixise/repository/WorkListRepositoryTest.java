package musixise.repository;

import musixise.MusixiseApp;
import musixise.domain.WorkList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by zhaowei on 16/11/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MusixiseApp.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class WorkListRepositoryTest {

    @Inject WorkListRepository workListRepository;

    @Test
    public void testFindAllByUserIdOrderByIdDesc() throws Exception {

        Long userId = 3l;
        List<WorkList> workListList = workListRepository.findAllByUserIdOrderByIdDesc(userId);
        System.out.println(workListList);

    }
}
