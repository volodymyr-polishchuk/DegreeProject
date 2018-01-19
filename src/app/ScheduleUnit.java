package app;

import java.util.Objects;

/**
 * Created by Vladimir on 03/01/18.
 **/
public class ScheduleUnit implements Comparable {
    private Group group;
    private Week[] weeks = new Week[52];

    public ScheduleUnit(Group group) {
        this.group = group;
        for (int i = 0; i < 52; i++) {
            weeks[i] = new Week();
        }
    }

    public Group getGroup() {
        return group;
    }

    public Week getWeek(int index) {
        return weeks[index];
    }

    public void setWeek(int index, Week week) {
        this.weeks[index] = week;
    }

    public String decode() {
        String line = "";
        for (Week week :
                weeks) {
            line += week.getMark();
        }
        return line;
    }

    public boolean encode(WeekList list, String line) {
        if (line.length() > 52) return false;
        for (int i = 0; i < 52; i++) {
            try {
                weeks[i] = list.getWeekByMark(line.charAt(i));
            } catch (ArrayIndexOutOfBoundsException e) {
                weeks[i] = new Week();
            }
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        return this.getGroup().getName().compareTo(((ScheduleUnit)o).getGroup().getName());
    }
}
