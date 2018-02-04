package frame;

import app.Group;
import app.lessons.LessonsUnit;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by Vladimir on 31/01/18.
 **/
public class NewLessonsPanel extends JPanel{
    private JTable jTable;
    private JButton налаштуванняButton;
    private JButton зберегтиButton;
    private JPanel contentPane;
    private JToggleButton button1;
    private JToggleButton button2;
    private JToggleButton button3;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton setButton;
    private ButtonGroup buttonGroup;
    private TableModel tableModel;
    private StudyPair nowStudyPair;
    private HashSet<Integer> rowForbidHashSet = new HashSet<>();
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

    public NewLessonsPanel() {
        nowStudyPair = new EmptyStudyPair();
        setLayout(new GridLayout());
        add(contentPane);
        InitialTable();
        InitialGroupButton();
        setButton.addActionListener(e -> {
            nowStudyPair = new StudyPairLonely(new Lesson(textField1.getText()), new Teacher(textField2.getText()), new Auditory(textField3.getText()));
            rowForbidHashSet.clear();
            tableModel.fireTableDataChanged();
        });
    }

    public NewLessonsPanel(String title) {
        this();
        setName(title);
    }

    private void InitialGroupButton() {
        Border out = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY);
        Border outCenter = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY);
        Border in = BorderFactory.createEmptyBorder(3, 5, 3, 5);
        button1.setBorder(BorderFactory.createCompoundBorder(out, in));
        button1.addActionListener(e -> ((TableModel)jTable.getModel()).fireTableDataChanged());
        button2.setBorder(BorderFactory.createCompoundBorder(outCenter, in));
        button2.addActionListener(e -> ((TableModel)jTable.getModel()).fireTableDataChanged());
        button3.setBorder(BorderFactory.createCompoundBorder(out, in));
        button3.addActionListener(e -> ((TableModel)jTable.getModel()).fireTableDataChanged());
        buttonGroup = new ButtonGroup();
        buttonGroup.add(button1);
        buttonGroup.add(button2);
        buttonGroup.add(button3);
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    private void InitialTable() {
        tableModel = new TableModel();
        jTable.setModel(tableModel);
        jTable.getTableHeader().setDefaultRenderer(new TableHeaderCellRenderer());
        jTable.setDefaultRenderer(String.class, new TableCellDayNameRenderer());
        jTable.setDefaultRenderer(Integer.class, new TableCellPairNumberRenderer());
        jTable.setDefaultRenderer(StudyPair.class, new TableCellSubjectRenderer());
        jTable.setShowGrid(false);
        jTable.setIntercellSpacing(new Dimension(0, 0));
        Enumeration<TableColumn> columns = jTable.getColumnModel().getColumns();
        jTable.setRowHeight(jTable.getRowHeight() * 2);
        jTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tableModel.setValueAt(nowStudyPair,
                        jTable.rowAtPoint(e.getPoint()),
                        jTable.columnAtPoint(e.getPoint())
                );
            }
        });
        while (columns.hasMoreElements()) {
            TableColumn column = columns.nextElement();
            switch (column.getModelIndex() % COLUMN_REPEAT) {
                case DAY_NAME_NUMBER:case PAIR_NUMBER: {
                    column.setMaxWidth(25);
                    column.setMinWidth(25);
                } break;
                case LESSONS_NAME_NUMBER:case TEACHER_NAME_NUMBER: {
                    column.setMinWidth(130);
                    column.setMaxWidth(130);
                } break;
                case AUDITORY_NUMBER: {
                    column.setMinWidth(40);
                    column.setMaxWidth(40);
                } break;
            }
        }
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

    private class TableModel extends AbstractTableModel {
        private ArrayList<NewLessonsUnit> units = new ArrayList<>();

        public TableModel() {
            NewLessonsUnit lessonsUnit = new NewLessonsUnit(new Group("", "ПС-16"), PAIR_IN_DAY, DAY_AT_WEEK);
            lessonsUnit.setPair(0, new StudyPairLonely(new Lesson("ОПІ"), new Teacher("Завірюха"), new Auditory("402")));
            lessonsUnit.setPair(1, new StudyPairLonely(new Lesson("ООП"), new Teacher("Завірюха"), new Auditory("402")));
            lessonsUnit.setPair(2, new StudyPairLonely(new Lesson("WEB"), new Teacher("Завірюха"), new Auditory("407")));
            lessonsUnit.setPair(3, new StudyPairLonely(new Lesson("Практика"), new Teacher("Заболотній"), new Auditory("407")));
            units.add(lessonsUnit);
            units.add(new NewLessonsUnit(new Group("", "ПС-26"), PAIR_IN_DAY, DAY_AT_WEEK));
            units.add(new NewLessonsUnit(new Group("", "ПС-36"), PAIR_IN_DAY, DAY_AT_WEEK));
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
                case LESSONS_NAME_NUMBER:case TEACHER_NAME_NUMBER:case AUDITORY_NUMBER:
                    units.get(columnIndex / COLUMN_REPEAT).setPair(rowIndex, (StudyPair)aValue);
            }
            fireTableDataChanged();
        }
    }

    private class TableCellDayNameRenderer extends DefaultTableCellRenderer {
        private final String[] daysName = new String[] {
            "ПОНЕДІЛОК", "ВІВТОРОК", "СЕРЕДА", "ЧЕТВЕРГ", "ПЯТНИЦЯ", "СУБОТА", "НЕДІЛЯ"
        };
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel labelTop = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            labelTop.setBackground(new Color(242, 242, 242));
            labelTop.setFont(new Font(labelTop.getFont().getName(), Font.BOLD, labelTop.getFont().getSize()));
            labelTop.setHorizontalAlignment(CENTER);
            if ((row * 2) % (PAIR_IN_DAY * 2) < daysName[row / DAY_AT_WEEK].length()) {
                labelTop.setText(String.valueOf(
                        daysName[row / DAY_AT_WEEK].charAt((row * 2) % (PAIR_IN_DAY * 2))
                ));
            }

            JLabel labelBottom = new JLabel();

            if (((row * 2) + 1) % (PAIR_IN_DAY * 2) < daysName[row / DAY_AT_WEEK].length()) {
                labelBottom.setText(String.valueOf(
                        daysName[row / DAY_AT_WEEK].charAt(((row * 2) + 1) % (PAIR_IN_DAY * 2))
                ));
            }
            labelBottom.setHorizontalAlignment(CENTER);
            labelBottom.setFont(new Font(labelBottom.getFont().getName(), Font.BOLD, labelBottom.getFont().getSize()));

            JPanel panel = new JPanel(new GridLayout(2, 1));
            panel.add(labelTop);
            panel.add(labelBottom);
            panel.setBorder(row % PAIR_IN_DAY == PAIR_IN_DAY - 1 ?
                    BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY)
                    :
                    BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY)
            );
            if (row % PAIR_IN_DAY == PAIR_IN_DAY - 1) panel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, Color.LIGHT_GRAY));
            return panel;
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
//            JPanel panel = (JPanel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            JComponent component = ((StudyPair) value).getRendererComponent(StudyPair.Query.values()[(column % COLUMN_REPEAT) - 2]);
            component.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
            if (row % PAIR_IN_DAY == PAIR_IN_DAY - 1)
                component.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, Color.LIGHT_GRAY));
            component.setBackground(Color.WHITE);
            if (rowForbidHashSet.contains(row)) {
                component.setBackground(new Color(0xA299FF));
                component.setOpaque(true);
            }
            for (StudyPair.Forbidden forbidden: ((StudyPair)value).getForbidden(nowStudyPair)) {
                if (forbidden == StudyPair.Forbidden.SELF_FORBIDDEN) {
                    component.setBackground(new Color(0xBBBAB8));
                    component.setOpaque(true);
                }
                if (forbidden == StudyPair.Forbidden.ROW_FORBIDDEN) {
                    rowForbidHashSet.add(row);
                }
            }
            return component;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setLayout(new GridLayout());
        frame.add(new NewLessonsPanel());
        frame.setVisible(true);
    }
}

