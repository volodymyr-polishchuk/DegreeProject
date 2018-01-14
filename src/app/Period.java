package app;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vladimir on 12/01/18.
 **/
public class Period {
    private Date sDate;
    private Date lDate;
    private int workDay;

    /**
     * @param sDate дата початку періоду
     * @param lDate дата кінця періоду
     * @param workDay кількість робочих днів періоду
     */
    public Period(Date sDate, Date lDate, int workDay) {
        this.sDate = sDate;
        this.lDate = lDate;
        this.workDay = workDay;
    }

    @Contract("_ -> !null")
    public static ArrayList<Period> GetWeekList(Date date) {
        final int DAY = 1000*60*60*24;

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());
        Date first;
        Date second;
        int workDay;
        ArrayList<Period> arr = new ArrayList<>(52);

        if ((c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) { c.add(Calendar.DAY_OF_MONTH, 1);
        } else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) { c.add(Calendar.DAY_OF_MONTH, 2);}

        for (int i = 0; i < 52; i++) {
            first = c.getTime();
            c.add(Calendar.DAY_OF_MONTH, Calendar.FRIDAY - c.get(Calendar.DAY_OF_WEEK));
            second = c.getTime();
            workDay = (int) ((second.getTime() - first.getTime()) / DAY) + 1;
            c.add(Calendar.DAY_OF_MONTH, 3);
            arr.add(new Period(first, second, workDay));
        }
        return arr;
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
