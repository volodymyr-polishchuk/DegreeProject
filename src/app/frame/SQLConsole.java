package app.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Vladimir on 01/04/18.
 **/
public class SQLConsole extends JDialog {
    private JPanel contentPane;
    private JTextField queryField;
    private JButton queryButton;
    private JTable queryTable;
    private JTextPane textPane1;
    private Connection connection;

    public SQLConsole(Connection connection) {
        this.connection = connection;
        setContentPane(contentPane);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(600, 600));
        setMinimumSize(new Dimension(600, 600));
        setLocationRelativeTo(null);
        setModal(true);

        queryButton.addActionListener(this::queryButtonClick);
        queryField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isControlDown()) {
                    queryButtonClick(null);
                }
            }
        });
        PopupMenu popupMenu = new PopupMenu();
        popupMenu.add(new MenuItem("Очистити")).addActionListener(e -> textPane1.setText(""));
        textPane1.add(popupMenu);
        textPane1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.show(textPane1, e.getX(), e.getY());
                }
            }
        });
    }

    private void queryButtonClick(ActionEvent event) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryField.getText());
            ArrayList<String> header = new ArrayList<>();
            ArrayList<ArrayList<String>> cells = new ArrayList<>();

            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                header.add(resultSet.getMetaData().getColumnName(i + 1));
            }

            while (resultSet.next()) {
                ArrayList<String> row = new ArrayList<>();
                for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                    String tLine = resultSet.getString(i + 1);
                    row.add(tLine);
                }
                cells.add(row);
            }
            String[][] outCells = new String[cells.size()][];
            for (int i = 0; i < cells.size(); i++) {
                outCells[i] = new String[cells.get(i).size()];
                for (int j = 0; j < cells.get(i).size(); j++) {
                    outCells[i][j] = cells.get(i).get(j);
                }
            }
            String[] outHeaders = new String[header.size()];
            for (int i = 0; i < header.size(); i++) {
                outHeaders[i] = header.get(i);
            }
            JTable table = new JTable(outCells, outHeaders);
            queryTable.setModel(table.getModel());
            log("Query success");
        } catch (SQLException e) {
            e.printStackTrace();
            log(e.getMessage());
        }
    }

    private void log(String text) {
        textPane1.setText(textPane1.getText() + text + "\n");
    }
}
