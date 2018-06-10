package app.data.loading;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by Vladimir on 23/05/18.
 **/
public class LoadingNumberTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        switch (column) {
            case LoadingTableModel.NUMBER_COLUMN:
                label.setForeground(new Color(0x646464));
                label.setHorizontalAlignment(CENTER);
                break;
            case LoadingTableModel.AUDITORY_COLUMN:
                label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
                label.setHorizontalAlignment(RIGHT);
                break;
            case LoadingTableModel.WEEK_LOADING_COLUMN:
                label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
                label.setHorizontalAlignment(RIGHT);
                label.setText(String.valueOf(Math.round(((float) value) * 100) / 100.0));
                break;
            default:
                label.setForeground(UIManager.getColor("Label.foreground"));
                label.setHorizontalAlignment(RIGHT);
        }
        return label;
    }
}
