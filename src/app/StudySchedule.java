package app;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Vladimir on 03/01/18.
 **/
public class StudySchedule {
    private int year;
    private String author;
    private ArrayList<ScheduleUnit> units;

    public StudySchedule(int year, String author) {
        this.year = year;
        this.author = author;
    }

    public void SaveToDatabase(Connection connection) throws SQLException {
        PreparedStatement st = connection.prepareStatement("INSERT IGNORE INTO studyschedules SET years = ?, author = ?;");
        st.setInt(1, year);
        st.setString(2, author);
        st.execute();
    }

    public static void main(String[] args) throws SQLException {
        StudySchedule schedule = new StudySchedule(2018, "Поліщук");
        schedule.SaveToDatabase((new DatabaseData("localhost", "3306", "Volodymyr", new char[]{'0', '0', '0', '0'}, "mydata")).getConnection());
    }
}


