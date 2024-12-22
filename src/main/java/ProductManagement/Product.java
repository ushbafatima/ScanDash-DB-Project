package ProductManagement;

import java.util.Objects;

// Base Product class
public class Product {
    private String productID;
    private String productName;
    private double price;
    private int quantity;
    private double discount;
    private String category;

    // Constructor
    public Product(String productID, String productName, double price, int quantity, double discount, String category) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.category = category;
    }

    // Getters and setters
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product product)) return false;
        return Objects.equals(productID, product.productID);
    }
}

// Subclass for Grocery products
class Grocery extends Product {
    private String groceryCategory;
    private String expiryDate;
    private String nutritionalInfo;

    // Constructor
    public Grocery(String productID, String productName, double price, int quantity, double discount, String category, String groceryCategory, String expiryDate, String nutritionalInfo) {
        super(productID, productName, price, quantity, discount, category);
        this.groceryCategory = groceryCategory;
        this.expiryDate = expiryDate;
        this.nutritionalInfo = nutritionalInfo;
    }

    // Getters and setters
    public String getGroceryCategory() {
        return groceryCategory;
    }

    public void setGroceryCategory(String groceryCategory) {
        this.groceryCategory = groceryCategory;
    }

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

// Subclass for Fresh Grocery
class FreshGrocery extends Grocery {
    private double weight;
    private boolean organic;
    private String type;

    // Constructor
    public FreshGrocery(String productID, String productName, double price, int quantity, double discount, String category, String groceryCategory, String expiryDate, String nutritionalInfo, double weight, boolean organic, String type) {
        super(productID, productName, price, quantity, discount, category, groceryCategory, expiryDate, nutritionalInfo);
        this.weight = weight;
    }

    // Getters and setters
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
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

// Subclass for Packaged Grocery
class PackagedGrocery extends Grocery {
    private String brand; // Matches text

    // Constructor
    public PackagedGrocery(String productID, String productName, double price, int quantity, double discount, String category, String groceryCategory, String expiryDate, String nutritionalInfo, String brand) {
        super(productID, productName, price, quantity, discount, category, groceryCategory, expiryDate, nutritionalInfo);
        this.brand = brand;
    }

    // Getter and setter for brand
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}

// Subclass for Electronics
class Electronics extends Product {
    private String model; // Matches text
    private String warrantyPeriod; // Matches text

    // Constructor
    public Electronics(String productID, String productName, double price, int quantity, double discount, String category, String model, String warrantyPeriod) {
        super(productID, productName, price, quantity, discount, category);
        this.model = model;
        this.warrantyPeriod = warrantyPeriod;
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

// Subclass for Cosmetics
class Cosmetics extends Product {
    private String ingredients; // Matches text
    private String brand; // Matches text

    // Constructor
    public Cosmetics(String productID, String productName, double price, int quantity, double discount, String category, String ingredients, String brand) {
        super(productID, productName, price, quantity, discount, category);
        this.ingredients = ingredients;
        this.brand = brand;
    }

    // Getters and setters
    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}

// Subclass for Appliances
class Appliances extends Product {
    private double capacity; // Matches float8
    private double efficiencyRate; // Matches float8

    // Constructor
    public Appliances(String productID, String productName, double price, int quantity, double discount, String category, double capacity, double efficiencyRate) {
        super(productID, productName, price, quantity, discount, category);
        this.capacity = capacity;
        this.efficiencyRate = efficiencyRate;
    }

    // Getters and setters
    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getEfficiencyRate() {
        return efficiencyRate;
    }

    public void setEfficiencyRate(double efficiencyRate) {
        this.efficiencyRate = efficiencyRate;
    }
}
