package frame;

import app.data.Preference;
import app.data.Teacher;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Vladimir on 17/02/18.
 **/
public class TeacherDialogModify extends JDialog{
    private JPanel ContentPane;
    private JTextField nameTextField;
    private JButton cancelButton;
    private JButton saveButton;
    private JCheckBox monCBox;
    private JCheckBox satCBox;
    private JCheckBox tueCBox;
    private JCheckBox wedCBox;
    private JCheckBox thuCBox;
    private JCheckBox friCBox;
    private Teacher teacher;

    private TeacherDialogModify() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(250, 230));
        setModal(true);
        setTitle("Створення викладачів");
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
    }

    private TeacherDialogModify(Teacher teacher) {
        this();
        nameTextField.setText(teacher.getName());
        Preference preference = teacher.getPreference();
        monCBox.setSelected(preference.isMonday());
        tueCBox.setSelected(preference.isTuesday());
        wedCBox.setSelected(preference.isWednesday());
        thuCBox.setSelected(preference.isThursday());
        friCBox.setSelected(preference.isFriday());
        satCBox.setSelected(preference.isSaturday());
        this.teacher = teacher;
        setTitle("Редагування викладачів");
    }

    @Nullable
    public static Teacher getModify() {
        TeacherDialogModify dialog = new TeacherDialogModify();
        dialog.setVisible(true);
//
        return dialog.nameTextField.getText().isEmpty() ? null : new Teacher(dialog.nameTextField.getText(),
                new Preference(
                        dialog.monCBox.isSelected(),
                        dialog.tueCBox.isSelected(),
                        dialog.wedCBox.isSelected(),
                        dialog.thuCBox.isSelected(),
                        dialog.friCBox.isSelected(),
                        dialog.satCBox.isSelected()));
    }

    public static Teacher getModify(Teacher a) {
        TeacherDialogModify dialog = new TeacherDialogModify(a);
        dialog.setVisible(true);
//
        return dialog.nameTextField.getText().isEmpty() ? dialog.teacher : new Teacher(dialog.nameTextField.getText(),
                new Preference(
                        dialog.monCBox.isSelected(),
                        dialog.tueCBox.isSelected(),
                        dialog.wedCBox.isSelected(),
                        dialog.thuCBox.isSelected(),
                        dialog.friCBox.isSelected(),
                        dialog.satCBox.isSelected()));
    }

    public static void main(String[] args) {
        System.out.println(getModify());
    }
}
