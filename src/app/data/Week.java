package app.data;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vladimir on 03/01/18.
 **/
public class Week {
    public static final String HOLIDAY_NAME = "Канікули";
    public static final String UNKNOWN_NAME = "Не визначено";

    private char mark;
    private String name;
    private Color color;
    private String abbreviation;

    public Week() {
        this.mark = 'X';
        this.name = UNKNOWN_NAME;
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
