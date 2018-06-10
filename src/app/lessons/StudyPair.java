package app.lessons;

import javax.swing.*;
import java.util.List;

/**
 * Created by Vladimir on 06/02/18.
 **/
public abstract class StudyPair {
    static StudyPair getEmptyInstance() {
        return new EmptyStudyPair();
    }

    public enum Query {
        LESSON, TEACHER, AUDITORY;
    }

    public enum Forbidden {
        DAY_FORBIDDEN,
        ROW_FORBIDDEN,
        COL_FORBIDDEN,
        SELF_FORBIDDEN,
        WEEK_FORBIDDEN,
        NON_FORBIDDEN,
        UNKNOWN_FORBIDDEN
    }

    abstract public JComponent getRendererComponent(StudyPair.Query data);

    abstract public StudyPair.Forbidden[] getForbidden(StudyPair studyPair);

    abstract public StudyPair.Forbidden[] getForbidden(StudyPair studyPair, List<LessonsUnit> units, int row, int col, int pairPerDay, int dayPerWeek);

    abstract public StudyPair.Forbidden[] getSelfForbidden(int row, int col, int pairPerDay, int dayPerWeek);
}
