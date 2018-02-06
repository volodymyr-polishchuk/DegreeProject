package app.lessons;

import app.Group;

/**
 * Created by Vladimir on 06/02/18.
 **/
public class LessonsUnit {
    private Group group;
    private StudyPair[] pairs;
    private int pairPerDay;
    private int dayPerWeek;

    public Group getGroup() {
        return group;
    }

    public StudyPair[] getPairs() {
        return pairs;
    }

    public StudyPair getPair(int index) {
        return pairs[index];
    }

    public void setPair(int index, StudyPair pairs) {
        this.pairs[index] = pairs;
    }

    public LessonsUnit(Group group, int pairPerDay, int dayPerWeek) {
        this.group = group;
        this.pairPerDay = pairPerDay;
        this.dayPerWeek = dayPerWeek;
        pairs = new StudyPair[pairPerDay * dayPerWeek];
        for (int i = 0; i < pairs.length; i++) {
            pairs[i] = StudyPair.getEmptyInstance();
        }
    }

    public int getPairPerDay() {
        return pairPerDay;
    }

    public int getDayPerWeek() {
        return dayPerWeek;
    }
}