class NewLessonsUnit {
    private Group group;
    private StudyPair[] pairs;

    public Group getGroup() {
        return group;
    }

    public StudyPair[] getPairs() {
        return pairs;
    }

    public StudyPair getPair(int index) {
        return pairs[index];
    }

    public void setPair(int index, StudyPair pairs) {
        this.pairs[index] = pairs;
    }

    public NewLessonsUnit(Group group, int pairPerDay, int dayPerWeek) {
        this.group = group;
        pairs = new StudyPair[pairPerDay * dayPerWeek];
        for (int i = 0; i < pairs.length; i++) {
            pairs[i] = StudyPair.getEmptyInstance();
        }
    }
}

abstract class StudyPair {
    public static StudyPair getEmptyInstance() {
        return new EmptyStudyPair();
    }

    enum Query {
        LESSON, TEACHER, AUDITORY;
    }

    enum Forbidden {
        DAY_FORBIDDEN,
        ROW_FORBIDDEN,
        COL_FORBIDDEN,
        SELF_FORBIDDEN,
        WEEK_FORBIDDEN,
        NON_FORBIDDEN,
        UNKNOWN_FORBIDDEN
    }

    abstract public JComponent getRendererComponent(Query data);

    abstract public Forbidden[] getForbidden(StudyPair studyPair);

