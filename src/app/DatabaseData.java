package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    boolean reconnect() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.address + ":" + this.port + "/" + this.databaseName + "?useSSL=false",
                    this.user, this.password);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public DatabaseData(String address, String port, String user, char[] password, String databaseName) throws SQLException {
        this.address = address;
        this.port = port;
        this.user = user;
        this.password = String.valueOf(password);
        this.databaseName = databaseName;
        connection = DriverManager.getConnection("jdbc:mysql://" + this.address + ":" + this.port + "/" + this.databaseName + "?useSSL=false&useUnicode=true&characterEncoding=utf-8",
                this.user, this.password);
    }

    public DatabaseData(String address, String port, String user, char[] password) throws SQLException {
        this.address = address;
        this.port = port;
        this.user = user;
        this.password = String.valueOf(password);

        connection = DriverManager.getConnection("jdbc:mysql://" + this.address + ":" + this.port + "?useSSL=false&useUnicode=true&characterEncoding=utf-8", this.user, this.password);
    }

    public Connection getConnection() {
        return connection;
    }
}
