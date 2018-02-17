package frame;

import app.data.Lesson;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Vladimir on 17/02/18.
 **/
public class LessonDialogModify extends JDialog {
    private JPanel ContentPane;
    private JTextField nameTextField;
    private JComboBox auditoryComboBox;
    private JButton cancelButton;
    private JButton saveButton;
    private Lesson lesson;

    private LessonDialogModify() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(250, 180));
        setModal(true);
        setTitle("Створення предметів");
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

    private LessonDialogModify(Lesson lesson) {
        this();
        nameTextField.setText(lesson.getName());
        this.lesson = lesson;
        setTitle("Редагування предметів");
    }

    @Nullable
    public static Lesson getModify() {
        LessonDialogModify dialog = new LessonDialogModify();
        dialog.setVisible(true);
//
        return dialog.nameTextField.getText().isEmpty() ? null : new Lesson(dialog.nameTextField.getText());
    }

    public static Lesson getModify(Lesson a) {
        LessonDialogModify dialog = new LessonDialogModify(a);
        dialog.setVisible(true);
//
        return dialog.nameTextField.getText().isEmpty() ? dialog.lesson : new Lesson(dialog.nameTextField.getText());
    }

    public static void main(String[] args) {
        System.out.println(getModify());
    }
}
