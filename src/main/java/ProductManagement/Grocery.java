package ProductManagement;

// Grocery class (subclass of Product)
public class Grocery extends Product {
    private String expiryDate;
    private String manufactureDate;

    // Constructor
    public Grocery(String name, String prodID, double price, int quantity, double discount, String expiryDate, String manufactureDate) {
        super(name, prodID, price, quantity, discount, "Grocery");
        this.expiryDate = expiryDate;
        this.manufactureDate = manufactureDate;
    }

    public Grocery() {
    }

    // Getters and setters
    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

}
