package frame;

import app.DegreeProject;
import app.Group;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorChooserUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;

/**
 * Created by Vladimir on 20/01/18.
 **/
public class LessonsPanel extends JPanel{
    private JPanel contentPane;
    private JTable lessonsTable;
    private JButton налаштуванняButton;
    private JButton зберегтиЗміниButton;
    private JToggleButton button1;
    private JToggleButton button2;
    private JToggleButton button3;
    private JButton closeButton;

    public LessonsPanel() {
        setLayout(new GridLayout());
        add(contentPane);
        InitialTable();
        Border out = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY);
        Border outCenter = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY);
        Border in = BorderFactory.createEmptyBorder(3, 5, 3, 5);
        button1.setBorder(BorderFactory.createCompoundBorder(out, in));
        button1.setFocusable(false);
        button2.setBorder(BorderFactory.createCompoundBorder(outCenter, in));
        button2.setFocusable(false);
        button2.setSelected(true);
        button3.setBorder(BorderFactory.createCompoundBorder(out, in));
        button3.setFocusable(false);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(button1);
        buttonGroup.add(button2);
        buttonGroup.add(button3);

    }

    public LessonsPanel(String s) {
        this();
        setName(s);
    }

    private void InitialTable() {
        lessonsTable.setModel(new TableModel());
        lessonsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        lessonsTable.setDefaultRenderer(Object.class, new TableCellRenderer());
        lessonsTable.getTableHeader().setResizingAllowed(false);
        lessonsTable.getTableHeader().setReorderingAllowed(false);
        Enumeration<TableColumn> enumeration = lessonsTable.getColumnModel().getColumns();
        while (enumeration.hasMoreElements()) {
            TableColumn column = enumeration.nextElement();
            if ((column.getModelIndex()) % 2 == 0) {
                column.setMaxWidth(25);
            } else {
                column.setMinWidth(250);
            }
        }
    }

    private class TableModel extends AbstractTableModel {
        String[] daysName = {"ПОНЕДІЛОК", "ВІВТОРОК", "СЕРЕДА", "ЧЕТВЕРГ", "ПЯТНИЦЯ"};
        String[] groups = {"ПС-16", "ПС-26", "ПС-36", "ПС-46"};
        final int daysHeight = 10;
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public String getColumnName(int column) {
            if (column % 2 == 0) {
                return "";
            } else {
                return groups[column / 2];
            }
        }

        @Override
        public int getRowCount() {
            return daysHeight * 5;
        }

        @Override
        public int getColumnCount() {
            return groups.length * 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            try {
                if (columnIndex % 2 == 0) return daysName[rowIndex / daysHeight].charAt(rowIndex % 10);
                switch (columnIndex) {
                    default: return 31 * rowIndex * columnIndex;
                }
            } catch (StringIndexOutOfBoundsException e) {
                return "";
            }
        }
    }

    private class TableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(JLabel.CENTER);
            if (isSelected) return label;
            if (column % 2 == 0) {
                label.setBackground(table.getTableHeader().getBackground());
            } else if ((row / 10) % 2 == 0) {
                label.setBackground(new Color(246, 246, 246));
            } else {
                label.setBackground(Color.WHITE);
            }
            return label;
        }
    }
}
