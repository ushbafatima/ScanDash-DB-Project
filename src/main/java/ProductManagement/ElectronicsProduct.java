package ProductManagement;

// ElectronicsProduct class (subclass of Product)
public class ElectronicsProduct extends Product {
    private String model;
    private String warrantyPeriod;

    // Constructor
    public ElectronicsProduct(String name, String prodID, double price, int quantity, double discount, String model, String warrantyPeriod) {
        super(name, prodID, price, quantity, discount, "Electronics");
        this.model = model;
        this.warrantyPeriod = warrantyPeriod;
    }

    public ElectronicsProduct() {
        // TODO Auto-generated constructor stub
    }

    // Getters and setters
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(String warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }
}
