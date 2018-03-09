package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Vladimir on 01/01/18.
 **/
public class DatabaseData {
    private String address;
    private String port;
    private String user;
    private String password;
    private String databaseName;

    private Connection connection;

    public DatabaseData(String address, String port, String user, char[] password, String databaseName) throws SQLException {
        this.address = address;
        this.port = port;
        this.user = user;
        this.password = String.valueOf(password);
        this.databaseName = databaseName;

        connection = DriverManager.getConnection("jdbc:mysql://" + this.address + ":" + this.port + "/" + this.databaseName + "?useSSL=false",
                this.user, this.password);
    }

    public DatabaseData(String address, String port, String user, char[] password) throws SQLException {
        this.address = address;
        this.port = port;
        this.user = user;
        this.password = String.valueOf(password);

        connection = DriverManager.getConnection("jdbc:mysql://" + this.address + ":" + this.port + "?useSSL=false", this.user, this.password);
    }

    public DatabaseData(Connection connection) {
        this.connection = connection;
    }

    public String getAddress() {
        return address;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public Connection getConnection() {
        return connection;
    }

    public void createDatabase() {
        try {
            Statement st = connection.createStatement();
            st.execute("CREATE DATABASE DegreeProjectBase");
            st.execute("USE DegreeProjectBase;");
            // Create table weeks
            st.execute("CREATE TABLE weeks(mark VARCHAR(1)," +
                    " name VARCHAR(256), " +
                    " color INTEGER, " +
                    " abbreviation VARCHAR(16));");
            // TODO треба описати створення всіх баз даних
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
