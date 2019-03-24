package plexapi.beans;

import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Created by Romain on 10/11/2018.
 */
public class PlexEpisode {
    private String title;

    private Boolean watched = false;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isWatched() {
        return watched;
    }

    public void setWatched(Boolean watched) {
        this.watched = watched;
    }

    @JsonSetter("viewCount")
    public void setWatchedFromViewCount(Integer viewCount) {
        if (viewCount != null && viewCount > 0) {
            this.watched = true;
        } else {
            this.watched = false;
        }
    }
}
