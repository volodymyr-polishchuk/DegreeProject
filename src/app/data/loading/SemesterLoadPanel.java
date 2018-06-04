package app.data.loading;

import app.DegreeProject;
import app.data.*;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Created by Vladimir on 23/05/18.
 **/
public class SemesterLoadPanel extends JPanel {
    private JPanel contentPane;
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JList<GroupLoad> groupList;
    private JTable mainTable;
    private JButton addGroupButton;
    private JButton removeGroupButton;
    private JTextField periodTextField;
    private JButton prevPeriodButton;
    private JButton nextPeriodButton;
    private JComboBox semesterComboBox;
    private JLabel groupNameLabel;
    private JButton addTableRecordButton;
    private JButton removeTableRecordButton;
    private JComboBox<Integer> weekCountComboBox;
    private JButton moveRecordUpButton;
    private JButton moveRecordDownButton;
    private JButton saveButton;
    private JButton exportButton;
    private JLabel weekLoadingCountLabel;
    private JPanel hiddenLeftPanel;
    private JLabel hideLeftPanelLabel;
    private JLabel showLeftPanelLabel;

    private LoadingTableModel loadingTableModel;
    private SemesterLoad semesterLoad;
    private LoadingGroupListModel loadingGroupListModel;

    public SemesterLoadPanel(String title) {
        setName(title);
        setLayout(new GridLayout());
        add(contentPane);
        initialMainTable();
        initialWeekCountComboBox();
        initialTableButtons();
        semesterLoad = new SemesterLoad();
        initialGroupListBox();
        initialGroupListButtons();
        initialPeriodsButtons();
    }

    public SemesterLoadPanel(String title, SemesterLoad semesterLoad) {
        this(title);
        this.semesterLoad = semesterLoad;
        initialGroupListBox();
        if (semesterLoad.getGroupLoads().size() > 0)
            removeGroupButton.setEnabled(true);
        updatePanelData();
    }

    private void initialPeriodsButtons() {
        prevPeriodButton.addActionListener(e -> {
            periodTextField.setText(semesterLoad.prevPeriod());
        });
        nextPeriodButton.addActionListener(e -> {
            periodTextField.setText(semesterLoad.nextPeriod());
        });
        semesterComboBox.addActionListener(e -> {
            semesterLoad.setSemester(semesterComboBox.getSelectedIndex() == 0);
        });
    }

