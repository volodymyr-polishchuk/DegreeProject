package app;

import frame.MainForm;

import java.awt.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Vladimir on 01/01/18.
 **/
public class DegreeProject {
    public static DatabaseData databaseData;
    public static WeekList WEEKLIST;
    public static GroupList GROUPLIST;

    public static void main(String[] args) throws IOException, SQLException {
//        ConnectionForm connectionForm = new ConnectionForm();
//        connectionForm.setVisible(true);
        try {
            databaseData = new DatabaseData("localhost", "3306", "Volodymyr", new char[]{'0', '0', '0', '0'}, "mydata");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        InitialMainFrame();

        ArrayList<ScheduleUnit> unit = new ArrayList<>();
        ScheduleUnit su = new ScheduleUnit(new Group());
        su.setWeek(1, WEEKLIST.getWeekByName("Канікули"));
        su.setWeek(2, WEEKLIST.getWeekByName("Навчання"));
        su.setWeek(3, WEEKLIST.getWeekByName("Канікули"));
        su.setWeek(4, WEEKLIST.getWeekByName("Навчання"));
        unit.add(su);
        unit.add(new ScheduleUnit(new Group()));
        unit.add(new ScheduleUnit(new Group()));
        StudySchedule schedule = new StudySchedule(2018, "Поліщук", unit);

        schedule.SaveToDatabase(databaseData.getConnection());
    }

    public static void InitialMainFrame() {
//        MainForm mainForm = new MainForm();
        try {
            WEEKLIST = new WeekList(databaseData.getConnection());
            GROUPLIST = new GroupList(databaseData.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
