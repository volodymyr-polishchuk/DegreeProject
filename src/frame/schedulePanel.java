package frame;

import app.DegreeProject;
import app.Group;
import app.ScheduleUnit;
import app.Week;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by Vladimir on 10/01/18.
 **/
public class schedulePanel extends JPanel{
    private JTable jTable;
    private JPanel mainPanel;
    private JList jList;
    private JButton додатиГрупуButton;
    private JButton вилучитиГрупуButton;
    private JButton зберегтиЗміниButton;
    private JTextField authorTextField;
    private JButton prevYearButton;
    private JButton nextYearButton;
    private JLabel YearsLabel;
    private JList list1;

    private SchedulerTableModel tableModel;

    public schedulePanel(String name) {
        setName(name);
        setLayout(new GridLayout());
        add(mainPanel);
        InitialTable();
        InitialList();
        додатиГрупуButton.addActionListener(e ->
                tableModel.addScheduleUnit(new ScheduleUnit(new Group("Програмування", authorTextField.getText())))
        );
        InitialButtons();
    }

    private void InitialButtons() {
        prevYearButton.addActionListener(e -> {
            String[] lines = YearsLabel.getText().split("-");
            lines[0] = String.valueOf(Integer.parseInt(lines[0]) - 1);
            lines[1] = String.valueOf(Integer.parseInt(lines[1]) - 1);
            YearsLabel.setText(lines[0] + "-" + lines[1]);
        });
        nextYearButton.addActionListener(e -> {
            String[] lines = YearsLabel.getText().split("-");
            lines[0] = String.valueOf(Integer.parseInt(lines[0]) + 1);
            lines[1] = String.valueOf(Integer.parseInt(lines[1]) + 1);
            YearsLabel.setText(lines[0] + "-" + lines[1]);
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
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent event) {
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
            }
        });

        jTable.setModel(tableModel);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable.getTableHeader().setReorderingAllowed(false);
        jTable.getTableHeader().setResizingAllowed(false);
        jTable.setDefaultRenderer(Week.class, Week.getInstanceTableCellRendererComponent());
        jTable.setDefaultRenderer(String.class, new Temp());
        tableModel.fireTableDataChanged();
        jTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (jList.getSelectedIndex() < 0) {
                    // TODO Треба кидати ошибку, що не обрано жодного елемента із списку елементів
                    return;
                }
                Week week = DegreeProject.WEEKLIST.getWeekByName(
                        (String) jList.getModel().getElementAt(
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

class Temp extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setBackground(new Color(235, 235, 235));
        return label;
    }
}

