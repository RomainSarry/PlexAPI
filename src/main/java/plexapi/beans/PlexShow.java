package plexapi.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Romain on 10/11/2018.
 */
public class PlexShow {
    private String key;

    private String title;

    private Map<Integer, PlexSeason> seasons;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<Integer, PlexSeason> getSeasons() {
        return seasons;
    }

    public void setSeasons(Map<Integer, PlexSeason> seasons) {
        this.seasons = seasons;
    }

    public PlexSeason getSeason(Integer number) {
        if (seasons == null) {
            return null;
        }
        return seasons.get(number);
    }

    public void putSeason(Integer number, PlexSeason season) {
        if (this.seasons == null) {
            seasons = new HashMap<Integer, PlexSeason>();
        }
        this.seasons.put(number, season);
    }

    public Integer getNumberOfUnwatchedEpisodes() {
        Integer numberOfEpisodes = 0;

        if (seasons != null && !seasons.isEmpty()) {
            for (Map.Entry<Integer, PlexSeason> seasonEntry : seasons.entrySet()) {
                Map<Integer, PlexEpisode> episodeMap = seasonEntry.getValue().getEpisodes();
                if (episodeMap != null && !episodeMap.isEmpty()) {
                    for (Map.Entry<Integer, PlexEpisode> episodeEntry : seasonEntry.getValue().getEpisodes().entrySet()) {
                        if (!episodeEntry.getValue().isWatched()) {
                            numberOfEpisodes++;
                        }
                    }
                }
            }
        }

        return numberOfEpisodes;
    }

    public Boolean hasEpisode(Integer season, Integer episode) {
        return getSeason(season) != null && getSeason(season).getEpisode(episode) != null;
    }
}
