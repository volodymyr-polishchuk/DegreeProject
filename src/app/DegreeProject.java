package app;

import frame.ConnectionForm;

import java.io.*;
import java.sql.SQLException;

/**
 * Created by Vladimir on 01/01/18.
 **/
public class DegreeProject {
    public static DatabaseData dd;
    public static WeekList weekList = new WeekList(dd.getConnection());

    public static void main(String[] args) throws IOException {
        try {
            dd = new app.DatabaseData("localhost", "3306", "Volodymyr", "0000");
            dd.CreateDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectionForm connectionForm = new ConnectionForm();
        connectionForm.setVisible(true);
    }
}