    abstract public Forbidden[] getForbidden(StudyPair studyPair, List<LessonsUnit> units, int row, int col, int pairPerDay, int dayPerWeek);

    abstract public Forbidden[] getSelfForbidden();
}

class EmptyStudyPair extends StudyPair {

    @Override
    public JComponent getRendererComponent(Query data) {
        JLabel jLabel = new JLabel("Empty");
        jLabel.setVerticalAlignment(SwingConstants.CENTER);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return jLabel;
    }

    @Override
    public Forbidden[] getForbidden(StudyPair studyPair) {
        return new Forbidden[0];
    }

    @Override
    public Forbidden[] getForbidden(StudyPair studyPair, List<LessonsUnit> units, int row, int col, int pairPerDay, int dayPerWeek) {
        return getForbidden(studyPair);
    }

    @Override
    public Forbidden[] getSelfForbidden() {
        return new Forbidden[0];
    }


}

class StudyPairLonely extends StudyPair {

    private Lesson lesson;
    private Teacher teacher;
    private Auditory auditory;

    public StudyPairLonely() {
        lesson = new Lesson();
        teacher = new Teacher();
        auditory = new Auditory();
    }

    public StudyPairLonely(Lesson lesson, Teacher teacher, Auditory auditory) {
        this.lesson = lesson;
        this.teacher = teacher;
        this.auditory = auditory;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Auditory getAuditory() {
        return auditory;
    }

    @Override
    public JComponent getRendererComponent(Query data) {
        JLabel label = new JLabel();
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        switch (data) {
            case LESSON: label.setText(lesson.getName());
                break;
            case TEACHER: label.setText(teacher.getName());
                break;
            case AUDITORY: label.setText(auditory.getName());
                break;
        }
        return label;
    }

    @Override
    public Forbidden[] getForbidden(StudyPair studyPair) {
        if (studyPair instanceof StudyPairLonely) {
            StudyPairLonely lonely = (StudyPairLonely) studyPair;
            if (this.teacher.equals(lonely.teacher) ||
                    this.auditory.equals(lonely.auditory)) {
                return new Forbidden[] {Forbidden.ROW_FORBIDDEN, Forbidden.SELF_FORBIDDEN};
            } else {
                return new Forbidden[] {Forbidden.NON_FORBIDDEN};
            }
        } else if (studyPair instanceof StudyPairDouble) {
            StudyPairDouble pairDouble = (StudyPairDouble) studyPair;
            return pairDouble.getForbidden(this);
        }
        return new Forbidden[] {Forbidden.UNKNOWN_FORBIDDEN};
    }

    @Override
    public Forbidden[] getForbidden(StudyPair studyPair, List<LessonsUnit> units, int row, int col, int pairPerDay, int dayPerWeek) {
        if (units == null || row == -1 || col == -1 || pairPerDay == -1 || dayPerWeek == -1) {
            return getForbidden(studyPair);
        }
        return getForbidden(studyPair);
    }

    @Override
    public Forbidden[] getSelfForbidden() {
        return new Forbidden[0];
    }
}

class StudyPairDouble extends StudyPair {

