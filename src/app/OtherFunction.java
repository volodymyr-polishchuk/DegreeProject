package app;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Vladimir on 03/01/18.
 **/
public class OtherFunction {
    public URI GetProjectFileURI(String filename) throws URISyntaxException {
        if (filename.charAt(0) == '/') {
            return new URI(getClass().getProtectionDomain().getCodeSource().getLocation() + filename);
        } else {
            return new URI(getClass().getProtectionDomain().getCodeSource().getLocation() + "/" + filename);
        }
    }
}

