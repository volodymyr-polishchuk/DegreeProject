package app.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Vladimir on 01/04/18.
 **/
public class SQLConsole extends JDialog {
    private JPanel contentPane;
    private JTextPane consoleTextPane;
    private JTextField queryField;
    private JButton queryButton;
    private Connection connection;

    public SQLConsole(Connection connection) {
        this.connection = connection;
        setContentPane(contentPane);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(600, 600));
        setLocationRelativeTo(null);
        setModal(true);

        queryButton.addActionListener(this::queryButtonClick);
    }

    private void queryButtonClick(ActionEvent event) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryField.getText());
            String line = "";
            while (resultSet.next()) {
                int count = resultSet.getMetaData().getColumnCount();
                for (int i = 0; i < count; i++) {
                    line += resultSet.getString(i) + " | ";
                }
                line += "\n";
            }
            consoleTextPane.setText(consoleTextPane.getText() + "\n" + line);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
