package Frame;

import javafx.application.Application;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Vladimir on 25/12/17.
 **/
public class LogForm extends JFrame {
    private JTextField FieldText;
    private JPasswordField FieldPassword;
    private JButton ButtonIn;
    private JButton ButtonClose;
    private JPanel MainPanel;

    public LogForm() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setContentPane(MainPanel);
        setSize(new Dimension(250, 180));
        ButtonIn.addActionListener(e -> {
            if ((FieldText.getText().equals("Volodymyr")) &&
                    (FieldPassword.getPassword() == "0000".toCharArray())) {
                FieldText.setText("Success");
            } else {
                FieldText.setText("Fail");
            }
        });
        ButtonClose.addActionListener(e -> {
            dispose();
        });
    }

    public static void main(String[] args) {
        JFrame frame = new LogForm();
    }
}
