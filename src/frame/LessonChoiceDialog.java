package frame;

import app.DegreeProject;
import app.data.Group;
import app.lessons.LessonsUnit;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LessonChoiceDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<ListItem> jList;
    private DefaultListModel<ListItem> listModel = new DefaultListModel<>();
    private Connection connection;

    public LessonChoiceDialog(Connection connection) {
        this.connection = connection;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        jList.setModel(listModel);
        listData();

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void listData() {
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM lessons_schedules");
            while (rs.next()) listModel.addElement(new ListItem(rs.getString("period")));
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void onOK() {
        ListItem item = jList.getSelectedValue();
        DegreeProject.mainForm.addTab(new LessonsPanel("Розклад занять за " + item.getPeriod(), item.getPeriod()));
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        LessonChoiceDialog dialog = new LessonChoiceDialog(DegreeProject.databaseData.getConnection());
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private class ListItem {
        private String period;

        public ListItem(String period) {
            this.period = period;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        @Override
        public String toString() {
            return "Розклад занять за період " + period;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ListItem listItem = (ListItem) o;

            return period != null ? period.equals(listItem.period) : listItem.period == null;

        }

        @Override
        public int hashCode() {
            return period != null ? period.hashCode() : 0;
        }
    }
}
