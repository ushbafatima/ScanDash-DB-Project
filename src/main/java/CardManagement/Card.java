package CardManagement;

public class Card {
    private String cardID;
    private boolean isActive;
    private String pin;
    private double storeCredit;
    private int creditPoints;

    // Constructor to initialize all attributes
    public Card(String cardID, double storeCredit, int creditPoints, boolean isActive, String pin) {
        this.cardID = cardID;
        this.storeCredit = storeCredit;
        this.creditPoints = creditPoints;
        this.isActive = isActive;
        this.pin = pin;
    }

    // Constructor to initialize cardID, isActive, and pin
    public Card(String cardID, boolean isActive, String pin) {
        this.cardID = cardID;
        this.isActive = isActive;
        this.pin = pin;
        this.storeCredit = 0.0; // Default value
        this.creditPoints = 0; // Default value
    }

    // Constructor to initialize the Card object
    public Card(String cardNumber) {
        this.cardID = cardNumber;
    }

    // Constructor to initialize cardID and isActive only
    public Card(String cardID, boolean isActive) {
        this.cardID = cardID;
        this.isActive = isActive;
        this.pin = null; // No PIN provided
        this.storeCredit = 0.0; // Default value
        this.creditPoints = 0; // Default value
    }

    // Default constructor
    public Card() {
        this.cardID = null;
        this.isActive = false;
        this.pin = null;
        this.storeCredit = 0.0;
        this.creditPoints = 0;
    }

    // Getters and Setters
    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public double getStoreCredit() {
        return storeCredit;
    }

    public void setStoreCredit(double storeCredit) {
        this.storeCredit = storeCredit;
    }

    public int getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(int creditPoints) {
        this.creditPoints = creditPoints;
    }

    // Method to authenticate the card
    public boolean authenticate(String scannedCardID, String enteredPIN) {
        return this.cardID != null && this.cardID.equals(scannedCardID) && this.pin != null && this.pin.equals(enteredPIN);
    }

    // Method to return card details as a string
    @Override
    public String toString() {
        return "Card [CardID=" + cardID + ", Active=" + isActive + ", PIN=" + pin +
                ", StoreCredit=" + storeCredit + ", CreditPoints=" + creditPoints + "]";
    }
}
