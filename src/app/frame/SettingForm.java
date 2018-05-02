package app.frame;

import app.DegreeProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Vladimir on 15/02/18.
 **/
public class SettingForm extends JDialog {
    private JTabbedPane jTabbedPane;
    private JPanel ContentPane;
    private JButton saveButton;
    private JButton cancelButton;
    private JComboBox<LAFItem> LAFComboBox;
    private JButton dropDatabaseButton;
    private JButton clearDatabaseButton;
    //    private JTextField databaseNameTextField;

    public SettingForm() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(800, 600));
        setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2,
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2);
        setContentPane(ContentPane);
        setModal(true);

        DefaultComboBoxModel<LAFItem> listModel = new DefaultComboBoxModel<>();
        listModel.addElement(new LAFItem("Windows"));
        listModel.addElement(new LAFItem("Java"));
        listModel.addElement(new LAFItem("JTattoo"));


        LAFComboBox.setModel(listModel);
        LAFComboBox.addActionListener(this::lafChoice);
        saveButton.addActionListener(e -> dispose());
        cancelButton.addActionListener(e -> dispose());

//        databaseNameTextField.setText(DegreeProject.defaultDB);

        dropDatabaseButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Ви намагаєтеся видалити базу даних!\nЦі дії відмінити не можливо!\nПродовжити?", "Увага", JOptionPane.YES_NO_CANCEL_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                try (Statement statement = DegreeProject.databaseData.getConnection().createStatement()) {
                    statement.execute("DROP DATABASE " + DegreeProject.defaultDB);
                    DegreeProject.mainForm.getMainFormMenuBar().MenuItemReconnect(null);
                    dispose();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        clearDatabaseButton.addActionListener(this::clearDatabaseData);
    }

    private void clearDatabaseData(ActionEvent event) {
        int result = JOptionPane.showConfirmDialog(null, "Ви намагаєтеся видалити всі дані!\nПродовжити?", "Увага", JOptionPane.YES_NO_CANCEL_OPTION);
        if (result != JOptionPane.YES_OPTION) return;
        try (Statement statement = DegreeProject.databaseData.getConnection().createStatement();
             PreparedStatement preparedStatement = DegreeProject.databaseData.getConnection().prepareStatement("DELETE FROM ?")) {
            ResultSet resultSet = statement.executeQuery("SHOW TABLES;");

            while (resultSet.next()) {
                preparedStatement.setString(1, resultSet.getString(1));
            }

            JOptionPane.showMessageDialog(null, "Всі дані успішно видалено!", "Повідомлення", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
     }

    private void lafChoice(ActionEvent event) {
        try {
            if (LAFComboBox.getSelectedItem().equals(new LAFItem("Windows"))) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } else if (LAFComboBox.getSelectedItem().equals(new LAFItem("Java"))) {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } else if (LAFComboBox.getSelectedItem().equals(new LAFItem("JTattoo"))) {
                UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
            }
            SwingUtilities.updateComponentTreeUI(DegreeProject.mainForm);
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private class LAFItem {
        private String name;

        LAFItem(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return " " + name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LAFItem lafItem = (LAFItem) o;

            return name != null ? name.equals(lafItem.name) : lafItem.name == null;

        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }
}
