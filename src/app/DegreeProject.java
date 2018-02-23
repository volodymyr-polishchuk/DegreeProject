package app;

import app.data.GroupList;
import app.data.WeekList;
import frame.ConnectionForm;
import frame.HelloPanel;
import frame.HelloPanel2;
import frame.MainForm;

import javax.swing.*;
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
            UIManager.put("OptionPane.yesButtonText"   , "Да");
            UIManager.put("OptionPane.noButtonText"    , "Ні");
            UIManager.put("OptionPane.cancelButtonText", "Відміна");
            UIManager.put("OptionPane.okButtonText"    , "Готово");
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        ConnectionForm connectionForm = new ConnectionForm();
        connectionForm.setVisible(true);
//        try {
//            databaseData = new DatabaseData("localhost", "3306", "Volodymyr", new char[]{'0', '0', '0', '0'}, "mydata");
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Помилка підключення до бази даних. \n\r" + e.getMessage());
//            e.printStackTrace();
//
//        }
//        InitialMainFrame();
    }

    public static void InitialMainFrame() {
        mainForm = new MainForm();
//        mainForm.addTab(new HelloPanel("Головне меню програми"));
        mainForm.addTab(new HelloPanel2("Головне меню програми"));
        try {
            WEEKLIST = new WeekList(databaseData.getConnection());
            GROUPLIST = new GroupList(databaseData.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
