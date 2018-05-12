package app.data;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by Vladimir on 10/05/18.
 *  Для визначення того як потрібно відмальовувати цей компонент в таблиці JTable
 **/
public class SchedulerTableCellRendererComponent extends DefaultTableCellRenderer {
    private static final int
            ROW_PERIODS = 0, ROW_WORK_DAYS = 1, ROW_WEEK_COUNT = 2, ROW_DATA = 3;
    private static final int
            COLUMN_NAMES = 0, COLUMNS_DATA = 1;
    private static final Color borderColor = new Color(164, 164, 164);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setHorizontalAlignment(CENTER);
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, borderColor));

        // Обробка таблиці в місці даних
        if (row >= ROW_DATA && column >= COLUMNS_DATA) {
            label.setBackground(Color.WHITE);
            if (value instanceof Week) {
                Week w = (Week) value;
                label.setBackground(w.getColor() == null ? Color.BLACK : w.getColor());
                label.setToolTipText(w.getName());
            }
        }

        // Обробка таблиці в місці підписів
        if ((row == ROW_PERIODS || row == ROW_WORK_DAYS || row == ROW_WEEK_COUNT) && column == COLUMN_NAMES) {
            label.setBackground(UIManager.getColor("Panel.background"));
            if (value instanceof String) {
                label.setToolTipText((String)value);
            }
        }

        // Підписи груп
        if (row >= ROW_DATA && column == COLUMN_NAMES) {
            label.setBackground(UIManager.getColor("Panel.background"));
            if (value instanceof Group) {
                Group g = (Group) value;
                label.setText(g.getName());
                label.setToolTipText(g.getComments());
            }
        }

        // Обробка виділеної стрічки
        if ((isSelected || hasFocus) && row >= 3) {
            label.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(122, 138, 153)));
        }
        return label;
    }
}
