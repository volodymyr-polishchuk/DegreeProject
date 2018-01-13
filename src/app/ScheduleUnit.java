package app;

/**
 * Created by Vladimir on 03/01/18.
 **/
public class ScheduleUnit {
    private Group group;
    private Week[] weeks = new Week[52];

    public ScheduleUnit(Group group) {
        this.group = group;
        for (Week i :
                weeks) {
            i = DegreeProject.weekList.getWeekByName("Навчання");
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
