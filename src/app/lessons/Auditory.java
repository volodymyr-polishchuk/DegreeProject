package app.lessons;

/**
 * Created by Vladimir on 06/02/18.
 **/
public class Auditory {
    private String name;

    public Auditory() {name = "";}

    public Auditory(String name) {this.name = name;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    @Override
    public String toString() {return "Auditory{" + name + '}';}

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
