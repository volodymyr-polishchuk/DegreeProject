package app.lessons;

import java.util.ArrayList;

/**
 * Created by Vladimir on 29/01/18.
 **/
public class LessonsDay {
    private int pairAtDay;
    private StudyPairSubject[] pairSubjects;

    public LessonsDay(int pairAtDay) {
        this.pairAtDay = pairAtDay;
        pairSubjects = new StudyPairSubject[getPairAtDay()];
        for (StudyPairSubject subject : pairSubjects) {
            subject = StudyPairSubject.getEmptyInstance();
        }
    }

    public StudyPairSubject getStudyPairSubject(int index) {
        return pairSubjects[index];
    }

    public void setStudyPairSubjects(int index, StudyPairSubject subject) {
        pairSubjects[index] = subject;
    }

    public int getPairAtDay() {
        return pairAtDay;
    }
}
