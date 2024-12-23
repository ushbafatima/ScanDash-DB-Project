package ProductManagement;

import DatabaseConfig.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductManagement {
    private static Connection con = DBConnection.connectToDB();
    private static PreparedStatement pst;
    private static ResultSet rs;

    public ProductManagement() {
        // Initialize the connection, statement, and result set to null
        con = null;
        pst = null;
        rs = null;
    }

    /******************* METHODS TO MANAGE PRODUCT OPERATIONS ***************/
    public static Product findProductInInventory(String ID) {
        Product product = null;
        try {
            String query = "SELECT * FROM inventory WHERE \"productID\" = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, ID);
            rs = pst.executeQuery();

            if (rs.next()) {
                product = new Product();
                product.setProdID(rs.getString("productID"));
                product.setName(rs.getString("productName"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setDiscount(rs.getDouble("discount"));
                product.setCategory(rs.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return product;
    }

    public static Boolean addProductToInventory(Product product) {

        try {
            // Check if the product already exists

            if (findProductInInventory(product.getProdID()) != null) {
                return false;
            }

            // Prepare SQL statement to insert the product
            String query = "INSERT INTO inventory (\"productID\", \"productName\", price, quantity, discount, category) VALUES (?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(query);
            pst.setString(1, product.getProdID());
            pst.setString(2, product.getName());
            pst.setDouble(3, product.getPrice());
            pst.setInt(4, product.getQuantity());
            pst.setDouble(5, product.getDiscount());
            pst.setString(6, product.getCategory());

            // Execute the SQL statement to insert the product into the database
            pst.executeUpdate();

            return true; // Return true to indicate successful addition of the product

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false to indicate unsuccessful addition of the product
        } finally {
            closeResources();
        }
    }

    public static boolean removeProductFromDB(Product product) {
        // Check if the product exists in the database
        if (findProductInInventory(product.getProdID()) == null) {
            return false; // Product does not exist, so return false
        }

        try {
            // Prepare SQL statement to delete the product
            String query = "DELETE FROM inventory WHERE \"productID\" = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, product.getProdID());

            // Execute the SQL statement to delete the product from the database
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                return true; // Return true to indicate successful removal of the product
            } else {
                return false; // Return false if no rows were affected (product not found or deletion failed)
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false to indicate unsuccessful removal of the product
        } finally {
            closeResources();
        }
    }

    public static boolean updateProductInDB(Product product) {
        // Check if the product exists in the database
        if (findProductInInventory(product.getProdID()) == null) {
            return false; // Product does not exist, so return false
        }

        try {
            // Prepare SQL statement to update the product
            String query = "UPDATE inventory SET \"productName\" = ?, price = ?, quantity = ?, discount = ?, category = ? WHERE \"productID\" = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, product.getName());
            pst.setDouble(2, product.getPrice());
            pst.setInt(3, product.getQuantity());
            pst.setDouble(4, product.getDiscount());
            pst.setString(5, product.getCategory());
            pst.setString(6, product.getProdID());

            // Execute the SQL statement to update the product in the database
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                return true; // Return true to indicate successful update of the product
            } else {
                return false; // Return false if no rows were affected (update failed)
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false to indicate unsuccessful update of the product
        } finally {
            closeResources();
        }
    }

    /******************* METHODS TO MANAGE PRODUCT OPERATIONS ***************/

    /******************* METHODS TO MANAGE CASTING ***************/
    public static Product createProductByCategory(Product product) {
        Product resultProduct;
        System.out.println(product.getCategory()); // Using println for better visibility

        switch (product.getCategory()) {
            case "Electronics":
                resultProduct = new ElectronicsProduct();
                break;
            case "Packaged Product":
                resultProduct = new PackagedProduct();
                break;
            case "Cosmetics":
                resultProduct = new CosmeticsProduct();
                break;
            case "Appliances":
                resultProduct = new ApplianceProduct();
                break;
            case "Fresh Grocery":
                resultProduct = new FreshGrocery();
                break;
            case "Grocery":
                resultProduct = new Grocery();
                break;
            default:
                // For unknown categories, return a generic Product instance
                resultProduct = new Product();
                break;
        }

        // Copy common details only if a specific subclass instance was created
        if (!resultProduct.getClass().equals(Product.class)) {
            copyCommonProductDetails(product, resultProduct);
        }

        return resultProduct;
    }

    private static void copyCommonProductDetails(Product source, Product destination) {
        destination.setProdID(source.getProdID());
        destination.setName(source.getName());
        destination.setPrice(source.getPrice());
        destination.setQuantity(source.getQuantity());
        destination.setDiscount(source.getDiscount());
        destination.setCategory(source.getCategory());
    }


    /******************* METHODS TO MANAGE CASTING ***************/

    /******************* METHODS TO ADD CATEGORY SPECIFIC PRODUCT ***************/
    public static Boolean addProductToCategoryDB(Product product) {
        if (product instanceof ElectronicsProduct) {
            return addElectronicsDescription((ElectronicsProduct) product);
        } else if (product instanceof ApplianceProduct) {
            return addApplianceDescription((ApplianceProduct) product);
        } else if (product instanceof PackagedProduct) {
            return addPackagedGroceryDescription((PackagedProduct) product);
        } else if (product instanceof CosmeticsProduct) {
            return addCosmeticsDescription((CosmeticsProduct) product);
        } else if (product instanceof FreshGrocery) {
            return addFreshGroceryDescription((FreshGrocery) product);
        } else if (product instanceof Grocery) {
            return addGroceryDescription((Grocery) product);
        } else {
            return false; // Unsupported product type
        }
    }

    private static Boolean addGroceryDescription(Grocery groceryProduct) {
        try {
            // Prepare SQL statement to insert data into grocery table or update if the product already exists
            String query = "INSERT INTO grocery (\"productID\", \"expiryDate\", \"nutritionalInfo\") " +
                    "VALUES (?, ?, ?) " +
                    "ON CONFLICT (\"productID\") DO UPDATE SET " +
                    "\"expiryDate\" = EXCLUDED.\"expiryDate\", \"nutritionalInfo\" = EXCLUDED.\"nutritionalInfo\"";

            pst = con.prepareStatement(query);
            pst.setString(1, groceryProduct.getProdID());
            pst.setString(2, groceryProduct.getExpiryDate());
            pst.setString(3, groceryProduct.getNutritionalInfo());

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    private static Boolean addElectronicsDescription(ElectronicsProduct electronicsProduct) {
        try {
            String query = "INSERT INTO electronics (\"productID\", \"warrantyPeriod\", \"model\") " +
                    "VALUES (?, ?, ?) " +
                    "ON CONFLICT (\"productID\") DO UPDATE SET " +
                    "\"warrantyPeriod\" = EXCLUDED.\"warrantyPeriod\", \"model\" = EXCLUDED.\"model\"";

            pst = con.prepareStatement(query);
            pst.setString(1, electronicsProduct.getProdID());
            pst.setString(2, electronicsProduct.getWarrantyPeriod());
            pst.setString(3, electronicsProduct.getModel());

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    private static Boolean addApplianceDescription(ApplianceProduct applianceProduct) {
        try {
            String query = "INSERT INTO appliances (\"productID\", \"capacity\", \"efficiencyRate\") " +
                    "VALUES (?, ?, ?) " +
                    "ON CONFLICT (\"productID\") DO UPDATE SET " +
                    "\"capacity\" = EXCLUDED.\"capacity\", \"efficiencyRate\" = EXCLUDED.\"efficiencyRate\"";

            pst = con.prepareStatement(query);
            pst.setString(1, applianceProduct.getProdID());
            pst.setDouble(2, applianceProduct.getCapacity());
            pst.setDouble(3, applianceProduct.getEfficiencyRate());

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    private static Boolean addPackagedGroceryDescription(PackagedProduct packagedProduct) {
        try {
            String query = "INSERT INTO \"packagedGrocery\" (\"productID\", \"brand\") " +
                    "VALUES (?, ?) " +
                    "ON CONFLICT (\"productID\") DO UPDATE SET " +
                    "\"brand\" = EXCLUDED.\"brand\"";

            pst = con.prepareStatement(query);
            pst.setString(1, packagedProduct.getProdID());
            pst.setString(2, packagedProduct.getBrand());

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    private static Boolean addCosmeticsDescription(CosmeticsProduct cosmeticsProduct) {
        try {
            String query = "INSERT INTO cosmetics (\"productID\", \"ingredients\", \"brand\") " +
                    "VALUES (?, ?, ?) " +
                    "ON CONFLICT (\"productID\") DO UPDATE SET " +
                    "\"ingredients\" = EXCLUDED.\"ingredients\", " +
                    "\"brand\" = EXCLUDED.\"brand\"";

            pst = con.prepareStatement(query);
            pst.setString(1, cosmeticsProduct.getProdID());
            pst.setString(2, cosmeticsProduct.getIngredients());
            pst.setString(3, cosmeticsProduct.getBrand());

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    private static Boolean addFreshGroceryDescription(FreshGrocery freshGrocery) {
        try {
            String query = "INSERT INTO \"freshGrocery\" (\"productID\", \"organic\", \"type\", \"weight\") " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (\"productID\") DO UPDATE SET " +
                    "\"organic\" = EXCLUDED.\"organic\", " +
                    "\"type\" = EXCLUDED.\"type\", " +
                    "\"weight\" = EXCLUDED.\"weight\"";

            pst = con.prepareStatement(query);
            pst.setString(1, freshGrocery.getProdID());
            pst.setString(2, freshGrocery.getExpiryDate());
            pst.setString(3, freshGrocery.getNutritionalInfo());
            pst.setString(4, freshGrocery.getWeight());

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    /******************* METHODS TO GET CATEGORY SPECIFIC PRODUCT ***************/
    public static Product findProductinCategoryDB(Product product) {
        if (product instanceof ApplianceProduct) {
            return getApplianceProduct((ApplianceProduct) product);

        } else if (product instanceof FreshGrocery) {
            return getFreshGroceryProduct((FreshGrocery) product);
        } else if (product instanceof PackagedProduct) {
            System.out.println("hi im prod");
            return getPackedProduct((PackagedProduct) product);
        } else if (product instanceof CosmeticsProduct) {
            return getCosmeticsProduct((CosmeticsProduct) product);
        } else if (product instanceof ElectronicsProduct) {
            return getElectronicsProduct((ElectronicsProduct) product);
        } else if (product instanceof Grocery) {
            return getGroceryProduct((Grocery) product);
        }
        return product; // Default return
    }

    private static Grocery getGroceryProduct(Grocery groceryProduct) {
        try {
            String query = "SELECT * FROM grocery WHERE \"productID\" = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, groceryProduct.getProdID());
            ResultSet rs = pst.executeQuery();

            // Check if the product with the given prodID exists
            if (rs.next()) {
                String expiryDate = rs.getString("expiryDate");
                String nutritionalInfo = rs.getString("nutritionalInfo");

                // Update the existing Grocery object with the retrieved information
                groceryProduct.setExpiryDate(expiryDate);
                groceryProduct.setNutritionalInfo(nutritionalInfo);
                // Return the updated Grocery object
                return groceryProduct;
            } else {
                // Product not found, set expiry date and manufacture date to null
                groceryProduct.setExpiryDate("null");
                groceryProduct.setNutritionalInfo("null");
                // Return the modified Grocery object
                return groceryProduct;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred during retrieval
        } finally {
            closeResources();
        }
    }

    private static ApplianceProduct getApplianceProduct(ApplianceProduct applianceProduct) {
        try {
            String query = "SELECT * FROM appliances WHERE \"productID\" = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, applianceProduct.getProdID());
            ResultSet rs = pst.executeQuery();

            // Check if the product with the given prodID exists
            if (rs.next()) {
                double capacity = rs.getDouble("capacity");
                double efficiencyRate = rs.getDouble("efficiencyRate");

                // Update the existing ApplianceProduct object with the retrieved information
                applianceProduct.setCapacity(capacity);
                applianceProduct.setEfficiencyRate(efficiencyRate);
                // Return the updated ApplianceProduct object
                return applianceProduct;
            } else {
                // Product not found, set capacity and efficiency rate to 0
                applianceProduct.setCapacity(0);
                applianceProduct.setEfficiencyRate(0.0);
                // Return the modified ApplianceProduct object
                return applianceProduct;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred during retrieval
        } finally {
            closeResources();
        }
    }

    private static FreshGrocery getFreshGroceryProduct(FreshGrocery freshGroceryProduct) {
        try {
            String query = "SELECT * FROM \"freshGrocery\" WHERE \"productID\" = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, freshGroceryProduct.getProdID());
            ResultSet rs = pst.executeQuery();

            // Check if the product with the given prodID exists
            if (rs.next()) {
                String expiryDate = rs.getString("expiryDate");
                String nutritionalInfo = rs.getString("nutritionalInfo");
                String weight = rs.getString("weight");
                String organic = rs.getString("organic");
                String type = rs.getString("type");

                // Update the existing FreshGrocery object with the retrieved information
                freshGroceryProduct.setExpiryDate(expiryDate);
                freshGroceryProduct.setNutritionalInfo(nutritionalInfo);
                freshGroceryProduct.setWeight(weight);
                freshGroceryProduct.setOrganic(Boolean.parseBoolean(organic));
                freshGroceryProduct.setType(type);

                // Return the updated FreshGrocery object
                return freshGroceryProduct;
            } else {

                return null;

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred during retrieval
        } finally {
            closeResources();
        }
    }

    private static PackagedProduct getPackedProduct(PackagedProduct packedGroceryProduct) {
        System.out.println("hi");
        try {
            String query = "SELECT * FROM \"packagedGrocery\" WHERE \"productID\" = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, packedGroceryProduct.getProdID());
            ResultSet rs = pst.executeQuery();

            // Check if the product with the given prodID exists
            if (rs.next()) {
                System.out.println("hi true");
                String expiryDate = rs.getString("expiryDate");
                String nutritionalInfo = rs.getString("nutritionalInfo");
                String brand = rs.getString("brand");

                // Update the existing PackedGrocery object with the retrieved information
                packedGroceryProduct.setExpiryDate(expiryDate);
                packedGroceryProduct.setNutritionalInfo(nutritionalInfo);
                packedGroceryProduct.setBrand(brand);
                // Return the updated PackedGrocery object
                return packedGroceryProduct;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred during retrieval
        } finally {
            closeResources();
        }
    }

    private static CosmeticsProduct getCosmeticsProduct(CosmeticsProduct cosmeticsProduct) {
        try {
            String query = "SELECT * FROM cosmetics WHERE \"productID\" = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, cosmeticsProduct.getProdID());
            ResultSet rs = pst.executeQuery();

            // Check if the product with the given prodID exists
            if (rs.next()) {
                String ingredients = rs.getString("ingredients");
                String brand = rs.getString("brand");

                // Update the existing CosmeticsProduct object with the retrieved information
                cosmeticsProduct.setIngredients(ingredients);
                cosmeticsProduct.setBrand(brand);
                // Return the updated CosmeticsProduct object
                return cosmeticsProduct;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred during retrieval
        } finally {
            closeResources();
        }
    }

    private static ElectronicsProduct getElectronicsProduct(ElectronicsProduct electronicsProduct) {
        try {
            String query = "SELECT * FROM electronics WHERE \"productID\" = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, electronicsProduct.getProdID());
            ResultSet rs = pst.executeQuery();

            // Check if the product with the given prodID exists
            if (rs.next()) {
                String warrantyPeriod = rs.getString("warrantyPeriod");
                String model = rs.getString("model"); // Retrieve the "model" column from the database

                // Update the existing ElectronicsProduct object with the retrieved information
                electronicsProduct.setWarrantyPeriod(warrantyPeriod);
                electronicsProduct.setModel(model); // Set the model in the ElectronicsProduct object
                // Return the updated ElectronicsProduct object
                return electronicsProduct;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred during retrieval
        } finally {
            closeResources();
        }
    }

    /******************* METHODS TO GET CATEGORY SPECIFIC PRODUCT ***************/

    private static void closeResources() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}