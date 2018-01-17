package app;

import java.sql.ResultSet;
import java.util.Date;

/**
 * Created by Vladimir on 03/01/18.
 **/
public class Group {
    private String department; // Відділення
    private String groupName; // Назва групи

    public Group(String department, String groupName) {
        this.department = department;
        this.groupName = groupName;
    }

    public Group(ResultSet resultSet) {

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
        return "Group{" +
                "d='" + department + '\'' +
                ", n='" + groupName + '\'' +
                '}';
    }
}
