package ProductManagement;

// ApplianceProduct class (subclass of Product)
public class ApplianceProduct extends Product {
    private Double efficiencyRating;
    private double capacity;

    // Constructor
    public ApplianceProduct(String name, String prodID, double price, int quantity, double discount, Double efficiencyRating, double capacity) {
        super(name, prodID, price, quantity, discount, "Appliance");
        this.efficiencyRating = efficiencyRating;
        this.capacity = capacity;
    }
    public ApplianceProduct() {

    }
    // Getters and setters
    public double getEfficiencyRate() {
        return efficiencyRating;
    }

    public void setEfficiencyRate(Double energyEfficiencyRating) {
        this.efficiencyRating = energyEfficiencyRating;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

}
