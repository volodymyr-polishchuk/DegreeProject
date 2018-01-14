package app;

/**
 * Created by Vladimir on 03/01/18.
 **/
public class ScheduleUnit {
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
}
