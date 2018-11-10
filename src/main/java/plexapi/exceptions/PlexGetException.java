package plexapi.exceptions;

/**
 * Created by Romain on 03/11/2018.
 */
public class PlexGetException extends PlexAPIException {

    private static final long serialVersionUID = 1L;

    public PlexGetException(String url) {
        super("Error getting from url : " + url);
    }
}
