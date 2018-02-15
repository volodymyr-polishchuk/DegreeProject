package frame;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Vladimir on 15/02/18.
 **/
public class ScheduleChoiceForm extends JFrame {
    private JButton choiceButton;
    private JButton cancelButton;
    private JList<String> jList;
    private JPanel ContentPane;
    private DefaultListModel<String> listModel = new DefaultListModel<>();

    public ScheduleChoiceForm(Connection connection) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(new Dimension(400, 300));
        setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2,
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2);
        setContentPane(ContentPane);
        jList.setModel(listModel);

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

    }
}
