package app;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vladimir on 03/01/18.
 **/
public class Group {
    protected String department; // Відділення
    protected String groupName; // Назва групи

    public Group(String department, String groupName) {
        this.department = department;
        this.groupName = groupName;
    }

    public Group(ResultSet resultSet) throws SQLException {
        this.groupName = resultSet.getString("name");
        this.department = resultSet.getInt("department") == 1 ? "Економіки-програмування" : "Будівельне";
    }

    public Group() {
        department = "Не визначено";
        groupName = "Не визначено";
    }

    public String getDepartment() {
        return department;
    }

    public String getName() {
        return groupName;
    }

    @Override
    public String toString() {
        return department + " - " +  groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (department != null ? !department.equals(group.department) : group.department != null) return false;
        return groupName != null ? groupName.equals(group.groupName) : group.groupName == null;

    }

    @Override
    public int hashCode() {
        int result = department != null ? department.hashCode() : 0;
        result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
        return result;
    }
}
