package app.frame;

import app.TextFieldSQLActionListener;
import app.data.Auditory;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Vladimir on 16/02/18.
 **/
public class AuditoryDialogModify extends JDialog{
    private JPanel contentPane;
    private JTextField jTextField;
    private JButton cancelButton;
    private JButton saveButton;
    private Auditory auditory;

    private AuditoryDialogModify() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(250, 120));
        setTitle("Створення аудиторії");
        setResizable(false);
        setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2,
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2);
        setContentPane(contentPane);
        saveButton.addActionListener(e -> dispose());
        cancelButton.addActionListener(e -> onCancel());
        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        jTextField.addKeyListener(TextFieldSQLActionListener.instance);
    }

    private void onCancel() {
        this.jTextField.setText("");
        dispose();
    }

    private AuditoryDialogModify(Auditory auditory) {
        this();
        jTextField.setText(auditory.getName());
        this.auditory = auditory;
        setTitle("Редагування аудиторії");
    }

    @Nullable
    public static Auditory getModify() {
        AuditoryDialogModify dialog = new AuditoryDialogModify();
        dialog.setModal(true);
        dialog.setVisible(true);
        return dialog.jTextField.getText().isEmpty() ? null : new Auditory(dialog.jTextField.getText());
    }

    public static Auditory getModify(Auditory a) {
        if (a == null) throw new NullPointerException("Auditory must be not null");
        AuditoryDialogModify dialog = new AuditoryDialogModify(a);
        dialog.setModal(true);
        dialog.setVisible(true);
        return dialog.jTextField.getText().isEmpty() ? dialog.auditory : new Auditory(dialog.auditory.getKey(), dialog.jTextField.getText());
    }
}
