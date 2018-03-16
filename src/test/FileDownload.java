package test;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


/**
 * Created by Vladimir on 16/03/18.
 **/
public class FileDownload {
    public static void main(String[] args) throws IOException {
        URL website = new URL("https://hwork.net/vova/mdb.msi");
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream("D:/mdb.msi");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
}
