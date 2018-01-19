package frame;

import app.*;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;

/**
 * Created by Vladimir on 10/01/18.
 **/
public class SchedulePanel extends JPanel{
    private JTable jTable;
    private JPanel mainPanel;
    private JList<String> jList;
    private JButton settingGroupButton;
    private JButton зберегтиЗміниButton;
    private JTextField authorTextField;
    private JButton prevYearButton;
    private JButton nextYearButton;
    private JLabel yearsLabel;

    private SchedulerTableModel tableModel;

    public SchedulePanel(String name) {
        setName(name);
        setLayout(new GridLayout());
        add(mainPanel);
        InitialTable();
        InitialList();
        settingGroupButton.addActionListener(e ->  {
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
                new GroupChoiceDialog(DegreeProject.GROUPLIST.GetAllWeek(), choice, (GroupChoiceListener) this::afterSettingGroup);
            }
        );
        InitialYearsPanel();
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
            lines[1] = String.valueOf(Integer.parseInt(lines[1]) - 1);
            yearsLabel.setText(lines[0] + "-" + lines[1]);
        });
        nextYearButton.addActionListener(e -> {
            String[] lines = yearsLabel.getText().split("-");
            lines[0] = String.valueOf(Integer.parseInt(lines[0]) + 1);
            lines[1] = String.valueOf(Integer.parseInt(lines[1]) + 1);
            yearsLabel.setText(lines[0] + "-" + lines[1]);
        });
    }

    private void InitialList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        ArrayList<Week> weeks = DegreeProject.WEEKLIST.GetAllWeek();
        for (Week week :
                weeks) {
            listModel.addElement(week.getName());
        }
        jList.setModel(listModel);
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
            @Override
            public void mouseClicked(MouseEvent e) {
                if (jList.getSelectedIndex() < 0) {
                    // TODO Треба кидати ошибку, що не обрано жодного елемента із списку елементів
                    return;
                }
                Week week = DegreeProject.WEEKLIST.getWeekByName(
                        jList.getModel().getElementAt(
                                jList.getSelectedIndex()
                        )
                );
                jTable.setValueAt(week, jTable.getSelectedRow(), jTable.getSelectedColumn());
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
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
//            @Override
//            public void mouseDragged(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseMoved(MouseEvent e) {
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
//            }
//        });
    }
}