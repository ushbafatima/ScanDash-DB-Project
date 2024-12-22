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

