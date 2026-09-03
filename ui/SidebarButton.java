package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarButton extends JButton {

    private boolean selected = false;


    public SidebarButton(String text) {

        super(text);


        setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        setForeground(UITheme.TEXT_LIGHT);

        setBackground(UITheme.SIDEBAR);

        setHorizontalAlignment(SwingConstants.LEFT);

        setFocusPainted(false);

        setBorderPainted(false);

        setContentAreaFilled(false);

        setOpaque(true);


        setBorder(
                BorderFactory.createEmptyBorder(
                        14,
                        24,
                        14,
                        10
                )
        );


        addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        if (!selected) {

                            setBackground(
                                    UITheme.SIDEBAR_HOVER
                            );
                        }
                    }


                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        if (!selected) {

                            setBackground(
                                    UITheme.SIDEBAR
                            );
                        }
                    }
                }
        );
    }


    public void setSelected(boolean selected) {

        this.selected = selected;


        if (selected) {

            setBackground(
                    UITheme.PRIMARY
            );

            setForeground(
                    Color.WHITE
            );
        }

        else {

            setBackground(
                    UITheme.SIDEBAR
            );

            setForeground(
                    UITheme.TEXT_LIGHT
            );
        }
    }
}