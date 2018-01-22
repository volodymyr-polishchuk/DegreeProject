package frame;

import app.Group;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;

/**
 * Created by Vladimir on 20/01/18.
 **/
public class LessonsPanel extends JPanel{
    private JPanel contentPane;
    private JTable lessonsTable;
    private JList list1;
    private JButton налаштуванняButton;
    private JButton зберегтиЗміниButton;
    private JRadioButton чисельникRadioButton;
    private JRadioButton загальнийRadioButton;
    private JRadioButton знаменникRadioButton;

    public LessonsPanel() {
        setLayout(new GridLayout());
        add(contentPane);
        InitialTable();
    }

    private void InitialTable() {
        lessonsTable.setModel(new TableModel());
        lessonsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        lessonsTable.setDefaultRenderer(Object.class, new TableCellRenderer());
        Enumeration<TableColumn> enumeration = lessonsTable.getColumnModel().getColumns();
        while (enumeration.hasMoreElements()) {
            TableColumn column = enumeration.nextElement();
            if ((column.getModelIndex()) % 2 == 0) {
                column.setMaxWidth(25);
            }
        }
    }

    public LessonsPanel(String s) {
        this();
        setName(s);
    }

    private class TableModel extends AbstractTableModel {
        String[] daysName = {"ПОНЕДІЛОК", "ВІВТОРОК", "СЕРЕДА", "ЧЕТВЕРГ", "ПЯТНИЦЯ"};
        final int daysHeight = 10;
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0: return "";
                case 1: return "ПС-16";
                case 2: return "ПС-26";
                case 3: return "ПС-36";
                case 4: return "ПС-46";
                default: return "";
            }
        }

        @Override
        public int getRowCount() {
            return daysHeight * 5;
        }

        @Override
        public int getColumnCount() {
            return 6;
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
            return label;
        }
    }
}
