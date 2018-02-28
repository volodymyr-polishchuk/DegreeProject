package frame;

import app.DegreeProject;
import app.data.*;
import app.lessons.*;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by Vladimir on 31/01/18.
 **/
public class LessonsPanel extends JPanel{
    private JTable jTable;
    private JButton settingButton;
    private JButton saveButton;
    private JPanel contentPane;
    private JToggleButton button1;
    private JToggleButton button2;
    private JToggleButton button3;
    private JComboBox<Lesson> lessonCBox;
    private JComboBox<Teacher> teacherCBox;
    private JComboBox<Auditory> auditoryCBox;
    private JLabel groupNameLabel;
    private JLabel workHourInWeekLabel;
    private JLabel studyPairInWeekLabel;
    private JButton exportButton;
    private JButton prevPeriodButton;
    private JButton nextPeriodButton;
    private JLabel periodLabel;
    private ButtonGroup buttonGroup;
    private LessonTableModel lessonTableModel;
    private StudyPair nowStudyPair;
    /**
     * Кількість пар в одному дні
     */
    private final int PAIR_IN_DAY = 5;
    private final int COLUMN_REPEAT = 5;
    // Константи, що позначають положення колонок в таблиці
    private final int DAY_NAME_NUMBER = 0;
    private final int PAIR_NUMBER = 1;
    private final int LESSONS_NAME_NUMBER = 2;
    private final int TEACHER_NAME_NUMBER = 3;
    private final int AUDITORY_NUMBER = 4;
    /**
     * Кількість днів в тижні починаючи від понеділка, де 1 - Понеділок, 2 - Понеділок...Вівторок, 3 - Понеділок...Середа
     */
    private final int DAY_AT_WEEK = 6;

    private LessonsPanel() {
        nowStudyPair = new EmptyStudyPair();
        setLayout(new GridLayout());
        add(contentPane);
        InitialTable();
        InitialGroupButton();
        settingButton.addActionListener(this::settingGroupClick);
        saveButton.addActionListener(this::saveButtonClick);
        InitialData();
        button1.addActionListener(this::setButtonClick);
        button2.addActionListener(this::setButtonClick);
        button3.addActionListener(this::setButtonClick);
        auditoryCBox.addActionListener(this::setButtonClick);
        teacherCBox.addActionListener(this::setButtonClick);
        lessonCBox.addActionListener(this::setButtonClick);
        nextPeriodButton.addActionListener(this::nextPeriodButtonClick);
        prevPeriodButton.addActionListener(this::prevPeriodButtonClick);
    }

    private void prevPeriodButtonClick(ActionEvent event) {
        String periodLine = periodLabel.getText();
        String[] args = periodLine.split("/");
        if (args.length != 2) {
            periodLabel.setText("2017-2018/1");
            return;
        }
        if (Integer.parseInt(args[1]) == 2) {
            periodLabel.setText(args[0] + "/1");
        } else if (Integer.parseInt(args[1]) == 1) {
            String[] years = args[0].split("-");
            years[0] = String.valueOf(Integer.valueOf(years[0]) - 1);
            years[1] = String.valueOf(Integer.valueOf(years[1]) - 1);
            periodLabel.setText(years[0] + "-" + years[1] + "/2");
        }
    }

    public LessonsPanel(String title) {
        this();
        setName(title);
    }

