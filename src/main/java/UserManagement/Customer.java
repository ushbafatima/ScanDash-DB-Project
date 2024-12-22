package UserManagement;

public class Customer {
    private String customerID;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Card card; // Each customer has a Card

    // Constructor to initialize the Customer object
    public Customer(String customerID, String firstName, String lastName, String phoneNumber, Card card) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.card = card;
    }

    // Constructor to initialize Customer object with partial details
    public Customer(String firstName, String lastName, String phoneNumber, String cardNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.card = new Card(cardNumber); // Create a Card object with the card number
    }

    // Getters and Setters
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    // Method to authenticate the customer using card details
    public boolean authenticateCustomer(String scannedCardID, String enteredPIN) {
        // Authenticate the card first using the authenticate method from Card class
        return card.authenticate(scannedCardID, enteredPIN);
    }

    // Method to return customer details as a string
    @Override
    public String toString() {
        return "Customer [CustomerID=" + customerID + ", FirstName=" + firstName + ", LastName=" + lastName
                + ", PhoneNumber=" + phoneNumber + ", Card=" + card + "]";
    }
}
