package app.data.loading;

import app.data.Group;

import java.util.ArrayList;

/**
 * Created by Vladimir on 23/05/18.
 **/
public class GroupLoad {
    private Group group;
    private int weekCount;
    private ArrayList<LoadUnit> loadUnits;

    public GroupLoad(Group group) {
        this.group = group;
        weekCount = 1;
        loadUnits = new ArrayList<>();
    }

    public int getWeekCount() {
        return weekCount;
    }

    public void setWeekCount(int weekCount) {
        this.weekCount = weekCount;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public ArrayList<LoadUnit> getLoadUnits() {
        return loadUnits;
    }

    public void setLoadUnits(ArrayList<LoadUnit> loadUnits) {
        this.loadUnits = loadUnits;
    }
}