    public LessonsPanel(String title, String period) {
        this(title);
//        TODO Створювати розклад занять відносно переданого періоду
        try {
            String sql = "SELECT * FROM lessons_schedules WHERE period LIKE ?";
            PreparedStatement ps = DegreeProject.databaseData.getConnection().prepareStatement(sql);
            ps.setString(1, period);
            ResultSet rs = ps.executeQuery();
            int k = -1;
            while (rs.next()) {
                k = rs.getInt("k");
            }
            if (k == -1) throw new IllegalArgumentException("Period not found {" + period + "}");
            String sqlGetAll = "SELECT * FROM lessons_data INNER JOIN groups ON groups = groups.k INNER JOIN departments ON groups.department = departments.k \n" +
                    "INNER JOIN lessons ON lessons_data.lesson = lessons.k INNER JOIN teachers ON lessons_data.teacher = teachers.k\n" +
                    "INNER JOIN auditorys ON lessons_data.auditory = auditorys.k WHERE lessons_data.lessons_schedule = ?;";
            ps = DegreeProject.databaseData.getConnection().prepareStatement(sqlGetAll);
            ps.setInt(1, k);
            rs = ps.executeQuery();
//  TODO Потрібно реалізувати обратне переведення із бази даних до структури даних в Java. Проблема з двойними парами
//            HashMap<Group, HashSet<StudyPair>> map = new HashMap<>();
//            while (rs.next()) {
//                Group group = new Group(new Department(rs.getInt("departments.k"), rs.getString("departments.name")), rs.getString("groups.name"));
//                if (map.containsKey(group)) {
//                    map.get(group).add(new St)
//                }
//            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    private void nextPeriodButtonClick(ActionEvent event) {
        String periodLine = periodLabel.getText();
        String[] args = periodLine.split("/");
        if (args.length != 2) {
            periodLabel.setText("2017-2018/1");
            return;
        }
        if (Integer.parseInt(args[1]) == 1) {
            periodLabel.setText(args[0] + "/2");
        } else if (Integer.parseInt(args[1]) == 2) {
            String[] years = args[0].split("-");
            years[0] = String.valueOf(Integer.valueOf(years[0]) + 1);
            years[1] = String.valueOf(Integer.valueOf(years[1]) + 1);
            periodLabel.setText(years[0] + "-" + years[1] + "/1");
        }
    }

    private void saveButtonClick(ActionEvent event) {
        LessonTableModel lessonTableModel = ((LessonTableModel) jTable.getModel());
        ArrayList<LessonsUnit> units = lessonTableModel.units;
        try (Statement st = DegreeProject.databaseData.getConnection().createStatement()) {
            ResultSet rs2 = st.executeQuery("SELECT * FROM lessons_schedules WHERE period LIKE '" + periodLabel.getText() + "'");
            if (rs2.next()) {
                // TODO Не правильно визначає чи існує уже такий запис у таблиці чи ні
                int inputResult = JOptionPane.showConfirmDialog(null,
                        "За даний період уже є розклад занять!\n\rПерезаписати?",
                        "Попередження", JOptionPane.YES_NO_CANCEL_OPTION);
                if (inputResult != JOptionPane.YES_OPTION) return;
                st.execute("DELETE FROM lessons_data WHERE lessons_schedule LIKE (SELECT k FROM lessons_schedules WHERE period LIKE '" + periodLabel.getText() + "')");
                st.execute("DELETE FROM lessons_schedules WHERE period LIKE '" + periodLabel.getText() + "'");
            }
            rs2.close();

            String sql = "INSERT INTO lessons_schedules(period, date_of_create, coments) VALUE (?, ?, ?)";
            PreparedStatement ps = DegreeProject.databaseData.getConnection().prepareStatement(sql);
            ps.setString(1, periodLabel.getText());
            ps.setDate(2, new Date(System.currentTimeMillis()));
            ps.setString(3, JOptionPane.showInputDialog(null, "Введіть коментар", "Коментар", JOptionPane.QUESTION_MESSAGE)); // TODO Потрібно реалізувати коментарі до розкладу занять
            ps.execute();

            int k = -1;
            ResultSet rs = st.executeQuery("SELECT k FROM lessons_schedules WHERE period LIKE '" + periodLabel.getText() + "'");
            while (rs.next()) k = rs.getInt("k");
            if (k == -1) throw new SQLException("Значення ключа не змінилося, отже запис не було додано");

            String sqlLessonsData = "INSERT INTO lessons_data(lessons_schedule, groups, pair_number, lesson, teacher, auditory) VALUE (?, ?, ?, ?, ?, ?)";
            ps = DegreeProject.databaseData.getConnection().prepareStatement(sqlLessonsData);
            ps.setInt(1, k);
            for (LessonsUnit unit : units) {
                ps.setInt(2, unit.getGroup().getKey());
                StudyPair[] pairs = unit.getPairs();
                for (int i = 0; i < pairs.length; i++) {
                    StudyPair pair = pairs[i];
                    if (pair instanceof StudyPairLonely) {
//                      Якщо пара одиночна, тоді записуємо
                        StudyPairLonely pairLonely = (StudyPairLonely) pair;
                        ps.setString(3, String.valueOf(i));
                        ps.setInt(4, pairLonely.getLesson().getKey());
                        ps.setInt(5, pairLonely.getTeacher().getKey());
                        ps.setInt(6, pairLonely.getAuditory().getKey());
                        ps.execute();
                    } else if (pair instanceof StudyPairDouble) {
//                      Якщо пара двойна - перевіряємо який з елементів пари дійсний і записуємо
                        StudyPairDouble pairDouble = (StudyPairDouble) pair;
                        if (pairDouble.getNumerator() != null && !pairDouble.getNumerator().isEmpty()) {
                            ps.setString(3, String.valueOf(i) + "/1");
                            ps.setInt(4, pairDouble.getNumerator().getLesson().getKey());
                            ps.setInt(5, pairDouble.getNumerator().getTeacher().getKey());
                            ps.setInt(6, pairDouble.getNumerator().getAuditory().getKey());
                            ps.execute();
                        }
                        if (pairDouble.getDenominator() != null && !pairDouble.getDenominator().isEmpty()) {
                            ps.setString(3, String.valueOf(i) + "/2");
                            ps.setInt(4, pairDouble.getDenominator().getLesson().getKey());
                            ps.setInt(5, pairDouble.getDenominator().getTeacher().getKey());
                            ps.setInt(6, pairDouble.getDenominator().getAuditory().getKey());
                            ps.execute();
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Дані успішно збережено!", "Повідомлення з бази даних", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    private void settingGroupClick(ActionEvent e) {
        DegreeProject.GROUPLIST.refresh();
        int[] choice = new int[DegreeProject.GROUPLIST.GetAllWeek().size()];
        int count = 0;
        ArrayList<Group> tList = DegreeProject.GROUPLIST.GetAllWeek();
        for (int i = 0; i < tList.size(); i++) {
            for (int j = 0; j < lessonTableModel.units.size(); j++) {
                if (tList.get(i).equals(lessonTableModel.units.get(j).getGroup())) {
                    choice[count++] = i;
                }
            }
        }
        choice = Arrays.copyOf(choice, count);
        new GroupChoiceDialog(DegreeProject.GROUPLIST.GetAllWeek(), choice, this::afterSettingGroup);
    }

    private void afterSettingGroup(ArrayList<Group> list) {
        ArrayList<LessonsUnit> listFromTable = new ArrayList<>(lessonTableModel.units);
        LessonsUnit tLessonUnit;

        for (int i = lessonTableModel.units.size() - 1; i >= 0; i--) {
            tLessonUnit = lessonTableModel.units.remove(i);
            for (Group group : list) {
                if (tLessonUnit.getGroup().equals(group)) {
                    listFromTable.add(tLessonUnit);
                }
            }
        }

        Group tGroup;
        boolean b = false;
        for (Group group : list) {
            tGroup = group;
            for (LessonsUnit lessonsUnit : listFromTable) {
                if (lessonsUnit.getGroup().equals(tGroup)) {
                    b = true;
                }
            }
            if (!b) listFromTable.add(new LessonsUnit(tGroup, PAIR_IN_DAY, DAY_AT_WEEK));
            b = false;
        }
        Collections.sort(listFromTable, (o1, o2) -> o1.getGroup().getName().compareTo(o2.getGroup().getName()));
        lessonTableModel.units.addAll(listFromTable);
        lessonTableModel.fireTableStructureChanged();
        lessonTableModel.fireTableDataChanged();
    }

    private void InitialData() {
        lessonCBox.addActionListener(e -> {
            if(lessonCBox.getSelectedItem() instanceof Lesson) {
                Lesson lesson = (Lesson) lessonCBox.getSelectedItem();
                Auditory auditory = lesson.getAuditory();
                if (auditory != null) {
                    auditoryCBox.setSelectedItem(auditory);
                }
            }
        });
        try (Statement s = DegreeProject.databaseData.getConnection().createStatement()) {
            DefaultComboBoxModel<Lesson> lessonModel = new DefaultComboBoxModel<>();
            lessonCBox.setModel(lessonModel);
            ResultSet rs = s.executeQuery("SELECT * FROM lessons INNER JOIN auditorys ON lessons.auditory = auditorys.k ORDER BY lessons.name");
            while (rs.next()) {
                lessonModel.addElement(
                        new Lesson(
                                rs.getInt("lessons.k"),
                                rs.getString("lessons.name"),
                                new Auditory(rs.getInt("auditorys.k"), rs.getString("auditorys.name")))
                );
            }
            DefaultComboBoxModel<Teacher> teacherModel = new DefaultComboBoxModel<>();
            teacherCBox.setModel(teacherModel);
            rs = s.executeQuery("SELECT * FROM teachers ORDER BY name");
            while (rs.next()) {
                teacherModel.addElement(
                        new Teacher(rs.getInt("k"), rs.getString("name"), Preference.parsePreference(rs.getString("preferences")))
                );
            }

            DefaultComboBoxModel<Auditory> auditoryModel = new DefaultComboBoxModel<>();
            auditoryCBox.setModel(auditoryModel);
            rs = s.executeQuery("SELECT * FROM auditorys ORDER BY name");
            while (rs.next()) {
                auditoryModel.addElement(new Auditory(rs.getInt("k"), rs.getString("name")));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setButtonClick(ActionEvent event) {
        switch (buttonGroup.getSelection().getActionCommand()) {
            case "BOTH": nowStudyPair = new StudyPairLonely(
                    (Lesson) lessonCBox.getModel().getSelectedItem(),
                    (Teacher) teacherCBox.getModel().getSelectedItem(),
                    (Auditory) auditoryCBox.getModel().getSelectedItem()
            ); break;
            case "NUMERATOR": nowStudyPair = new StudyPairDouble(
                    new StudyPairLonely(
                            (Lesson) lessonCBox.getModel().getSelectedItem(),
                            (Teacher) teacherCBox.getModel().getSelectedItem(),
                            (Auditory) auditoryCBox.getModel().getSelectedItem()),
                    new StudyPairLonely()
            ); break;
            case "DENOMINATOR": nowStudyPair = new StudyPairDouble(
                    new StudyPairLonely(),
                    new StudyPairLonely(
                            (Lesson) lessonCBox.getModel().getSelectedItem(),
                            (Teacher) teacherCBox.getModel().getSelectedItem(),
                            (Auditory) auditoryCBox.getModel().getSelectedItem())
            ); break;
        }
        lessonTableModel.updateForbids(nowStudyPair);
    }

    private void InitialGroupButton() {
        Border out = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY);
        Border outCenter = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY);
        Border in = BorderFactory.createEmptyBorder(3, 5, 3, 5);
        button1.setBorder(BorderFactory.createCompoundBorder(out, in));
        button1.addActionListener(e -> ((LessonTableModel)jTable.getModel()).fireTableDataChanged());
        button2.setBorder(BorderFactory.createCompoundBorder(outCenter, in));
        button2.addActionListener(e -> ((LessonTableModel)jTable.getModel()).fireTableDataChanged());
        button3.setBorder(BorderFactory.createCompoundBorder(out, in));
        button3.addActionListener(e -> ((LessonTableModel)jTable.getModel()).fireTableDataChanged());
        buttonGroup = new ButtonGroup();
        buttonGroup.add(button1);
        buttonGroup.add(button2);
        buttonGroup.add(button3);
    }

    public LessonTableModel getLessonTableModel() {
        return lessonTableModel;
    }

    private void InitialTable() {
        lessonTableModel = new LessonTableModel();
        jTable.setModel(lessonTableModel);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable.setShowGrid(false);
        jTable.setIntercellSpacing(new Dimension(0, 0));
        jTable.setRowHeight((int) (jTable.getRowHeight() * 1.5));

        jTable.getTableHeader().setReorderingAllowed(false);
        jTable.getTableHeader().setResizingAllowed(false);
        jTable.getTableHeader().setDefaultRenderer(new TableHeaderCellRenderer());

        jTable.setDefaultRenderer(String.class, new TableCellDayNameRenderer(PAIR_IN_DAY));
        jTable.setDefaultRenderer(Integer.class, new TableCellPairNumberRenderer());
        jTable.setDefaultRenderer(StudyPair.class, new TableCellSubjectRenderer());

        jTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {mouseTableClick(e);
            }
        });

        lessonTableModel.addTableModelListener(e -> {
            Enumeration<TableColumn> columns = jTable.getColumnModel().getColumns();
            while (columns.hasMoreElements()) {
                TableColumn column = columns.nextElement();
                switch (column.getModelIndex() % COLUMN_REPEAT) {
                    case DAY_NAME_NUMBER:case PAIR_NUMBER: {
                        column.setMaxWidth(20); column.setMinWidth(20);
                    } break;
                    case LESSONS_NAME_NUMBER: {
                        column.setMinWidth(120); column.setMaxWidth(120);
                    } break;
                    case TEACHER_NAME_NUMBER: {
                        column.setMinWidth(100); column.setMaxWidth(100);
                    } break;
                    case AUDITORY_NUMBER: {
                        column.setMinWidth(30); column.setMaxWidth(30);
                    } break;
                }
            }
        });
    }

    private class TableHeaderCellRenderer extends DefaultTableCellHeaderRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable jTable, Object o, boolean b, boolean b1, int row, int col) {
            JLabel label = (JLabel)super.getTableCellRendererComponent(jTable, o, b, b1, row, col);
            label.setHorizontalAlignment(LEFT);
            label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            int r = col % COLUMN_REPEAT;
            if (r == 4 || r == 0 || r == 1) {
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
            }
            return label;
        }
    }

    private class LessonTableModel extends AbstractTableModel {
        private ArrayList<LessonsUnit> units = new ArrayList<>();
        private HashMap<app.lessons.StudyPair.Forbidden, HashSet<Point>> fMap = new HashMap<>();

        public LessonTableModel() {
        }

        public HashMap<StudyPair.Forbidden, HashSet<Point>> getfMap() {
            return fMap;
        }

        public StudyPair.Forbidden getForbidden(LessonTableModel lessonTableModel, int row, int column) {
            StudyPair.Forbidden forbidden = StudyPair.Forbidden.UNKNOWN_FORBIDDEN;
            HashMap<StudyPair.Forbidden, HashSet<Point>> map = lessonTableModel.getfMap();
            if (map.get(StudyPair.Forbidden.ROW_FORBIDDEN) != null) {
                for (Point point: map.get(StudyPair.Forbidden.ROW_FORBIDDEN)) {
                    if (point.getX() == row) {
                        forbidden = StudyPair.Forbidden.ROW_FORBIDDEN;
                        break;
                    }
                }
            }
            for (StudyPair.Forbidden f: map.keySet()) {
                if (!map.get(f).contains(new Point(row, column / COLUMN_REPEAT))) continue;
                switch (f) {
                    case SELF_FORBIDDEN: forbidden = StudyPair.Forbidden.SELF_FORBIDDEN;
                        break;
                    case NON_FORBIDDEN: forbidden = StudyPair.Forbidden.NON_FORBIDDEN;
                        break;
                }
            }
            return forbidden;
        }

        public void updateForbids(StudyPair studyPair) {
            fMap.clear();
            StudyPair.Forbidden[] forbidsArr;
            for (int col = 0; col < units.size(); col++) {
                LessonsUnit unit = units.get(col);
                for (int row = 0; row < unit.getPairPerDay() * unit.getDayPerWeek(); row++) {
                    forbidsArr = unit.getPair(row).getForbidden(studyPair, units, row, col, unit.getPairPerDay(), unit.getDayPerWeek());
                    for (StudyPair.Forbidden f: forbidsArr) {
                        HashSet<Point> set = fMap.get(f) == null ? new HashSet<>() : fMap.get(f);
                        set.add(new Point(row, col));
                        fMap.put(f, set);
                    }
                }
            }
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return DAY_AT_WEEK * PAIR_IN_DAY;
        }

        @Override
        public String getColumnName(int column) {
            if (column % COLUMN_REPEAT == TEACHER_NAME_NUMBER) return units.get(column / COLUMN_REPEAT).getGroup().getName();
            return "";
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex % COLUMN_REPEAT) {
                case DAY_NAME_NUMBER: return String.class;
                case PAIR_NUMBER: return Integer.class;
                case LESSONS_NAME_NUMBER:case TEACHER_NAME_NUMBER:case AUDITORY_NUMBER: return StudyPair.class;
                default: return Object.class;
            }
        }

        @Override
        public int getColumnCount() {
            return units == null ? 2 : units.size() * COLUMN_REPEAT;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex % COLUMN_REPEAT) {
                case LESSONS_NAME_NUMBER:case TEACHER_NAME_NUMBER:case AUDITORY_NUMBER:
                    return units.get(columnIndex / COLUMN_REPEAT).getPair(rowIndex);
            }
            return "";
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            switch (columnIndex % COLUMN_REPEAT) {
                case LESSONS_NAME_NUMBER:case TEACHER_NAME_NUMBER:case AUDITORY_NUMBER: {
                    if (units.get(columnIndex / COLUMN_REPEAT).getPair(rowIndex) instanceof StudyPairDouble
                            && aValue instanceof StudyPairDouble) {
                        units.get(columnIndex / COLUMN_REPEAT).setPair(rowIndex,
                                StudyPairDouble.unite(
                                        (StudyPairDouble)units.get(columnIndex / COLUMN_REPEAT).getPair(rowIndex),
                                        (StudyPairDouble)aValue));
                    } else {
                        units.get(columnIndex / COLUMN_REPEAT).setPair(rowIndex, (StudyPair)aValue);
                    }
                }
            }
            updateForbids(nowStudyPair);
            fireTableDataChanged();
        }
    }

    private class TableCellPairNumberRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
            label.setHorizontalAlignment(CENTER);
            label.setText(String.valueOf((row + 1) % (PAIR_IN_DAY) == 0 ? PAIR_IN_DAY : (row + 1) % (PAIR_IN_DAY)));
            label.setBackground(new Color(242, 242, 242));
            if (row % PAIR_IN_DAY == PAIR_IN_DAY - 1) label.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, Color.LIGHT_GRAY));
            return label;
        }
    }

    private class TableCellSubjectRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JComponent component = ((StudyPair) value).getRendererComponent(StudyPair.Query.values()[(column % COLUMN_REPEAT) - 2]);
            component.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
            if (row % PAIR_IN_DAY == PAIR_IN_DAY - 1)
                component.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, Color.LIGHT_GRAY));
            component.setBackground(new Color(0xC5DCA0));
            component.setOpaque(true);
            for (StudyPair.Forbidden forbidden : nowStudyPair.getSelfForbidden(row, column, PAIR_IN_DAY, DAY_AT_WEEK)) {
                if (forbidden == StudyPair.Forbidden.DAY_FORBIDDEN) {
                    component.setBackground(new Color(0xBBBAB8));
                    return component;
                }
            }
            StudyPair.Forbidden forbidden = ((LessonTableModel)table.getModel()).getForbidden(lessonTableModel, row, column);

            switch (forbidden) {
                case SELF_FORBIDDEN: component.setBackground(new Color(0x818AA3));
                    break;
                case ROW_FORBIDDEN: component.setBackground(new Color(0xF9DAD0));
                    break;
                case NON_FORBIDDEN: component.setBackground(new Color(0xF5F2B8));
                    break;
                case UNKNOWN_FORBIDDEN: component.setBackground(new Color(0xC5DCA0));
            }
            return component;
        }
    }

    private void mouseTableClick(MouseEvent e) {
        int row = jTable.rowAtPoint(e.getPoint());
        int column = jTable.columnAtPoint(e.getPoint());
        lessonTableModel.setValueAt(nowStudyPair, row, column);
        analyzeTable(row, column);
    }

    private void analyzeTable(int row, int column) {
        LessonsUnit unit = getLessonTableModel().units.get(column / COLUMN_REPEAT);
        int pairCountNumerator = 0;
        int pairCountDenominator = 0;
        for (StudyPair pair : unit.getPairs()) {
            if (pair instanceof StudyPairLonely) {
                StudyPairLonely lonely = (StudyPairLonely) pair;
                if (lonely.isEmpty()) continue;
                pairCountNumerator++;
                pairCountDenominator++;
            } else if (pair instanceof StudyPairDouble) {
                StudyPairLonely numerator = ((StudyPairDouble)pair).getNumerator();
                StudyPairLonely denominator = ((StudyPairDouble)pair).getDenominator();
                if (!numerator.isEmpty()) pairCountNumerator++;
                if (!denominator.isEmpty()) pairCountDenominator++;
            }
        }
        groupNameLabel.setText(unit.getGroup().getName());
        workHourInWeekLabel.setText(pairCountNumerator == pairCountDenominator ?
                (pairCountNumerator * 2) + " годин; в середньому на день " + ((pairCountDenominator * 2 + pairCountNumerator * 2) / 10) :
                "(Ч) " + (pairCountNumerator * 2) + " годин; (З) " + (pairCountDenominator * 2) + " годин;  в середньому на день " + ((pairCountDenominator * 2 + pairCountNumerator * 2) / 10)
        );
        studyPairInWeekLabel.setText(pairCountNumerator == pairCountDenominator ?
                (pairCountNumerator) + " пар; в середньому на день " + ((pairCountDenominator + pairCountNumerator) / 10) :
                "(Ч) " + (pairCountNumerator) + " годин; (З) " + (pairCountDenominator) + " годин;  в середньому на день " + ((pairCountDenominator + pairCountNumerator) / 10)
        );
    }
}


