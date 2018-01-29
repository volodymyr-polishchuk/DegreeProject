package test.lessons;

import app.Group;

/**
 * Created by Vladimir on 29/01/18.
 **/
public abstract class LessonsUnit {

    private Group group;
    private StudyPairSubject[] pairSubjects;

    abstract StudyPairSubject getPairByCell(int column, int row);

    abstract int getDaysOfWeek();

    abstract int getPairOfDay();

    public LessonsUnit(Group group) {
        this.group = group;
        pairSubjects = new StudyPairSubject[getDaysOfWeek() * getPairOfDay()];
    }

    public Group getGroup() {
        return group;
    }
    
}

class StudyPairSubject {
    private StudySubject[] subjects;
    public static final int NUMERATOR = 0;
    public static final int DENOMINATOR = 1;
    public static final int BOTH = 2;
    
    public StudyPairSubject() {
        subjects = new StudySubject[2];
    }

    public void setSubject(int how, StudySubject subject) {
        if (how > 2 || how < 0) throw new IllegalArgumentException("Argument must be NUMERATOR, DENOMINATOR or BOTH");
        switch (how) {
            case NUMERATOR: subjects[how] = subject;
                break;
            case DENOMINATOR: subjects[how] = subject;
                break;
            case BOTH:
                subjects[NUMERATOR] = subject;
                subjects[DENOMINATOR] = subject;
                break;
        }
    }

    public StudySubject getStudySubject(int i) {
        if (i > 2 || i < 0) throw new IllegalArgumentException("Argument must be NUMERATOR, DENOMINATOR or BOTH");
        return i == 2 ? subjects[NUMERATOR] : subjects[i];
    }
}

class StudySubject {
    private Subject subject;
    private Teacher teacher;
    private Auditory auditory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudySubject that = (StudySubject) o;

        if (!subject.equals(that.subject)) return false;
        if (!teacher.equals(that.teacher)) return false;
        return auditory.equals(that.auditory);

    }

    @Override
    public int hashCode() {
        int result = subject.hashCode();
        result = 31 * result + teacher.hashCode();
        result = 31 * result + auditory.hashCode();
        return result;
    }
}

abstract class NamedItem {
    private String name;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

class Subject extends NamedItem {

}

class Teacher extends NamedItem {

}

class Auditory extends NamedItem {

}