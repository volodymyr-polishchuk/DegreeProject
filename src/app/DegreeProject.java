package app;

import frame.ConnectionForm;
import frame.MainForm;
import frame.schedulePanel;

import java.io.*;

/**
 * Created by Vladimir on 01/01/18.
 **/
public class DegreeProject {
    public static DatabaseData databaseData;
    public static WeekList WEEKLIST;

    public static void main(String[] args) throws IOException {
//        try {
//            databaseData = new DatabaseData("localhost", "3306", "Volodymyr", "0000", "mydata");
//            WEEKLIST = new WeekList(databaseData.getConnection());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

//        System.out.println(WEEKLIST.GetAllWeek());
//        ConnectionForm connectionForm = new ConnectionForm();
//        connectionForm.setVisible(true);

//        MainForm form = new MainForm();
//        form.addTab(new schedulePanel("Графік навчання"));
        ConnectionForm connectionForm = new ConnectionForm();
        connectionForm.setVisible(true);
    }
}
