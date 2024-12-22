package UserManagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardManagement {
    public static Connection conn;
    public static Card getCardFromDB(String cardUID) throws SQLException {
        Connection conn = DBConnection.connectToDB();
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

    // Method to set card credentials
    public static boolean setCardCredentials(String cardID, String cardPIN) {
        String query = "UPDATE CardCredentials SET PIN = ? WHERE CardID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cardPIN);  // Set the new PIN
            stmt.setString(2, cardID);  // Set the card ID to update

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Return true if the update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Return false in case of an error
        }
    }
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
