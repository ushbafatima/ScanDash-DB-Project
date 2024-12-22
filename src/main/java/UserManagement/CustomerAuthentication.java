package UserManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerAuthentication {

        // Method to fetch card details from the database
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
