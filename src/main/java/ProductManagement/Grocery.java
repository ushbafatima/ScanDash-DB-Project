package ProductManagement;

// Grocery class (subclass of Product)
public class Grocery extends Product {
    private String expiryDate;
    private String nutritionalInfo;

    // Constructor
    public Grocery(String name, String prodID, double price, int quantity, double discount, String expiryDate, String nutritionalInfo) {
        super(name, prodID, price, quantity, discount, "Grocery");
        this.expiryDate = expiryDate;
        this.nutritionalInfo = nutritionalInfo;
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

    public String getNutritionalInfo() {
        return nutritionalInfo;
    }

    public void setNutritionalInfo(String nutritionalInfo) {
        this.nutritionalInfo = nutritionalInfo;
    }

}
