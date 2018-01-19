package app;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Vladimir on 03/01/18.
 **/

public class WeekList {
    private ArrayList<Week> list = new ArrayList<>();
    private Connection connection;

    public WeekList(Connection connection) throws SQLException {
        this.connection = connection;
        loadFromDatabase();
    }

    private void loadFromDatabase() throws SQLException {
        //Завантажуємо з бази даних дані
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM weeks");
        list.clear();
        while (resultSet.next()) {
            list.add(new Week(resultSet));
        }
    }

    public Week getWeekByName(String name) {
        //Вибираємо із списку тиждень за назвою і повертаємо його
        for (Week week : list) {
            if (week.getName().equals(name)) {
                return week;
            }
        }
        return new Week();
    }

    public Week getWeekByMark(char mark) {
        //Вибираємо із списку тиждень за позначенням і повертаємо його
        for (Week week: list) {
            if (week.getMark() == mark) {
                return week;
            }
        }
        return new Week();
    }

    public ArrayList<Week> GetAllWeek() {
        return list;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
