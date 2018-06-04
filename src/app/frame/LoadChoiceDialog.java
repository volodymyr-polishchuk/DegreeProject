package app.frame;

import app.DegreeProject;
import app.data.loading.SemesterLoad;
import app.data.loading.SemesterLoadPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoadChoiceDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<ListItem> jList;
    private DefaultListModel<ListItem> listModel = new DefaultListModel<>();
    private Connection connection;

    public LoadChoiceDialog(Connection connection) {
        this.connection = connection;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(new Dimension(300, 300));
        setLocationRelativeTo(null);
        setTitle("Перегляд/редагування навантаження");

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
        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    e.consume();
                    buttonOK.doClick();
                }
            }
        });
    }

    private void listData() {
        try (ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM semester_load")){
            while (rs.next()) listModel.addElement(new ListItem(rs.getInt("k"), rs.getString("period")));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    protected void onOK() {
        ListItem item = jList.getSelectedValue();
        DegreeProject.mainForm.addTab(new SemesterLoadPanel("Навантаження на період " + item.getPeriod(), new SemesterLoad(item.getKey())), "Навантаження на період " + item.getPeriod());
        dispose();
    }

    protected JList<ListItem> getJList() {
        return jList;
    }

    private void onCancel() {
        dispose();
    }

    protected class ListItem {
        private int k;
        private String period;

        ListItem(int k, String period) {
            this.k = k;
            this.period = period;
        }

        public int getKey() {
            return k;
        }

        public String getPeriod() {
            return period;
        }

        @Override
        public String toString() {
            return "Навантаження на період " + period;
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
