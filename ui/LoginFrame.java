package ui;

import models.User;
import services.AuthenticationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Modern login screen for Hospital Management System.
 */
public class LoginFrame extends JFrame {

    // =========================
    // COLORS
    // =========================

    private final Color PRIMARY_BLUE =
            new Color(37, 99, 235);

    private final Color DARK_BLUE =
            new Color(15, 23, 42);

    private final Color LIGHT_BLUE =
            new Color(59, 130, 246);

    private final Color BACKGROUND =
            new Color(248, 250, 252);

    private final Color TEXT_DARK =
            new Color(30, 41, 59);

    private final Color TEXT_LIGHT =
            new Color(148, 163, 184);

    private final Color ERROR_RED =
            new Color(220, 38, 38);


    // =========================
    // SERVICES
    // =========================

    private final AuthenticationService authService;


    // =========================
    // COMPONENTS
    // =========================

    private JTextField usernameField;

    private JPasswordField passwordField;

    private JLabel errorLabel;

    private JButton showPasswordButton;

    private boolean passwordVisible = false;


    // =========================
    // CONSTRUCTOR
    // =========================

    public LoginFrame() {

        authService = new AuthenticationService();

        initializeFrame();

        createUI();
    }


    // =========================
    // FRAME SETTINGS
    // =========================

    private void initializeFrame() {

        setTitle(
                "Hospital Management System - Login"
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setSize(1100, 700);

        setMinimumSize(
                new Dimension(900, 600)
        );

        setLocationRelativeTo(null);

        setResizable(true);
    }


    // =========================
    // MAIN UI
    // =========================

    private void createUI() {

        JPanel mainPanel =
                new JPanel(
                        new GridLayout(1, 2)
                );

        mainPanel.setBackground(BACKGROUND);

        mainPanel.add(
                createLeftPanel()
        );

        mainPanel.add(
                createRightPanel()
        );

        setContentPane(mainPanel);
    }


    // =========================================================
    // LEFT BRANDING PANEL
    // =========================================================

    private JPanel createLeftPanel() {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBackground(DARK_BLUE);

        panel.setBorder(
                new EmptyBorder(
                        70,
                        60,
                        70,
                        60
                )
        );


        // Spacer

        panel.add(
                Box.createVerticalGlue()
        );


        // =========================
        // HOSPITAL ICON
        // =========================

        JLabel iconLabel =
                new JLabel("✚");

        iconLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        72
                )
        );

        iconLabel.setForeground(
                new Color(96, 165, 250)
        );

        iconLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(iconLabel);

        panel.add(
                Box.createVerticalStrut(20)
        );


        // =========================
        // TITLE
        // =========================

