package Windows.ManagerInterfaceWindows;

import ProductManagement.DBConnection;
import CardManagement.CardManagement;
import UserManagement.CustomerManagement;
import UserManagement.Customer;
import Windows.GeneralWindows.FirstWindow;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import static CardManagement.CardManagement.setCardCredentials;
import static UserManagement.CustomerManagement.addCustomerToDB;

public class ManagerTasksWindow extends JFrame {

    private JPanel contentPane;
    private JPanel centralPanel; // Main panel for the buttons
    private JPanel detailsPanel; // Panel for the dynamic content
    private Timer slideTimer; // Timer for sliding animation
    private int slideOffset = 0;
    JComboBox<String> cardNoBox;

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
                ManagerTasksWindow frame = new ManagerTasksWindow();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public ManagerTasksWindow() {

        // Start a thread to connect to the database when the window is launched
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Connect to the database in the background
                CardManagement.conn = DBConnection.connectToDB();  // Connect to DB
                CustomerManagement.conn = DBConnection.connectToDB();
            }
        }).start();


        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent default close operation
        // Add a window listener to handle the window close event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(ManagerTasksWindow.this,
                        "Do you want to quit Scan Dash?", "Confirm Exit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    // Exit the program
                    System.exit(0);
                }
                // If the response is NO_OPTION, do nothing and stay in the current window
            }
        });setBounds(500, 200, 500, 400); // Initial window size 500x400
        setTitle("Manager Tasks");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());

        setLocationRelativeTo(null); // Center the window on the screen

        contentPane = new JPanel(null);
        setContentPane(contentPane);

        // Central panel for buttons
        centralPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 100)); // Dark blue background
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        centralPanel.setBounds(0, 0, 150, 400); // Fixed on the left
        centralPanel.setLayout(null);
        contentPane.add(centralPanel);

        // Add "Manager" label
        JLabel managerLabel = new JLabel("Manager");
        managerLabel.setForeground(Color.WHITE);
        managerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        managerLabel.setBounds(10, 10, 130, 30); // Positioned at the top-left corner
        centralPanel.add(managerLabel);

        // Buttons on the central panel
        JButton addUserBtn = new JButton("Add Customer");
        JButton manageInventoryBtn = new JButton("Manage Inventory");
        JButton removeUserBtn = new JButton("Remove Customer");
        JButton logoutButton = new JButton("Logout");

        configureButton(addUserBtn);
        configureButton(manageInventoryBtn);
        configureButton(removeUserBtn);

        // Configure logout button to be red
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);

        centralPanel.add(addUserBtn);
        centralPanel.add(manageInventoryBtn);
        centralPanel.add(removeUserBtn);
        centralPanel.add(logoutButton);

        // Details panel
        detailsPanel = new JPanel();
        detailsPanel.setBackground(new Color(30, 30, 30));
        detailsPanel.setLayout(null);
        detailsPanel.setBounds(500, 0, 350, 400); // Start off-screen (hidden by default)
        detailsPanel.setVisible(false); // Initially hidden
        contentPane.add(detailsPanel);

        // Add action listener to buttons
        addUserBtn.addActionListener(e -> loadAddCustomerDetails());
        removeUserBtn.addActionListener(e -> loadRemoveCustomerDetails());

        manageInventoryBtn.addActionListener(e -> {
            ManageInventoryWindow manageInventoryWindow = new ManageInventoryWindow();
            manageInventoryWindow.setVisible(true); // Show the Manage Inventory window
            dispose();
        });

        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new FirstWindow().setVisible(true);
            }
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

    private void configureButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void resizeComponents() {
        int width = getWidth();
        int height = getHeight();

        centralPanel.setBounds(0, 0, 150, height); // Fixed size on the left

        // Keep the detailsPanel visible and responsive
        if (detailsPanel.isVisible()) {
            detailsPanel.setBounds(slideOffset, 0, width - 150, height);
        }

        // Position buttons in centralPanel
        int buttonWidth = 130;
        int buttonHeight = 40;
        int buttonX = (centralPanel.getWidth() - buttonWidth) / 2;

        int spacing = 20; // Reduced spacing for better alignment
        int totalHeight = (buttonHeight * 4) + (spacing * 3);
        int startY = (height - totalHeight) / 2 - 50; // Adjusted starting position (higher)

        for (int i = 1; i <= centralPanel.getComponentCount(); i++) {
            if (centralPanel.getComponent(i - 1) instanceof JButton) {
                JButton button = (JButton) centralPanel.getComponent(i - 1);
                button.setBounds(buttonX, startY + ((i - 1) * (buttonHeight + spacing)), buttonWidth, buttonHeight);
            }
        }
    }

    private void loadAddCustomerDetails() {
        setupDetailsPanel();

        // First Name Label and Field
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setForeground(Color.WHITE);
        firstNameLabel.setBounds(30, 30, 100, 25);
        detailsPanel.add(firstNameLabel);

        JTextField firstNameField = new JTextField();
        firstNameField.setBounds(150, 30, 150, 25);
        detailsPanel.add(firstNameField);

        // Last Name Label and Field
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setForeground(Color.WHITE);
        lastNameLabel.setBounds(30, 70, 100, 25);
        detailsPanel.add(lastNameLabel);

        JTextField lastNameField = new JTextField();
        lastNameField.setBounds(150, 70, 150, 25);
        detailsPanel.add(lastNameField);

        // Contact Number Label and Field
        JLabel contactLabel = new JLabel("Contact Number:");
        contactLabel.setForeground(Color.WHITE);
        contactLabel.setBounds(30, 110, 120, 25);
        detailsPanel.add(contactLabel);

        JTextField contactField = new JTextField();
        contactField.setBounds(150, 110, 150, 25);
        detailsPanel.add(contactField);

        // Card Name Label and Dropdown
        JLabel cardNameLabel = new JLabel("Card Number:");
        cardNameLabel.setForeground(Color.WHITE);
        cardNameLabel.setBounds(30, 150, 100, 25);
        detailsPanel.add(cardNameLabel);

        // Create the JComboBox for card selection
        cardNoBox = new JComboBox<>();
        cardNoBox.setBounds(150, 150, 150, 25);

        updateCardNumbersBox();

        detailsPanel.add(cardNoBox);

        // Add Customer Button
        JButton addCustomerBtn = new JButton("Add Customer");
        addCustomerBtn.setBounds(60, 200, 130, 30);
        addCustomerBtn.setBackground(new Color(0, 180, 0));
        addCustomerBtn.setForeground(Color.WHITE);
        detailsPanel.add(addCustomerBtn);

        // Cancel Button
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(200, 200, 100, 30);
        cancelBtn.setBackground(Color.RED);
        cancelBtn.setForeground(Color.WHITE);
        detailsPanel.add(cancelBtn);

        // Cancel Button Action: Slide out the panel
        cancelBtn.addActionListener(e -> slidePanel(false));

        // Add action listener for Add Customer button
        addCustomerBtn.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String contact = contactField.getText().trim().replaceAll("\\s", "");;
            String cardNumber = (String) cardNoBox.getSelectedItem();
            // Validation: Ensure all fields are filled
            if (firstName.isEmpty() || lastName.isEmpty() || contact.isEmpty() || cardNumber == null || cardNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Validation: Ensure first and last name are valid (only letters, no spaces)
            if (!isCorrectName(firstName)||!isCorrectName(lastName)) {
                JOptionPane.showMessageDialog(this, "Name must contain only letters.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validation: Ensure phone number is valid (only numeric characters, 11 digits)
            if (!isValidPhoneNumber(contact)) {
                JOptionPane.showMessageDialog(this, "Phone number must be exactly 11 digits, with no spaces.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Call the findCustomer method to retrieve the customer based on phone
            Customer customer = CustomerManagement.getCustomerFromDB(contact);

            if (customer != null) {
                // Customer exists, display message with their name
                String message = "Customer already exists with name: " + customer.getFirstName() + " " + customer.getLastName();
                JOptionPane.showMessageDialog(this, message, "Customer Found", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // Customer does not exist, set customer details
            customer = new Customer(firstName, lastName, contact, cardNumber);
            // Add customer to the database or perform other actions
            boolean added = addCustomerToDB(customer);
            if (added) {
                JOptionPane.showMessageDialog(this, "Customer successfully added!", "Success", JOptionPane.INFORMATION_MESSAGE);
                firstNameField.setText("");
                lastNameField.setText("");
                contactField.setText("");

            } else {
                JOptionPane.showMessageDialog(this, "Failed to add customer.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Proceed with setting the PIN
            showSetPinPopup(cardNumber);
        });
        // Slide the panel in
        slidePanel(true);
    }


    private void showSetPinPopup(String cardID) {
        JDialog pinDialog = new JDialog(this, "Set PIN", true);
        pinDialog.setSize(300, 200);
        pinDialog.setLayout(null);
        // Keep the title but disable the close button
        pinDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Disable default close operation
        pinDialog.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG); // Removes the "X" button
        JLabel pinLabel = new JLabel("Enter PIN:");
        pinLabel.setBounds(30, 30, 100, 25);
        pinDialog.add(pinLabel);

        JPasswordField pinField = new JPasswordField();
        pinField.setBounds(120, 30, 150, 25);
        pinDialog.add(pinField);

        JLabel confirmPinLabel = new JLabel("Confirm PIN:");
        confirmPinLabel.setBounds(30, 70, 100, 25);
        pinDialog.add(confirmPinLabel);

        JPasswordField confirmPinField = new JPasswordField();
        confirmPinField.setBounds(120, 70, 150, 25);
        pinDialog.add(confirmPinField);

        JButton setPinButton = new JButton("Set PIN");
        setPinButton.setBounds(80, 120, 100, 30);
        setPinButton.setBackground(new Color(0, 180, 0)); // Green background
        setPinButton.setForeground(Color.WHITE);
        pinDialog.add(setPinButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(190, 120, 80, 30);
        cancelButton.setBackground(Color.RED); // Red background
        cancelButton.setForeground(Color.WHITE);
        pinDialog.add(cancelButton);

        // Set PIN button action
        setPinButton.addActionListener(e -> {
            String pin = new String(pinField.getPassword());
            String confirmPin = new String(confirmPinField.getPassword());

            if (!pin.matches("\\d{4}")) {
                JOptionPane.showMessageDialog(pinDialog, "PIN must be exactly 4 numeric digits.", "Invalid PIN", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (!pin.equals(confirmPin)) {
                JOptionPane.showMessageDialog(pinDialog, "PINs do not match. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                confirmPinField.setText("");
                return;
            } else {
                JOptionPane.showMessageDialog(pinDialog, "PIN successfully set!");
                try {
                    setCardCredentials(cardID, confirmPin);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                updateCardNumbersBox();
                pinDialog.dispose();
            }
        });


        pinDialog.setLocationRelativeTo(this);
        pinDialog.setVisible(true);
    }


    private void loadRemoveCustomerDetails() {
        setupDetailsPanel();

        // Contact Number Label and Field
        JLabel contactLabel = new JLabel("Contact Number:");
        contactLabel.setForeground(Color.WHITE);
        contactLabel.setBounds(30, 50, 150, 25);
        detailsPanel.add(contactLabel);

        JTextField contactField = new JTextField();
        contactField.setBounds(150, 50, 150, 25);
        detailsPanel.add(contactField);

        // Remove Button
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            String contact = contactField.getText().trim();

            // Check if the field is empty
            if (contact.isEmpty()) {
                JOptionPane.showMessageDialog(detailsPanel, "Contact field cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if the phone number is valid (digits only)
            if (!isValidPhoneNumber(contact)) {
                JOptionPane.showMessageDialog(detailsPanel, "Contact must contain only digits.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Fetch customer by contact
            Customer customer = CustomerManagement.getCustomerFromDB(contact);

            // Check if customer exists
            if (customer == null) {
                JOptionPane.showMessageDialog(detailsPanel, "Customer does not exist.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Customer found, you can proceed with the removal
            int confirm = JOptionPane.showConfirmDialog(detailsPanel,
                    "Are you sure you want to remove customer:\n "
                            + customer.getFirstName() + " " + customer.getLastName()
                            + "?\nCard Number: " + customer.getCard().getCardID(),
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = CustomerManagement.removeCustomerFromDB(customer);
                if (success) {
                    JOptionPane.showMessageDialog(detailsPanel, "Customer removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    contactField.setText("");
                } else {
                    JOptionPane.showMessageDialog(detailsPanel, "Failed to remove customer.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        removeButton.setBounds(80, 100, 100, 30);
        removeButton.setBackground(Color.RED);
        removeButton.setForeground(Color.WHITE);
        detailsPanel.add(removeButton);
        // Cancel Button
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(200, 100, 100, 30);
        cancelBtn.setBackground(Color.RED);
        cancelBtn.setForeground(Color.WHITE);
        detailsPanel.add(cancelBtn);

        // Cancel Button Action: Slide out the panel
        cancelBtn.addActionListener(e -> slidePanel(false));

        // Slide the panel in
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

    /*********** ADD CUSTOMER UTILITY FUNCTIONS ***************/
    public Boolean isCorrectName(String name) {
        // Regular expression to allow only letters (no spaces or special characters)
        return name.matches("[a-zA-Z]+");
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        // Check if the phone number has exactly 11 digits and contains only numbers
        return phoneNumber.matches("\\d{11}");
    }
    private void updateCardNumbersBox() {
        try {
            cardNoBox.removeAllItems(); // Clear existing items

            // Fetch available cards
            String[] availableCards = CardManagement.getAvailableCards();

            // Add available cards to the combo box
            if (availableCards != null && availableCards.length > 0) {
                for (String card : availableCards) {
                    cardNoBox.addItem(card);
                }
            } else {
                // Show message if no cards are available
                JOptionPane.showMessageDialog(this, "No cards available", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching available cards: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


}





