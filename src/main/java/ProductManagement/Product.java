package ProductManagement;

// Product class
public class Product {
    private String name;
    private String prodID;
    private double price;
    private int quantity;
    private double discount;
    private String category;

    // Constructor
    public Product(String name, String prodID, double price, int quantity, double discount, String category) {
        this.name = name;
        this.prodID = prodID;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.category = category;
    }

    public Product() {

    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

// Grocery class (subclass of Product)
class Grocery extends Product {
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

// FreshGrocery class (subclass of Grocery)
class FreshGrocery extends Grocery {
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

// PackagedProduct class (subclass of Grocery)
class PackagedProduct extends Grocery {
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

// ElectronicsProduct class (subclass of Product)
class ElectronicsProduct extends Product {
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

// CosmeticsProduct class (subclass of Product)
class CosmeticsProduct extends Product {
    private String brand;
    private String ingredients;

    // Constructor
    public CosmeticsProduct(String name, String prodID, double price, int quantity, double discount, String brand, String ingredients) {
        super(name, prodID, price, quantity, discount, "Cosmetics");
        this.brand = brand;
        this.ingredients = ingredients;
    }

    public CosmeticsProduct() {

    }

    // Getters and setters
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}

// ApplianceProduct class (subclass of Product)
class ApplianceProduct extends Product {
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