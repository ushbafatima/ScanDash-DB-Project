package CartManagement;

import ProductManagement.Product;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import UserManagement.DBConnection;

public class CustomerCart {
    private Map<Product, Integer> cart;

    public CustomerCart() {
        this.cart = new HashMap<>();
    }

    public boolean addToCart(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return false;
        }

        // Check if adding this quantity exceeds available inventory
        int currentQuantity = getCurrentQuantityInCart(product);
        if (currentQuantity + quantity > product.getQuantity()) {
            return false;
        }

        cart.put(product, cart.getOrDefault(product, 0) + quantity);
        return true;
    }

    public boolean removeFromCart(Product product, int quantity) {
        if (product == null || !cart.containsKey(product)) {
            return false;
        }

        int currentQuantity = cart.get(product);
        if (quantity >= currentQuantity) {
            cart.remove(product);
        } else {
            cart.put(product, currentQuantity - quantity);
        }
        return true;
    }

    public void clearCart() {
        cart.clear();
    }

    public Map<Product, Integer> getCart() {
        return new HashMap<>(cart);
    }

    public double calculateBill() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double itemPrice = product.getPrice() * (1 - product.getDiscount()/100);
            total += itemPrice * quantity;
        }
        return total;
    }

    public boolean checkout() {
        if (cart.isEmpty()) {
            return false;
        }

        Connection conn = DBConnection.connectToDB();
        if (conn == null) {
            return false;
        }

        try {
            conn.setAutoCommit(false);

            // Update inventory and delete if quantity becomes 0
            String updateInventorySql =
                    "UPDATE inventory " +
                            "SET quantity = CASE " +
                            "    WHEN quantity - ? <= 0 THEN 0 " +
                            "    ELSE quantity - ? " +
                            "END " +
                            "WHERE \"productID\" = ?::uuid";

            String deleteInventorySql =
                    "DELETE FROM inventory WHERE quantity = 0";

            try (PreparedStatement updateInventoryStmt = conn.prepareStatement(updateInventorySql);
                 PreparedStatement deleteInventoryStmt = conn.prepareStatement(deleteInventorySql)) {

                for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
                    Product product = entry.getKey();
                    int quantity = entry.getValue();

                    // Update inventory
                    updateInventoryStmt.setInt(1, quantity);
                    updateInventoryStmt.setInt(2, quantity);
                    updateInventoryStmt.setString(3, product.getProductID());
                    updateInventoryStmt.executeUpdate();
                }

                // Delete products with zero quantity
                deleteInventoryStmt.executeUpdate();

                conn.commit();
                clearCart();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getCurrentQuantityInCart(Product product) {
        return cart.getOrDefault(product, 0);
    }

    public boolean isProductInCart(Product product) {
        return cart.containsKey(product);
    }
}
