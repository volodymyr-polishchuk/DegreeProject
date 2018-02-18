package app.data;

/**
 * Created by Vladimir on 18/02/18.
 **/
public class Department implements StudyData {
    private int key = -1;
    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

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
}
