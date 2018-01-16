package frame;

import app.DatabaseData;
import app.DegreeProject;
import app.WeekList;

import javax.swing.*;
import java.awt.*;
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

    public ConnectionForm() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(ContentPanel);
        pack();
        setResizable(false);
        setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.getWidth()) / 2),
                (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.getHeight()) / 2));

//      JList
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("Databases.txt")));
//        DefaultListModel<String> listModel = new DefaultListModel<>();
//        bufferedReader.lines().forEach(s -> listModel.addElement(s.split(";")[0]));
//        list1.setModel(listModel);
//        list1.addListSelectionListener(e -> {
//            System.out.println(list1.getModel().getElementAt(list1.getSelectedIndex()));
//            Шукає перший елемент з таким іменем з'єднання і виводимо дані в поля
//        });


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
            MainForm mainForm = new MainForm();
            WEEKLIST = new WeekList(DegreeProject.databaseData.getConnection());
        });
    }

}
