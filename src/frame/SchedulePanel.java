package frame;

import app.*;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;

/**
 * Created by Vladimir on 10/01/18.
 **/
public class SchedulePanel extends JPanel{
    private JTable jTable;
    private JPanel mainPanel;
    private JList<Week> jList;
    private JButton settingGroupButton;
    private JButton saveButton;
    private JTextField authorTextField;
    private JButton prevYearButton;
    private JButton nextYearButton;
    private JLabel yearsLabel;
    private JLabel groupNameLabel;
    private JLabel studyDaysLabel;
    private JLabel weeksLabel;

    private SchedulerTableModel tableModel;

    SchedulePanel(String name) {
        setName(name);
        setLayout(new GridLayout());
        add(mainPanel);
        InitialTable();
        InitialList();
        settingGroupButton.addActionListener(this::settingGroupClick);
        InitialYearsPanel();
    }

    private void settingGroupClick(ActionEvent e) {
        int[] choice = new int[DegreeProject.GROUPLIST.GetAllWeek().size()];
        int count = 0;
        ArrayList<Group> tList = DegreeProject.GROUPLIST.GetAllWeek();
        for (int i = 0; i < tList.size(); i++) {
            for (int j = 0; j < tableModel.getAllScheduleUnits().size(); j++) {
                if (tList.get(i).getName().equals(tableModel.getAllScheduleUnits().get(j).getName())) {
                    choice[count++] = i;
                }
            }
        }
        choice = Arrays.copyOf(choice, count);
        new GroupChoiceDialog(DegreeProject.GROUPLIST.GetAllWeek(), choice, this::afterSettingGroup);
        saveButton.setEnabled(true);
    }

    private void afterSettingGroup(ArrayList<Group> list) {
        ArrayList<ScheduleUnit> listFromTable = new ArrayList<>(tableModel.getSizeScheduleUnits());
        ScheduleUnit tScheduleUnit;

        for (int i = tableModel.getSizeScheduleUnits() - 1; i >= 0; i--) {
            tScheduleUnit = tableModel.removeScheduleUnit(i);
            for (Group aListFromFrame : list) {
                if (tScheduleUnit.getName().equals(aListFromFrame.getName())) {
                    listFromTable.add(tScheduleUnit);
                }
            }
        }

        Group tGroup;
        boolean b = false;
        for (Group aListFromFrame : list) {
            tGroup = aListFromFrame;
            for (ScheduleUnit aListFromTable : listFromTable) {
                if (aListFromTable.getName().equals(tGroup.getName())) {
                    b = true;
                }
            }
            if (!b) listFromTable.add(new ScheduleUnit(tGroup));
            b = false;
        }

        for (ScheduleUnit unit :listFromTable) {
            tableModel.addScheduleUnit(unit);
        }
    }

