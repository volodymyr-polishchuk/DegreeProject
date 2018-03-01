package app.data;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vladimir on 03/01/18.
 **/
public class Week {
    private char mark;
    private String name;
    private Color color;
    private String abbreviation;

    public Week() {
        this.mark = 'X';
        this.name = "Не визначено";
        this.color = Color.WHITE;
        this.abbreviation = "X";
    }

    public Week(char mark, String name, Color color, String abbreviation) {
        this.mark = mark;
        this.name = name;
        this.color = color;
        this.abbreviation = abbreviation;
    }

    public Week(ResultSet resultSet) throws SQLException {
        this.mark = resultSet.getString("mark").charAt(0);
        this.name = resultSet.getString("name");
        this.color = new Color(resultSet.getInt("color"));
        this.abbreviation = resultSet.getString("abbreviation");
    }

    private Week (Week week) {
        this.mark = week.getMark();
        this.name = week.getName();
        this.color = week.getColor();
        this.abbreviation = week.getAbbreviation();
    }

    public char getMark() {
        return mark;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public static DefaultTableCellRenderer getInstanceTableCellRendererComponent() {
        return new TableCellRendererComponent();
    }

    // Для визначення того як потрібно відмальовувати цей компонент в таблиці JTable
    private static class TableCellRendererComponent extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(CENTER);
            if (row >= 3 && column >= 1) {
                try {
                    Week w = (Week)value;
                    label.setBackground(w.getColor());
                } catch (ClassCastException e) {
                    label.setBackground(Color.WHITE);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                label.setBackground(new Color(238, 238, 238));
            }
            if ((isSelected || hasFocus) && row >= 3) {
                label.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(122, 138, 153)));
            }
            return label;
        }
    }

    @Override
    public String toString() {
        return abbreviation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Week week = (Week) o;

        if (mark != week.mark) return false;
        if (name != null ? !name.equals(week.name) : week.name != null) return false;
        if (color != null ? !color.equals(week.color) : week.color != null) return false;
        return abbreviation != null ? abbreviation.equals(week.abbreviation) : week.abbreviation == null;
    }

    @Override
    public int hashCode() {
        int result = (int) mark;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        return result;
    }
}
