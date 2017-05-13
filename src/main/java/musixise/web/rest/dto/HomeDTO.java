package musixise.web.rest.dto;

import java.util.List;

/**
 * Created by zhaowei on 17/5/13.
 */
public class HomeDTO {

    private List<WorkListDTO> hostSongs;

    private List<MusixiserDTO> hotMusixisers;

    private List<WorkListDTO> latestSongs;

    private List<MusixiserDTO> latestMusixisers;

    public List<WorkListDTO> getHostSongs() {
        return hostSongs;
    }

    public void setHostSongs(List<WorkListDTO> hostSongs) {
        this.hostSongs = hostSongs;
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
}
