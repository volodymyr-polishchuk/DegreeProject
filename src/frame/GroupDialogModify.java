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
public class GroupDialogModify {
    private JTextField nameTextField;
    private JPanel ContentPane;
    private JButton saveButton;
    private JButton cancelButton;
    private JComboBox departmentComboBox;
    private DefaultComboBoxModel<Department> comboBoxModel = new DefaultComboBoxModel<>();

//    private LessonDialogModify() {
//        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//        setSize(new Dimension(250, 180));
//        setModal(true);
//        setTitle("Створення предметів");
//        setResizable(false);
//        setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2,
//                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2);
//        setContentPane(ContentPane);
//        saveButton.addActionListener(e -> dispose());
//        cancelButton.addActionListener(e -> {
//            nameTextField.setText("");
//            dispose();
//        });
//        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
//        auditoryComboBox.setModel(comboBoxModel);
//        auditoryComboBox.setRenderer(new DefaultListCellRenderer() {
//            @Override
//            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//                JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//                label.setText(((Auditory)(value)).getName());
//                return label;
//            }
//        });
//        try (Statement st = DegreeProject.databaseData.getConnection().createStatement();
//             ResultSet rs = st.executeQuery("SELECT * FROM auditorys")){
//            while (rs.next()) {
//                comboBoxModel.addElement(new Auditory(rs.getInt("k"), rs.getString("name")));
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    private LessonDialogModify(Lesson lesson) {
//        this();
//        nameTextField.setText(lesson.getName());
//        this.lesson = lesson;
//        setTitle("Редагування предметів");
//    }
//
//    @Nullable
//    public static Lesson getModify() {
//        LessonDialogModify dialog = new LessonDialogModify();
//        dialog.setVisible(true);
////
//        return dialog.nameTextField.getText().isEmpty() ? null :
//                new Lesson(
//                        dialog.nameTextField.getText(),
//                        dialog.comboBoxModel.getElementAt(dialog.auditoryComboBox.getSelectedIndex()));
//    }
//
//    public static Lesson getModify(Lesson a) {
//        if (a == null) throw new NullPointerException("Lesson must not null");
//        LessonDialogModify dialog = new LessonDialogModify(a);
//        dialog.setVisible(true);
////
//        return dialog.nameTextField.getText().isEmpty() ? dialog.lesson :
//                new Lesson(
//                        dialog.lesson.getKey(),
//                        dialog.nameTextField.getText(),
//                        dialog.comboBoxModel.getElementAt(dialog.auditoryComboBox.getSelectedIndex()));
//    }
}