    private void InitialYearsPanel() {
        yearsLabel.setText(Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.YEAR) + 1));
        prevYearButton.addActionListener(e -> {
            String[] lines = yearsLabel.getText().split("-");
            lines[0] = String.valueOf(Integer.parseInt(lines[0]) - 1);
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(lines[0]) - 1, Calendar.SEPTEMBER, 1);
            tableModel.setPeriods(c.getTime());
            lines[1] = String.valueOf(Integer.parseInt(lines[1]) - 1);
            yearsLabel.setText(lines[0] + "-" + lines[1]);
        });
        nextYearButton.addActionListener(e -> {
            String[] lines = yearsLabel.getText().split("-");
            lines[0] = String.valueOf(Integer.parseInt(lines[0]) + 1);
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(lines[0]) - 1, Calendar.SEPTEMBER, 1);
            tableModel.setPeriods(c.getTime());
            lines[1] = String.valueOf(Integer.parseInt(lines[1]) + 1);
            yearsLabel.setText(lines[0] + "-" + lines[1]);
        });
    }

    private void InitialList() {
        DefaultListModel<Week> listModel = new DefaultListModel<>();
        ArrayList<Week> weeks = DegreeProject.WEEKLIST.GetAllWeek();
        for (Week week : weeks) {
            listModel.addElement(week);
        }
        jList.setModel(listModel);
        jList.setCellRenderer(new ListRenderer());

    }

    private class ListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Week week = (Week)value;
            label.setText(week.getName());

            JPanel panel = new JPanel(new BorderLayout());

            JLabel colorLabel = new JLabel("   ");
            colorLabel.setOpaque(true);
            colorLabel.setBackground(week.getColor());

            panel.add(colorLabel, BorderLayout.LINE_START);
            panel.add(label, BorderLayout.CENTER);

            return panel;

        }
    }

    private void InitialTable() {
        tableModel = new SchedulerTableModel();
        tableModel.addTableModelListener(event -> {
            jTable.setRowHeight(0, 70);
            Enumeration<TableColumn> e = jTable.getColumnModel().getColumns();
            while (e.hasMoreElements()) {
                TableColumn column = e.nextElement();
                if (column.getModelIndex() == 0) {
                    column.setMinWidth(50);
                } else {
                    column.setMinWidth(5);
                }
            }
        });

        jTable.setModel(tableModel);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable.getTableHeader().setReorderingAllowed(false);
        jTable.getTableHeader().setResizingAllowed(false);
        jTable.setDefaultRenderer(Object.class, Week.getInstanceTableCellRendererComponent());
        tableModel.fireTableDataChanged();
        jTable.addMouseListener(new MouseListener() {
            int col = 0;
            @Override
            public void mouseClicked(MouseEvent e) {
                mouseClickEvent(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                col = jTable.columnAtPoint(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (jList.getSelectedIndex() < 0) {
                    // TODO Треба кидати ошибку, що не обрано жодного елемента із списку елементів
                    return;
                }
                int row = jTable.getSelectedRow();
                Week week = jList.getModel().getElementAt(jList.getSelectedIndex());
//                if (col > jTable.columnAtPoint(e.getPoint())) {
//                    for (int i = jTable.columnAtPoint(e.getPoint()); i >= col; i--) {
//                        jTable.setValueAt(week, row, i);
//                    }
//                } else {
                    for (int i = col; i <= jTable.columnAtPoint(e.getPoint()); i++) {
                        jTable.setValueAt(week, row, i);
                    }
//                }
                UpdateScheduleLabels(tableModel.getScheduleUnit(row - 3));
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
//Скрипт який виводить підсказки, хаває сильно багато процесорного часу
//        jTable.addMouseMotionListener(new MouseMotionListener() {
//            boolean b = false;
//            @Override
//            public void mouseDragged(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                if (b) {
//                    b = false;
//                    return;
//                }
//                String result = "";
//                int column = jTable.columnAtPoint(e.getPoint());
//                int row = jTable.rowAtPoint(e.getPoint());
//                if (column != - 1 || row != -1) {
//                    Object o = jTable.getValueAt(row, column);
//                    if (o.getClass() == Week.class) {
//                        result = ((Week)o).getName();
//                    } else if (o.getClass() == Integer.class) {
//                        result = String.valueOf(o);
//                    } else {
//                        result = (String)o;
//                    }
//                }
//                jTable.setToolTipText(result);
//                b = !b;
//            }
//        });
    }

    private void mouseClickEvent(MouseEvent e) {
        ScheduleUnit tScheduleUnit = tableModel.getScheduleUnit(jTable.getSelectedRow() - 3);
        if (tScheduleUnit == null) return;
        // Виконує встановлення значення тижня
        if (jList.getSelectedIndex() < 0) {
            // TODO Треба кидати ошибку, що не обрано жодного елемента із списку елементів
            return;
        }
        Week week = jList.getModel().getElementAt(jList.getSelectedIndex());
        jTable.setValueAt(week, jTable.getSelectedRow(), jTable.getSelectedColumn());

        UpdateScheduleLabels(tScheduleUnit);
    }

    private void UpdateScheduleLabels(ScheduleUnit tScheduleUnit) {
        // Виводить назву групи
        groupNameLabel.setText(tScheduleUnit.getName());

        // Виводить кількість навчальних днів
        Period tPeriod;
        int count = 0;
        for (int i = 0; i < 52; i++) {
            tPeriod = tableModel.getPeriod(i);
            if (!(tScheduleUnit.getWeek(i).getName().equals("Канікули") ||
                    tScheduleUnit.getWeek(i).getName().equals("Не визначено"))) {
                count += tPeriod.getWorkDay();
                // TODO треба переробити, бо якщо значення зміняться все піде крахом
            }
        }
        studyDaysLabel.setText(count + " днів");

        // Виводить кількість тижнів то типу
        HashMap<Week, Integer> map = new HashMap<>();
        for (int i = 0; i < 52; i++) {
            map.put(
                    tScheduleUnit.getWeek(i),
                    map.containsKey(tScheduleUnit.getWeek(i))
                            ? map.get(tScheduleUnit.getWeek(i)) + tableModel.getPeriod(i).getWorkDay()
                            : tableModel.getPeriod(i).getWorkDay());
        }
        weeksLabel.setText("<html>");
        for (Map.Entry<Week, Integer> mapItem: map.entrySet()){
            weeksLabel.setText(weeksLabel.getText() + mapItem.getKey().getName() + " &rArr; " + mapItem.getValue() + "<br>");
        }
        weeksLabel.setText(weeksLabel.getText() + "</html>");
    }
}