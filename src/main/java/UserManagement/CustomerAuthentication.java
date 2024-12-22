package UserManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerAuthentication {

        // Method to fetch card details from the database
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
    public static Boolean cardFound(Card card) {
        return card!=null;
    }
    public static Boolean isCardActive (Card card) {
        return card.isActive();
    }
    public static String scanCard() {
        // Call the method to get the UID
        String cardID = null;
        try {
            cardID = CardScanner.getCardUID(); // Fetch the UID from Arduino
        } catch (Exception e) {
            System.err.println("Error fetching card UID: " + e.getMessage());
            e.printStackTrace();
            return null; // Stop further execution if UID fetching fails
        }
        return cardID;
    }

    public static Boolean authenticateCard(Card card, String cardID, String enteredPIN) {
        return card.authenticate(cardID, enteredPIN);
    }
    public static Boolean isValidCardID(String cardID) {
        return cardID.length()==8;
    }

}
