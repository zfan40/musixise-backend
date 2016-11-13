package musixise.repository;

import musixise.MusixiseApp;
import musixise.domain.WorkListFollow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by zhaowei on 16/11/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MusixiseApp.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class WorkListFollowRepositoryTest {

    @Inject  WorkListFollowRepository workListFollowRepository;
    @Test
    public void testFindAllByUserIdAndWorkId() throws Exception {
        Long userId = 3l;
        Long workId = 5l;
        Optional<WorkListFollow> workListFollow = workListFollowRepository.findOneByUserIdAndWorkId(userId, workId);
        System.out.println(workListFollow);
    }

    @Test
    public void testDeleteByUserIdAndWorkId() {
        Long userId = 3l;
        Long workId = 2l;
        int s = workListFollowRepository.deleteByUserIdAndWorkId(userId, workId);
    }
}
