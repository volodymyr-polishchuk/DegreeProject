package app.data;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Vladimir on 17/01/18.
 **/
public class GroupList {
    ArrayList<Group> list;
    Connection connection;

    public GroupList(Connection connection) throws SQLException {
        this.connection = connection;
        list = new ArrayList<>();
        loadFromDatabase();
    }

    private void loadFromDatabase() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM groups");
            list.clear();
            while (resultSet.next()) {
                list.add(new Group(resultSet));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Помилка читання даних з бази \n\r MySQL -> " + e.getSQLState());
            e.printStackTrace();
        }
    }

    public Group getByName(String name) {
        for (Group group : list) {
            if (group.getName().equals(name)) {
                return group;
            }
        }
        return new Group();
    }

    public int getIndexByName(String name) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList<Group> GetAllWeek() {
        return list;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
