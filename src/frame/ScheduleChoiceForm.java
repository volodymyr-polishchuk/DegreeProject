package frame;

import app.DegreeProject;
import app.data.Group;
import app.schedules.ScheduleUnit;

import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vladimir on 15/02/18.
 **/
public class ScheduleChoiceForm extends JFrame {
    private JButton choiceButton;
    private JButton cancelButton;
    private JList<String> jList;
    private JPanel ContentPane;
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    Connection connection;

    public ScheduleChoiceForm(Connection connection) {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(400, 300));
        setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2,
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2);
        setContentPane(ContentPane);
        jList.setModel(listModel);
        this.connection = connection;

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM schedules;");
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
        HashMap<Integer, Group> groups = new HashMap<>();
        HashMap<Integer, String> department = new HashMap<>();
        try {
            Statement st = connection.createStatement();
            ResultSet drs = st.executeQuery("SELECT * FROM departments;");
            while (drs.next()) {
                department.put(drs.getInt("k"), drs.getString("name"));
            }
            drs.close();
            ResultSet grs = st.executeQuery("SELECT * FROM groups;");
            while (grs.next()) {
                groups.put(grs.getInt("k"), new Group(department.get(grs.getInt("department")), grs.getString("name")));
            }
            ResultSet rsKey = st.executeQuery("SELECT * FROM schedules WHERE period = '" + year + "';");
            int key = 0;
            while (rsKey.next()) {
                key = rsKey.getInt("k");
            }
            ResultSet rs = st.executeQuery("SELECT * FROM schedules_data WHERE schedule = '" + key + "';");
            while (rs.next()) {
                ScheduleUnit unit = new ScheduleUnit(groups.get(rs.getInt("groups")));
                String weeks = rs.getString("data");
                for (int i = 0; i < 52; i++) {
                    unit.setWeek(i, DegreeProject.WEEKLIST.getWeekByMark(weeks.charAt(i)));
                }
                units.add(unit);
            }
            System.out.println(units);
            SchedulePanel panel = new SchedulePanel("Навчальний графік");
            units.forEach(unit -> {
                panel.getTableModel().addScheduleUnit(unit);
            });
            panel.setPeriod(year);
            dispose();
            DegreeProject.mainForm.addTab(panel, "Навчальний графік");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
