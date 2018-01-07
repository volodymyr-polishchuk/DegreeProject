package app;

import java.awt.Color;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Vladimir on 03/01/18.
 **/

public class WeekList {
    private ArrayList<Week> list = new ArrayList<>();
    private Connection connection;

    public WeekList(Connection connection) {
        this.connection = connection;
        loadFromDatabase();
    }

    private void loadFromDatabase() {
        //Завантажуємо з бази даних дані
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM weeks");
            list.clear();
            while (resultSet.next()) {
                Week week = new Week(resultSet.getString(1).charAt(0), resultSet.getString(2), Color.decode(resultSet.getString(3)), resultSet.getString(4));
                list.add(week);
            }
        } catch (SQLException e) {
            //Тут треба буде якось кидати явну помилку
            e.printStackTrace();
        }
    }

    public Week getWeekByName(String name) {
        //Вибираємо із списку тиждень за назвою і повертаємо його
        for (Week week : list) {
            if (week.getName().equals(name)) {
                return week;
            }
        }
        return null;
    }

    public Week getWeekByMark(char mark) {
        //Вибираємо із списку тиждень за позначенням і повертаємо його
        for (Week week: list) {
            if (week.getMark() == mark) {
                return week;
            }
        }
        return null;
    }

    public void addWeek(char mark, String name, Color color, String abbreviation) throws SQLException {
        //Створює новий тиждень, додає його до бази даних
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO weeks(mark, name, color, abbreviation) VALUE (" +
                "'" + mark + "', " +
                "'" + name + "', " +
                "'" + OtherFunction.ColorToHexString(color) + "', " +
                "'" + abbreviation + "');");
        loadFromDatabase();
        // TODO Треба б отимізувати тут. Замість повної загрузки даних можна працювати тільки зі списком
    }

    public void removeWeekByMark(char mark) throws SQLException {
        //Видаляємо тиждень із бази дани за назвою і повертаємо видалений елемент
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM weeks WHERE mark = '" + mark + "';");
        loadFromDatabase();
        // TODO Треба б отимізувати тут. Замість повної загрузки даних можна працювати тільки зі списком
    }

    public void removeWeekByName(String name) throws SQLException {
        //Видаляємо тиждень із бази дани за назвою і повертаємо видалений елемент
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM weeks WHERE name = '" + name + "';");
        loadFromDatabase();
        // TODO Треба б отимізувати тут. Замість повної загрузки даних можна працювати тільки зі списком
    }

    public ArrayList<Week> GetAllWeek() {
        return list;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        connection.close();
    }
}

//class Test {
//    public static void main(String[] args) {
//        try {
//            WeekList weekList = new WeekList(DriverManager.getConnection("jdbc:mysql://localhost:3306/mydata?useSSL=false", "Volodymyr", "0000"));
//            weekList.addWeek('9', "Настановча сесія", Color.DARK_GRAY, "НС");
//            Week week = weekList.getWeekByName("Настановча сесія");
//            if (week != null) {
//                System.out.println(week.getAbbreviation());
//            }
//            weekList.removeWeekByMark('9');
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
