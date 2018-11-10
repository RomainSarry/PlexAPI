package plexapi.exceptions;

/**
 * Created by Romain on 10/11/2018.
 */
public class PlexScanException extends PlexAPIException {
    public PlexScanException() {
        super("Error scanning library");
    }
}
