package app;

import ui.LoginFrame;

import javax.swing.*;

public class GUIStarter {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            LoginFrame loginFrame =
                    new LoginFrame();

            loginFrame.setVisible(true);
        });
    }
}