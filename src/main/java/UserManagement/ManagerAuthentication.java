package UserManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerAuthentication {

    // Method to fetch manager details from the database
    public static Manager getManagerFromDB(String username) throws SQLException {
        Connection conn = DBConnection.connectToDB();
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

    public static Boolean managerFound(Manager manager){
        return manager!=null;
    }

    public static Boolean authenticateManager(Manager manager, String enteredUsername, String enteredPassword) {
        return manager.authenticate(enteredUsername, enteredPassword);
    }
}
