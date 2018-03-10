package app;

import app.data.GroupList;
import app.data.WeekList;
import app.frame.ConnectionForm;
import app.frame.HelloPanel2;
import app.frame.MainForm;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

        ConnectionForm connectionForm = new ConnectionForm(true);
        connectionForm.setVisible(true);
    }

    public static void InitialMainFrame() {
        mainForm = new MainForm();
        mainForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    DegreeProject.databaseData.getConnection().close();
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
        try {
            WEEKLIST = new WeekList(databaseData.getConnection());
            GROUPLIST = new GroupList(databaseData.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
