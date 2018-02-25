package frame;

import app.DatabaseData;
import app.DegreeProject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;

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
    private JCheckBox rememberCheckBox;

    public ConnectionForm() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(ContentPanel);
        pack();
        setResizable(false);
        setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.getWidth()) / 2),
                (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.getHeight()) / 2));
        String fileDataInput = "";
        try {
            File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
                    new File(new URI(file.getParentFile().toURI() + "/database.txt")))));
            fileDataInput = reader.readLine();
            reader.close();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        if (!fileDataInput.isEmpty()) {
            String[] lines = fileDataInput.split(";");
            if (lines.length == 5) {
                addressTextField.setText(lines[0]);
                portTextField.setText(lines[1]);
                userTextField.setText(lines[2]);
                passwordField.setText(lines[3]);
            }
        }

        connectButton.addActionListener(e -> {
            try {
                DegreeProject.databaseData = new DatabaseData(addressTextField.getText(),
                        portTextField.getText(),
                        userTextField.getText(),
                        passwordField.getPassword(),
                        "asfsc");
                ResultSet rs = DegreeProject.databaseData.getConnection().createStatement().executeQuery("SHOW TABLES");
                HashSet<String> set = new HashSet<>();
                set.add("auditorys"); set.add("departments");
                set.add("groups"); set.add("lessons");
                set.add("lessons_data"); set.add("lessons_schedules");
                set.add("schedules"); set.add("schedules_data");
                set.add("teachers"); set.add("weeks");
                while (rs.next()) {
                    if (!set.contains(rs.getString(1))) {
                        JOptionPane.showMessageDialog(
                                null,
                                "База даних не відповідає потрібній структурі!",
                                "Помилка",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException e1) {
                if (e1.getSQLState().equals("42000")) {
                    int r = JOptionPane.showConfirmDialog(null, "База даних 'asfcs' потрібна для роботи не знайдена! \n\r" +
                            "Створити нову базу?", "Налаштування бази даних", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (r) {
                        case JOptionPane.YES_OPTION: {
                            //TODO Виконувати створення і накатування бази даних
                            return;
                        }
                        default: return;
                    }
                }
                JOptionPane.showMessageDialog(this, "Підключення не встановлено. \n\r " +
                        "Сервер повернув помилку:" + e1.getMessage(),
                        "Помилка сервера", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
                return;
            }

            if (rememberCheckBox.isSelected()) rememberMe();
            logDate();
            DegreeProject.InitialMainFrame();
            dispose();
        });
    }

    private void logDate() {
        try {
            File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    new File(new URI(file.getParentFile().toURI() + "/log.txt")))));
            writer.write((new Date(System.currentTimeMillis())).toString());
            writer.flush();
            writer.close();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private void rememberMe() {
        try {
            File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(
                                    new File(
                                            new URI(file.getParentFile().toURI() + "/database.txt")
                                    ))));
            writer.write(addressTextField.getText() + ";");
            writer.write(portTextField.getText() + ";");
            writer.write(userTextField.getText() + ";");
            writer.write(String.valueOf(passwordField.getPassword()) + ";");
            writer.write("asfsc");
            writer.flush();
            writer.close();
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }

}
