package frame;

import app.*;

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
    private JButton встановитиДаніButton;
    private JButton додатиГрупуButton;
    private JButton видалитиГрупуButton;

    private SchedulerTableModel tableModel;

    public schedulePanel(String name) {
        setName(name);
        setLayout(new GridLayout());
        add(mainPanel);
        InitialTable();
        InitialList();
        додатиГрупуButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Group group = new Group("Програмування", "ПС-46");
                ScheduleUnit unit = new ScheduleUnit(group);
                unit.setWeek(0, DegreeProject.WEEKLIST.getWeekByName("Канікули"));
                unit.setWeek(1, DegreeProject.WEEKLIST.getWeekByName("Канікули"));
                unit.setWeek(2, DegreeProject.WEEKLIST.getWeekByName("Канікули"));
                unit.setWeek(3, DegreeProject.WEEKLIST.getWeekByName("Канікули"));
                unit.setWeek(4, DegreeProject.WEEKLIST.getWeekByName("Канікули"));
                unit.setWeek(5, DegreeProject.WEEKLIST.getWeekByName("Канікули"));
                unit.setWeek(6, DegreeProject.WEEKLIST.getWeekByName("Навчання"));
                unit.setWeek(7, DegreeProject.WEEKLIST.getWeekByName("Навчання"));
                unit.setWeek(8, DegreeProject.WEEKLIST.getWeekByName("Навчання"));
                unit.setWeek(9, DegreeProject.WEEKLIST.getWeekByName("Навчання"));
                unit.setWeek(10, DegreeProject.WEEKLIST.getWeekByName("Екзаменаційна сесія"));
                unit.setWeek(11, DegreeProject.WEEKLIST.getWeekByName("Екзаменаційна сесія"));
                unit.setWeek(12, DegreeProject.WEEKLIST.getWeekByName("Екзаменаційна сесія"));
                unit.setWeek(13, DegreeProject.WEEKLIST.getWeekByName("Технологічна практика"));
                tableModel.addScheduleUnit(unit);
            }
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

