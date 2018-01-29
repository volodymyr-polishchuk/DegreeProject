package app.lessons;

/**
 * Created by Vladimir on 29/01/18.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudySubject that = (StudySubject) o;

        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (teacher != null ? !teacher.equals(that.teacher) : that.teacher != null) return false;
        return auditory != null ? auditory.equals(that.auditory) : that.auditory == null;
    }

    @Override
    public int hashCode() {
        int result = subject != null ? subject.hashCode() : 0;
        result = 31 * result + (teacher != null ? teacher.hashCode() : 0);
        result = 31 * result + (auditory != null ? auditory.hashCode() : 0);
        return result;
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
}
