package Windows.CustomerInterfaceWindows;

import UserManagement.CardManagement;
import UserManagement.CustomerAuthentication;
import UserManagement.Card;
import Windows.GeneralWindows.FirstWindow;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class CustomerScanCardWindow extends JFrame {
    private JPanel contentPane;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        EventQueue.invokeLater(() -> {
            try {
                CustomerScanCardWindow frame = new CustomerScanCardWindow();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CustomerScanCardWindow() {
        setUndecorated(true);
        setBounds(500, 200, 500, 400);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        setLocation(x, y);

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(Color.BLACK);
        setContentPane(contentPane);

        setTitle("Scan Card");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 0, 100));
        topPanel.setPreferredSize(new Dimension(500, 60));
        topPanel.setLayout(null);

        JLabel scanningCardLabel = new JLabel("SCANNING CARD");
        scanningCardLabel.setForeground(Color.WHITE);
        scanningCardLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        scanningCardLabel.setBounds(150, 10, 200, 40);
        topPanel.add(scanningCardLabel);

        // Red close button
        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.RED);
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setBounds(460, 10, 30, 30); // Adjust size and position
        closeButton.addActionListener(e -> {
            dispose(); // Close the current window
            SwingUtilities.invokeLater(() -> {
                dispose();
                FirstWindow firstWindow = new FirstWindow(); // Replace with the constructor of your FirstWindow class
                firstWindow.setVisible(true); // Make the first window visible
            });
        });topPanel.add(closeButton);

        contentPane.add(topPanel, BorderLayout.NORTH);

        JPanel gifPanel = new JPanel();
        gifPanel.setLayout(new BorderLayout());
        JLabel gifLabel = new JLabel("");
        gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon gifIcon = new ImageIcon(this.getClass().getResource("/Images/scanCard.gif"));
        gifLabel.setIcon(gifIcon);
        gifPanel.add(gifLabel, BorderLayout.CENTER);
        contentPane.add(gifPanel, BorderLayout.CENTER);


        // Use a separate thread for card scanning logic
        new Thread(this::processCard).start();
    }

    private void processCard() {
        String cardID=null;
        while(true) {
            cardID = CustomerAuthentication.scanCard(); // Fetch card ID
            if (cardID != null) {
                break;
            }
        }

        if (!CustomerAuthentication.isValidCardID(cardID)) {
            showErrorAndRestart("Invalid CardID. Please try again.");
            return;
        }

        try {
            Card card = CardManagement.getCardFromDB(cardID); // Fetch card from database

            if (!CustomerAuthentication.cardFound(card)) {
                dispose();
                showErrorAndRestart("Card Not Found. Restarting scanning...");
            } else if (!CustomerAuthentication.isCardActive(card)) {
                dispose();
                showErrorAndRestart("Card is not active. Restarting scanning...");
            } else {
                // Authentication successful
                String finalCardID = cardID;
                SwingUtilities.invokeLater(() -> {
                    dispose();
                    CustomerCardPinWindow newWindow = new CustomerCardPinWindow(finalCardID);
                    newWindow.setVisible(true);
                    dispose();
                    return;
                });
            }
        } catch (SQLException e) {
            showErrorAndRestart("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showErrorAndRestart(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
            restartScanningWindow();
        });
    }

    private void restartScanningWindow() {
        dispose(); // Close the current window
        SwingUtilities.invokeLater(() -> {
            CustomerScanCardWindow newWindow = new CustomerScanCardWindow();
            newWindow.setVisible(true); // Open a new instance of the scanning window
        });
    }
}
