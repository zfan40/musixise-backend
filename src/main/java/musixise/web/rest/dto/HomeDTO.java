package musixise.web.rest.dto;

import java.util.List;

/**
 * Created by zhaowei on 17/5/13.
 */
public class HomeDTO {

    private List<WorkListDTO> hosSongs;

    private List<MusixiserDTO> hotMusixisers;

    private List<WorkListDTO> latestSongs;

    private List<MusixiserDTO> latestMusixisers;

    private List<BannerDTO> banners;

    private List<AdsDTO> ads;

    public List<AdsDTO> getAds() {
        return ads;
    }

    public void setAds(List<AdsDTO> ads) {
        this.ads = ads;
    }

    public List<WorkListDTO> getHosSongs() {
        return hosSongs;
    }

    public void setHosSongs(List<WorkListDTO> hosSongs) {
        this.hosSongs = hosSongs;
    }

    public List<MusixiserDTO> getHotMusixisers() {
        return hotMusixisers;
    }

    public void setHotMusixisers(List<MusixiserDTO> hotMusixisers) {
        this.hotMusixisers = hotMusixisers;
    }

    public List<WorkListDTO> getLatestSongs() {
        return latestSongs;
    }

    public void setLatestSongs(List<WorkListDTO> latestSongs) {
        this.latestSongs = latestSongs;
    }

    public List<MusixiserDTO> getLatestMusixisers() {
        return latestMusixisers;
    }

    public void setLatestMusixisers(List<MusixiserDTO> latestMusixisers) {
        this.latestMusixisers = latestMusixisers;
    }

    public List<BannerDTO> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerDTO> banners) {
        this.banners = banners;
    }
}
