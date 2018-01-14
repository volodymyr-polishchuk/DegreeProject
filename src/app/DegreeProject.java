package app;

import frame.mainForm;
import frame.schedulePanel;

import java.io.*;
import java.sql.SQLException;

/**
 * Created by Vladimir on 01/01/18.
 **/
public class DegreeProject {
    public static DatabaseData databaseData;
    public static WeekList WEEKLIST;

    public static void main(String[] args) throws IOException {
        try {
            databaseData = new app.DatabaseData("localhost", "3306", "Volodymyr", "0000", "mydata");
            WEEKLIST = new WeekList(databaseData.getConnection());
//            databaseData.CreateDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        System.out.println(WEEKLIST.GetAllWeek());
//        ConnectionForm connectionForm = new ConnectionForm();
//        connectionForm.setVisible(true);

        mainForm form = new mainForm();
        form.addTab(new schedulePanel("Графік навчання"));
    }
}
