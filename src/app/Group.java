package app;

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

    public String getDepartment() {
        return department;
    }

    public String getGroupName() {
        return groupName;
    }
}
