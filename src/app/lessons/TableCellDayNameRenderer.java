package app.lessons;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by Vladimir on 26/02/18.
 **/
public class TableCellDayNameRenderer extends DefaultTableCellRenderer {
    private final int PAIR_IN_DAY;
    public TableCellDayNameRenderer(int pair_in_day) {PAIR_IN_DAY = pair_in_day;}
    private final String[] daysName = new String[]{
            "ПОНЕДІЛОК", "ВІВТОРОК", "СЕРЕДА", "ЧЕТВЕРГ", "ПЯТНИЦЯ", "СУБОТА", "НЕДІЛЯ"
    };

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel labelTop = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        labelTop.setBackground(new Color(242, 242, 242));
        labelTop.setFont(new Font(labelTop.getFont().getName(), Font.BOLD, labelTop.getFont().getSize()));
        labelTop.setHorizontalAlignment(CENTER);
        if ((row * 2) % (PAIR_IN_DAY * 2) < daysName[row / PAIR_IN_DAY].length()) {
            labelTop.setText(String.valueOf(
                    daysName[row / PAIR_IN_DAY].charAt((row * 2) % (PAIR_IN_DAY * 2))
            ));
        }
        JLabel labelBottom = new JLabel();
        if (((row * 2) + 1) % (PAIR_IN_DAY * 2) < daysName[row / PAIR_IN_DAY].length()) {
            labelBottom.setText(String.valueOf(
                    daysName[row / PAIR_IN_DAY].charAt(((row * 2) + 1) % (PAIR_IN_DAY * 2))));
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
        if (row % PAIR_IN_DAY == PAIR_IN_DAY - 1)
            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, Color.LIGHT_GRAY));
        return panel;
    }
}
