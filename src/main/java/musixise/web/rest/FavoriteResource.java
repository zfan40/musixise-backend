package musixise.web.rest;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhaowei on 16/11/12.
 */

@Api(value = "favorite", description = "收藏接口", position = 1)
@RestController
@RequestMapping("/api/musixisers")
public class FavoriteResource {
}
