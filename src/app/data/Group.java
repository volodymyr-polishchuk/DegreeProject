package app.data;

/**
 * Created by Vladimir on 03/01/18.
 **/
public class Group implements StudyData, Comparable<Group> {
    private int key = -1;
    private Department department; // Відділення
    private String name; // Назва групи
    private String comments;

    public Group(Department department, String name) {
        this(-1, department, name, "");
    }

    public Group(int key, Department department, String name) {
        this(key, department, name, "");
    }

    public Group(int key, Department department, String name, String comments) {
        this.key = key;
        this.department = department;
        this.name = name;
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Department getDepartment() {
        return department;
    }

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

    @Override
    public String toString() {
        return name + " - " + department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (department != null ? !department.equals(group.department) : group.department != null) return false;
        if (name != null ? !name.equals(group.name) : group.name != null) return false;
        return comments != null ? comments.equals(group.comments) : group.comments == null;

    }

    @Override
    public int hashCode() {
        int result = department != null ? department.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Group o) {
        return this.getName().compareTo(o.getName());
    }
}
