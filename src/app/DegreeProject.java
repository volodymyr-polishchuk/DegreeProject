package app;

import frame.ConnectionForm;
import frame.MainForm;

import java.io.*;
import java.sql.SQLException;

/**
 * Created by Vladimir on 01/01/18.
 **/
public class DegreeProject {
    public static DatabaseData databaseData;
    public static WeekList WEEKLIST;
    public static GroupList GROUPLIST;

    public static void main(String[] args) throws IOException {
//        ConnectionForm connectionForm = new ConnectionForm();
//        connectionForm.setVisible(true);
        try {
            databaseData = new DatabaseData("localhost", "3306", "Volodymyr", new char[]{'0', '0', '0', '0'}, "mydata");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        InitialMainFrame();
    }

    public static void InitialMainFrame() {
        MainForm mainForm = new MainForm();
        try {
            WEEKLIST = new WeekList(databaseData.getConnection());
            GROUPLIST = new GroupList(databaseData.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
