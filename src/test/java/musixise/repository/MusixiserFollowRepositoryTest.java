package musixise.repository;

import musixise.MusixiseApp;
import musixise.domain.MusixiserFollow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.junit.Assert.*;

/**
 * Created by zhaowei on 16/11/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MusixiseApp.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class MusixiserFollowRepositoryTest {

    @Inject MusixiserFollowRepository musixiserFollowRepository;
    @Test
    public void testFindAllByUserId() throws Exception {

        Pageable pageable = new PageRequest(1, 10);
        Page<MusixiserFollow> musixiserFollows = musixiserFollowRepository.findAll(pageable);

    }

    @Test
    public void testfindByUserIdAndFollowUid() {
        MusixiserFollow musixiserFollow = musixiserFollowRepository.findByUserIdAndFollowUid(3l, 4l);
        MusixiserFollow musixiserFollow2 = musixiserFollowRepository.findByUserIdAndFollowUid(1l, 4l);
        System.out.println(musixiserFollow);
    }
}
