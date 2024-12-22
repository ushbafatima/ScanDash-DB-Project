package Windows.ManagerInterfaceWindows;

import ProductManagement.*;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ManageInventoryWindow extends JFrame {

    private JPanel contentPane;
    private JPanel leftPanel; // Blue panel on the left
    private JPanel detailsPanel; // Panel for dynamic content
    private Timer slideTimer; // Timer for sliding animation
    private int slideOffset = 0;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        EventQueue.invokeLater(() -> {
            try {
                ManageInventoryWindow frame = new ManageInventoryWindow();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public ManageInventoryWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 200, 500, 400); // Initial window size 700x500
        setTitle("Manage Inventory");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());

        setLocationRelativeTo(null); // Center the window on the screen

        contentPane = new JPanel(null);
        setContentPane(contentPane);


        // Left blue panel
        leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 100)); // Blue background
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        leftPanel.setBounds(0, 0, 150, 500); // Fixed width on the left
        leftPanel.setLayout(null);
        contentPane.add(leftPanel);

        // Add "Inventory" label
        JLabel inventoryLabel = new JLabel("Inventory");
        inventoryLabel.setForeground(Color.WHITE);
        inventoryLabel.setFont(new Font("Arial", Font.BOLD, 18));
        inventoryLabel.setBounds(10, 10, 130, 30); // Positioned at the top-left corner
        leftPanel.add(inventoryLabel);

        // Buttons on the left panel
        JButton addProductBtn = new JButton("Add Product");
        JButton removeProductBtn = new JButton("Remove Product");
        JButton viewInventoryBtn = new JButton("View Inventory");
        JButton backButton = new JButton("Back");

        configureButton(addProductBtn);
        configureButton(removeProductBtn);
        configureButton(viewInventoryBtn);

        // Configure back button to be red
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);

        leftPanel.add(addProductBtn);
        leftPanel.add(removeProductBtn);
        leftPanel.add(viewInventoryBtn);
        leftPanel.add(backButton);

        // Details panel
        detailsPanel = new JPanel();
        detailsPanel.setBackground(new Color(30, 30, 30));
        detailsPanel.setLayout(null);
        detailsPanel.setBounds(700, 0, 550, 500); // Start off-screen (hidden by default)
        detailsPanel.setVisible(false); // Initially hidden
        contentPane.add(detailsPanel);

        // Add action listeners to buttons
        addProductBtn.addActionListener(e -> loadAddProductDetails());
        removeProductBtn.addActionListener(e -> loadRemoveProductDetails());
        viewInventoryBtn.addActionListener(e -> loadViewInventoryDetails());

        backButton.addActionListener(e -> {
            ManagerTasksWindow managerTasksWindow = new ManagerTasksWindow();
            managerTasksWindow.setVisible(true); // Navigate back to Manager Tasks
            dispose();
        });

        // Resize behavior
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizeComponents();
            }
        });

        resizeComponents();
    }

    // Method to switch panels in the CardLayout
    private void switchPanel(String panelName) {
        CardLayout cl = (CardLayout) detailsPanel.getLayout();
        detailsPanel.setVisible(true); // Make the details panel visible
        cl.show(detailsPanel, panelName);
    }


    private void configureButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void resizeComponents() {
        int width = getWidth();
        int height = getHeight();

        leftPanel.setBounds(0, 0, 150, height); // Fixed size on the left

        // Keep the detailsPanel visible and responsive
        if (detailsPanel.isVisible()) {
            detailsPanel.setBounds(slideOffset, 0, width - 150, height);
        }

        // Position buttons in leftPanel
        int buttonWidth = 130;
        int buttonHeight = 40;
        int buttonX = (leftPanel.getWidth() - buttonWidth) / 2;

        int spacing = 20; // Spacing between buttons
        int totalHeight = (buttonHeight * 4) + (spacing * 3);

        // Adjust this line to make buttons higher
        int startY = (height - totalHeight) / 2 - 50; // Subtracting 50 raises the buttons higher

        for (int i = 1; i <= leftPanel.getComponentCount(); i++) {
            if (leftPanel.getComponent(i - 1) instanceof JButton) {
                JButton button = (JButton) leftPanel.getComponent(i - 1);
                button.setBounds(buttonX, startY + ((i - 1) * (buttonHeight + spacing)), buttonWidth, buttonHeight);
            }
        }
    }
    private void loadAddProductDetails() {
        setupDetailsPanel();
        JLabel productNameLabel = new JLabel("Product Name:");
        productNameLabel.setForeground(Color.WHITE);
        productNameLabel.setBounds(30, 30, 100, 25);
        detailsPanel.add(productNameLabel);

        JTextField productNameField = new JTextField();
        productNameField.setBounds(150, 30, 150, 25);
        detailsPanel.add(productNameField);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setForeground(Color.WHITE);
        categoryLabel.setBounds(30, 70, 100, 25);
        detailsPanel.add(categoryLabel);

        JComboBox<String> categoryComboBox = new JComboBox<>(new String[]{"Electronics", "Appliances", "Cosmetics", "Grocery", "Fresh Grocery", "Packaged Grocery"});
        categoryComboBox.setBounds(150, 70, 150, 25);
        detailsPanel.add(categoryComboBox);

        JPanel categoryDetailsPanel = new JPanel();
        categoryDetailsPanel.setBounds(30, 110, detailsPanel.getWidth() - 60, 300);
        categoryDetailsPanel.setBackground(new Color(40, 40, 40));
        categoryDetailsPanel.setLayout(null);
        detailsPanel.add(categoryDetailsPanel);

        categoryComboBox.addActionListener(e -> {
            categoryDetailsPanel.removeAll();
            String selectedCategory = (String) categoryComboBox.getSelectedItem();

            if ("Grocery".equals(selectedCategory)) {
                JLabel expiryDateLabel = new JLabel("Expiry Date (DD-MM-YYYY):");
                expiryDateLabel.setForeground(Color.WHITE);
                expiryDateLabel.setBounds(10, 10, 150, 25);
                categoryDetailsPanel.add(expiryDateLabel);

                JTextField expiryDateDD = new JTextField();
                expiryDateDD.setBounds(10, 40, 30, 25);
                categoryDetailsPanel.add(expiryDateDD);

                JTextField expiryDateMM = new JTextField();
                expiryDateMM.setBounds(50, 40, 30, 25);
                categoryDetailsPanel.add(expiryDateMM);

                JTextField expiryDateYY = new JTextField();
                expiryDateYY.setBounds(90, 40, 50, 25);
                categoryDetailsPanel.add(expiryDateYY);

                JLabel manDate = new JLabel("Manufacture Date (DD-MM-YYYY):");
                manDate.setForeground(Color.WHITE);
                manDate.setBounds(10, 70, 200, 25);
                categoryDetailsPanel.add(manDate);

                JTextField manufactureDateDD = new JTextField();
                manufactureDateDD.setBounds(10, 100, 30, 25);
                categoryDetailsPanel.add(manufactureDateDD);

                JTextField manufactureDateMM = new JTextField();
                manufactureDateMM.setBounds(50, 100, 30, 25);
                categoryDetailsPanel.add(manufactureDateMM);

                JTextField manufactureDateYY = new JTextField();
                manufactureDateYY.setBounds(90, 100, 50, 25);
                categoryDetailsPanel.add(manufactureDateYY);

                JButton addDescriptionbtn = new JButton("Add Product");
                addDescriptionbtn.setBounds(100, 200, 130, 30);
                addDescriptionbtn.setBackground(new Color(0, 180, 0));
                addDescriptionbtn.setForeground(Color.WHITE);
                categoryDetailsPanel.add(addDescriptionbtn);

                addDescriptionbtn.addActionListener(evt -> {
                    if (validateGroceryDescriptions(expiryDateDD, expiryDateMM, expiryDateYY, manufactureDateDD, manufactureDateMM, manufactureDateYY)) {
                        Grocery groceryProduct = new Grocery();
                        setGroceryDescription(groceryProduct, expiryDateDD, expiryDateMM, expiryDateYY, manufactureDateDD, manufactureDateMM, manufactureDateYY);

                        JOptionPane.showMessageDialog(detailsPanel, "Grocery product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        slidePanel(false);
                    }
                });
            }else if ("Fresh Grocery".equals(selectedCategory)) {
                // Fresh Grocery-Specific Fields
                JLabel expiryDateLabel = new JLabel("Expiry Date (DD-MM-YYYY):");
                expiryDateLabel.setForeground(Color.WHITE);
                expiryDateLabel.setBounds(10, 10, 150, 25);
                categoryDetailsPanel.add(expiryDateLabel);

                JTextField expiryDateDD = new JTextField();
                expiryDateDD.setBounds(10, 40, 30, 25);
                categoryDetailsPanel.add(expiryDateDD);

                JTextField expiryDateMM = new JTextField();
                expiryDateMM.setBounds(50, 40, 30, 25);
                categoryDetailsPanel.add(expiryDateMM);

                JTextField expiryDateYY = new JTextField();
                expiryDateYY.setBounds(90, 40, 50, 25);
                categoryDetailsPanel.add(expiryDateYY);

                JLabel manDate = new JLabel("Manufacture Date (DD-MM-YYYY):");
                manDate.setForeground(Color.WHITE);
                manDate.setBounds(10, 70, 200, 25);
                categoryDetailsPanel.add(manDate);

                JTextField manufactureDateDD = new JTextField();
                manufactureDateDD.setBounds(10, 100, 30, 25);
                categoryDetailsPanel.add(manufactureDateDD);

                JTextField manufactureDateMM = new JTextField();
                manufactureDateMM.setBounds(50, 100, 30, 25);
                categoryDetailsPanel.add(manufactureDateMM);

                JTextField manufactureDateYY = new JTextField();
                manufactureDateYY.setBounds(90, 100, 50, 25);
                categoryDetailsPanel.add(manufactureDateYY);

                JLabel weightLbl = new JLabel("Weight:");
                weightLbl.setForeground(Color.WHITE);
                weightLbl.setBounds(10, 140, 100, 25);
                categoryDetailsPanel.add(weightLbl);

                JTextField weightField = new JTextField();
                weightField.setBounds(60, 140, 100, 25);
                categoryDetailsPanel.add(weightField);

                JComboBox<String> weightBox = new JComboBox<>(new String[]{"Kg", "g", "lbs"});
                weightBox.setBounds(170, 140, 60, 25);
                categoryDetailsPanel.add(weightBox);

                JButton addDescriptionbtn = new JButton("Add Product");
                addDescriptionbtn.setBounds(100, 200, 130, 30);
                addDescriptionbtn.setBackground(new Color(0, 180, 0));
                addDescriptionbtn.setForeground(Color.WHITE);
                categoryDetailsPanel.add(addDescriptionbtn);

                addDescriptionbtn.addActionListener(evt -> {
                    if (validateFreshGroceryDescriptions(expiryDateDD, expiryDateMM, expiryDateYY, manufactureDateDD, manufactureDateMM, manufactureDateYY, weightField)) {
                        FreshGrocery freshGroceryProduct = new FreshGrocery();
                        setFreshGroceryDescription(freshGroceryProduct, expiryDateDD, expiryDateMM, expiryDateYY, manufactureDateDD, manufactureDateMM, manufactureDateYY, weightField, weightBox);

                        JOptionPane.showMessageDialog(detailsPanel, "Fresh Grocery product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        slidePanel(false);
                    }
                });
            }else if ("Packaged Grocery".equals(selectedCategory)) {
                // Packaged Grocery-Specific Fields
                JLabel expiryDateLabel = new JLabel("Expiry Date (DD-MM-YYYY):");
                expiryDateLabel.setForeground(Color.WHITE);
                expiryDateLabel.setBounds(10, 10, 150, 25);
                categoryDetailsPanel.add(expiryDateLabel);

                JTextField expiryDateDD = new JTextField();
                expiryDateDD.setBounds(10, 40, 30, 25);
                categoryDetailsPanel.add(expiryDateDD);

                JTextField expiryDateMM = new JTextField();
                expiryDateMM.setBounds(50, 40, 30, 25);
                categoryDetailsPanel.add(expiryDateMM);

                JTextField expiryDateYY = new JTextField();
                expiryDateYY.setBounds(90, 40, 50, 25);
                categoryDetailsPanel.add(expiryDateYY);

                JLabel manDate = new JLabel("Manufacture Date (DD-MM-YYYY):");
                manDate.setForeground(Color.WHITE);
                manDate.setBounds(10, 70, 200, 25);
                categoryDetailsPanel.add(manDate);

                JTextField manufactureDateDD = new JTextField();
                manufactureDateDD.setBounds(10, 100, 30, 25);
                categoryDetailsPanel.add(manufactureDateDD);

                JTextField manufactureDateMM = new JTextField();
                manufactureDateMM.setBounds(50, 100, 30, 25);
                categoryDetailsPanel.add(manufactureDateMM);

                JTextField manufactureDateYY = new JTextField();
                manufactureDateYY.setBounds(90, 100, 50, 25);
                categoryDetailsPanel.add(manufactureDateYY);

                JLabel brandLbl = new JLabel("Brand:");
                brandLbl.setForeground(Color.WHITE);
                brandLbl.setBounds(10, 140, 100, 25);
                categoryDetailsPanel.add(brandLbl);

                JTextField brandField = new JTextField();
                brandField.setBounds(50, 140, 150, 25);
                categoryDetailsPanel.add(brandField);

                JButton addDescriptionbtn = new JButton("Add Product");
                addDescriptionbtn.setBounds(100, 200, 130, 30);
                addDescriptionbtn.setBackground(new Color(0, 180, 0));
                addDescriptionbtn.setForeground(Color.WHITE);
                categoryDetailsPanel.add(addDescriptionbtn);

                addDescriptionbtn.addActionListener(evt -> {
                    if (validatePackagedGroceryDescriptions(expiryDateDD, expiryDateMM, expiryDateYY, manufactureDateDD, manufactureDateMM, manufactureDateYY, brandField)) {
                        PackagedProduct packagedProduct = new PackagedProduct();
                        setPackagedGroceryDescription(packagedProduct, expiryDateDD, expiryDateMM, expiryDateYY, manufactureDateDD, manufactureDateMM, manufactureDateYY, brandField);

                        JOptionPane.showMessageDialog(detailsPanel, "Packaged Grocery product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        slidePanel(false);
                    }
                });
            } else if ("Appliances".equals(selectedCategory)) {
                // Add Appliances-specific fields
                JLabel capacityLabel = new JLabel("Capacity (L):");
                capacityLabel.setForeground(Color.WHITE);
                capacityLabel.setBounds(10, 10, 100, 25);
                categoryDetailsPanel.add(capacityLabel);

                JTextField capacityField = new JTextField();
                capacityField.setBounds(120, 10, 150, 25);
                categoryDetailsPanel.add(capacityField);

                JLabel efficiencyLabel = new JLabel("Efficiency Rate (%):");
                efficiencyLabel.setForeground(Color.WHITE);
                efficiencyLabel.setBounds(10, 50, 150, 25);
                categoryDetailsPanel.add(efficiencyLabel);

                JTextField efficiencyField = new JTextField();
                efficiencyField.setBounds(170, 50, 100, 25);
                categoryDetailsPanel.add(efficiencyField);

                JButton addDescriptionbtn = new JButton("Add Product");
                addDescriptionbtn.setBounds(100, 200, 130, 30);
                addDescriptionbtn.setBackground(new Color(0, 180, 0));
                addDescriptionbtn.setForeground(Color.WHITE);
                categoryDetailsPanel.add(addDescriptionbtn);

                addDescriptionbtn.addActionListener(evt -> {
                    if (validateApplianceDescriptions(capacityField, efficiencyField)) {
                        ApplianceProduct applianceProduct = new ApplianceProduct();
                        setApplianceDescription(applianceProduct, capacityField, efficiencyField);

                        JOptionPane.showMessageDialog(detailsPanel, "Appliance product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        slidePanel(false);
                    }
                });
            }else if ("Cosmetics".equals(selectedCategory)) {
                // Add Cosmetics-specific fields
                JLabel brandLabel = new JLabel("Brand:");
                brandLabel.setForeground(Color.WHITE);
                brandLabel.setBounds(10, 10, 100, 25);
                categoryDetailsPanel.add(brandLabel);

                JTextField brandField = new JTextField();
                brandField.setBounds(120, 10, 150, 25);
                categoryDetailsPanel.add(brandField);

                JLabel ingredientsLabel = new JLabel("Ingredients:");
                ingredientsLabel.setForeground(Color.WHITE);
                ingredientsLabel.setBounds(10, 50, 100, 25);
                categoryDetailsPanel.add(ingredientsLabel);

                JTextField ingredientsField = new JTextField();
                ingredientsField.setBounds(120, 50, 150, 25);
                categoryDetailsPanel.add(ingredientsField);

                JButton addDescriptionbtn = new JButton("Add Product");
                addDescriptionbtn.setBounds(30, 200, 130, 30);
                addDescriptionbtn.setBackground(new Color(0, 180, 0));
                addDescriptionbtn.setForeground(Color.WHITE);
                categoryDetailsPanel.add(addDescriptionbtn);

                addDescriptionbtn.addActionListener(evt -> {
                    if (validateCosmeticsDescriptions(brandField, ingredientsField)) {
                        CosmeticsProduct cosmeticsProduct = new CosmeticsProduct();
                        setCosmeticsDescription(cosmeticsProduct, brandField, ingredientsField);

                        JOptionPane.showMessageDialog(detailsPanel, "Cosmetics product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        slidePanel(false);
                    }
                });
            }else if ("Electronics".equals(selectedCategory)) {
                // Add Electronics-specific fields
                JLabel modelLabel = new JLabel("Model:");
                modelLabel.setForeground(Color.WHITE);
                modelLabel.setBounds(10, 10, 100, 25);
                categoryDetailsPanel.add(modelLabel);

                JTextField modelField = new JTextField();
                modelField.setBounds(120, 10, 150, 25);
                categoryDetailsPanel.add(modelField);

                JLabel warrantyLabel = new JLabel("Warranty Period:");
                warrantyLabel.setForeground(Color.WHITE);
                warrantyLabel.setBounds(10, 50, 100, 25);
                categoryDetailsPanel.add(warrantyLabel);

                JTextField warrantyField = new JTextField();
                warrantyField.setBounds(120, 50, 150, 25);
                categoryDetailsPanel.add(warrantyField);

                JComboBox<String> periodBox = new JComboBox<>(new String[]{"Months", "Years"});
                periodBox.setBounds(120, 90, 80, 25);
                categoryDetailsPanel.add(periodBox);

                JButton addDescriptionbtn = new JButton("Add Product");
                addDescriptionbtn.setBounds(100, 200, 130, 30);
                addDescriptionbtn.setBackground(new Color(0, 180, 0));
                addDescriptionbtn.setForeground(Color.WHITE);
                categoryDetailsPanel.add(addDescriptionbtn);

                addDescriptionbtn.addActionListener(evt -> {
                    if (validateElectronicsDescriptions(modelField, warrantyField)) {
                        ElectronicsProduct electronicsProduct = new ElectronicsProduct();
                        setElectronicsDescription(electronicsProduct, modelField, warrantyField, periodBox);

                        JOptionPane.showMessageDialog(detailsPanel, "Electronics product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        slidePanel(false);
                    }
                });
            }

            categoryDetailsPanel.revalidate();
            categoryDetailsPanel.repaint();
        });

        slidePanel(true);
    }
    private void loadRemoveProductDetails() {
        setupDetailsPanel();

        JLabel productIdLabel = new JLabel("Product ID:");
        productIdLabel.setForeground(Color.WHITE);
        productIdLabel.setBounds(30, 50, 100, 25);
        detailsPanel.add(productIdLabel);

        JTextField productIdField = new JTextField();
        productIdField.setBounds(150, 50, 150, 25);
        detailsPanel.add(productIdField);

        JButton removeButton = new JButton("Remove");
        removeButton.setBounds(80, 100, 100, 30);
        removeButton.setBackground(Color.RED);
        removeButton.setForeground(Color.WHITE);
        detailsPanel.add(removeButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(200, 100, 100, 30);
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);
        detailsPanel.add(cancelButton);

        cancelButton.addActionListener(e -> slidePanel(false));

        // Remove Product Action Listener with Error Handling
        removeButton.addActionListener(e -> {
            try {
                String productIdText = productIdField.getText().trim();

                if (productIdText.isEmpty()) {
                    throw new IllegalArgumentException("Product ID field must not be empty.");
                }

                int productId;
                try {
                    productId = Integer.parseInt(productIdText);
                    if (productId < 0) throw new IllegalArgumentException("Product ID must be a positive integer.");
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Invalid input for Product ID. Please enter a valid integer.");
                }

                // Simulate removing the product (replace with real database logic)
                System.out.println("Product removed: ID " + productId);
                JOptionPane.showMessageDialog(detailsPanel, "Product removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                slidePanel(false);

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(detailsPanel, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(detailsPanel, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        slidePanel(true);
    }
    private void loadViewInventoryDetails() {
        setupDetailsPanel();

        JLabel inventoryLabel = new JLabel("Inventory List:");
        inventoryLabel.setForeground(Color.WHITE);
        inventoryLabel.setBounds(30, 30, 200, 25);
        detailsPanel.add(inventoryLabel);

        // Use a JScrollPane for the inventory list
        JTextArea inventoryList = new JTextArea();
        inventoryList.setBackground(new Color(50, 50, 50));
        inventoryList.setForeground(Color.WHITE);
        inventoryList.setEditable(false);
        inventoryList.setText("Sample Inventory Data...");

        JScrollPane inventoryScrollPane = new JScrollPane(inventoryList);
        int listHeight = detailsPanel.getHeight() - 150; // Leave more space at the bottom
        inventoryScrollPane.setBounds(30, 70, detailsPanel.getWidth() - 60, listHeight - 70);
        inventoryScrollPane.setBackground(new Color(50, 50, 50));
        detailsPanel.add(inventoryScrollPane);

        // Position the Close button higher
        JButton closeButton = new JButton("Close");
        closeButton.setBounds(150, listHeight + 20, 100, 30); // Place it just below the list
        closeButton.setBackground(Color.RED);
        closeButton.setForeground(Color.WHITE);
        detailsPanel.add(closeButton);

        closeButton.addActionListener(e -> slidePanel(false));

        // Add resize listener to adjust the inventory area dynamically
        detailsPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                int panelWidth = detailsPanel.getWidth();
                int panelHeight = detailsPanel.getHeight();
                int adjustedListHeight = panelHeight - 150;

                // Update bounds for the inventory list and Close button
                inventoryScrollPane.setBounds(30, 70, panelWidth - 60, adjustedListHeight - 70);
                closeButton.setBounds((panelWidth - 100) / 2, adjustedListHeight + 20, 100, 30);
            }
        });

        slidePanel(true);
    }

    private void setupDetailsPanel() {
        detailsPanel.removeAll();
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }

    private void slidePanel(boolean slideIn) {
        int start = slideIn ? getWidth() : 150;
        int end = slideIn ? 150 : getWidth();
        slideOffset = start;

        slideTimer = new Timer(10, e -> {
            if ((slideIn && slideOffset > end) || (!slideIn && slideOffset < end)) {
                detailsPanel.setBounds(slideOffset, 0, getWidth() - 150, getHeight());
                slideOffset += slideIn ? -15 : 15;
                detailsPanel.repaint();
            } else {
                slideTimer.stop();
                if (!slideIn) detailsPanel.setVisible(false);
            }
        });
        detailsPanel.setVisible(true);
        slideTimer.start();
    }
    private LocalDate parseDate(String dateString) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(dateString, formatter);
    }

    private Boolean validateApplianceDescriptions(JTextField capacityField, JTextField efficiencyField) {
        try {
            double capacity = Double.parseDouble(capacityField.getText());
            if (capacity < 0) {
                JOptionPane.showMessageDialog(null, "Capacity must be a positive number", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid capacity value", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            double efficiencyRate = Double.parseDouble(efficiencyField.getText());
            if (efficiencyRate < 0 || efficiencyRate > 100) {
                JOptionPane.showMessageDialog(null, "Efficiency rate must be a number between 0 and 100", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid efficiency rate value", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void setApplianceDescription(ApplianceProduct applianceProduct, JTextField capacityField, JTextField efficiencyField) {
        double capacity = Double.parseDouble(capacityField.getText());
        double efficiencyRate = Double.parseDouble(efficiencyField.getText());

        applianceProduct.setCapacity(capacity);
        applianceProduct.setEfficiencyRate(efficiencyRate);
    }
    private Boolean validateCosmeticsDescriptions(JTextField brandField, JTextField ingredientsField) {
        String brand = brandField.getText();
        String ingredients = ingredientsField.getText();

        // Validate brand
        if (brand == null || brand.trim().isEmpty() || brand.matches("^\\d+$") || !brand.matches(".*\\D.*")) {
            JOptionPane.showMessageDialog(null, "Enter a valid brand name", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        // Validate ingredients
        if (ingredients == null || ingredients.trim().isEmpty() || ingredients.matches("^\\d+$") || !ingredients.matches(".*\\D.*")) {
            JOptionPane.showMessageDialog(null, "Enter valid ingredients", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }

    private void setCosmeticsDescription(CosmeticsProduct product, JTextField brandField, JTextField ingredientsField) {
        String brand = brandField.getText();
        String ingredients = ingredientsField.getText();

        product.setBrand(brand);
        product.setIngredients(ingredients);
    }
    private Boolean validateElectronicsDescriptions(JTextField modelField, JTextField warrantyField) {
        String model = modelField.getText();
        String warranty = warrantyField.getText();

        // Validate model
        if (model == null || model.trim().isEmpty() || model.matches("^\\d+$") || !model.matches(".*\\D.*")) {
            JOptionPane.showMessageDialog(null, "Enter a valid model name", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        // Validate warranty
        if (warranty == null || !warranty.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Enter a valid warranty period (digits only)", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }
    private void setElectronicsDescription(ElectronicsProduct product, JTextField modelField, JTextField warrantyField, JComboBox<String> periodBox) {
        String model = modelField.getText();
        String warrantyPeriod = warrantyField.getText() + " " + (String) periodBox.getSelectedItem();

        product.setModel(model);
        product.setWarrantyPeriod(warrantyPeriod);
    }
    private Boolean validateGroceryDescriptions(JTextField expiryDateDD, JTextField expiryDateMM, JTextField expiryDateYY,
                                                JTextField manufactureDateDD, JTextField manufactureDateMM, JTextField manufactureDateYY) {
        LocalDate expiryDate = parseDate(expiryDateDD.getText(), expiryDateMM.getText(), expiryDateYY.getText());
        LocalDate manufactureDate = parseDate(manufactureDateDD.getText(), manufactureDateMM.getText(), manufactureDateYY.getText());

        if (expiryDate == null || manufactureDate == null) {
            JOptionPane.showMessageDialog(null, "Please enter valid date formats", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (!isValidManufactureDate(manufactureDate)) {
            JOptionPane.showMessageDialog(null, "Please enter a correct manufacture date", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (expiryDate.isEqual(manufactureDate)) {
            JOptionPane.showMessageDialog(null, "Expiry date cannot be the same as manufacture date", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (expiryDate.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(null, "Expiry date cannot be before the current date", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (expiryDate.isBefore(manufactureDate)) {
            JOptionPane.showMessageDialog(null, "Expiry date cannot be before manufacture date", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }
    private void setGroceryDescription(Grocery product, JTextField expiryDateDD, JTextField expiryDateMM, JTextField expiryDateYY,
                                       JTextField manufactureDateDD, JTextField manufactureDateMM, JTextField manufactureDateYY) {
        LocalDate expiryDate = parseDate(expiryDateDD.getText(), expiryDateMM.getText(), expiryDateYY.getText());
        LocalDate manufactureDate = parseDate(manufactureDateDD.getText(), manufactureDateMM.getText(), manufactureDateYY.getText());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if (expiryDate != null && manufactureDate != null) {
            product.setExpiryDate(expiryDate.format(formatter));
            product.setNutritionalInfo(manufactureDate.format(formatter));
        }
    }

    private LocalDate parseDate(String day, String month, String year) {
        try {
            int dayValue = Integer.parseInt(day);
            int monthValue = Integer.parseInt(month);
            int yearValue = Integer.parseInt(year);

            if (dayValue < 1 || dayValue > 31)
                return null;
            if (monthValue < 1 || monthValue > 12)
                return null;
            if (year.length() != 4)
                return null;
            return LocalDate.of(yearValue, monthValue, dayValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    private boolean isValidManufactureDate(LocalDate manufactureDate) {
        try {
            if (manufactureDate.isAfter(LocalDate.now()))
                return false;
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
    private Boolean validateFreshGroceryDescriptions(JTextField expiryDateDD, JTextField expiryDateMM, JTextField expiryDateYY,
                                                     JTextField manufactureDateDD, JTextField manufactureDateMM, JTextField manufactureDateYY,
                                                     JTextField weightField) {
        LocalDate expiryDate = parseDate(expiryDateDD.getText(), expiryDateMM.getText(), expiryDateYY.getText());
        LocalDate manufactureDate = parseDate(manufactureDateDD.getText(), manufactureDateMM.getText(), manufactureDateYY.getText());
        String weight = weightField.getText();

        if (expiryDate == null || manufactureDate == null) {
            JOptionPane.showMessageDialog(null, "Please enter valid date formats", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (!isValidManufactureDate(manufactureDate)) {
            JOptionPane.showMessageDialog(null, "Please enter a correct manufacture date\nFormat: DD-MM-YYYY", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (expiryDate.isEqual(manufactureDate)) {
            JOptionPane.showMessageDialog(null, "Expiry date cannot be the same as manufacture date", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (expiryDate.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(null, "Expiry date cannot be before the current date", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (expiryDate.isBefore(manufactureDate)) {
            JOptionPane.showMessageDialog(null, "Expiry date cannot be before manufacture date", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (!isValidWeight(weight)) {
            JOptionPane.showMessageDialog(null, "Enter a valid weight (positive number only)", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }

    private void setFreshGroceryDescription(FreshGrocery product, JTextField expiryDateDD, JTextField expiryDateMM, JTextField expiryDateYY,
                                            JTextField manufactureDateDD, JTextField manufactureDateMM, JTextField manufactureDateYY,
                                            JTextField weightField, JComboBox<String> weightBox) {
        LocalDate expiryDate = parseDate(expiryDateDD.getText(), expiryDateMM.getText(), expiryDateYY.getText());
        LocalDate manufactureDate = parseDate(manufactureDateDD.getText(), manufactureDateMM.getText(), manufactureDateYY.getText());
        String weight = weightField.getText();
        String weightUnit = (String) weightBox.getSelectedItem();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if (expiryDate != null && manufactureDate != null) {
            product.setExpiryDate(expiryDate.format(formatter));
            product.setNutritionalInfo(manufactureDate.format(formatter));
        }

        product.setWeight(Double.parseDouble(weight) + " " + weightUnit);
    }

    public static boolean isValidWeight(String weight) {
        if (weight == null || weight.trim().isEmpty()) {
            return false;
        }

        if (!weight.matches("\\d+(\\.\\d+)?")) { // Supports integers and decimals
            return false;
        }

        double weightValue = Double.parseDouble(weight);
        return weightValue > 0; // Weight must be positive
    }
    private Boolean validatePackagedGroceryDescriptions(JTextField expiryDateDD, JTextField expiryDateMM, JTextField expiryDateYY,
                                                        JTextField manufactureDateDD, JTextField manufactureDateMM, JTextField manufactureDateYY,
                                                        JTextField brandField) {
        LocalDate expiryDate = parseDate(expiryDateDD.getText(), expiryDateMM.getText(), expiryDateYY.getText());
        LocalDate manufactureDate = parseDate(manufactureDateDD.getText(), manufactureDateMM.getText(), manufactureDateYY.getText());
        String brand = brandField.getText();

        if (expiryDate == null || manufactureDate == null) {
            JOptionPane.showMessageDialog(null, "Please enter valid date formats", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (!isValidManufactureDate(manufactureDate)) {
            JOptionPane.showMessageDialog(null, "Please enter a correct manufacture date\nFormat: DD-MM-YYYY", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (expiryDate.isEqual(manufactureDate)) {
            JOptionPane.showMessageDialog(null, "Expiry date cannot be the same as manufacture date", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (expiryDate.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(null, "Expiry date cannot be before the current date", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (expiryDate.isBefore(manufactureDate)) {
            JOptionPane.showMessageDialog(null, "Expiry date cannot be before manufacture date", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (!isValidBrand(brand)) {
            JOptionPane.showMessageDialog(null, "Enter a valid brand name", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }
    private void setPackagedGroceryDescription(PackagedProduct product, JTextField expiryDateDD, JTextField expiryDateMM, JTextField expiryDateYY,
                                               JTextField manufactureDateDD, JTextField manufactureDateMM, JTextField manufactureDateYY,
                                               JTextField brandField) {
        LocalDate expiryDate = parseDate(expiryDateDD.getText(), expiryDateMM.getText(), expiryDateYY.getText());
        LocalDate manufactureDate = parseDate(manufactureDateDD.getText(), manufactureDateMM.getText(), manufactureDateYY.getText());
        String brand = brandField.getText();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if (expiryDate != null && manufactureDate != null) {
            product.setExpiryDate(expiryDate.format(formatter));
            product.setNutritionalInfo(manufactureDate.format(formatter));
        }
        product.setBrand(brand);
    }

    public static boolean isValidBrand(String brand) {
        if (brand == null || brand.trim().isEmpty()) {
            return false;
        }

        if (brand.matches("\\d+") || brand.trim().isEmpty()) { // Prevent digits-only or empty brands
            return false;
        }

        return true;
    }


}
