package app.data;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

/**
 * Created by Vladimir on 06/02/18.
 **/
public class Teacher implements StudyData, Comparable<Teacher> {
    private int key = -1;
    private String name;
    private Preference preference;

    public Teacher() {name = "";}

    public Teacher(String name) {this.name = name;}

    public Teacher(String name, Preference preference) {
        this.name = name;
        this.preference = preference;
    }

    public Teacher(int key, String name, Preference preference) {
        this.key = key;
        this.name = name;
        this.preference = preference;
    }

    @Override
    public String getName() {return name;}

    @Override
    public void setName(String name) {this.name = name;}

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public boolean keyExist() {
        return key > 0;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Teacher teacher = (Teacher) o;

        if (name != null ? !name.equals(teacher.name) : teacher.name != null) return false;
        return preference != null ? preference.equals(teacher.preference) : teacher.preference == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (preference != null ? preference.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Teacher o) {
        return this.getName().compareTo(o.getName());
    }
}
