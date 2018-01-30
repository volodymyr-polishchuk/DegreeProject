package app.lessons;

/**
 * Created by Vladimir on 30/01/18.
 **/
abstract class NamedItem {
    private String name;

    public NamedItem() {
        name = "";
    }

    public NamedItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
