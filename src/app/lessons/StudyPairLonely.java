package app.lessons;

import javax.swing.*;
import java.util.List;

/**
 * Created by Vladimir on 06/02/18.
 **/
public class StudyPairLonely extends StudyPair {

    private Lesson lesson;
    private Teacher teacher;
    private Auditory auditory;

    public StudyPairLonely() {
        lesson = new Lesson();
        teacher = new Teacher();
        auditory = new Auditory();
    }

    public StudyPairLonely(Lesson lesson, Teacher teacher, Auditory auditory) {
        this.lesson = lesson;
        this.teacher = teacher;
        this.auditory = auditory;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Auditory getAuditory() {
        return auditory;
    }

    @Override
    public JComponent getRendererComponent(Query data) {
        JLabel label = new JLabel();
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        switch (data) {
            case LESSON: label.setText(lesson.getName());
                break;
            case TEACHER: label.setText(teacher.getName());
                break;
            case AUDITORY: label.setText(auditory.getName());
                break;
        }
        return label;
    }

    @Override
    public Forbidden[] getForbidden(StudyPair studyPair) {
        if (studyPair instanceof StudyPairLonely) {
            StudyPairLonely lonely = (StudyPairLonely) studyPair;
            if (this.teacher.equals(lonely.teacher) ||
                    this.auditory.equals(lonely.auditory)) {
                return new Forbidden[] {Forbidden.ROW_FORBIDDEN, Forbidden.SELF_FORBIDDEN};
            } else {
                return new Forbidden[] {Forbidden.NON_FORBIDDEN};
            }
        } else if (studyPair instanceof StudyPairDouble) {
            StudyPairDouble pairDouble = (StudyPairDouble) studyPair;
            return pairDouble.getForbidden(this);
        }
        return new Forbidden[] {Forbidden.UNKNOWN_FORBIDDEN};
    }

    @Override
    public Forbidden[] getForbidden(StudyPair studyPair, List<LessonsUnit> units, int row, int col, int pairPerDay, int dayPerWeek) {
        if (units == null || row == -1 || col == -1 || pairPerDay == -1 || dayPerWeek == -1) {
            return getForbidden(studyPair);
        }
        return getForbidden(studyPair);
    }

    @Override
    public Forbidden[] getSelfForbidden() {
        return new Forbidden[0];
    }
}