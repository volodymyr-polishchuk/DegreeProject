package app.data;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

/**
 * Created by Vladimir on 06/02/18.
 **/
public class Teacher implements StudyData {
    private String name;
    private Preference preference;

    public Teacher() {name = "";}

    public Teacher(String name) {this.name = name;}

    public Teacher(String name, Preference preference) {
        this.name = name;
        this.preference = preference;
    }

    @Override
    public String getName() {return name;}

    @Override
    public void setName(String name) {this.name = name;}

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }
//
//    @Override
//    public String toString() {return "Teacher{" + name + '}';}


    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", preference=" + preference +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Teacher teacher = (Teacher) o;

        return name != null ? name.equals(teacher.name) : teacher.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
