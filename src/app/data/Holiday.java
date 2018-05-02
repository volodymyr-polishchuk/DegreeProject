package app.data;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vladimir on 02/05/18.
 **/
public class Holiday implements StudyData, Comparable<Holiday> {
    private int key = -1;
    private Calendar calendar;
    private boolean repeat;

    public Holiday(Date date, boolean repeat) {
        this(date, repeat, -1);
    }

    public Holiday(Date date, boolean repeat, int key) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.repeat = repeat;
        this.key = key;
    }

    @Override
    public String getName() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return format.format(calendar.getTime()) + (repeat ? " - повторювати" : "");
    }

    @Override
    public void setName(String name) {
//        nothing
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public boolean keyExist() {
        return key > -1;
    }

    public Date getDate() {
        return calendar.getTime();
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void setDate(Date date) {
        this.calendar.setTime(date);
    }

    public boolean getRepeat() {
        return repeat;
    }

    @Override
    public int compareTo(@NotNull Holiday o) {
        return calendar.compareTo(o.calendar);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Holiday holiday = (Holiday) o;

        if (repeat != holiday.repeat) return false;
        return calendar != null ? calendar.equals(holiday.calendar) : holiday.calendar == null;

    }

    @Override
    public int hashCode() {
        int result = calendar != null ? calendar.hashCode() : 0;
        result = 31 * result + (repeat ? 1 : 0);
        return result;
    }
}
