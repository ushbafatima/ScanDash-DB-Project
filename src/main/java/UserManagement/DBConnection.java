package UserManagement;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    public static Connection connectToDB() {
        Properties properties = new Properties();

        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.err.println("Unable to find application.properties");
                return null;
            }
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Failed to load properties file!");
            e.printStackTrace();
            return null;
        }

        String url = properties.getProperty("database.url");
        String user = properties.getProperty("database.username");
        String password = properties.getProperty("database.password");

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
            return null;
        }
    }
}
