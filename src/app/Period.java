package app;

import java.util.Date;

/**
 * Created by Vladimir on 12/01/18.
 **/
public class Period {
    private Date sDate;
    private Date lDate;
    private int workDay;

    public Period(Date sDate, Date lDate, int workDay) {
        this.sDate = sDate;
        this.lDate = lDate;
        this.workDay = workDay;
    }

    public Date getStartDate() {
        return sDate;
    }

    public Date getLastDate() {
        return lDate;
    }

    public int getWorkDay() {
        return workDay;
    }
}
