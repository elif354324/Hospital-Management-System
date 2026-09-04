package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;

    private JPanel mainPanel;

    private JPanel sidebar;

    private Map<String, SidebarButton> menuButtons;

    private DashboardPanel dashboardPanel;

    private DoctorPanel doctorPanel;

    private EmergencyPanel emergencyPanel;


    public MainFrame() {

        menuButtons =
                new HashMap<>();


        initializeFrame();

        createSidebar();

        createMainPanel();

        setupResponsiveLayout();


        setVisible(true);
    }


    // =========================================
    // FRAME SETTINGS
    // =========================================

    private void initializeFrame() {

        setTitle(
                "Hospital Management System"
        );


        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );


        setSize(
                1300,
                800
        );


        setMinimumSize(
                new Dimension(
                        800,
                        600
                )
        );


        setLocationRelativeTo(null);


        setLayout(
                new BorderLayout()
        );
    }


    // =========================================
    // SIDEBAR
    // =========================================

    private void createSidebar() {

        sidebar =
                new JPanel();


        sidebar.setLayout(
                new BoxLayout(
                        sidebar,
                        BoxLayout.Y_AXIS
                )
        );


        sidebar.setBackground(
                UITheme.SIDEBAR
        );


        sidebar.setPreferredSize(
                new Dimension(
                        240,
                        0
                )
        );


        createLogoSection();


        sidebar.add(
                Box.createVerticalStrut(20)
        );


        addMenuButton(
                "Dashboard",
                "DASHBOARD"
        );


        addMenuButton(
                "Patients",
                "PATIENTS"
        );


        addMenuButton(
                "Doctors",
                "DOCTORS"
        );


        addMenuButton(
                "Appointments",
                "APPOINTMENTS"
        );


        addMenuButton(
                "Emergency",
                "EMERGENCY"
        );


        sidebar.add(
                Box.createVerticalStrut(20)
        );


        sidebar.add(
                createSeparator()
        );


        sidebar.add(
                Box.createVerticalStrut(15)
        );


        addMenuButton(
                "System Information",
                "SYSTEM"
        );


        sidebar.add(
                Box.createVerticalGlue()
        );


        sidebar.add(
                createFooter()
        );


        add(
                sidebar,
                BorderLayout.WEST
        );
    }


    // =========================================
    // LOGO
    // =========================================

    private void createLogoSection() {

        JPanel logoPanel =
                new JPanel();


        logoPanel.setBackground(
                UITheme.SIDEBAR
        );


        logoPanel.setLayout(
                new BoxLayout(
                        logoPanel,
                        BoxLayout.Y_AXIS
                )
        );


        logoPanel.setBorder(
                new EmptyBorder(
                        28,
                        20,
                        20,
                        20
                )
        );


        JLabel logo =
                new JLabel(
                        "H"
                );


        logo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        38
                )
        );

        logo.setForeground(UITheme.PRIMARY_LIGHT);

        logo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel title =
                new JLabel(
                        "HOSPITAL"
                );


        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        21
                )
        );


        title.setForeground(
                Color.WHITE
        );


        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel subtitle =
                new JLabel(
                        "MANAGEMENT SYSTEM"
                );


        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );


        subtitle.setForeground(
                UITheme.TEXT_SECONDARY
        );


        subtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        logoPanel.add(logo);


        logoPanel.add(
                Box.createVerticalStrut(8)
        );


        logoPanel.add(title);


        logoPanel.add(
                Box.createVerticalStrut(4)
        );


        logoPanel.add(subtitle);


        sidebar.add(logoPanel);
    }


    // =========================================
    // MENU BUTTON
    // =========================================

    private void addMenuButton(

            String text,

            String page
    ) {

        SidebarButton button =
                new SidebarButton(text);


        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        50
                )
        );


        button.addActionListener(
                e -> showPage(page)
        );


        menuButtons.put(
                page,
                button
        );


        sidebar.add(button);
    }


    // =========================================
    // SEPARATOR
    // =========================================

    private JSeparator createSeparator() {

        JSeparator separator =
                new JSeparator();


        separator.setForeground(
                UITheme.SIDEBAR_HOVER
        );


        separator.setMaximumSize(

                new Dimension(
                        Integer.MAX_VALUE,
                        1
                )
        );


        return separator;
    }


    // =========================================
    // FOOTER
    // =========================================

    private JPanel createFooter() {

        JPanel footer =
                new JPanel();


        footer.setBackground(
                UITheme.SIDEBAR
        );


        footer.setLayout(
                new BoxLayout(
                        footer,
                        BoxLayout.Y_AXIS
                )
        );


        footer.setBorder(
                new EmptyBorder(
                        15,
                        20,
                        20,
                        20
                )
        );


        JLabel version =
                new JLabel(
                        "Version 1.0"
                );


        version.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        11
                )
        );


        version.setForeground(
                UITheme.TEXT_SECONDARY
        );


        version.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel info =
                new JLabel(
                        "Data Structures Project"
                );


        info.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );


        info.setForeground(
                UITheme.TEXT_SECONDARY
        );


        info.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        footer.add(version);


        footer.add(
                Box.createVerticalStrut(5)
        );


        footer.add(info);


        return footer;
    }


    // =========================================
    // MAIN PANEL
    // =========================================

    private void createMainPanel() {

        cardLayout =
                new CardLayout();


        mainPanel =
                new JPanel(
                        cardLayout
                );


        dashboardPanel =
                new DashboardPanel();


        mainPanel.add(
                dashboardPanel,
                "DASHBOARD"
        );


        mainPanel.add(
                new PatientPanel(),
                "PATIENTS"
        );

        doctorPanel = new DoctorPanel();
        mainPanel.add(
                doctorPanel,
                "DOCTORS"
        );


        mainPanel.add(
                new AppointmentPanel(),
                "APPOINTMENTS"
        );

        emergencyPanel = new EmergencyPanel();
        mainPanel.add(
                emergencyPanel,
                "EMERGENCY"
        );


        mainPanel.add(
                new SystemPanel(),
                "SYSTEM"
        );


        add(
                mainPanel,
                BorderLayout.CENTER
        );


        showPage("DASHBOARD");
    }

    private void setupResponsiveLayout() {

    addComponentListener(
            new ComponentAdapter() {

                @Override
                public void componentResized(
                        ComponentEvent e
                ) {

                    int width =
                            getWidth();

                    if (width < 1000) {

                        sidebar.setPreferredSize(
                                new Dimension(
                                        180,
                                        0
                                )
                        );

                    } else {

                        sidebar.setPreferredSize(
                                new Dimension(
                                        240,
                                        0
                                )
                        );
                    }

                    revalidate();
                    repaint();
                }
            }
    );
}


    // =========================================
    // PAGE NAVIGATION
    // =========================================

 private void showPage(
        String page
) {

    cardLayout.show(
            mainPanel,
            page
    );

    updateSelectedButton(page);


    if (page.equals("DASHBOARD")) {

        dashboardPanel.refresh();

    } else if (page.equals("DOCTORS")) {

        doctorPanel.refresh();

    } else if (page.equals("EMERGENCY")) {

        emergencyPanel.refresh();
    }
}


    // =========================================
    // SELECTED BUTTON
    // =========================================

    private void updateSelectedButton(
            String selectedPage
    ) {

        for (
                Map.Entry<String, SidebarButton> entry
                        : menuButtons.entrySet()
        ) {

            boolean selected =
                    entry.getKey()
                            .equals(selectedPage);


            entry.getValue()
                    .setSelected(selected);
        }
    }
}