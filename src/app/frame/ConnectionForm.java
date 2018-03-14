package app.frame;

import app.DatabaseData;
import app.DegreeProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
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
    private boolean autoConnect = false;
    private boolean doConnect = false;

    public ConnectionForm() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(ContentPanel);
        pack();
        setResizable(false);
        setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.getWidth()) / 2),
                (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.getHeight()) / 2));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                if (doConnect && autoConnect) {
                    connectButton.doClick();
                }
            }
        });

        connectButton.addActionListener(this::connectionButtonClick);
        researchSaveData();
    }

    public ConnectionForm(boolean autoConnect) {
        this();
        this.autoConnect = autoConnect;
    }

    private void researchSaveData() {
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
                doConnect = true;
            }
        }
    }

    private void connectionButtonClick(ActionEvent event) {
        try {
            DegreeProject.databaseData = new DatabaseData(addressTextField.getText(),
                    portTextField.getText(),
                    userTextField.getText(),
                    passwordField.getPassword(),
                    "asfsc");
            ResultSet rs = DegreeProject.databaseData.getConnection().createStatement().executeQuery("SHOW TABLES");
            HashSet<String> set = new HashSet<>();
            set.add("auditorys");       set.add("departments");
            set.add("groups");          set.add("lessons");
            set.add("lessons_data");    set.add("lessons_schedules");
            set.add("schedules");       set.add("schedules_data");
            set.add("teachers");        set.add("weeks");
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
                        try {
                            DegreeProject.databaseData = new DatabaseData(addressTextField.getText(),
                                    portTextField.getText(),
                                    userTextField.getText(),
                                    passwordField.getPassword());
                            DegreeProject.databaseData.getConnection().createStatement().execute("CREATE DATABASE asfsc;");
                            DegreeProject.databaseData.getConnection().createStatement().execute("USE asfsc;");
                            dataBaseCreate(DegreeProject.databaseData.getConnection());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } break;
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
    }

    private void dataBaseCreate(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            String createDepartment = "CREATE TABLE departments (k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(256) NOT NULL);";
            statement.execute(createDepartment);
            String createWeeks = "CREATE TABLE weeks (mark VARCHAR(1) NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(256) NOT NULL, color INT(11) NOT NULL, abbreviation VARCHAR(16) NOT NULL);";
            statement.execute(createWeeks);
            String addUniqueKeyWeeks = "CREATE UNIQUE INDEX weeks_mark_uindex ON weeks (mark);";
            statement.execute(addUniqueKeyWeeks);
            String createGroups = "CREATE TABLE groups (name VARCHAR(256) NOT NULL, department INT(11) NOT NULL, dateofcreate DATE NOT NULL, comments VARCHAR(256), k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, CONSTRAINT groups_departments_k_fk FOREIGN KEY (department) REFERENCES departments (k));";
            statement.execute(createGroups);
            String addPrimaryKeyGroup = "CREATE INDEX groups_departments_k_fk ON groups (department);";
            statement.execute(addPrimaryKeyGroup);
            String createSchedules = "CREATE TABLE schedules (period VARCHAR(256) NOT NULL, date_of_create DATE NOT NULL, coments VARCHAR(256), k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT);";
            statement.execute(createSchedules);
            String createAuditorys = "CREATE TABLE auditorys (name VARCHAR(256) NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT);";
            statement.execute(createAuditorys);
            String createTeachers = "CREATE TABLE teachers (name VARCHAR(256) NOT NULL, preferences VARCHAR(256), k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT);";
            statement.execute(createTeachers);
            String createLessons = "CREATE TABLE lessons (name VARCHAR(256) NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, auditory INT(11) NOT NULL, CONSTRAINT lessons_ibfk_1 FOREIGN KEY (auditory) REFERENCES auditorys (k));";
            statement.execute(createLessons);
            String addPrimaryKeyLessons = "CREATE INDEX auditory ON lessons (auditory);";
            statement.execute(addPrimaryKeyLessons);
            String createSchedulesData = "CREATE TABLE schedules_data (schedule INT(11) NOT NULL, groups INT(11) NOT NULL, data VARCHAR(52) NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, CONSTRAINT schedules_data_ibfk_1 FOREIGN KEY (schedule) REFERENCES schedules (k), CONSTRAINT schedules_data_ibfk_2 FOREIGN KEY (groups) REFERENCES groups (k) ON DELETE CASCADE);";
            statement.execute(createSchedulesData);
            String addPrimaryKeySchedulesData1 = "CREATE INDEX schedule ON schedules_data (schedule);";
            statement.execute(addPrimaryKeySchedulesData1);
            String addPrimaryKeySchedulesData2 = "CREATE INDEX schedules_data_ibfk_2 ON schedules_data (groups);";
            statement.execute(addPrimaryKeySchedulesData2);
            String createLessonsSchedules = "CREATE TABLE lessons_schedules (k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, period VARCHAR(256) NOT NULL, date_of_create DATE NOT NULL, coments VARCHAR(256));";
            statement.execute(createLessonsSchedules);
            String createLessonsData = "CREATE TABLE lessons_data (k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, lessons_schedule INT(11) NOT NULL, groups INT(11) NOT NULL, pair_number VARCHAR(256) NOT NULL, lesson INT(11) NOT NULL, teacher INT(11) NOT NULL, auditory INT(11) NOT NULL, CONSTRAINT lessons_data_ibfk_1 FOREIGN KEY (lessons_schedule) REFERENCES lessons_schedules (k), CONSTRAINT lessons_data_ibfk_2 FOREIGN KEY (groups) REFERENCES groups (k), CONSTRAINT lessons_data_ibfk_3 FOREIGN KEY (lesson) REFERENCES lessons (k), CONSTRAINT lessons_data_ibfk_4 FOREIGN KEY (teacher) REFERENCES teachers (k), CONSTRAINT lessons_data_ibfk_5 FOREIGN KEY (auditory) REFERENCES auditorys (k));";
            statement.execute(createLessonsData);
            String addPrimaryKeyLessonsData1 = "CREATE INDEX auditory ON lessons_data (auditory);";
            statement.execute(addPrimaryKeyLessonsData1);
            String addPrimaryKeyLessonsData2 = "CREATE INDEX groups ON lessons_data (groups);";
            statement.execute(addPrimaryKeyLessonsData2);
            String addPrimaryKeyLessonsData3 = "CREATE INDEX lesson ON lessons_data (lesson);";
            statement.execute(addPrimaryKeyLessonsData3);
            String addPrimaryKeyLessonsData4 = "CREATE INDEX lessons_schedule ON lessons_data (lessons_schedule);";
            statement.execute(addPrimaryKeyLessonsData4);
            String addPrimaryKeyLessonsData5 = "CREATE INDEX teacher ON lessons_data (teacher);";
            statement.execute(addPrimaryKeyLessonsData5);
            statement.execute("INSERT INTO departments (name) VALUE ('Будівельне відділення')");
            statement.execute("INSERT INTO departments (name) VALUE ('Відділення економіки-програмування')");
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/asfsc_weeks.sql")));
            reader.lines().forEach(s -> {
                try {
                    statement.execute(s);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
