package app.data.loading;

import app.data.Group;

import java.util.ArrayList;

/**
 * Created by Vladimir on 23/05/18.
 **/
public class GroupLoad {
    private int key = -1;
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

    public float getWeekLoadingSum() {
        float sum = 0;
        for (LoadUnit item : loadUnits) {
            sum += item.getWeekLoad();
        }
        return sum;
    }

    public boolean keyExists() {
        return key > -1;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
