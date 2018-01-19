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
}
