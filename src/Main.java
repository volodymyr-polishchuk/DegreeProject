import frame.ConnectionForm;

import java.io.*;

/**
 * Created by Vladimir on 01/01/18.
 **/
public class Main {
    public static DatabaseData dd;
    public static void main(String[] args) throws IOException {
//        try {
//            dd = new DatabaseData("localhost", "3306", "Volodymyr", "0000");
//            dd.CreateDatabase();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        ConnectionForm connectionForm = new ConnectionForm();
        connectionForm.setVisible(true);
    }
}
