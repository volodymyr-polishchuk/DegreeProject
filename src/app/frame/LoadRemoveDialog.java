package app.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.*;

/**
 * Created by Vladimir on 03/03/18.
 **/
public class LoadRemoveDialog extends JDialog {
    private JList<ListItem> jList;
    private DefaultListModel<ListItem> listModel = new DefaultListModel<>();
    private JButton removeButton;
    private JButton cancelButton;
    private JPanel contentPane;
    private Connection connection;

    public LoadRemoveDialog(Connection connection) {
        this.connection = connection;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        setLayout(new GridLayout());
        add(contentPane);
        getRootPane().setDefaultButton(removeButton);
        setTitle("Видалення навантаження");
        setSize(new Dimension(300, 300));
        setLocationRelativeTo(null);

        fillList();
        removeButton.addActionListener(this::removeButtonClick);
        cancelButton.addActionListener(e -> dispose());
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        dispose();
    }

    private void removeButtonClick(ActionEvent event) {
        String sqlRemoveData = "DELETE FROM semester_load WHERE semester_load.k = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlRemoveData)){
            ps.setInt(1, jList.getSelectedValue().getKey());
            ps.execute();
            dispose();
            JOptionPane.showMessageDialog(null, "Дані успішно видалено!", "Повідомлення бази даних", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    private void fillList() {
        String sql = "SELECT * FROM semester_load";
        try (ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            while (resultSet.next()) {listModel.addElement(new ListItem(resultSet.getInt("k"), resultSet.getString("period")));}
            jList.setModel(listModel);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    private class ListItem {
        private int key;
        private String period;

        ListItem(int key, String period) {
            this.key = key;
            this.period = period;
        }

        public String getPeriod() {
            return period;
        }

        public int getKey() {
            return key;
        }

        @Override
        public String toString() {
            return "Навантаження на " + period;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ListItem that = (ListItem) o;

            return period != null ? period.equals(that.period) : that.period == null;
        }

        @Override
        public int hashCode() {
            return period != null ? period.hashCode() : 0;
        }
    }
}
