package app;

import frame.ConnectionForm;
import frame.HelloPanel;
import frame.MainForm;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Vladimir on 01/01/18.
 **/
public class DegreeProject {
    public static DatabaseData databaseData;
    public static WeekList WEEKLIST;
    public static GroupList GROUPLIST;
    public static MainForm mainForm;

    public static void main(String[] args) throws IOException, SQLException {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

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
        mainForm = new MainForm();
        mainForm.addTab(new HelloPanel("Головне меню програми"));
        try {
            WEEKLIST = new WeekList(databaseData.getConnection());
            GROUPLIST = new GroupList(databaseData.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
