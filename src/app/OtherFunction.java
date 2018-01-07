package app;

import java.awt.*;

/**
 * Created by Vladimir on 03/01/18.
 **/
public class OtherFunction {
    public static String ColorToHexString(Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2);
    }
}
