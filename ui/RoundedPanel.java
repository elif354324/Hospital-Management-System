package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Modern panel with rounded corners.
 */
public class RoundedPanel extends JPanel {

    private final int cornerRadius;
    private Color backgroundColor;
    private boolean shadowEnabled;

    public RoundedPanel(
            int cornerRadius,
            Color backgroundColor
    ) {

        this.cornerRadius = cornerRadius;
        this.backgroundColor = backgroundColor;
        this.shadowEnabled = false;

        setOpaque(false);
    }

    public void setShadowEnabled(boolean shadowEnabled) {

        this.shadowEnabled = shadowEnabled;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int width = getWidth();
        int height = getHeight();

        // =========================
        // SHADOW
        // =========================

        if (shadowEnabled) {

            g2.setColor(
                    new Color(0, 0, 0, 25)
            );

            for (int i = 6; i >= 1; i--) {

                g2.fillRoundRect(
                        i,
                        i,
                        width - (i * 2),
                        height - (i * 2),
                        cornerRadius,
                        cornerRadius
                );
            }
        }

        // =========================
        // BACKGROUND
        // =========================

        g2.setColor(backgroundColor);

        g2.fillRoundRect(
                0,
                0,
                width,
                height,
                cornerRadius,
                cornerRadius
        );

        g2.dispose();

        super.paintComponent(g);
    }
}