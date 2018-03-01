package app;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

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

    public static void main(String[] args) {
        Date date = new Date(System.currentTimeMillis());
        java.sql.Date date1 = new java.sql.Date(System.currentTimeMillis());
        System.out.println(date);
        System.out.println(date1);
    }
}

