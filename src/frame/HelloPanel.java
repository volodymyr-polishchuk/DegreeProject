package frame;

import app.DegreeProject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Vladimir on 23/01/18.
 **/
public class HelloPanel extends JPanel {
    private JPanel contentPane;
    private JList list1;
    private JButton перейтиButton;
    private JButton перейтиButton1;
    private JButton перейтиButton2;
    private JLabel jLabelDatabaseName;
    private JLabel jLabelLastLog;

    public HelloPanel(String name) {
        setName(name);
        setLayout(new GridLayout());
        add(contentPane);
        initialLabels();
    }

    private void initialLabels() {
        jLabelDatabaseName.setText(DegreeProject.databaseData.getDatabaseName());
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(
                    new URI(getClass().getProtectionDomain().getCodeSource().getLocation() + "/log.txt")
            ))));
            String lastDate;
            lastDate = reader.readLine();
            if (!(lastDate != null && lastDate.isEmpty())) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH).parse(lastDate));
                lastDate = "";
                lastDate += calendar.get(Calendar.DAY_OF_MONTH) + "/";
                lastDate += calendar.get(Calendar.MONTH) + "/";
                lastDate += calendar.get(Calendar.YEAR);
            } else {
                lastDate = "невідомо";
            }
            jLabelLastLog.setText(lastDate);
        } catch (IOException | URISyntaxException | ParseException e) {
            e.printStackTrace();
        }
    }


}
