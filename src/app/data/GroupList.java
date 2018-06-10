package app.data;

import app.DegreeProject;

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
    private ArrayList<Group> list;

    public GroupList() throws SQLException {
        list = new ArrayList<>();
        loadFromDatabase();
    }

    private void loadFromDatabase() {
        try {
            Statement statement = DegreeProject.databaseData.getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM groups JOIN departments ON departments.k = groups.department");
            list.clear();
            while (rs.next()) {
                list.add(new Group(rs.getInt("groups.k"), new Department(rs.getInt("departments.k"), rs.getString("departments.name")), rs.getString("groups.name")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Помилка читання даних з бази \n\r MySQL -> " + e.getSQLState());
            e.printStackTrace();
        }
    }

    public ArrayList<Group> getList() {
        return list;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void refresh() {
        loadFromDatabase();
    }
}
