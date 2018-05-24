package app.data.loading;

import app.data.Group;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vladimir on 23/05/18.
 **/
public class SemesterLoad {
    private int year;
    private boolean semester; // true - 1; false - 2
    private ArrayList<GroupLoad> groupLoads;

    public SemesterLoad() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        year = calendar.get(Calendar.YEAR);
        semester = calendar.get(Calendar.MONTH) > Calendar.JUNE;
        groupLoads = new ArrayList<>();
    }

    public String getPeriod() {
        return year + "-" + (year + 1) + "/" + (semester ? 1 : 2);
    }

    public void setPeriod(String line) {
        String[] lines = line.split("/");
        String[] years = lines[0].split("-");
        year = Integer.parseInt(years[0]);
        semester = lines[1].equals("1");
    }

    public ArrayList<GroupLoad> getGroupLoads() {
        return groupLoads;
    }

    public void setGroupLoads(ArrayList<GroupLoad> groupLoads) {
        this.groupLoads = groupLoads;
    }

    public void add(Group group) {
        for (GroupLoad groupLoad : getGroupLoads()) {
            if (groupLoad.getGroup().equals(group)) {
                return;
            }
        }
        getGroupLoads().add(new GroupLoad(group));
    }

    public String nextPeriod() {
        year++;
        return year + "-" + (year + 1);
    }

    public String prevPeriod() {
        year--;
        return year + "-" + (year + 1);
    }

    public boolean isSemester() {
        return semester;
    }

    public void setSemester(boolean semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
