package app.schedules;

import app.DegreeProject;
import app.data.Group;
import app.data.Period;
import app.data.Week;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

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

    public void export(File file, String period) throws IOException {
        final int trRow = 5;
        final int trCell = 0;

        HSSFWorkbook workbook = new HSSFWorkbook();
        Font font = workbook.createFont();
        font.setFontName("Calibri");
        HSSFPalette customPalette = workbook.getCustomPalette();
        for (int i = 0; i < DegreeProject.WEEKLIST.GetAllWeek().size(); i++) {
            java.awt.Color color = DegreeProject.WEEKLIST.GetAllWeek().get(i).getColor();
            customPalette.setColorAtIndex((short) (i + 55), (byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
        }
        HashMap<Week, CellStyle> styleHashMap = new HashMap<>();
        for (int i = 0; i < DegreeProject.WEEKLIST.GetAllWeek().size(); i++) {
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setFillForegroundColor((short) (i + 55));
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setFont(font);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            cellStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
            cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
            styleHashMap.put(DegreeProject.WEEKLIST.GetAllWeek().get(i), cellStyle);
        }

        Sheet sheet = workbook.createSheet("Графік навчання за " + period);
//Місяці
        int first = 1 + trCell;
        int last = 0;
        int prevMonth;
        c.setTime(periods.get(0).getStartDate());
        prevMonth = c.get(Calendar.MONTH);
        for (int i = 0; i < 52 - 1; i++) {
            c.setTime(periods.get(i).getStartDate());
            if (c.get(Calendar.MONTH) == prevMonth) {
                last = i + 1;
            } else {
                sheet.addMergedRegion(new CellRangeAddress(trRow, trRow, first + trCell, last + trCell));
                first = i + 1;
                prevMonth = c.get(Calendar.MONTH);
            }
        }
        sheet.addMergedRegion(new CellRangeAddress(trRow, trRow, first + trCell, last + trCell + 1));

        CellStyle monthStyle = workbook.createCellStyle();
        monthStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        monthStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        monthStyle.setBorderBottom(BorderStyle.THIN);
        monthStyle.setBorderRight(BorderStyle.THIN);
        monthStyle.setFont(font);
        monthStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        monthStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        monthStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        monthStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Row monthRow = sheet.createRow(trRow);
        Cell nameMonthCell = monthRow.createCell(trCell);
        nameMonthCell.setCellStyle(monthStyle);
        nameMonthCell.setCellValue("Місяць");
        for (int i = 1 + trCell; i < 52 + 1 + trCell; i++) {
            Cell monthCell = monthRow.createCell(i);
            monthCell.setCellStyle(monthStyle);
            c.setTime(periods.get(i - 1).getStartDate());
            monthCell.setCellValue(MONTHS[c.get(Calendar.MONTH)]);
        }
//Дати
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setRotation((short)90);
        dateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        dateStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        dateStyle.setBorderBottom(BorderStyle.THIN);
        dateStyle.setBorderRight(BorderStyle.THIN);
        dateStyle.setFont(font);
        dateStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        dateStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Row dateRow = sheet.createRow(1 + trRow);
        Cell nameDateCell = dateRow.createCell(trCell);
        nameDateCell.setCellStyle(monthStyle);
        nameDateCell.setCellValue("Період");
        for (int i = 1 + trCell; i < 52 + 1 + trCell; i++) {
            Cell dateCell = dateRow.createCell(i);
            dateCell.setCellStyle(dateStyle);
            String line = "";
            c.setTime(periods.get(i - 1).getStartDate());
            line += AddZeroBefore(c.get(Calendar.DATE)) + ".";
            line += AddZeroBefore(c.get(Calendar.MONTH) + 1) +  "-";
            c.setTime(periods.get(i - 1).getLastDate());
            line += AddZeroBefore(c.get(Calendar.DATE)) + ".";
            line += AddZeroBefore(c.get(Calendar.MONTH) + 1);
            dateCell.setCellValue(line);
        }
//Кількість робочих днів
        Row workDayRow = sheet.createRow(2 + trRow);
        Cell nameWordDayCell = workDayRow.createCell(trCell);
        nameWordDayCell.setCellValue("Робочих");
        nameWordDayCell.setCellStyle(monthStyle);
        for (int i = 1 + trCell; i < 52 + 1 + trCell; i++) {
            Cell workDayCell = workDayRow.createCell(i);
            workDayCell.setCellStyle(monthStyle);
            workDayCell.setCellValue(periods.get(i - 1).getWorkDay());
        }
//Номер тижня
        Row weekRow = sheet.createRow(3 + trRow);
        Cell nameWeekCell = weekRow.createCell(trCell);
        nameWeekCell.setCellStyle(monthStyle);
        nameWeekCell.setCellValue("Тиждень");
        for (int i = 1 + trCell; i < 52 + 1 + trCell; i++) {
            Cell weekCell = weekRow.createCell(i);
            weekCell.setCellStyle(monthStyle);
            weekCell.setCellValue(i - trCell);
        }
//Дані
        for (int i = 0; i < units.size(); i++) {
            Group group = units.get(i);
            Row groupRow = sheet.createRow(4 + trRow + i);
            Cell nameCell = groupRow.createCell(trCell);
            nameCell.setCellStyle(monthStyle);
            nameCell.setCellValue(group.getName());
            for (int j = 0; j < 52; j++) {
                Cell dataCell = groupRow.createCell(j + trCell + 1);
                dataCell.setCellStyle(styleHashMap.getOrDefault(units.get(i).getWeek(j), workbook.createCellStyle()));
                dataCell.setCellValue(units.get(i).getWeek(j).getAbbreviation());
            }
        }

        for (int i = 0; i < DegreeProject.WEEKLIST.GetAllWeek().size(); i++) {
            Row weekNameRow = sheet.createRow(i + 6 + units.size() + trRow);
            Cell colorCell = weekNameRow.createCell(1 + trCell);
            colorCell.setCellStyle(styleHashMap.getOrDefault(DegreeProject.WEEKLIST.GetAllWeek().get(i), workbook.createCellStyle()));
            colorCell.setCellValue(DegreeProject.WEEKLIST.GetAllWeek().get(i).getAbbreviation());
            sheet.addMergedRegion(new CellRangeAddress(i + 6 + units.size() + trRow, i + 6 + units.size() + trRow, 2 + trCell, 2 + 10 + trCell));
            Cell nameCell = weekNameRow.createCell(2 + trCell);
            nameCell.setCellStyle(monthStyle);
            nameCell.setCellValue(DegreeProject.WEEKLIST.GetAllWeek().get(i).getName());
        }

        for (int i = trCell; i < 53 + trCell; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(new FileOutputStream(file));
        workbook.close();
        file.setWritable(true);
        if (JOptionPane.showConfirmDialog(
                null,
                "Графік навчання збережено до\n" + file.getPath() + "\n Відкрити файл?",
                "Повідомлення",
                JOptionPane.YES_NO_CANCEL_OPTION
        ) == JOptionPane.YES_OPTION) {
            Desktop.getDesktop().open(file);
        }

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
                    return MONTHS[c.get(Calendar.MONTH)];
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
