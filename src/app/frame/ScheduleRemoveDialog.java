package app.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Vladimir on 16/02/18.
 **/
public class ScheduleRemoveDialog extends JDialog{
    private JButton removeButton;
    private JButton cancelButton;
    private JList<String> jList;
    private JPanel contentPane;
    private Connection connection;
    private DefaultListModel<String> listModel = new DefaultListModel<>();

    public ScheduleRemoveDialog(Connection connection) throws HeadlessException {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(300, 300));
        setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2,
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2);
        setModal(true);
        setTitle("Видалення графіку навчання");

        setContentPane(contentPane);
        jList.setModel(listModel);
        this.connection = connection;

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM schedules")) {
            while (rs.next()) {
                listModel.addElement("Навчальний графік " + rs.getString("period"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }


        cancelButton.addActionListener(e -> dispose());

        removeButton.addActionListener(this::removeClick);
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        dispose();
    }

    private void removeClick(ActionEvent event) {
        try (Statement st = connection.createStatement()) {
            int r = JOptionPane.showConfirmDialog(null, "Ви впевнені, що бажає видалити: " + listModel.getElementAt(jList.getSelectedIndex()), "Видалення", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (r == JOptionPane.NO_OPTION || r == JOptionPane.CANCEL_OPTION) return;
            String period = listModel.getElementAt(jList.getSelectedIndex()).split(" ")[2];
            st.execute("DELETE FROM schedules_data WHERE schedule LIKE (SELECT k FROM schedules WHERE period LIKE '" + period + "')");
            st.execute("DELETE FROM schedules WHERE period LIKE '" + period + "'");
            JOptionPane.showMessageDialog(null, "Видалення пройшло успішно", "Видалення", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }
}
