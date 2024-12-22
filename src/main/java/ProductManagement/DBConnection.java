package ProductManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Method to establish the database connection
    public static Connection connectToDB() {
        // Connection details
        String url = "jdbc:postgresql://aws-0-ap-south-1.pooler.supabase.com:5432/postgres";
        String user = "postgres.hcsvpkykasdfmmflldby";
        String password = "_ScanDash7226";
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
