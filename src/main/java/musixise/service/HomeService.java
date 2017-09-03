package musixise.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import musixise.domain.Config;
import musixise.repository.ConfigRepository;
import musixise.web.rest.dto.*;
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

    @Resource ConfigRepository configRepository;

    public HomeDTO getHome(Long userId) {

        HomeDTO homeDTO = new HomeDTO();
        List<WorkListDTO> hotSongs = workListService.getHotSongs(userId);
        List<WorkListDTO> latestSongs = workListService.getLatestSongs(userId);

        List<MusixiserDTO> hotMusixisers = musixiserService.getHotMusixisers(userId);
        List<MusixiserDTO> latestMusixisers = musixiserService.getLatestMusixisers(userId);

        homeDTO.setHosSongs(hotSongs);
        homeDTO.setLatestSongs(latestSongs);
        homeDTO.setHotMusixisers(hotMusixisers);
        homeDTO.setLatestMusixisers(latestMusixisers);
        homeDTO.setBanners(getBanner());
        homeDTO.setAds(getAds());

        return homeDTO;
    }

    private List<BannerDTO> getBanner() {

        Config banner = configRepository.findOneByCkey("main_banner");

        try {
            return JSON.parseObject(banner.getCval(), new TypeReference<List<BannerDTO>>() {});
        } catch (Exception e) {

        }
        return null;
    }

    private List<AdsDTO> getAds() {

        Config banner = configRepository.findOneByCkey("main_ads");

        try {
            return JSON.parseObject(banner.getCval(), new TypeReference<List<AdsDTO>>() { });
        } catch (Exception e) {

        }
        return null;
    }

}
