package musixise.service;

import musixise.web.rest.dto.HomeDTO;
import musixise.web.rest.dto.MusixiserDTO;
import musixise.web.rest.dto.WorkListDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhaowei on 17/5/13.
 */
@Service
public class HomeService {

    @Resource WorkListService workListService;

    @Resource MusixiserService musixiserService;

    public HomeDTO getHome() {

        HomeDTO homeDTO = new HomeDTO();
        List<WorkListDTO> hotSongs = workListService.getHotSongs();
        List<WorkListDTO> latestSongs = workListService.getLatestSongs();

        List<MusixiserDTO> hotMusixisers = musixiserService.getHotMusixisers();
        List<MusixiserDTO> latestMusixisers = musixiserService.getLatestMusixisers();

        homeDTO.setHostSongs(hotSongs);
        homeDTO.setLatestSongs(latestSongs);
        homeDTO.setHotMusixisers(hotMusixisers);
        homeDTO.setLatestMusixisers(latestMusixisers);

        return homeDTO;
    }
}
