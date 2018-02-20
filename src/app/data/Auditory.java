package app.data;

/**
 * Created by Vladimir on 06/02/18.
 **/
public class Auditory implements StudyData{
    private int key = -1;
    private String name;

    public Auditory() {name = "";}

    public Auditory(String name) {this.name = name;}

    public Auditory(int key, String name) {
        this.key = key;
        this.name = name;
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

    @Override
    public String toString() {return name;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Auditory auditory = (Auditory) o;

        return name != null ? name.equals(auditory.name) : auditory.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
