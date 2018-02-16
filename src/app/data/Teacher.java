package app.data;

/**
 * Created by Vladimir on 06/02/18.
 **/
public class Teacher implements StudyData {
    private String name;

    public Teacher() {name = "";}

    public Teacher(String name) {this.name = name;}

    @Override
    public String getName() {return name;}

    @Override
    public void setName(String name) {this.name = name;}

    @Override
    public String toString() {return "Teacher{" + name + '}';}

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
