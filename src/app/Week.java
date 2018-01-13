package app;

import java.awt.Color;
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

    public Week(WeekList list, String name) {
        this(list.getWeekByName(name));
    }

    public Week(WeekList list, char mark) {
        this(list.getWeekByMark(mark));
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
}
