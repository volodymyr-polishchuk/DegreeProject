package app.lessons;

/**
 * Created by Vladimir on 06/02/18.
 **/
public class Lesson {
    private String name;

    public Lesson() {name = "";}

    public Lesson(String name) {this.name = name;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    @Override
    public String toString() {return "Lesson{" + name + '}';}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        return name != null ? name.equals(lesson.name) : lesson.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
