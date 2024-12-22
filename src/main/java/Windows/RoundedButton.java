package Windows;

import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.font.FontRenderContext;
import java.awt.geom.RoundRectangle2D;


public class RoundedButton extends JButton {
    private static final long serialVersionUID = 1L;

    public RoundedButton(String label) {
        super(label);
        setPreferredSize(new Dimension(80, 30)); // Adjust size for the text
        setContentAreaFilled(false);
        setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(getBackground().darker());
        } else {
            g.setColor(getBackground());
        }
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10); // Rounded edges
        g.setColor(getForeground());
        g.setFont(getFont());
        g.drawString(getText(), getWidth() / 2 - g.getFontMetrics().stringWidth(getText()) / 2,
                getHeight() / 2 + g.getFontMetrics().getAscent() / 2 - 2); // Center the text
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10); // Rounded edges
    }

    @Override
    public boolean contains(int x, int y) {
        int width = getWidth();
        int height = getHeight();
        return new RoundRectangle2D.Double(0, 0, width, height, 10, 10).contains(x, y);
    }
}