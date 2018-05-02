package app.data;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vladimir on 02/05/18.
 **/
public class Holiday implements StudyData {
    private int key = -1;
    private Calendar calendar;
    private boolean repeat;

    public Holiday(Date date) {
        this(date, false);
    }

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
        return "Holiday";
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

    public Holiday(boolean repeat) {
        this.repeat = repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void setDate(Date date) {
        this.calendar.setTime(date);
    }
}