    private StudyPairLonely numerator;
    private StudyPairLonely denominator;

    public StudyPairDouble() {
        numerator = new StudyPairLonely();
        denominator = new StudyPairLonely();
    }

    public StudyPairDouble(StudyPairLonely numerator, StudyPairLonely denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public JComponent getRendererComponent(Query data) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JLabel labelTop = new JLabel();
        JLabel labelBottom = new JLabel();
        labelTop.setHorizontalAlignment(SwingConstants.CENTER);
        labelBottom.setHorizontalAlignment(SwingConstants.CENTER);
        switch (data) {
            case LESSON: {
                labelTop.setText("Ч/ " + numerator.getLesson().getName());
                labelBottom.setText("З\\ " + denominator.getLesson().getName());
            } break;
            case TEACHER: {
                labelTop.setText(numerator.getTeacher().getName());
                labelBottom.setText(denominator.getTeacher().getName());
            } break;
            case AUDITORY: {
                labelTop.setText(numerator.getAuditory().getName());
                labelBottom.setText(denominator.getAuditory().getName());
            } break;
        }
        panel.add(labelTop);
        panel.add(labelBottom);
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createEmptyBorder());
        panel.setBackground(Color.WHITE);
        return panel;
    }

    @Override
    public Forbidden[] getForbidden(StudyPair studyPair) {
        if (studyPair instanceof StudyPairDouble) {
            StudyPairDouble pairDouble = (StudyPairDouble) studyPair;
            HashSet<Forbidden> fb = new HashSet<>();
            Collections.addAll(fb, this.numerator.getForbidden(pairDouble.numerator));
            Collections.addAll(fb, this.denominator.getForbidden(pairDouble.numerator));
            Collections.addAll(fb, this.numerator.getForbidden(pairDouble.denominator));
            Collections.addAll(fb, this.denominator.getForbidden(pairDouble.denominator));
            Forbidden[] forbids = new Forbidden[fb.size()];
            int i = 0;
            for (Forbidden forbidden: fb) {
                forbids[i++] = forbidden;
            }
            return forbids;
        } else if (studyPair instanceof StudyPairLonely) {
            StudyPairLonely lonely = (StudyPairLonely) studyPair;
            HashSet<Forbidden> fb = new HashSet<>();
            Collections.addAll(fb, this.numerator.getForbidden(lonely));
            Collections.addAll(fb, this.denominator.getForbidden(lonely));
            Forbidden[] forbids = new Forbidden[fb.size()];
            int i = 0;
            for (Forbidden forbidden: fb) {
                forbids[i++] = forbidden;
            }
            return forbids;
        }
        return new Forbidden[] {Forbidden.UNKNOWN_FORBIDDEN};
    }

    @Override
    public Forbidden[] getForbidden(StudyPair studyPair, List<LessonsUnit> units, int row, int col, int pairPerDay, int dayPerWeek) {
        return getForbidden(studyPair);
    }

    @Override
    public Forbidden[] getSelfForbidden() {
        return new Forbidden[0];
    }
}

class Lesson {
    private String name;

    public Lesson() {name = "Lesson = ?";}

    public Lesson(String name) {this.name = name;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    @Override
    public String toString() {return "Lesson{" + name + '}';}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        return name != null ? name.equals(lesson.name) : lesson.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}

class Teacher {
    private String name;

    public Teacher() {name = "Teacher = ?";}

    public Teacher(String name) {this.name = name;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    @Override
    public String toString() {return "Teacher{" + name + '}';}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Teacher teacher = (Teacher) o;

        return name != null ? name.equals(teacher.name) : teacher.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}

class Auditory {
    private String name;

    public Auditory() {name = "Auditory = ?";}

    public Auditory(String name) {this.name = name;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    @Override
    public String toString() {return "Auditory{" + name + '}';}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Auditory auditory = (Auditory) o;

        return name != null ? name.equals(auditory.name) : auditory.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}


