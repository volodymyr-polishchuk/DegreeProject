package app.data.loading;

import app.DegreeProject;
import app.data.Department;
import app.data.Group;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vladimir on 24/05/18.
 **/
public abstract class FireChoiceGroupFrame extends JFrame {
    private JList<Group> groupJList;
    private DefaultListModel<Group> listModel;
    private JPanel jPanel;

    public FireChoiceGroupFrame() throws HeadlessException {
        initialList();
        initialPanel();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocation(MouseInfo.getPointerInfo().getLocation());

        setContentPane(jPanel);
        setSize(250, 200);
        setResizable(false);

        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                dispose();
            }
        });
        groupJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onChoice(groupJList.getSelectedValue());
                dispose();
            }
        });
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    }

    private void initialPanel() {
        jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JScrollPane jScrollPane = new JScrollPane(groupJList);
        jPanel.add(jScrollPane);
        jPanel.setPreferredSize(new Dimension(250, 200));
    }

    private void initialList() {
        groupJList = new JList<>();
        listModel = new DefaultListModel<>();
        String sqlQuery = "SELECT * FROM groups INNER JOIN departments ON groups.department = departments.k ORDER BY departments.name, groups.name";
        try {
            ResultSet resultSet = DegreeProject.databaseData.getConnection().createStatement().executeQuery(sqlQuery);
            while (resultSet.next()) {
                listModel.addElement(new Group(
                        resultSet.getInt("groups.k"),
                        new Department(resultSet.getInt("departments.k"), resultSet.getString("departments.name")),
                        resultSet.getString("groups.name"), resultSet.getString("groups.comments")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        groupJList.setModel(listModel);
        DefaultListCellRenderer listCellRenderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setToolTipText(((Group)value).getComments());
                return label;
            }
        };
        groupJList.setCellRenderer(listCellRenderer);
    }

    abstract void onChoice(Group group);
}
