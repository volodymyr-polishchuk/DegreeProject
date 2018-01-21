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
    private ArrayList<StudyPair> list;
}

abstract class StudyPair {

}

class StudyOneSubject extends StudyPair {
    private Subject subject;
    private Teacher teacher;
    private Auditory auditory;
}

class StudyTwoSubject extends StudyPair {
    private StudyOneSubject numerator;
    private StudyOneSubject denominator;
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
