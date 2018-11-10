package plexapi.beans;

import java.util.Map;

/**
 * Created by Romain on 10/11/2018.
 */
public class PlexSeason {
    private String key;

    private Map<Integer, PlexEpisode> episodes;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<Integer, PlexEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Map<Integer, PlexEpisode> episodes) {
        this.episodes = episodes;
    }

    public PlexEpisode getEpisode(Integer number) {
        return episodes.get(number);
    }

    public void putEpisode(Integer number, PlexEpisode episode) {
        this.episodes.put(number, episode);
    }
}
