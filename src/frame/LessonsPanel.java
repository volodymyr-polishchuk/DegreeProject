package frame;

import app.DegreeProject;
import app.Group;
import app.lessons.*;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.stream.Collectors;

import static app.lessons.PairType.NUMERATOR;

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
    private TableModel tableModel;
    ButtonGroup buttonGroup;

    /**
     * Кільсть колонок, що потрібні для відображення інформації. Номери колонок задаються нижче
     */
    private final int COLUMN_REPEAT = 5;
    // Константи, що позначають положення колонок в таблиці
    private final int DAY_NAME_NUMBER = 0;
    private final int PAIR_NUMBER = 1;
    private final int LESSONS_NAME_NUMBER = 2;
    private final int TEACHER_NAME_NUMBER = 3;
    private final int AUDITORY_NUMBER = 4;
    /**
     * Кількість пар в одному дні
     */
    private final int PAIR_IN_DAY = 5;
    /**
     * Кількість колонок для відображення одного дня
     */
    private final int DAYS_HEIGHT = PAIR_IN_DAY * 2;
    /**
     * Кількість днів в тижні починаючи від понеділка, де 1 - Понеділок, 2 - Понеділок...Вівторок, 3 - Понеділок...Середа
     */
    private final int DAY_AT_WEEK = 5;

    public LessonsPanel() {
        setLayout(new GridLayout());
        add(contentPane);
        InitialTable();
        InitialButtons();
    }

    public LessonsPanel(String s) {
        this();
        setName(s);
    }

    private void InitialButtons() {
        Border out = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY);
        Border outCenter = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY);
        Border in = BorderFactory.createEmptyBorder(3, 5, 3, 5);
        button1.setBorder(BorderFactory.createCompoundBorder(out, in));
        button1.setFocusable(false);
        button1.setActionCommand("NUMERATOR");
        button1.addActionListener(e -> ((TableModel)lessonsTable.getModel()).fireTableDataChanged());
        button2.setBorder(BorderFactory.createCompoundBorder(outCenter, in));
        button2.setFocusable(false);
        button2.setSelected(true);
        button2.setActionCommand("BOTH");
        button2.addActionListener(e -> ((TableModel)lessonsTable.getModel()).fireTableDataChanged());
        button3.setBorder(BorderFactory.createCompoundBorder(out, in));
        button3.setFocusable(false);
        button3.setActionCommand("DENOMINATOR");
        button3.addActionListener(e -> ((TableModel)lessonsTable.getModel()).fireTableDataChanged());
        buttonGroup = new ButtonGroup();
        buttonGroup.add(button1);
        buttonGroup.add(button2);
        buttonGroup.add(button3);
    }

    private void InitialTable() {
        tableModel = new TableModel();
        lessonsTable.setModel(tableModel);
        lessonsTable.setDefaultRenderer(Object.class, new TableCellRenderer());
        lessonsTable.getTableHeader().setDefaultRenderer(new TableHeaderModelRenderer());

        lessonsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        lessonsTable.getTableHeader().setResizingAllowed(false);
        lessonsTable.getTableHeader().setReorderingAllowed(false);

        lessonsTable.setShowGrid(false);
        lessonsTable.setIntercellSpacing(new Dimension(0, 0));

        lessonsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Enumeration<TableColumn> enumeration = lessonsTable.getColumnModel().getColumns();
        while (enumeration.hasMoreElements()) {
            TableColumn column = enumeration.nextElement();
            switch (column.getModelIndex() % COLUMN_REPEAT) {
                case DAY_NAME_NUMBER:case PAIR_NUMBER: {
                    column.setMaxWidth(25);
                    column.setMinWidth(24);
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
        lessonsTable.addMouseListener(new TableMouseListener());
    }

    private class TableMouseListener implements java.awt.event.MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            int row = lessonsTable.rowAtPoint(e.getPoint());
            int col = lessonsTable.columnAtPoint(e.getPoint());
//            lessonsTable.getModel().setValueAt(new StudySubject(new Subject("Предмет"), new Teacher(), new Auditory()), row, col);
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class TableHeaderModelRenderer extends DefaultTableCellHeaderRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable jTable, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(jTable, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(LEFT);
            switch (column % COLUMN_REPEAT) {
                case LESSONS_NAME_NUMBER:case TEACHER_NAME_NUMBER: {
                    label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
                } break;
                case AUDITORY_NUMBER: {
                    label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
                } break;
            }
            return label;
        }
    }

    private class TableModel extends AbstractTableModel {
        String[] daysName = {"ПОНЕДІЛОК", "ВІВТОРОК", "СЕРЕДА", "ЧЕТВЕРГ", "ПЯТНИЦЯ", "СУБОТА", "НЕДІЛЯ"};
        String[] columnName = {"<html><b>Предмет</b></html>", "<html><b>Викладач</b></html>", "<html><b>Ауд.</b></html>"};
        ArrayList<LessonsUnit> lessonsUnits = new ArrayList<>();

        public TableModel() {
            LessonsUnit unit;
            for (Group group : DegreeProject.GROUPLIST.GetAllWeek()) {
                unit = new LessonsUnit(group, DAY_AT_WEEK, PAIR_IN_DAY);
                lessonsUnits.add(unit);
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public String getColumnName(int column) {
            switch (column % COLUMN_REPEAT) {
                case TEACHER_NAME_NUMBER: return lessonsUnits.get(column / COLUMN_REPEAT).getGroup().getName();
                default: return "";
            }
        }

        @Override
        public int getRowCount() {
            return DAYS_HEIGHT * DAY_AT_WEEK + 1;
        }

        @Override
        public int getColumnCount() {
//            return groups.length * COLUMN_REPEAT;
            return lessonsUnits == null ? 1 : lessonsUnits.size() * COLUMN_REPEAT;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            try {
                lessonsUnits.get(columnIndex / COLUMN_REPEAT)
                        .setPairSubjects(
                                rowIndex - 1,
                                PairType.valueOf(buttonGroup.getSelection().getActionCommand()),
                                (StudySubject)aValue
                        );
                fireTableDataChanged();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }

        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            /*Header*/
            if (rowIndex == 0) {
                switch (columnIndex % COLUMN_REPEAT) {
                    case DAY_NAME_NUMBER:       return "";
                    case PAIR_NUMBER:           return "";
                    case LESSONS_NAME_NUMBER:   return columnName[0];
                    case TEACHER_NAME_NUMBER:   return columnName[1];
                    case AUDITORY_NUMBER:       return columnName[2];
                }
            }
            /*Cells*/
            try {
                if (columnIndex % COLUMN_REPEAT == 0) return daysName[(rowIndex - 1) / DAYS_HEIGHT].charAt((rowIndex - 1) % DAYS_HEIGHT);

                switch (columnIndex % COLUMN_REPEAT) {
                    case PAIR_NUMBER:           return rowIndex % 2 == 0 ? "" : (((rowIndex % DAYS_HEIGHT) + 1) / 2);
                    case LESSONS_NAME_NUMBER:   return lessonsUnits.get(columnIndex / COLUMN_REPEAT)
                            .getStudySubjectByRow(rowIndex - 1, PairType.valueOf(buttonGroup.getSelection().getActionCommand()))
                            .getSubject();
                    case TEACHER_NAME_NUMBER:   return lessonsUnits.get(columnIndex / COLUMN_REPEAT)
                            .getStudySubjectByRow(rowIndex - 1, PairType.valueOf(buttonGroup.getSelection().getActionCommand()))
                            .getTeacher();
                    case AUDITORY_NUMBER:       return lessonsUnits.get(columnIndex / COLUMN_REPEAT)
                            .getStudySubjectByRow(rowIndex - 1, PairType.valueOf(buttonGroup.getSelection().getActionCommand()))
                            .getAuditory();
                    default:                    return "";
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
            /*Set color*/
            if (column % COLUMN_REPEAT == 0) {
                label.setBackground(table.getTableHeader().getBackground());
            } else if (((row - 1) / DAYS_HEIGHT) % 2 == 0) {
                label.setBackground(new Color(246, 246, 246));
            } else {
                label.setBackground(Color.WHITE);
            }
            /*Set font*/
            switch (column % COLUMN_REPEAT) {
                case DAY_NAME_NUMBER: {
                    label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));
                }
            }
            /*Set border*/
            if (column % COLUMN_REPEAT == 0) {
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
                if (row % DAYS_HEIGHT == 0)
                    label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
            } else if (row % 2 == 0) {
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
            } else {
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
            }
            return label;
        }
    }
}
