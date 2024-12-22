package ProductManagement;

// FreshGrocery class (subclass of Grocery)
public class FreshGrocery extends Grocery {
    private String weight;
    private boolean organic;
    private String type;

    // Constructor
    public FreshGrocery(String name, String prodID, double price, int quantity, double discount, String expiryDate, String manufactureDate, String weight) {
        super(name, prodID, price, quantity, discount, expiryDate, manufactureDate);
        this.weight = weight;
    }

    public FreshGrocery() {

    }

    // Getters and setters
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight=weight;
    }

    public boolean isOrganic() {
        return organic;
    }

    public void setOrganic(boolean organic) {
        this.organic = organic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
