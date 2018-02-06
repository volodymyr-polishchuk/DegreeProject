package app.DeprecatedLessons;

import app.Group;

import java.util.Arrays;

/**
 * Created by Vladimir on 29/01/18.
 **/
public class LessonsUnit {

    private Group group;
    private StudyPairSubject[] pairSubjects;
    private int daysOfWeek;
    private int pairOfDay;

    public LessonsUnit(Group group, int daysOfWeek, int pairOfDay) {
        this.group = group;
        this.daysOfWeek = daysOfWeek;
        this.pairOfDay = pairOfDay;
        pairSubjects = new StudyPairSubject[getDaysOfWeek() * getPairOfDay()];
        for (int i = 0; i < getDaysOfWeek() * getPairOfDay(); i++) {
            pairSubjects[i] = new StudyPairSubject();
        }
    }

    public StudySubject getStudySubjectByRow(int row, PairType type) {
        if (PairType.BOTH == type && row % 2 != 0) return new StudySubject();
        return pairSubjects[row / 2].getStudySubject(type);
    }

    public StudyPairSubject getStudyPairByRow(int row) {
        return pairSubjects[row / 2];
    }

    public void setPairSubjects(int row, PairType type, StudySubject subject) {
        pairSubjects[row / 2].setSubject(type, subject);
    }

    public int getDaysOfWeek() {
        return daysOfWeek;
    }

    public int getPairOfDay() {
        return pairOfDay;
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LessonsUnit that = (LessonsUnit) o;

        if (daysOfWeek != that.daysOfWeek) return false;
        if (pairOfDay != that.pairOfDay) return false;
        if (group != null ? !group.equals(that.group) : that.group != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(pairSubjects, that.pairSubjects);

    }

    @Override
    public int hashCode() {
        int result = group != null ? group.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(pairSubjects);
        result = 31 * result + daysOfWeek;
        result = 31 * result + pairOfDay;
        return result;
    }

    @Override
    public String toString() {
        return "LessonsUnit{" +
                "group=" + group +
                ", pairSubjects=" + Arrays.toString(pairSubjects) +
                '}';
    }
}
