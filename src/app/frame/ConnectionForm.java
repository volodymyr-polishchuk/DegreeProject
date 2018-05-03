package app.frame;

import app.DatabaseData;
import app.DegreeProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    private JLabel otherConnectionLabel;
    private JLabel createTables;
    private boolean autoConnect = false;
    private boolean doConnect = false;

    public ConnectionForm() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(ContentPanel);
        if (DegreeProject.icon != null) this.setIconImage(DegreeProject.icon);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                super.windowActivated(e);
                if (doConnect && autoConnect) {
                    connectionButtonClick(null);
                }
            }
        });

        connectButton.addActionListener(this::connectionButtonClick);
        researchSaveData();
        otherConnectionLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DegreeProject.defaultDB = JOptionPane.showInputDialog(null, "Введіть назву бази даних для підключення", "Зміна бази даних", JOptionPane.QUESTION_MESSAGE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                otherConnectionLabel.setForeground(new Color(0x000E9C));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                otherConnectionLabel.setForeground(new Color(0x4049E9));
            }
        });
        addressTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) portTextField.requestFocus();
            }
        });
        portTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) userTextField.requestFocus();
            }
        });
        userTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) passwordField.requestFocus();
            }
        });
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) connectButton.doClick();
            }
        });
        createTables.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JProgressBar jProgressBar = new JProgressBar(0, 20);
                jProgressBar.setValue(0);
                jProgressBar.setStringPainted(true);

                jProgressBar.setPreferredSize(new Dimension(200, 5));
                JFrame jFrame = new JFrame();
                jFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                jFrame.setLayout(new GridLayout(4, 1));
                jFrame.setSize(200, 80);
                jFrame.setLocationRelativeTo(null);
                jFrame.add(Box.createVerticalGlue());
                jFrame.add(new JLabel("Створення бази даних"));
                jFrame.add(jProgressBar);
                jFrame.add(Box.createVerticalGlue());
                jFrame.setResizable(false);
                jFrame.setUndecorated(true);
                jFrame.setVisible(true);
                jFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowOpened(WindowEvent e) {
                        super.windowOpened(e);
                        new Thread(() -> {
                            try {
                                DegreeProject.databaseData = new DatabaseData(addressTextField.getText(),
                                        portTextField.getText(),
                                        userTextField.getText(),
                                        passwordField.getPassword());
                                dataBaseCreate(DegreeProject.databaseData.getConnection(), jProgressBar);
                                jFrame.dispose();
                                JOptionPane.showMessageDialog(null, "База успішно створена", "Повідомлення", JOptionPane.INFORMATION_MESSAGE);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        }).start();
                    }
                });
            }
        });
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
                    new File(new URI(file.getParentFile().toURI() + "/database.ini")))));
            fileDataInput = reader.readLine();
            reader.close();
        } catch (URISyntaxException | IOException e) {
            System.out.println("File with database data not fount");
            e.printStackTrace();
        }

        if (!fileDataInput.isEmpty()) {
            String[] lines = fileDataInput.split(";");
            if (lines.length == 5) {
                addressTextField.setText(lines[0]);
                portTextField.setText(lines[1]);
                userTextField.setText(lines[2]);
                passwordField.setText(lines[3]);
                DegreeProject.defaultDB = lines[4];
                doConnect = true;
            }
        }
    }

    private void connectionButtonClick(ActionEvent event) {
        try {
            DegreeProject.databaseData = new DatabaseData(addressTextField.getText(),
                    portTextField.getText(), userTextField.getText(),
                    passwordField.getPassword(), DegreeProject.defaultDB);
            ResultSet rs = DegreeProject.databaseData.getConnection().createStatement().executeQuery("SHOW TABLES");
            HashSet<String> set = new HashSet<>();
            set.add("auditorys");       set.add("departments");         set.add("groups");          set.add("lessons");
            set.add("lessons_data");    set.add("lessons_schedules");   set.add("schedules");       set.add("schedules_data");
            set.add("teachers");        set.add("weeks");               set.add("holidays");
            while (rs.next()) {
                if (!set.contains(rs.getString(1))) {
                    JOptionPane.showMessageDialog(null, "База даних не відповідає потрібній структурі!",
                            "Помилка", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e1) {
            if (e1.getSQLState().equals("42000")) {
                int r = JOptionPane.showConfirmDialog(null, "База даних '" + DegreeProject.defaultDB + "' потрібна для роботи не знайдена! \n\r" +
                        "Створити нову базу?", "Налаштування бази даних", JOptionPane.YES_NO_CANCEL_OPTION);
                switch (r) {
                    case JOptionPane.YES_OPTION: {
                        JProgressBar jProgressBar = new JProgressBar(0, 20);
                        jProgressBar.setValue(0);
                        jProgressBar.setStringPainted(true);

                        jProgressBar.setPreferredSize(new Dimension(200, 5));
                        JFrame jFrame = new JFrame();
                        jFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                        jFrame.setLayout(new GridLayout(4, 1));
                        jFrame.setSize(200, 80);
                        jFrame.setLocationRelativeTo(null);
                        jFrame.add(Box.createVerticalGlue());
                        jFrame.add(new JLabel("Створення бази даних"));
                        jFrame.add(jProgressBar);
                        jFrame.add(Box.createVerticalGlue());
                        jFrame.setResizable(false);
                        jFrame.setUndecorated(true);
                        jFrame.setVisible(true);
                        jFrame.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowOpened(WindowEvent e) {
                                super.windowOpened(e);
                                new Thread(() -> {
                                    try {
                                        DegreeProject.databaseData = new DatabaseData(addressTextField.getText(),
                                                portTextField.getText(),
                                                userTextField.getText(),
                                                passwordField.getPassword());
                                        DegreeProject.databaseData.getConnection().createStatement().execute("CREATE DATABASE " + DegreeProject.defaultDB + ";");
                                        DegreeProject.databaseData.getConnection().createStatement().execute("USE " + DegreeProject.defaultDB + ";");
                                        dataBaseCreate(DegreeProject.databaseData.getConnection(), jProgressBar);
                                        jFrame.dispose();
                                        JOptionPane.showMessageDialog(null, "База успішно створена", "Повідомлення", JOptionPane.INFORMATION_MESSAGE);
                                    } catch (SQLException e1) {
                                        e1.printStackTrace();
                                    }
                                }).start();
                            }
                        });
                    }
                    default: return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Підключення встановити не вдалося",
                        "Помилка підключення", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
                return;
            }
        }

        if (rememberCheckBox.isSelected()) rememberMe();
        DegreeProject.InitialMainFrame();
        dispose();
    }

    private void dataBaseCreate(Connection connection, JProgressBar jProgressBar) {
        try (Statement statement = connection.createStatement()) {
            String createDepartment = "CREATE TABLE departments (k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(256) NOT NULL);";
            statement.execute(createDepartment);
            jProgressBar.setValue(1);
            String createWeeks = "CREATE TABLE weeks (mark VARCHAR(1) NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(256) NOT NULL, color INT(11) NOT NULL, abbreviation VARCHAR(16) NOT NULL);";
            statement.execute(createWeeks);
            jProgressBar.setValue(2);
            String addUniqueKeyWeeks = "CREATE UNIQUE INDEX weeks_mark_uindex ON weeks (mark);";
            statement.execute(addUniqueKeyWeeks);
            jProgressBar.setValue(3);
            String createGroups = "CREATE TABLE groups (name VARCHAR(256) NOT NULL, department INT(11) NOT NULL, dateofcreate DATE NOT NULL, comments VARCHAR(256), k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, CONSTRAINT groups_departments_k_fk FOREIGN KEY (department) REFERENCES departments (k));";
            statement.execute(createGroups);
            jProgressBar.setValue(4);
            String addPrimaryKeyGroup = "CREATE INDEX groups_departmentskfk ON groups (department);";
            statement.execute(addPrimaryKeyGroup);
            jProgressBar.setValue(5);
            String createSchedules = "CREATE TABLE schedules (period VARCHAR(256) NOT NULL, date_of_create DATE NOT NULL, coments VARCHAR(256), k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT);";
            statement.execute(createSchedules);
            jProgressBar.setValue(6);
            String createAuditorys = "CREATE TABLE auditorys (name VARCHAR(256) NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT);";
            statement.execute(createAuditorys);
            jProgressBar.setValue(7);
            String createTeachers = "CREATE TABLE teachers (name VARCHAR(256) NOT NULL, preferences VARCHAR(256), k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT);";
            statement.execute(createTeachers);
            jProgressBar.setValue(8);
            String createLessons = "CREATE TABLE lessons (name VARCHAR(256) NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, auditory INT(11) NOT NULL, CONSTRAINT lessons_ibfk_1 FOREIGN KEY (auditory) REFERENCES auditorys (k));";
            statement.execute(createLessons);
            jProgressBar.setValue(9);
            String addPrimaryKeyLessons = "CREATE INDEX auditory ON lessons (auditory);";
            statement.execute(addPrimaryKeyLessons);
            jProgressBar.setValue(10);
            String createSchedulesData = "CREATE TABLE schedules_data (schedule INT(11) NOT NULL, groups INT(11) NOT NULL, data VARCHAR(52) NOT NULL, k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, CONSTRAINT schedules_data_ibfk_1 FOREIGN KEY (schedule) REFERENCES schedules (k), CONSTRAINT schedules_data_ibfk_2 FOREIGN KEY (groups) REFERENCES groups (k) ON DELETE CASCADE);";
            statement.execute(createSchedulesData);
            jProgressBar.setValue(11);
            String addPrimaryKeySchedulesData1 = "CREATE INDEX schedule ON schedules_data (schedule);";
            statement.execute(addPrimaryKeySchedulesData1);
            jProgressBar.setValue(12);
            String addPrimaryKeySchedulesData2 = "CREATE INDEX schedules_dataibfk2 ON schedules_data (groups);";
            statement.execute(addPrimaryKeySchedulesData2);
            jProgressBar.setValue(13);
            String createLessonsSchedules = "CREATE TABLE lessons_schedules (k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, period VARCHAR(256) NOT NULL, date_of_create DATE NOT NULL, coments VARCHAR(256));";
            statement.execute(createLessonsSchedules);
            jProgressBar.setValue(14);
            String createLessonsData = "CREATE TABLE lessons_data (k INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, lessons_schedule INT(11) NOT NULL, groups INT(11) NOT NULL, pair_number VARCHAR(256) NOT NULL, lesson INT(11) NOT NULL, teacher INT(11) NOT NULL, auditory INT(11) NOT NULL, CONSTRAINT lessons_data_ibfk_1 FOREIGN KEY (lessons_schedule) REFERENCES lessons_schedules (k), CONSTRAINT lessons_data_ibfk_2 FOREIGN KEY (groups) REFERENCES groups (k), CONSTRAINT lessons_data_ibfk_3 FOREIGN KEY (lesson) REFERENCES lessons (k), CONSTRAINT lessons_data_ibfk_4 FOREIGN KEY (teacher) REFERENCES teachers (k), CONSTRAINT lessons_data_ibfk_5 FOREIGN KEY (auditory) REFERENCES auditorys (k));";
            statement.execute(createLessonsData);
            jProgressBar.setValue(15);
            String addPrimaryKeyLessonsData1 = "CREATE INDEX auditory ON lessons_data (auditory);";
            statement.execute(addPrimaryKeyLessonsData1);
            jProgressBar.setValue(16);
            String addPrimaryKeyLessonsData2 = "CREATE INDEX groups ON lessons_data (groups);";
            statement.execute(addPrimaryKeyLessonsData2);
            jProgressBar.setValue(17);
            String addPrimaryKeyLessonsData3 = "CREATE INDEX lesson ON lessons_data (lesson);";
            statement.execute(addPrimaryKeyLessonsData3);
            jProgressBar.setValue(18);
            String addPrimaryKeyLessonsData4 = "CREATE INDEX lessons_schedule ON lessons_data (lessons_schedule);";
            statement.execute(addPrimaryKeyLessonsData4);
            jProgressBar.setValue(19);
            String addPrimaryKeyLessonsData5 = "CREATE INDEX teacher ON lessons_data (teacher);";
            statement.execute(addPrimaryKeyLessonsData5);
            statement.execute("INSERT INTO departments (name) VALUE ('Будівельне відділення')");
            statement.execute("INSERT INTO departments (name) VALUE ('Відділення економіки-програмування')");
            statement.execute("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('0', 'Навчання', -1, ' ');");
            statement.execute("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('1', 'Канікули', -14575885, 'K');");
            statement.execute("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('2', 'Технологічна практика', -772554, 'Т');");
            statement.execute("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('3', 'Навчальна практика', -1499549, 'Н');");
            statement.execute("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('4', 'Переддипломна практика', -16728876, 'П');");
            statement.execute("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('5', 'Геодезетична практика', -7617718, 'Г');");
            statement.execute("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('6', 'Екзаменаційна сесія', -5317, 'С');");
            statement.execute("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('7', 'Дипломне проектування', -16777216, 'Ш');");
            statement.execute("INSERT INTO weeks (mark, name, color, abbreviation) VALUES ('8', 'Державна атестація', -43230, 'Д');");
            jProgressBar.setValue(20);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void rememberMe() {
        try {
            File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(
                                            new URI(file.getParentFile().toURI() + "/database.ini")))));
            writer.write(addressTextField.getText() + ";");
            writer.write(portTextField.getText() + ";");
            writer.write(userTextField.getText() + ";");
            writer.write(String.valueOf(passwordField.getPassword()) + ";");
            writer.write(DegreeProject.defaultDB);
            writer.flush();
            writer.close();
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }

}
