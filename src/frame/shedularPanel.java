package frame;

import app.Period;
import app.Week;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table1.getTableHeader().setReorderingAllowed(false);
        table1.getTableHeader().setResizingAllowed(false);
        table1.setRowHeight(0, 70);
        table1.setDefaultRenderer(String.class, new SchedulerTableCellRenderer()); // Тут не правильно вказано, треба для кожного класу окремо назначати обробник
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
    private ArrayList<Period> periods = app.OtherFunction.GetWeekList(new Date(System.currentTimeMillis()));
    private Calendar c = Calendar.getInstance();

    @Override
    public int getRowCount() {
        return 5;
    }

    @Override
    public int getColumnCount() {
        return 53;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0: return "Група";
            default: return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
//            case 0: return String.class;
// Треба буде добавити під Week
            default: return String.class;
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
                case 0: return "<html><b>Період</b></html>";
                case 1: return "<html><b>Тиждень</b></html>";
                case 2: return "ПС-46";
                case 3: return "ПС-47";
                case 4: return "ПС-48";
                default: return "";
            }
            default: switch (rowIndex) {
                case 0:  {
                    String line = "<html>";
                    c.setTime(periods.get(columnIndex - 1).getStartDate());
                    line += AddZeroBefore(c.get(Calendar.DATE)) + "<br><u>";
                    line += AddZeroBefore(c.get(Calendar.MONTH) + 1) +  "</u><br>";
                    c.setTime(periods.get(columnIndex - 1).getLastDate());
                    line += AddZeroBefore(c.get(Calendar.DATE)) + "<br>";
                    line += AddZeroBefore(c.get(Calendar.MONTH) + 1) + "</html>";
                    return line;
                }
                case 1: return columnIndex;
                default: return "";
            }
        }
    }

    /**
     * Перетвоює одноцифрове число у форма двох цифрового
     * @param i число 0, 1, 2, ..., 9, 10, 11, ...
     * @return повертає число у форматі 00, 01, 02, ..., 09, 10, 11, ...
     */
    private String AddZeroBefore(int i) {
        return i<10?"0"+i:""+i;
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

class SchedulerTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel)super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        label.setBackground(Color.WHITE);
        if (row >= 2 && column >= 1) {
            try {
                Week week = (Week)value;
                label.setBackground(week.getColor());
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }

        return label;
    }
}