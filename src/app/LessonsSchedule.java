package app;

import java.util.ArrayList;

/**
 * Created by Vladimir on 21/01/18.
 **/
public class LessonsSchedule {
    private ArrayList<LessonsUnit> list;

}

class LessonsUnit {
    private Group group;
    private ArrayList<LessonsDay> list;

}

class LessonsDay {
    private ArrayList<StudyPairSubject> list;
}

class StudyPairSubject {
    private StudySubject numerator;
    private StudySubject denominator;
}

class StudySubject {
    private Subject subject;
    private Teacher teacher;
    private Auditory auditory;

}

class Subject {
    private String name;
}

class Teacher {
    private String name;
}

class Auditory {
    private String name;
}
