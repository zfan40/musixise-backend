package musixise.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import musixise.domain.User;
import musixise.domain.WorkList;
import musixise.service.HomeService;
import musixise.service.UserService;
import musixise.web.rest.dto.HomeDTO;
import musixise.web.rest.dto.OutputDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by zhaowei on 17/5/13.
 */
@Api(value = "home", description = "首页接口", position = 1)
@RestController
@RequestMapping("/api/home")
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Resource HomeService homeService;

    @Resource UserService userService;

    @RequestMapping(value = "",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "首页", notes = "获取首页信息", response = WorkList.class, position = 2)
    @Timed
    public ResponseEntity<?> index() {
        log.debug("REST request to get home");

        Long userId = 0l;
        User user = userService.get();
        if (user != null) {
            userId = user.getId();
        }
        HomeDTO homeDTO = homeService.getHome(userId);
        return ResponseEntity.ok(new OutputDTO<>(0, "success", homeDTO));
    }
}
