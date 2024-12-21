package User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CustomerAuthentication {

    static Connection conn = connectToDB();

    public static void main(String[] args) {
        if (conn == null) {
            return; // Stop execution if the connection fails
        }

        // Start scanning card
        System.out.println("Please scan your card.");

        // Call the method to get the UID
        String cardID = null;
        try {
            cardID = CardScanner.getCardUID(); // Fetch the UID from Arduino
        } catch (Exception e) {
            System.err.println("Error fetching card UID: " + e.getMessage());
            e.printStackTrace();
            return; // Stop further execution if UID fetching fails
        }

        try {
            Card card = getCardFromDB(cardID); // Retrieve card details from the database

            if (card == null) {
                System.out.println("Card not found. Authentication failed.");
                return;
            }

            if (card.isActive()) {
                // Card is active, proceed to ask for PIN
                Scanner scanner = new Scanner(System.in);
                String storedPin = card.getPin(); // Retrieve stored PIN

                // Give the user 3 attempts to enter the PIN
                int attempts = 0;
                while (attempts < 3) {
                    System.out.print("Enter PIN: ");
                    String enteredPin = scanner.nextLine();

                    if (card.authenticate(cardID, enteredPin)) {
                        System.out.println("Authentication successful.");
                        break;
                    } else {
                        attempts++;
                        if (attempts < 3) {
                            System.out.println("Incorrect PIN. You have " + (3 - attempts) + " attempt(s) remaining.");
                        } else {
                            System.out.println("Incorrect PIN. Authentication failed. You have exceeded the maximum attempts.");
                        }
                    }
                }
                scanner.close();
            } else {
                // Card is not active
                System.out.println("Card is not active. Authentication failed.");
            }
        } catch (SQLException e) {
            System.err.println("Error while querying the database.");
            e.printStackTrace();
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

    // Method to fetch card details from the database
    private static Card getCardFromDB(String cardUID) throws SQLException {
        String query = "SELECT c.CardID, c.isActive, c.storeCredit, c.creditPoints, cc.PIN " +
                "FROM Card c " +
                "JOIN CardCredentials cc ON c.CardID = cc.CardID " +
                "WHERE c.CardID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cardUID); // Set the card UID to match

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Fetch card details
                    String cardID = rs.getString("CardID");
                    boolean isActive = rs.getBoolean("isActive");
                    double storeCredit = rs.getDouble("storeCredit");
                    int creditPoints = rs.getInt("creditPoints");
                    String pin = rs.getString("PIN");

                    return new Card(cardID, storeCredit, creditPoints, isActive, pin); // Return a Card object with all details
                }
            }
        }
        return null; // Return null if the card does not exist
    }
}
