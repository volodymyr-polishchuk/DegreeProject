package app.frame;

import app.*;
import app.data.Group;
import app.data.Period;
import app.data.Week;
import app.schedules.ScheduleUnit;
import app.schedules.SchedulerTableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static javax.swing.JOptionPane.NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;

/**
 * Created by Vladimir on 10/01/18.
 **/
public class SchedulePanel extends JPanel{
    private JTable jTable;
    private JPanel mainPanel;
    private JButton settingGroupButton;
    private JButton saveButton;
    private JButton prevYearButton;
    private JButton nextYearButton;
    private JLabel yearsLabel;
    private JLabel groupNameLabel;
    private JLabel studyDaysLabel;
    private JLabel weeksLabel;
    private JComboBox<Week> jComboBox;
    private JButton exportButton;

    private SchedulerTableModel tableModel;

    SchedulePanel(String name) {
        setName(name);
        setLayout(new GridLayout());
        add(mainPanel);
        InitialTable();
        settingGroupButton.addActionListener(this::settingGroupClick);
        InitialYearsPanel();
        initialComboBox();
        saveButton.addActionListener(this::saveButtonClick);
        exportButton.addActionListener(this::exportButtonClick);
    }

    private void exportButtonClick(ActionEvent event) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;
        try {
            tableModel.export(chooser.getSelectedFile());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
//        JFileChooser chooser = new JFileChooser();
//        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
//            return;
//        }
//        Workbook workbook = new HSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Графік навчання");
//
//        CellStyle dateStyle = workbook.createCellStyle();
//        dateStyle.setRotation((short)90);
//        dateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        dateStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        dateStyle.setBorderBottom(BorderStyle.THIN);
//        dateStyle.setBorderRight(BorderStyle.THIN);
//        org.apache.poi.ss.usermodel.Font dateFont = workbook.createFont();
//        dateFont.setFontName("Calibri");
//        dateStyle.setFont(dateFont);
//
//        CellStyle groupStyle = workbook.createCellStyle();
//        groupStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        groupStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        groupStyle.setBorderBottom(BorderStyle.THIN);
//        groupStyle.setBorderRight(BorderStyle.THIN);
//        groupStyle.setFont(dateFont);
//
//        CellStyle monthStyle = workbook.createCellStyle();
//        monthStyle.cloneStyleFrom(groupStyle);
//        monthStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
//
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 5));
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 10));
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 15));
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 16, 20));
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 21, 25));
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 26, 30));
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 31, 35));
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 36, 40));
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 41, 45));
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 46, 50));
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 51, 52));
//
//        Row monthRow = sheet.createRow(0);
//        for (int i = 1; i < 52; i++) {
//            Cell cell = monthRow.createCell(i);
//            cell.setCellValue("Вересень");
//            cell.setCellStyle(groupStyle);
//        }
//
//        Row dateRow = sheet.createRow(1);
//
//        for (int i = 0; i < 52; i++) {
//            Cell nowCell = dateRow.createCell(i + 1);
//            nowCell.setCellStyle(dateStyle);
//            nowCell.setCellValue("10/12..12/12");
//        }
//
//        CellStyle style[] = new CellStyle[6];
//        for (int i = 0; i < 6; i++) {
//            style[i] = workbook.createCellStyle();
//            style[i].cloneStyleFrom(monthStyle);
//            style[i].setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        }
//        for (int i = 2; i < 10; i++) {
//            Row groupRow = sheet.createRow(i);
//            Cell nowCell = groupRow.createCell(0);
//            nowCell.setCellValue("Group " + i);
//            nowCell.setCellStyle(groupStyle);
//            for (int j = 1; j < 53; j++) {
//                Cell dCell = groupRow.createCell(j);
//                switch ((int)(Math.random() * 6)) {
//                    case 0:
//                        dCell.setCellValue("Н");
//                        style[0].setFillForegroundColor(IndexedColors.AQUA.getIndex());
//                        dCell.setCellStyle(style[0]);
//                        break;
//                    case 1:
//                        dCell.setCellValue("П");
//                        style[1].setFillForegroundColor(IndexedColors.BLUE.getIndex());
//                        dCell.setCellStyle(style[1]);
//                        break;
//                    case 2:
//                        dCell.setCellValue("С");
//                        style[2].setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
//                        dCell.setCellStyle(style[2]);
//                        break;
//                    case 3:
//                        dCell.setCellValue("Д");
//                        style[3].setFillForegroundColor(IndexedColors.BROWN.getIndex());
//                        dCell.setCellStyle(style[3]);
//                        break;
//                    case 4:
//                        dCell.setCellValue("А");
//                        style[4].setFillForegroundColor(IndexedColors.GREEN.getIndex());
//                        dCell.setCellStyle(style[4]);
//                        break;
//                    case 5:
//                        dCell.setCellValue("К");
//                        style[5].setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
//                        dCell.setCellStyle(style[5]);
//                        break;
//                }
//            }
//
//        }
//
//        for (int i = 0; i < 53; i++) {
//            sheet.autoSizeColumn(i);
//        }
//
//        File file = chooser.getSelectedFile();
//        try {
//            workbook.write(new FileOutputStream(file));
//            if (!file.setWritable(true)) {
//                JOptionPane.showMessageDialog(null, "Файл не вдалося зробити змінним", "Помилка", JOptionPane.WARNING_MESSAGE);
//            }
//            JOptionPane.showMessageDialog(null, "Успішно збережено до файлу " + file.getPath(), "Успіх", JOptionPane.INFORMATION_MESSAGE);
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(null, e.getMessage());
//            e.printStackTrace();
//        }
    }

    public SchedulerTableModel getTableModel() {
        return tableModel;
    }

    public void setPeriod(String line) {
        yearsLabel.setText(line);
        tableModel.fireTableStructureChanged();
    }

    private void saveButtonClick(ActionEvent e) {
        try {
            Connection c = DegreeProject.databaseData.getConnection();
            Statement statement = c.createStatement();
            int schedule_key = 0;
            ArrayList<ScheduleUnit> units = ((SchedulerTableModel)jTable.getModel()).getUnits();

            String sqlQuery = "SELECT * FROM schedules WHERE period LIKE '" + yearsLabel.getText() + "'";
            if (statement.execute(sqlQuery)) {
                int rewriteOption = JOptionPane.showConfirmDialog(
                        null,
                        "В базі даних уже існує графік навчання\n\rза цей період. Перезаписати?",
                        "Попередження",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (rewriteOption == YES_OPTION) {
                    statement.execute(
                            "DELETE FROM schedules_data WHERE schedule LIKE " +
                                    "(SELECT k FROM schedules WHERE period LIKE '" + yearsLabel.getText() + "')"
                    );
                    statement.execute("DELETE FROM schedules WHERE period LIKE '" + yearsLabel.getText() + "'");
                } else {
                    return;
                }
            }

            PreparedStatement ps = c.prepareStatement("INSERT IGNORE schedules(period, date_of_create, coments) VALUE (?, ?, ?);");
            ps.setString(1, yearsLabel.getText());
            ps.setDate(2, new Date(System.currentTimeMillis()));
            ps.setString(3, JOptionPane.showInputDialog(null, "Коментар", "Введіть коментар", JOptionPane.QUESTION_MESSAGE));
            ps.execute();

            ResultSet rs = statement.executeQuery("SELECT k FROM schedules WHERE period = '" + yearsLabel.getText() + "';");
            while (rs.next()) {
                schedule_key = rs.getInt("k");
            }
            for (ScheduleUnit unit : units) {
                ps = c.prepareStatement("INSERT IGNORE schedules_data(schedule, groups, data) VALUE (?, ?, ?);");
                ps.setInt(1, schedule_key);
                ps.setInt(2, unit.getKey());
                String line = "";
                for (int i = 0; i < 52; i++) {
                    line += unit.getWeek(i).getMark();
                }
                ps.setString(3, line);
                ps.execute();
            }
            rs.close();
            statement.close();
            ps.close();
            JOptionPane.showMessageDialog(null, "Дані успішно збережено");
        } catch (SQLException | ClassCastException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage());
            e1.printStackTrace();
        }
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

    private void initialComboBox() {
        DefaultComboBoxModel<Week> boxModel = new DefaultComboBoxModel<>();
        DegreeProject.WEEKLIST.GetAllWeek().forEach(boxModel::addElement);
        jComboBox.setModel(boxModel);

        jComboBox.setRenderer(new DefaultListCellRenderer() {
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
        });
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

            saveButton.setEnabled(true);
        });
        nextYearButton.addActionListener(e -> {
            String[] lines = yearsLabel.getText().split("-");
            lines[0] = String.valueOf(Integer.parseInt(lines[0]) + 1);
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(lines[0]) - 1, Calendar.SEPTEMBER, 1);
            tableModel.setPeriods(c.getTime());
            lines[1] = String.valueOf(Integer.parseInt(lines[1]) + 1);
            yearsLabel.setText(lines[0] + "-" + lines[1]);

            saveButton.setEnabled(true);
        });
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
                saveButton.setEnabled(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int row = jTable.getSelectedRow();
                Week week = jComboBox.getModel().getElementAt(jComboBox.getSelectedIndex());
                for (int i = col; i <= jTable.columnAtPoint(e.getPoint()); i++) {
                    jTable.setValueAt(week, row, i);
                }
                UpdateScheduleLabels(tableModel.getScheduleUnit(row - 3));
                saveButton.setEnabled(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
        jTable.getTableHeader().setDefaultRenderer(new DefaultTableCellHeaderRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jTable, Object o, boolean b, boolean b1, int row, int col) {
                final Color borderColor = new Color(207, 207, 207);
                JLabel label = (JLabel) super.getTableCellRendererComponent(jTable, o, b, b1, row, col);
                if (col == 0) return label;
                int first = col, last, count;
                while (first-- > 0)
                    if (!jTable.getTableHeader().getColumnModel().getColumn(first).getHeaderValue().equals(o)) {
                        first++; break;
                }
                last = first;
                while (last++ < 53)
                    if (last == 53 || !jTable.getTableHeader().getColumnModel().getColumn(last).getHeaderValue().equals(o)) {
                        last--;
                        break;
                }
                if (col == last) {
                    label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, borderColor));
                } else {
                    label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor));
                }
                count = last - first + 1;
                label.setText("");
                BufferedImage image = new BufferedImage(26, 18, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = (Graphics2D) image.getGraphics();
                g.setColor(label.getBackground());
                g.fillRect(0, 0, jTable.getTableHeader().getColumnModel().getColumn(col).getWidth() + 10, jTable.getTableHeader().getHeight() + 5);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setFont(new Font(label.getFont().getName(), Font.PLAIN, label.getFont().getSize()));
                g.setColor(label.getForeground());
                int w = jTable.getTableHeader().getColumnModel().getColumn(col).getWidth();
                int wi = (int) g.getFontMetrics().getStringBounds(String.valueOf(o), g).getWidth();
                g.drawString(String.valueOf(o), (int) (((w*count - wi)/2.0) - w*((col - first) % count)), 14);
                label.setIcon(new ImageIcon(image));
                return label;
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
        Week week = jComboBox.getModel().getElementAt(jComboBox.getSelectedIndex());
        jTable.setValueAt(week, jTable.getSelectedRow(), jTable.getSelectedColumn());

        UpdateScheduleLabels(tScheduleUnit);
    }

    private void UpdateScheduleLabels(ScheduleUnit tScheduleUnit) {
        if (tScheduleUnit == null) return;
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

        int a = 0;
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
            if (tScheduleUnit.getWeek(i).equals(DegreeProject.WEEKLIST.getWeekByName("Навчання"))) {
                a++;
            } else if (tScheduleUnit.getWeek(i).equals(DegreeProject.WEEKLIST.getWeekByName("Канікули"))) {
                if (a != 0) list.add(a);
                a = 0;
            }
        }
        if (a != 0) list.add(a);
        studyDaysLabel.setText(studyDaysLabel.getText() + "; Навчання по семестрах " + list);

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

    public void showSetting() {
        settingGroupButton.doClick();
    }
}