package frame;

import app.DatabaseData;
import app.DegreeProject;
import app.WeekList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import static app.DegreeProject.WEEKLIST;

/**
 * Created by Vladimir on 26/12/17.
 **/
public class ConnectionForm extends JFrame{
    private JPanel ContentPanel;
    private JTextField addressTextField;
    private JTextField portTextField;
    private JTextField userTextField;
    private JPasswordField passwordField;
    private JButton connectButton;
    private JTextField databaseField;
    private JCheckBox запамЯтатиCheckBox;

    public ConnectionForm() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(ContentPanel);
        pack();
        setResizable(false);
        setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.getWidth()) / 2),
                (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.getHeight()) / 2));

        connectButton.addActionListener(e -> {
            try {
                DegreeProject.databaseData = new DatabaseData(addressTextField.getText(),
                        portTextField.getText(),
                        userTextField.getText(),
                        passwordField.getPassword(),
                        databaseField.getText());
            } catch (SQLException e1) {
                e1.printStackTrace();
                return;
            }
            DegreeProject.InitialMainFrame();
        });
    }

}
