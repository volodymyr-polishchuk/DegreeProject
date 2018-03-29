package app.frame;

import app.DegreeProject;
import app.data.Department;
import app.data.Group;
import app.schedules.ScheduleUnit;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Vladimir on 15/02/18.
 **/
public class ScheduleChoiceDialog extends JDialog {
    private JButton choiceButton;
    private JButton cancelButton;
    private JList<String> jList;
    private JPanel ContentPane;
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private Connection connection;

    public ScheduleChoiceDialog(Connection connection) {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(300, 300));
        setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2,
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2);
        setModal(true);
        setTitle("Перегляд/редагування графіку навчання");

        setContentPane(ContentPane);
        jList.setModel(listModel);
        this.connection = connection;

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM schedules;")) {
            while (rs.next()) {
                listModel.addElement("Навчальний графік " + rs.getString("period"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        choiceButton.addActionListener(e -> {
            String s = listModel.elementAt(jList.getSelectedIndex());
            onChoice(s);
        });

        cancelButton.addActionListener(e -> {
            dispose();
        });
    }

    public void onChoice(String args) {
        String year = args.split(" ")[2];
        ArrayList<ScheduleUnit> units = new ArrayList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rsKey = st.executeQuery("SELECT * FROM schedules WHERE period = '" + year + "';");
            int key = 0;
            while (rsKey.next()) {
                key = rsKey.getInt("k");
            }
            rsKey.close();
            ResultSet rs = st.executeQuery(
                    "SELECT * FROM schedules_data INNER JOIN groups ON schedules_data.groups = groups.k INNER JOIN departments ON groups.department = departments.k WHERE schedule = '" + key + "'");
            while (rs.next()) {
                ScheduleUnit unit = new ScheduleUnit(
                        new Group(
                                rs.getInt("groups.k"),
                                new Department(rs.getInt("departments.k"), rs.getString("departments.name")),
                                rs.getString("groups.name"))
                );
                String weeks = rs.getString("data");
                for (int i = 0; i < 52; i++) {
                    unit.setWeek(i, DegreeProject.WEEKLIST.getWeekByMark(weeks.charAt(i)));
                }
                units.add(unit);
            }
            rs.close();
            SchedulePanel panel = new SchedulePanel("Навчальний графік");
            units.forEach(unit -> {
                panel.getTableModel().addScheduleUnit(unit);
            });
            panel.setPeriod(year);
            dispose();
            DegreeProject.mainForm.addTab(panel, "Навчальний графік");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }
}
