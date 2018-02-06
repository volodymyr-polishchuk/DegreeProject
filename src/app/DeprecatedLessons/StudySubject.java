package app.DeprecatedLessons;

/**
 * Created by Vladimir on 30/01/18.
 **/
public class StudySubject {

    private Subject subject;
    private Teacher teacher;
    private Auditory auditory;

    public StudySubject() {
        subject = new Subject();
        teacher = new Teacher();
        auditory = new Auditory();
    }

    public StudySubject(Subject subject, Teacher teacher, Auditory auditory) {
        this.subject = subject;
        this.teacher = teacher;
        this.auditory = auditory;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Auditory getAuditory() {
        return auditory;
    }

    public void setAuditory(Auditory auditory) {
        this.auditory = auditory;
    }

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

    @Override
    public String toString() {
        return "StudySubject{" +
                "subject=" + subject +
                ", teacher=" + teacher +
                ", auditory=" + auditory +
                '}';
    }
}
