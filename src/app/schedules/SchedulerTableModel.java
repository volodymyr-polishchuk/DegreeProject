package app.schedules;

import app.data.Period;
import app.data.Week;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Vladimir on 14/01/18.
 **/
public class SchedulerTableModel extends AbstractTableModel {
    private ArrayList<Period> periods;
    private ArrayList<ScheduleUnit> units = new ArrayList<>();
    private Calendar c = Calendar.getInstance();
    private final String[] MONTHS = {"Січень","Лютий","Березень","Квітень","Травень","Червень","Липень","Серпень","Вересень","Жовтень","Листопад","Грудень"};

    public SchedulerTableModel() {
        super();
        Calendar c = Calendar.getInstance();
        c.set(2017, Calendar.SEPTEMBER, 1);
        periods = Period.GetWeekList(c.getTime());
    }

    public void setPeriods(Date date) {
        periods = Period.GetWeekList(date);
        fireTableDataChanged();
        fireTableStructureChanged();
    }

    public void setUnits(ArrayList<ScheduleUnit> units) {
        this.units = units;
    }

    public ArrayList<ScheduleUnit> getUnits() {
        return units;
    }

    /**
     * @param index 0..51 - номер тижня
     * @return Period за зазначеним номером тижня
     */
    public Period getPeriod(int index) {
        return periods.get(index);
    }

    @Override
    public int getRowCount() {
        return units == null ? 3 : 3 + units.size();
    }

    @Override
    public int getColumnCount() {
        return 53;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0: return "Місяць";
            default: {
                c.setTime(periods.get(columnIndex - 1).getStartDate());
//                if (c.get(Calendar.MONTH) % 2 == 0) {
                    return MONTHS[c.get(Calendar.MONTH)];
//                } else {
//                    return "<html><b>" + MONTHS[c.get(Calendar.MONTH)] + "</b></html>";
//                }
            }
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            default: return Object.class;
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
                case 1: return "<html><b>Робочих днів</b></html>";
                case 2: return "<html><b>Тиждень</b></html>";
                default: return units.get(rowIndex - 3).getName();
            }
            default: switch (rowIndex) {
                case 0:  {
                    String line = "<html>";
                    c.setTime(periods.get(columnIndex - 1).getStartDate());
                    line += AddZeroBefore(c.get(Calendar.DATE)) + "<br><b><u>";
                    line += AddZeroBefore(c.get(Calendar.MONTH) + 1) +  "</u></b><br>";
                    c.setTime(periods.get(columnIndex - 1).getLastDate());
                    line += AddZeroBefore(c.get(Calendar.DATE)) + "<br><b>";
                    line += AddZeroBefore(c.get(Calendar.MONTH) + 1) + "</b></html>";
                    return line;
                }
                case 1: return periods.get(columnIndex - 1).getWorkDay();
                case 2: return columnIndex;
                default: return units.get(rowIndex - 3).getWeek(columnIndex - 1);
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
        if (rowIndex >= 3) {
            if (columnIndex >= 1) {
                units.get(rowIndex - 3).setWeek(columnIndex - 1, (Week)aValue);
                fireTableDataChanged();
            }
        }
    }

    public void addScheduleUnit(ScheduleUnit scheduleUnit) {
        units.add(scheduleUnit);
        Collections.sort(units);
        fireTableDataChanged();
    }

    public ScheduleUnit getScheduleUnit(int index) {
        try {
            return units.get(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public ScheduleUnit removeScheduleUnit(int index) {
        ScheduleUnit t = units.remove(index);
        fireTableDataChanged();
        return t;
    }

    public int getSizeScheduleUnits() {
        return units.size();
    }

    public ArrayList<ScheduleUnit> getAllScheduleUnits() {
        return units;
    }
}