        JLabel title =
                new JLabel(
                        "<html>HOSPITAL<br>MANAGEMENT<br>SYSTEM</html>"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        38
                )
        );

        title.setForeground(Color.WHITE);

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(title);

        panel.add(
                Box.createVerticalStrut(25)
        );


        // =========================
        // SUBTITLE
        // =========================

        JLabel subtitle =
                new JLabel(
                        "<html>Smart healthcare management<br>"
                                + "for a modern hospital.</html>"
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        16
                )
        );

        subtitle.setForeground(
                new Color(203, 213, 225)
        );

        subtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(subtitle);


        panel.add(
                Box.createVerticalGlue()
        );


        // =========================
        // SECURITY INFO
        // =========================

        JLabel security =
                new JLabel(
                        "🔒 Secure Hospital Information System"
                );

        security.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        security.setForeground(
                new Color(148, 163, 184)
        );

        security.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(security);


        panel.add(
                Box.createVerticalStrut(15)
        );


        // =========================
        // SYSTEM STATUS
        // =========================

        JLabel status =
                new JLabel(
                        "●  SYSTEM ONLINE"
                );

        status.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        status.setForeground(
                new Color(74, 222, 128)
        );

        status.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(status);


        return panel;
    }


    // =========================================================
    // RIGHT LOGIN PANEL
    // =========================================================

    private JPanel createRightPanel() {

        JPanel wrapper =
                new JPanel(
                        new GridBagLayout()
                );

        wrapper.setBackground(BACKGROUND);


        RoundedPanel loginCard =
                new RoundedPanel(
                        25,
                        Color.WHITE
                );

        loginCard.setShadowEnabled(true);

        loginCard.setLayout(
                new BoxLayout(
                        loginCard,
                        BoxLayout.Y_AXIS
                )
        );

        loginCard.setPreferredSize(
                new Dimension(420, 500)
        );

        loginCard.setBorder(
                new EmptyBorder(
                        45,
                        45,
                        45,
                        45
                )
        );


        // =========================
        // WELCOME
        // =========================

        JLabel welcome =
                new JLabel(
                        "Welcome Back"
                );

        welcome.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        welcome.setForeground(TEXT_DARK);

        welcome.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        loginCard.add(welcome);


        loginCard.add(
                Box.createVerticalStrut(10)
        );


        JLabel description =
                new JLabel(
                        "Sign in to access your workspace"
                );

        description.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        description.setForeground(TEXT_LIGHT);

        description.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        loginCard.add(description);


        loginCard.add(
                Box.createVerticalStrut(35)
        );


        // =========================
        // USERNAME
        // =========================

        JLabel usernameLabel =
                createFieldLabel(
                        "USERNAME"
                );

        loginCard.add(usernameLabel);

        loginCard.add(
                Box.createVerticalStrut(8)
        );


        usernameField =
                createModernTextField();

        usernameField.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        48
                )
        );

        loginCard.add(usernameField);


        loginCard.add(
                Box.createVerticalStrut(22)
        );


        // =========================
        // PASSWORD
        // =========================

        JLabel passwordLabel =
                createFieldLabel(
                        "PASSWORD"
                );

        loginCard.add(passwordLabel);

        loginCard.add(
                Box.createVerticalStrut(8)
        );


        JPanel passwordPanel =
                createPasswordFieldPanel();

        passwordPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        48
                )
        );

        loginCard.add(passwordPanel);


        loginCard.add(
                Box.createVerticalStrut(12)
        );


        // =========================
        // ERROR MESSAGE
        // =========================

        errorLabel =
                new JLabel(" ");

        errorLabel.setForeground(ERROR_RED);

        errorLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        errorLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        loginCard.add(errorLabel);


        loginCard.add(
                Box.createVerticalStrut(15)
        );


        // =========================
        // LOGIN BUTTON
        // =========================

        ModernButton loginButton =
                new ModernButton(
                        "SIGN IN",
                        PRIMARY_BLUE,
                        LIGHT_BLUE
                );

        loginButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        52
                )
        );

        loginButton.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        loginButton.addActionListener(
                this::handleLogin
        );

        loginCard.add(loginButton);


        // ENTER KEY LOGIN

        passwordField.addActionListener(
                this::handleLogin
        );


        wrapper.add(loginCard);

        return wrapper;
    }


    // =========================================================
    // FIELD COMPONENTS
    // =========================================================

    private JLabel createFieldLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        label.setForeground(TEXT_LIGHT);

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return label;
    }


    private JTextField createModernTextField() {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        15
                )
        );

        field.setForeground(TEXT_DARK);

        field.setBackground(
                new Color(248, 250, 252)
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                new Color(226, 232, 240),
                                1,
                                true
                        ),

                        new EmptyBorder(
                                0,
                                14,
                                0,
                                14
                        )
                )
        );

        return field;
    }


    private JPanel createPasswordFieldPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                new Color(248, 250, 252)
        );

        panel.setBorder(
                BorderFactory.createLineBorder(
                        new Color(226, 232, 240),
                        1,
                        true
                )
        );


        passwordField =
                new JPasswordField();

        passwordField.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        15
                )
        );

        passwordField.setForeground(TEXT_DARK);

        passwordField.setBackground(
                new Color(248, 250, 252)
        );

        passwordField.setBorder(
                new EmptyBorder(
                        0,
                        14,
                        0,
                        5
                )
        );


        showPasswordButton =
                new JButton("SHOW");

        showPasswordButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        showPasswordButton.setForeground(PRIMARY_BLUE);

        showPasswordButton.setFocusPainted(false);

        showPasswordButton.setBorderPainted(false);

        showPasswordButton.setContentAreaFilled(false);

        showPasswordButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        showPasswordButton.addActionListener(
                e -> togglePassword()
        );


        panel.add(
                passwordField,
                BorderLayout.CENTER
        );

        panel.add(
                showPasswordButton,
                BorderLayout.EAST
        );

        return panel;
    }


    // =========================================================
    // PASSWORD VISIBILITY
    // =========================================================

    private void togglePassword() {

        passwordVisible =
                !passwordVisible;


        if (passwordVisible) {

            passwordField.setEchoChar(
                    (char) 0
            );

            showPasswordButton.setText(
                    "HIDE"
            );

        } else {

            passwordField.setEchoChar(
                    '•'
            );

            showPasswordButton.setText(
                    "SHOW"
            );
        }
    }


    // =========================================================
    // LOGIN LOGIC
    // =========================================================

    private void handleLogin(
            ActionEvent event
    ) {

        String username =
                usernameField
                        .getText()
                        .trim();

        String password =
                new String(
                        passwordField.getPassword()
                );


        // =========================
        // EMPTY VALIDATION
        // =========================

        if (username.isEmpty()
                || password.isEmpty()) {

            showError(
                    "Please enter your username and password."
            );

            return;
        }


        // =========================
        // AUTHENTICATION
        // =========================

        boolean success =
                authService.login(
                        username,
                        password
                );


        if (!success) {

            showError(
                    "Invalid username or password."
            );

            passwordField.setText("");

            return;
        }


        // =========================
        // SUCCESS
        // =========================

        User user =
                authService.getCurrentUser();

        JOptionPane.showMessageDialog(
                this,
                "Welcome, "
                        + user.getUsername()
                        + "!\n\n"
                        + "Role: "
                        + user.getRole(),
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE
        );


        openDashboard(user);
    }


    // =========================================================
    // ERROR DISPLAY
    // =========================================================

    private void showError(
            String message
    ) {

        errorLabel.setText(
                "⚠ " + message
        );
    }


    // =========================================================
    // OPEN DASHBOARD
    // =========================================================

    private void openDashboard(
            User user
    ) {

        dispose();


        /*
         * IMPORTANT:
         *
         * Buradaki DashboardFrame kısmını,
         * senin projendeki mevcut ana pencerenin
         * adına göre bağlayacağız.
         *
         * Örneğin DashboardFrame varsa:
         *
         * DashboardFrame dashboard =
         *         new DashboardFrame(user);
         *
         * dashboard.setVisible(true);
         */

        new MainFrame(user);
    }


    // =========================================================
    // MAIN (TEST)
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    LoginFrame login =
                            new LoginFrame();

                    login.setVisible(true);
                }
        );
    }
}