    private void initialGroupListButtons() {
        showLeftPanelLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                leftPanel.setVisible(true);
                hiddenLeftPanel.setVisible(false);
            }
        });
        hideLeftPanelLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                leftPanel.setVisible(false);
                hiddenLeftPanel.setVisible(true);
            }
        });
        addGroupButton.addActionListener(e -> {
            JFrame frame = new FireChoiceGroupFrame() {
                @Override
                void onChoice(Group group) {
                    semesterLoad.add(group);
                    loadingGroupListModel.fireDataChange();
                    removeGroupButton.setEnabled(true);
                    groupList.setSelectedIndex(loadingGroupListModel.getSize() - 1);
                }
            };
            frame.setVisible(true);
        });

        removeGroupButton.addActionListener(e -> {
            if (groupList.getSelectedIndex() >= 0) {
                semesterLoad.getGroupLoads().remove(groupList.getSelectedIndex());
                loadingGroupListModel.fireDataChange();
                if (loadingGroupListModel.getSize() == 0) {
                    addTableRecordButton.setEnabled(false);
                    removeTableRecordButton.setEnabled(false);
                    moveRecordUpButton.setEnabled(false);
                    moveRecordDownButton.setEnabled(false);
                    weekCountComboBox.setEnabled(false);
                    removeGroupButton.setEnabled(false);
                    loadingTableModel.setGroupLoad(null);
                    loadingTableModel.fireTableDataChanged();
                }
            }
        });
    }

    private void initialGroupListBox() {
        loadingGroupListModel = new LoadingGroupListModel(semesterLoad);
        groupList.setModel(loadingGroupListModel);
        groupList.setCellRenderer(new LoadingGroupCellRenderer());
        groupList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadingTableModel.setGroupLoad(groupList.getSelectedValue());
                loadingTableModel.fireTableDataChanged();
                addTableRecordButton.setEnabled(true);
                removeTableRecordButton.setEnabled(true);
                moveRecordUpButton.setEnabled(true);
                moveRecordDownButton.setEnabled(true);
                weekCountComboBox.setEnabled(true);
                exportButton.setEnabled(true);
            }
        });
    }

    private void initialTableButtons() {
        addTableRecordButton.addActionListener(e -> {
            loadingTableModel.addNewRecord();
            loadingTableModel.fireTableDataChanged();
            mainTable.setRowSelectionInterval(loadingTableModel.getRowCount() - 1, loadingTableModel.getRowCount() - 1);
        });
        removeTableRecordButton.addActionListener(e -> {
            int select = mainTable.getSelectedRow();
            loadingTableModel.removeRecord(mainTable.getSelectedRow());
            loadingTableModel.fireTableDataChanged();
            if (select < loadingTableModel.getRowCount() && loadingTableModel.getRowCount() > 0)
                mainTable.setRowSelectionInterval(select, select);
            else if (loadingTableModel.getRowCount() > 0) {
                mainTable.setRowSelectionInterval(select - 1, select - 1);
            }
        });
        moveRecordUpButton.addActionListener(e -> {
            if (loadingTableModel.moveRecordUp(mainTable.getSelectedRow())) {
                int select = mainTable.getSelectedRow();
                loadingTableModel.fireTableDataChanged();
                mainTable.setRowSelectionInterval(select - 1, select - 1);
            }
        });
        moveRecordDownButton.addActionListener(e -> {
            if (loadingTableModel.moveRecordDown(mainTable.getSelectedRow())) {
                int select = mainTable.getSelectedRow();
                loadingTableModel.fireTableDataChanged();
                mainTable.setRowSelectionInterval(select + 1, select + 1);
            }
        });
        saveButton.addActionListener(e -> {
            try {
                semesterLoad.writeToDatabase();
                JOptionPane.showMessageDialog(null, "Дані успішно збережені", "Повідомлення", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "Сервер повернув помилку:\n" + e1.getSQLState());
            }
        });
        exportButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    semesterLoad.exportToExcel(fileChooser.getSelectedFile());
                    int confirmResult = JOptionPane.showConfirmDialog(null, "Дані успішно збержено. Відкрити файл?", "Повідомлення", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (confirmResult == JOptionPane.YES_OPTION) {
                        Desktop.getDesktop().open(fileChooser.getSelectedFile());
                    }
                } catch (IOException err) {
                    err.printStackTrace();
                    JOptionPane.showMessageDialog(null, err.getMessage());
                }
            }

        });
    }

    private void initialWeekCountComboBox() {
        for (int i = 1; i <= 32; i++) {
            weekCountComboBox.addItem(i);
        }
        weekCountComboBox.addActionListener(this::weekCountActionListener);
    }

    private void weekCountActionListener(ActionEvent event) {
        if (loadingTableModel.getGroupLoad() != null) {
            loadingTableModel.getGroupLoad().setWeekCount((int)weekCountComboBox.getSelectedItem());
            loadingTableModel.fireTableDataChanged();
        }
    }

    private void initialMainTable() {
        loadingTableModel = new LoadingTableModel();
        mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainTable.setDefaultRenderer(Number.class, new LoadingNumberTableCellRenderer());

        mainTable.getTableHeader().setReorderingAllowed(false);
        mainTable.getTableHeader().setResizingAllowed(false);

        loadingTableModel.addTableModelListener(event -> updateColumnSize());
        loadingTableModel.addTableModelListener(e -> updatePanelData());

        mainTable.setModel(loadingTableModel);
        mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        updateColumnSize();
        initialCellEditor();
    }

    private void updatePanelData() {
        if (loadingTableModel.getGroupLoad() != null) {
            groupNameLabel.setText(loadingTableModel.getGroupLoad().getGroup().getName() + " - " + loadingTableModel.getGroupLoad().getGroup().getDepartment().getName());
            weekCountComboBox.setSelectedItem(loadingTableModel.getGroupLoad().getWeekCount());
            weekLoadingCountLabel.setText(String.format("%.2f", loadingTableModel.getGroupLoad().getWeekLoadingSum()));
        }
        semesterComboBox.setSelectedIndex(semesterLoad.getSemester() ? 0 : 1);
        periodTextField.setText(semesterLoad.getYear() + "-" + (semesterLoad.getYear() + 1));
    }

    private void initialCellEditor() {
        JComboBox<Lesson> lessonJComboBox = new JComboBox<>();
        try {
            String sqlQuery = "SELECT * FROM lessons INNER JOIN auditorys ON lessons.auditory = auditorys.k ORDER BY lessons.name";
            ResultSet resultSet = DegreeProject.databaseData.getConnection().createStatement().executeQuery(sqlQuery);
            while (resultSet.next()) {
                Lesson lesson = new Lesson(resultSet.getInt("lessons.k"), resultSet.getString("lessons.name"),
                        new Auditory(resultSet.getInt("auditorys.k"), resultSet.getString("auditorys.name")));
                lessonJComboBox.addItem(lesson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mainTable.getColumnModel().getColumn(LoadingTableModel.LESSON_COLUMN).setCellEditor(new DefaultCellEditor(lessonJComboBox));

        JComboBox<Teacher> teacherJComboBox = new JComboBox<>();
        try {
            String sqlQuery = "SELECT * FROM teachers ORDER BY teachers.name";
            ResultSet resultSet = DegreeProject.databaseData.getConnection().createStatement().executeQuery(sqlQuery);
            while (resultSet.next()) {
                Teacher teacher = new Teacher(
                        resultSet.getInt("teachers.k"),
                        resultSet.getString("teachers.name"),
                        Preference.parsePreference(resultSet.getString("preferences"))
                );
                teacherJComboBox.addItem(teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mainTable.getColumnModel().getColumn(LoadingTableModel.TEACHER_COLUMN).setCellEditor(new DefaultCellEditor(teacherJComboBox));

        JComboBox<String> formControlComboBox = new JComboBox<>(new String[]{"ДПА", "Екзамен", "Залік", "відсутній"});
        formControlComboBox.setEditable(true);
        mainTable.getColumnModel().getColumn(LoadingTableModel.CONTROL_FORM_COLUMN).setCellEditor(new DefaultCellEditor(formControlComboBox));
    }

    private void updateColumnSize() {
        Enumeration<TableColumn> e = mainTable.getColumnModel().getColumns();
        while (e.hasMoreElements()) {
            TableColumn column = e.nextElement();
            switch (column.getModelIndex()) {
                case LoadingTableModel.NUMBER_COLUMN:
                    column.setPreferredWidth(10);
                    break;
                case LoadingTableModel.LESSON_COLUMN:
                    column.setPreferredWidth(200);
                    break;
                case LoadingTableModel.TEACHER_COLUMN:
                    column.setPreferredWidth(150);
                    break;
            }
        }
    }
}
