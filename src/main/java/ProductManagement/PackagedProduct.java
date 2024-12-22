package ProductManagement;

// PackagedProduct class (subclass of Grocery)
public class PackagedProduct extends Grocery {
    private String brand;

    // Constructor
    public PackagedProduct(String name, String prodID, double price, int quantity, double discount, String expiryDate, String manufactureDate, String brand) {
        super(name, prodID, price, quantity, discount, expiryDate, manufactureDate);
        this.brand = brand;
    }
    public PackagedProduct() {

    }
    // Getter and setter for brand
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
