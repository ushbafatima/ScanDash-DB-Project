package Windows;

import Windows.CustomerScanCardWindow;
import Windows.ManagerLoginWindow;
import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FirstWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        EventQueue.invokeLater(() -> {
            try {
                FirstWindow frame = new FirstWindow();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public FirstWindow() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        FirstWindow.this,
                        "Do you want to quit Scan Dash?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        setBounds(500, 200, 500, 400);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        setLocation(x, y);

        // Frame Settings
        setTitle("Scan Dash");
        setSize(650, 400);
        setMinimumSize(new Dimension(650, 400)); // Set a minimum size to avoid collapsing
        setLocationRelativeTo(null); // Ensures window is centered on start

        // Set Icon
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/Images/Cart Icon.png"));
        setIconImage(icon.getImage());

        // Use GridBagLayout for the entire frame
        setLayout(new GridBagLayout());

        // GridBagConstraints for component positioning
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // GIF Panel (top part)
        JPanel gifPanel = new JPanel(new GridBagLayout());
        JLabel gifLabel = new JLabel("SCANDASH", SwingConstants.CENTER);
        ImageIcon gifIcon = new ImageIcon(this.getClass().getResource("/Images/SCAN2_1.gif"));
        gifLabel.setIcon(gifIcon);
        gifLabel.setPreferredSize(new Dimension(600, 120)); // Adjust the size as needed
        gifPanel.add(gifLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(gifPanel, gbc);

        // Center Panel for Title
        JPanel centerPanel = new JPanel(new GridBagLayout());
        JLabel titleLabel = new JLabel("Smart Cart System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Sans Serif", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        centerPanel.add(titleLabel);
        gbc.gridx = 0;
        gbc.gridy = 1;  // Place this below the gif panel
        gbc.gridwidth = 1;
        add(centerPanel, gbc);

        // Bottom Panel for Buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());

        // "Access Smart Cart as" label
        JLabel instructionLabel = new JLabel("Access Smart Cart as:");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;  // Below the title
        gbc.gridwidth = 2;
        buttonPanel.add(instructionLabel, gbc);

        // Manager Button
        JButton btnManager = new JButton("Manager");
        btnManager.addActionListener(e -> {
            dispose();
            new ManagerLoginWindow().setVisible(true);
        });
        btnManager.setBackground(new Color(0, 0, 100));
        btnManager.setForeground(Color.WHITE);
        btnManager.setFocusPainted(false);
        btnManager.setPreferredSize(new Dimension(120, 30)); // Optional: Set button size
        gbc.gridy = 3;  // Below the "Access Smart Cart as" label
        buttonPanel.add(btnManager, gbc);

        // Customer Button
        JButton btnCustomer = new JButton("Customer");
        btnCustomer.addActionListener(e -> {
            dispose();
            new CustomerScanCardWindow().setVisible(true);
        });
        btnCustomer.setBackground(new Color(0, 0, 100));
        btnCustomer.setForeground(Color.WHITE);
        btnCustomer.setFocusPainted(false);
        btnCustomer.setPreferredSize(new Dimension(120, 30)); // Optional: Set button size
        gbc.gridy = 4;  // Below the Manager button
        buttonPanel.add(btnCustomer, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        add(buttonPanel, gbc);



        setResizable(true); // Allows window resizing
    }
}