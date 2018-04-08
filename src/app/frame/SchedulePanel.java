package app.frame;

import app.*;
import app.data.Group;
import app.data.Period;
import app.data.Week;
import app.schedules.ScheduleUnit;
import app.schedules.SchedulerTableModel;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

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
        initialTable();
        settingGroupButton.addActionListener(this::settingGroupClick);
        initialYearsPanel();
        initialComboBox();
        saveButton.addActionListener(this::saveButtonClick);
        exportButton.addActionListener(this::exportButtonClick);
    }

    private void exportButtonClick(ActionEvent event) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("schedule.xls"));
        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;
        if (!chooser.getSelectedFile().getPath().endsWith(".xls")) {
            chooser.setSelectedFile(new File(chooser.getSelectedFile().getPath() + ".xls"));
        }
        try {
            tableModel.export(chooser.getSelectedFile(), yearsLabel.getText());
            System.out.println(chooser.getSelectedFile().getPath() + "->setWritable: " + chooser.getSelectedFile().setWritable(true));
            if (JOptionPane.showConfirmDialog(
                    null,
                    "Графік навчання збережено до\n" + chooser.getSelectedFile().getPath() + "\n Відкрити файл?",
                    "Повідомлення",
                    JOptionPane.YES_NO_CANCEL_OPTION
            ) == JOptionPane.YES_OPTION) {
                Desktop.getDesktop().open(chooser.getSelectedFile());
            }
            DegreeProject.mainForm.setStatusBar("Дані успішно збережено до файлу " + chooser.getSelectedFile().getPath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
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
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            resultSet.last();
            int size = resultSet.getRow();
            if (size > 0) {
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
                ps.setInt(2, unit.getGroup().getKey());
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
        DegreeProject.GROUPLIST.refresh();
        ArrayList<Group> existsGroups = new ArrayList<>();
        tableModel.getAllScheduleUnits().forEach(a -> existsGroups.add(a.getGroup()));
        ArrayList<Group> outlastGroups = (ArrayList<Group>) new MultiChoiceDialog<>(DegreeProject.GROUPLIST.getList(), existsGroups).showAndGetData();
        ArrayList<ScheduleUnit> outlastScheduleUnits = new ArrayList<>();
        tableModel.getAllScheduleUnits().stream().filter(item -> outlastGroups.remove(item.getGroup())).forEach(outlastScheduleUnits::add);
        outlastGroups.forEach(item -> outlastScheduleUnits.add(new ScheduleUnit(item)));
        Collections.sort(outlastScheduleUnits);
        tableModel.setUnits(outlastScheduleUnits);
        tableModel.fireTableStructureChanged();
        tableModel.fireTableDataChanged();
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

    private void initialYearsPanel() {
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

    private void initialTable() {
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
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1: mouseClickEvent(e);
                        break;
                    case MouseEvent.BUTTON2: {
                        int row = jTable.rowAtPoint(e.getPoint());
                        int col = jTable.columnAtPoint(e.getPoint());
                        try {
                            Week week = (Week) jTable.getValueAt(row, col);
                            jComboBox.setSelectedItem(week);
                        } catch (ClassCastException ex) {
                            ex.printStackTrace();
                        }
                    } break;
                }
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
        groupNameLabel.setText(tScheduleUnit.getGroup().getName());

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