package test;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Vladimir on 10/06/18.
 **/
public class CalendarTest {
    public static void main(String[] args) {
        GregorianCalendar calendar = new GregorianCalendar(2018, Calendar.SEPTEMBER, 1);
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
    }
}
