package app.frame;

import app.DegreeProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by Vladimir on 15/02/18.
 **/
public class SettingForm extends JDialog {
    private JTabbedPane tabbedPane1;
    private JPanel ContentPane;
    private JButton зберегтиButton;
    private JButton скасуватиButton;
    private JComboBox<LAFItem> LAFComboBox;
    private DefaultComboBoxModel<LAFItem> listModel = new DefaultComboBoxModel<>();

    public SettingForm() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(800, 600));
        setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2,
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2);
        setContentPane(ContentPane);
        setModal(true);

        listModel.addElement(new LAFItem("Windows"));
        listModel.addElement(new LAFItem("Java"));
        listModel.addElement(new LAFItem("JTattoo"));

        LAFComboBox.setModel(listModel);
        LAFComboBox.addActionListener(this::lafChoice);
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

        public LAFItem(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
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
