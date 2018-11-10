package plexapi.exceptions;

/**
 * Created by Romain on 10/11/2018.
 */
public abstract class PlexAPIException extends Exception {
    public PlexAPIException(String message) {
        super(message);
    }

    public PlexAPIException() {
    }
}
