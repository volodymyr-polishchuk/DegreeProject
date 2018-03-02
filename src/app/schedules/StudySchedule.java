package app.schedules;

import app.DegreeProject;
import app.data.WeekList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Vladimir on 03/01/18.
 **/
@Deprecated
public class StudySchedule {
    private int year;
    private String author;
    private ArrayList<ScheduleUnit> units;

    public StudySchedule(int year, String author) {
        this.year = year;
        this.author = author;
    }

    public StudySchedule(int year, String author, ArrayList<ScheduleUnit> units) {
        this.year = year;
        this.author = author;
        this.units = units;
    }

    public void SaveToDatabase(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT IGNORE INTO studyschedules SET years = ?, author = ?;");
        preparedStatement.setInt(1, year);
        preparedStatement.setString(2, author);
        preparedStatement.execute();

        preparedStatement = connection.prepareStatement("DELETE FROM scheduleunits WHERE scheduleunits.years = ?");
        preparedStatement.setInt(1, year);
        preparedStatement.execute();

        for (ScheduleUnit unit : units) {
            preparedStatement = connection.prepareStatement("INSERT IGNORE INTO mydata.scheduleunits " +
                    "(mydata.scheduleunits.years, mydata.scheduleunits.group, mydata.scheduleunits.weeks) VALUE (?, ?, ?)");
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, DegreeProject.GROUPLIST.getIndexByName(unit.getName()));
            preparedStatement.setString(3, unit.decode());
            preparedStatement.execute();
            //TODO треба придумати інші назви колонкам в таблицях, бо поточні є резервованими під систему
        }
    }

    public void LoadFromDatabase(Connection connection, WeekList weekList, int year) {

    }
}


