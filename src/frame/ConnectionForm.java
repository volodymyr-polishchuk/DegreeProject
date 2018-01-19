package frame;

import app.DatabaseData;
import app.DegreeProject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

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
    private JCheckBox rememberCheckBox;

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
            if (rememberCheckBox.isSelected()) {
                try {
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(
                                            new File(
                                                    new URI(getClass().getProtectionDomain().getCodeSource().getLocation() + "/databases.txt")
                                            ))));
                    writer.write(addressTextField.getText() + ";");
                    writer.write(portTextField.getText() + ";");
                    writer.write(userTextField.getText() + ";");
                    writer.write(String.valueOf(passwordField.getPassword()) + ";");
                    writer.write(databaseField.getText());
                    writer.flush();
                    writer.close();
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
            DegreeProject.InitialMainFrame();
            dispose();
        });
    }

}
