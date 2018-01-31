package frame;

import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Enumeration;
import java.util.Objects;

/**
 * Created by Vladimir on 31/01/18.
 **/
public class NewLessonsPanel extends JPanel{
    private JTable jTable;
    private JButton налаштуванняButton;
    private JButton зберегтиButton;
    private JPanel contentPane;
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
     * Кількість днів в тижні починаючи від понеділка, де 1 - Понеділок, 2 - Понеділок...Вівторок, 3 - Понеділок...Середа
     */
    private final int DAY_AT_WEEK = 5;

    public NewLessonsPanel() {
        setLayout(new GridLayout());
        add(contentPane);
        InitialTable();
    }

    void InitialTable() {
        jTable.setModel(new TableModel());
        jTable.getTableHeader().setDefaultRenderer(new TableHeaderCellRenderer());
        jTable.setDefaultRenderer(String.class, new TableCellDayNameRenderer());
        jTable.setDefaultRenderer(Integer.class, new TableCellPairNumberRenderer());
        jTable.setDefaultRenderer(Object.class, new TableCellSubjectRenderer());
        jTable.setShowGrid(false);
        jTable.setIntercellSpacing(new Dimension(0, 0));
        Enumeration<TableColumn> columns = jTable.getColumnModel().getColumns();
        jTable.setRowHeight(jTable.getRowHeight() * 2);
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
        private String[] groupName = new String[] {"ПС-16", "ПС-26"};

        @Override
        public int getRowCount() {
            return DAY_AT_WEEK * PAIR_IN_DAY;
        }

        @Override
        public String getColumnName(int column) {
            if (column % COLUMN_REPEAT == TEACHER_NAME_NUMBER) return groupName[column / COLUMN_REPEAT];
            return "";
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex % COLUMN_REPEAT) {
                case DAY_NAME_NUMBER: return String.class;
                case PAIR_NUMBER: return Integer.class;
//                case LESSONS_NAME_NUMBER:case TEACHER_NAME_NUMBER:case AUDITORY_NUMBER: return
                default: return Object.class;
            }
        }

        @Override
        public int getColumnCount() {
            return COLUMN_REPEAT * groupName.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return null;
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
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY));
            if (row % PAIR_IN_DAY == PAIR_IN_DAY - 1) label.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, Color.LIGHT_GRAY));
            return label;
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


