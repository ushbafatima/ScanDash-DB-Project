package ProductManagement;

// FreshGrocery class (subclass of Grocery)
public class FreshGrocery extends Grocery {
    private String weight;

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


}
