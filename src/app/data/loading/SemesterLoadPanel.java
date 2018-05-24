package app.data.loading;

import app.DegreeProject;
import app.data.*;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
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
    private JButton editGroupButton;
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
    private JButton зберегтиButton;
    private JButton експортуватиButton;

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
        editGroupButton.addActionListener(e -> System.out.println("SemesterLoadPanel.initialGroupListButtons"));

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
            }
        });
    }

    public SemesterLoadPanel(String title, SemesterLoad semesterLoad) {
        this(title);
        this.semesterLoad = semesterLoad;
    }

    private void initialTableButtons() {
        addTableRecordButton.addActionListener(e -> {
            loadingTableModel.addNewRecord();
            loadingTableModel.fireTableDataChanged();
        });
        removeTableRecordButton.addActionListener(e -> {
            loadingTableModel.removeRecord(mainTable.getSelectedRow());
            loadingTableModel.fireTableDataChanged();
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
            groupNameLabel.setText(loadingTableModel.getGroupLoad().getGroup().getName());
            weekCountComboBox.setSelectedItem(loadingTableModel.getGroupLoad().getWeekCount());
        }
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
            String sqlQuery = "SELECT * FROM teachers";
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
