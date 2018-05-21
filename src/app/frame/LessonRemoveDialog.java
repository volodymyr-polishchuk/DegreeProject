package app.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.*;

/**
 * Created by Vladimir on 03/03/18.
 **/
public class LessonRemoveDialog extends JDialog {
    private JList<LessonsPeriod> jList;
    private DefaultListModel<LessonsPeriod> listModel = new DefaultListModel<>();
    private JButton removeButton;
    private JButton cancelButton;
    private JPanel contentPane;
    private Connection connection;

    public LessonRemoveDialog(Connection connection) {
        this.connection = connection;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        setLayout(new GridLayout());
        add(contentPane);
        getRootPane().setDefaultButton(removeButton);
        setTitle("Видалення розкладу занять");
        setSize(new Dimension(400, 300));
        setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2,
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2);

        fillList();
        removeButton.addActionListener(this::removeButtonClick);
        cancelButton.addActionListener(e -> dispose());
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        dispose();
    }

    private void removeButtonClick(ActionEvent event) {
        try {
            String sqlRemoveData = "DELETE FROM lessons_data WHERE lessons_schedule = (SELECT k FROM lessons_schedules WHERE period LIKE ?)";
            PreparedStatement ps = connection.prepareStatement(sqlRemoveData);
            ps.setString(1, jList.getSelectedValue().getPeriod());
            ps.execute();
            String sqlRemoveLessonsSchedule = "DELETE FROM lessons_schedules WHERE period LIKE ?";
            ps = connection.prepareStatement(sqlRemoveLessonsSchedule);
            ps.setString(1, jList.getSelectedValue().getPeriod());
            ps.execute();
            ps.close();
            JOptionPane.showMessageDialog(null, "Дані успішно видалено!", "Повідомлення бази даних", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }

    }

    private void fillList() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM lessons_schedules");
            while (resultSet.next()) {
                listModel.addElement(new LessonsPeriod(resultSet.getString("period")));
            }
            jList.setModel(listModel);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }

    }

    private class LessonsPeriod {
        private String period;

        LessonsPeriod(String period) {
            this.period = period;
        }

        public String getPeriod() {
            return period;
        }

        @Override
        public String toString() {
            return "Розклад занять за " + period;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LessonsPeriod that = (LessonsPeriod) o;

            return period != null ? period.equals(that.period) : that.period == null;

        }

        @Override
        public int hashCode() {
            return period != null ? period.hashCode() : 0;
        }
    }
}
