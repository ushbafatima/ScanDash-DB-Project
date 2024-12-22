package Windows.GeneralWindows;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.Serial;

import javax.swing.SwingConstants;
import javax.swing.JProgressBar;

import com.formdev.flatlaf.*;
import javax.swing.UIManager;

public class StartUpWindow extends javax.swing.JFrame {
    @Serial
    private static final long serialVersionUID = 1L;
    private static JProgressBar progressBar;

    public StartUpWindow() {
        setUndecorated(true);
        setBounds(500, 200, 500, 400);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        setLocation(x, y);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel titlelbl = new JLabel("");
        titlelbl.setHorizontalAlignment(SwingConstants.CENTER);
        titlelbl.setBackground(Color.GREEN);
        ImageIcon icon1 = new ImageIcon(this.getClass().getResource("/Images/SCAN1_1.gif"));
        titlelbl.setIcon(icon1);
        titlelbl.setBounds(-50, -20, 600, 300);
        contentPane.add(titlelbl);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(102, 153, 204));
        progressBar.setBounds(52, 250, 397, 25);
        contentPane.add(progressBar);


    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        StartUpWindow frame = new StartUpWindow();
        frame.setVisible(true);

        try {
            for (int x = 0; x <= 100; x++) {
                StartUpWindow.progressBar.setValue(x);
                Thread.sleep(30);
                if (x == 100) {
                    Thread.sleep(1000);
                    frame.setVisible(false);
                    new FirstWindow().setVisible(true);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
