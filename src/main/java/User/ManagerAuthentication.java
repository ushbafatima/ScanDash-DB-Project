package User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ManagerAuthentication {

    static Connection conn = connectToDB();

    public static void main(String[] args) {

        if (conn == null) {
            return;  // Stop execution if connection fails
        }

        // Start authentication process
        System.out.println("Manager Authentication");

        Scanner scanner = new Scanner(System.in);
        int attempts = 0;
        Manager manager = null;

        // Loop to allow up to 3 attempts
        while (attempts < 3) {
            System.out.print("Enter Username: ");
            String enteredUsername = scanner.nextLine();

            // Check if username ends with .scandash
            if (!enteredUsername.endsWith(".scandash")) {
                System.out.println("Username must end with '.scandash'. Please try again.");

                continue; // Skip further processing if username doesn't match pattern
            }

            // Fetch manager from the database
            try {
                manager = getManagerFromDB(enteredUsername);
            } catch (SQLException e) {
                System.err.println("Error while querying the database.");
                e.printStackTrace();
                return;
            }

            // Check if the manager exists
            if (manager == null) {
                System.out.println("Manager not found. Please try again.");
                attempts++;
            } else {
                // Now ask for the password if manager exists
                System.out.print("Enter Password: ");
                String enteredPassword = scanner.nextLine();

                // Authenticate the manager
                if (manager.authenticate(enteredUsername, enteredPassword)) {
                    System.out.println("Authentication successful");
                    break; // Authentication successful, exit the loop
                } else {
                    System.out.println("Incorrect password. You have " + (3 - attempts - 1) + " attempt(s) remaining.");
                    attempts++;
                }
            }

            // If 3 attempts have been exhausted
            if (attempts == 3) {
                System.out.println("Authentication failed. You have exceeded the maximum attempts.");
            }
        }
    }

    // Method to establish the database connection
    private static Connection connectToDB() {
        // Connection details
        String url = "jdbc:postgresql://aws-0-ap-south-1.pooler.supabase.com:5432/postgres";
        String user = "postgres.hcsvpkykasdfmmflldby";
        String password = "_ScanDash7226";
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database successfully!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
            return null;
        }
    }

    // Method to fetch manager details from the database
    private static Manager getManagerFromDB(String username) throws SQLException {
        String query = "SELECT ManagerID, Username, Password FROM ManagerCredentials WHERE Username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username); // Set the entered username

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int managerID = rs.getInt("ManagerID");
                    String password = rs.getString("Password");
                    return new Manager(managerID, username, password);
                }
            }
        }
        return null;  // Return null if no manager found with the given username
    }
}
