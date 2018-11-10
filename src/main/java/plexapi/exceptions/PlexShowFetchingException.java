package plexapi.exceptions;

/**
 * Created by Romain on 27/10/2018.
 */
public class PlexShowFetchingException extends PlexAPIException {
    public PlexShowFetchingException(String showName) {
        super("Error fecthing show : " + showName);
    }

    public PlexShowFetchingException() {
    }
}
