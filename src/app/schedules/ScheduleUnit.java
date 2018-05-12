package app.schedules;

import app.data.Group;
import app.data.Week;
import app.data.WeekList;

public class ScheduleUnit implements Comparable<ScheduleUnit> {
    private Group group;
    private Week[] weeks = new Week[52];


    public ScheduleUnit(Group group) {
//        this.group = new Group(group.getKey(), group.getDepartment(), group.getName(), group.getComments());
        this.group = group;
        for (int i = 0; i < 52; i++) {
            weeks[i] = new Week();
        }
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

    public Group getGroup() {
        return group;
    }

    @Override
    public int compareTo(ScheduleUnit o) {
        return this.getGroup().getName().compareTo(o.getGroup().getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScheduleUnit unit = (ScheduleUnit) o;

        return group != null ? group.equals(unit.group) : unit.group == null;

    }

    @Override
    public int hashCode() {
        return group != null ? group.hashCode() : 0;
    }
}
