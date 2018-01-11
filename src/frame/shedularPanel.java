package frame;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.Enumeration;

/**
 * Created by Vladimir on 10/01/18.
 **/
public class shedularPanel extends JPanel{
    private JTable table1;
    private JPanel panel1;
    private JList list1;
    private JButton встановитиДаніButton;
    private JButton додатиГрупуButton;
    private JButton видалитиГрупуButton;

    public shedularPanel(String name) {
        setName(name);
        setLayout(new GridLayout());
        add(panel1);
        table1.setModel(new SchedulerTableModel());
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        Enumeration<TableColumn> e = table1.getColumnModel().getColumns();
        while (e.hasMoreElements()) {
            TableColumn column = e.nextElement();
            if (column.getModelIndex() == 0) {
                column.setMinWidth(50);
            } else {
                column.setMinWidth(5);
            }
        }
    }
}

class SchedulerTableModel implements TableModel {

    @Override
    public int getRowCount() {
        return 10;
    }

    @Override
    public int getColumnCount() {
        return 10;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0: return "Група";
            default: return String.valueOf(columnIndex);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return String.class;
            default: return Character.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: switch (rowIndex) {
                case 0: return "ПС-46";
                default: return "ПС-47";
            }
            default: return 'Н';
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
