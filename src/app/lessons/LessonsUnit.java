package app.lessons;

import app.Group;

import java.util.Arrays;

/**
 * Created by Vladimir on 29/01/18.
 **/
public class LessonsUnit {
    private Group group;

    public Group getGroup() {
        return group;
    }

    private int daysOfWeek;
    private LessonsDay[] lessonsDays;

    public LessonsUnit(Group group, int daysOfWeek, int pairAtDay) {
        this.group = group;
        this.daysOfWeek = daysOfWeek;
        lessonsDays = new LessonsDay[getDaysOfWeek()];
        for (LessonsDay day : lessonsDays) {
            day = new LessonsDay(pairAtDay);
        }
    }

    public LessonsDay getLessonsDay(int index) {
        return lessonsDays[index];
    }

    public LessonsDay setLessonsDays(int index, LessonsDay lessonsDay) {
        LessonsDay tLessonsDay = lessonsDays[index];
        lessonsDays[index] = lessonsDay;
        return tLessonsDay;
    }

    public int getDaysOfWeek() {
        return daysOfWeek;
    }

    @Override
    public String toString() {
        return "LessonsUnit{" +
                "group=" + group +
                ", lessonsDays=" + Arrays.toString(lessonsDays) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LessonsUnit that = (LessonsUnit) o;

        if (group != null ? !group.equals(that.group) : that.group != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(lessonsDays, that.lessonsDays);

    }

    @Override
    public int hashCode() {
        int result = group != null ? group.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(lessonsDays);
        return result;
    }
}
