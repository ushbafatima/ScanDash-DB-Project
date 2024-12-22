package CardManagement;

import UserManagement.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardManagement {
    public static Connection conn;

    // Fetch card details from the database
    public static Card getCardFromDB(String cardUID) throws Exception {
        System.out.println("\n\n" + cardUID);

        // SQL query to fetch card details based on CardID
        String query = "SELECT c.CardID, c.isActive, c.storeCredit, c.creditPoints, cc.PIN " +
                "FROM Card c " +
                "JOIN CardCredentials cc ON c.CardID = cc.CardID " +
                "WHERE c.CardID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the cardUID as a parameter in the query
            stmt.setString(1, cardUID);

            try (ResultSet rs = stmt.executeQuery()) {
                // Check if a record was returned
                if (rs.next()) {
                    // Fetch card details
                    String cardID = rs.getString("CardID");
                    boolean isActive = rs.getBoolean("isActive");
                    double storeCredit = rs.getDouble("storeCredit");
                    int creditPoints = rs.getInt("creditPoints");
                    String pin = rs.getString("PIN");

                    // Decrypt the PIN from the database
                    pin = BlowfishCipher.decrypt(pin);

                    // Return a new Card object with all the details
                    return new Card(cardID, storeCredit, creditPoints, isActive, pin);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while executing query: " + e.getMessage());
            throw e; // Rethrow exception if needed
        }

        // Return null if no card was found
        return null;
    }


    // Set card credentials in the database
    public static boolean setCardCredentials(String cardID, String cardPIN) throws Exception {
        cardPIN = BlowfishCipher.encrypt(cardPIN); // Encrypt cardID before updating
        String query = "UPDATE CardCredentials SET PIN = ? WHERE CardID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cardPIN);  // Set the new PIN
            stmt.setString(2, cardID);  // Set the encrypted card ID to update

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Return true if the update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Return false in case of an error
        }
    }

    // Fetch all available cards (inactive cards)
    public static String[] getAvailableCards() throws SQLException {
        String query = "SELECT cardID FROM card WHERE isActive = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setBoolean(1, false);  // Cards that are inactive (available)
            try (ResultSet rs = pst.executeQuery()) {
                List<String> availableCardsList = new ArrayList<>();
                while (rs.next()) {
                    availableCardsList.add(rs.getString("cardID"));
                }
                return availableCardsList.toArray(new String[0]);
            }
        }
    }
}
