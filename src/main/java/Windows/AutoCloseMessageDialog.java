package Windows;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class AutoCloseMessageDialog {

    public static void showMessage(Component parent, String message, String title, int messageType, int timeout) {
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setModal(true);

        // Create a content panel for the dialog
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(new JLabel(message, SwingConstants.CENTER), BorderLayout.CENTER);
        dialog.getContentPane().add(contentPanel);

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(parent);

        // Use a Timer to close the dialog after the specified timeout
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dispose();
            }
        }, timeout);

        dialog.setVisible(true);
    }

}
