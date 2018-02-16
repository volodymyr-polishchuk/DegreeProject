package frame;

import app.data.Auditory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by Vladimir on 16/02/18.
 **/
public class AuditoryModifyForm extends JFrame{
    private JPanel ContentPane;
    private JTextField jTextField;
    private JButton cancelButton;
    private JButton saveButton;

    public AuditoryModifyForm() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(150, 250));
        setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getWidth()) / 2,
                (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getHeight()) / 2);
        setContentPane(ContentPane);
        saveButton.addActionListener(this::saveButtonClick);
    }

    public AuditoryModifyForm(Auditory auditory) {
        this();
        jTextField.setText(auditory.getName());
    }

    private void saveButtonClick(ActionEvent event) {
        save(new Auditory(jTextField.getText()));
    }

    public void save(Auditory auditory) {

    }
}
