package frame;

import app.DegreeProject;
import app.data.Department;
import app.data.Group;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Vladimir on 18/02/18.
 **/
public class GroupDialogModify extends JDialog{
    private JTextField nameTextField;
    private JPanel ContentPane;
    private JButton saveButton;
    private JButton cancelButton;
    private Group group;
    private JComboBox<Department> departmentComboBox;
    private JTextField commentsTextField;
    private DefaultComboBoxModel<Department> comboBoxModel = new DefaultComboBoxModel<>();

    private GroupDialogModify() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(300, 250));
        setModal(true);
        setTitle("Створення групи");
        setResizable(false);
        setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2,
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2);
        setContentPane(ContentPane);
        saveButton.addActionListener(e -> dispose());
        cancelButton.addActionListener(e -> {
            nameTextField.setText("");
            dispose();
        });
        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        departmentComboBox.setModel(comboBoxModel);
        departmentComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setText(((Department)(value)).getName());
                return label;
            }
        });
        try (Statement st = DegreeProject.databaseData.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM departments ORDER BY name")){
            while (rs.next()) {
                comboBoxModel.addElement(new Department(rs.getInt("k"), rs.getString("name")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    private GroupDialogModify(Group group) {
        this();
        nameTextField.setText(group.getName());
        this.group = group;
        setTitle("Редагування груп");
    }

    @Nullable
    public static Group getModify() {
        GroupDialogModify dialog = new GroupDialogModify();
        dialog.setVisible(true);
//
        return dialog.nameTextField.getText().isEmpty() ? null :
                new Group(
                        -1,
                        dialog.comboBoxModel.getElementAt(dialog.departmentComboBox.getSelectedIndex()),
                        dialog.nameTextField.getText(),
                        dialog.commentsTextField.getText());
    }

    public static Group getModify(Group a) {
        if (a == null) throw new NullPointerException("Lesson must not null");
        GroupDialogModify dialog = new GroupDialogModify(a);
        dialog.departmentComboBox.setSelectedItem(a.getDepartment());
        dialog.commentsTextField.setText(a.getComments());
        dialog.setVisible(true);
//
        return dialog.nameTextField.getText().isEmpty() ? dialog.group :
                new Group(
                        dialog.group.getKey(),
                        dialog.comboBoxModel.getElementAt(dialog.departmentComboBox.getSelectedIndex()),
                        dialog.nameTextField.getText(),
                        dialog.commentsTextField.getText());
    }
}
