package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern rounded button with hover effect.
 */
public class ModernButton extends JButton {

    private Color normalColor;
    private Color hoverColor;

    private boolean hovered = false;

    public ModernButton(
            String text,
            Color normalColor,
            Color hoverColor
    ) {

        super(text);

        this.normalColor = normalColor;
        this.hoverColor = hoverColor;

        setForeground(Color.WHITE);

        setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);

        setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        setOpaque(false);

        addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(MouseEvent e) {

                        hovered = true;

                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                        hovered = false;

                        repaint();
                    }
                }
        );
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 =
                (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        Color color =
                hovered
                        ? hoverColor
                        : normalColor;

        g2.setColor(color);

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                14,
                14
        );

        g2.dispose();

        super.paintComponent(g);
    }
}