package app.data;

/**
 * Created by Vladimir on 06/02/18.
 **/

public class Lesson implements StudyData, Comparable<Lesson>{
    private int key = -1;
    private String name;
    private Auditory auditory;

    public Auditory getAuditory() {
        return auditory;
    }

    public void setAuditory(Auditory auditory) {
        this.auditory = auditory;
    }

    public Lesson() {name = "";}

    public Lesson(String name) {this.name = name;}

    public Lesson(String name, Auditory auditory) {
        this.name = name;
        this.auditory = auditory;
    }

    public Lesson(int key, String name, Auditory auditory) {
        this.key = key;
        this.name = name;
        this.auditory = auditory;
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
        return key >= 0;
    }

    @Override
    public String toString() {return name;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        if (name != null ? !name.equals(lesson.name) : lesson.name != null) return false;
        return auditory != null ? auditory.equals(lesson.auditory) : lesson.auditory == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (auditory != null ? auditory.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Lesson o) {
        return this.getName().compareTo(o.getName());
    }
}
