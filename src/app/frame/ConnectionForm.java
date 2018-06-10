package app.frame;

import app.DatabaseData;
import app.DegreeProject;
import app.data.DegreeDBMetaData;

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

import static javax.swing.JOptionPane.YES_OPTION;

/**
 * Created by Vladimir on 26/12/17.
 **/
public class ConnectionForm extends JFrame{
    private JPanel contentPane;
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
        setContentPane(contentPane);
        if (DegreeProject.mainIcon != null) this.setIconImage(DegreeProject.mainIcon);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                super.windowActivated(e);
                if (doConnect && autoConnect) {
                    connectionButtonClick(null);
                    autoConnect = false;
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
                createDatabaseScript();
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
            if (!DatabaseStructureChecker(rs)) {
                JOptionPane.showMessageDialog(null, "База даних не відповідає потрібній структурі!\n" +
                                "За допомогою зверність до системного адміністратора",
                        "Помилка", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e1) {
            SQLExceptionHandler(e1);
            return;
        }

        if (rememberCheckBox.isSelected()) rememberMe();
        DegreeProject.InitialMainFrame();
        dispose();
    }

    private boolean DatabaseStructureChecker(ResultSet rs) throws SQLException {
        while (rs.next()) if (!DegreeDBMetaData.DATABASE_TABLES.contains(rs.getString(1))) return false;
        return true;
    }

    private void SQLExceptionHandler(SQLException e1) {
        if (e1.getSQLState().equals("42000")) {
            int r = JOptionPane.showConfirmDialog(null, "База даних '" + DegreeProject.defaultDB + "' потрібна для роботи не знайдена! \n\r" +
                    "Створити нову базу?", "Налаштування бази даних", JOptionPane.YES_NO_CANCEL_OPTION);
            if (r == YES_OPTION) createDatabaseScript();
        } else {
            JOptionPane.showMessageDialog(this, "Підключення встановити не вдалося",
                    "Помилка підключення", JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
        }
    }

    private void createDatabaseScript() {
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
        jFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        jFrame.setVisible(true);
        WindowAdapter tempWindowAdapter = new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                createScriptWindowOpened(e);
            }

            private void createScriptWindowOpened(WindowEvent e) {
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
        };
        jFrame.addWindowListener(tempWindowAdapter);
    }

    private void dataBaseCreate(Connection connection, JProgressBar jProgressBar) {
        try (Statement statement = connection.createStatement()) {
            jProgressBar.setMaximum(DegreeDBMetaData.CREATE_COMMANDS.size() + DegreeDBMetaData.FILL_CONSTANT_COMMANDS.size());
            int counter = 0;
            for (String createCommand : DegreeDBMetaData.CREATE_COMMANDS) {
                statement.execute(createCommand);
                jProgressBar.setValue(counter++);
            }
            for (String fillConstantCommand : DegreeDBMetaData.FILL_CONSTANT_COMMANDS) {
                statement.execute(fillConstantCommand);
                jProgressBar.setValue(counter++);
            }
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
