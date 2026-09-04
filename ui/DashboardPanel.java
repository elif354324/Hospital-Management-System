package ui;

import app.HospitalData;
import models.Appointment;
import models.Doctor;
import models.Patient;
import services.AppointmentScheduler;
import services.RecordRegistry;
import structures.HashTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class DashboardPanel extends JPanel {

    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color CARD = Color.WHITE;
    private final Color TEXT = new Color(35, 45, 55);
    private final Color SECONDARY = new Color(110, 120, 130);
    private final Color PRIMARY = new Color(37, 99, 235);
    private final Color GREEN = new Color(22, 163, 74);
    private final Color ORANGE = new Color(234, 88, 12);
    private final Color RED = new Color(220, 38, 38);

    private JLabel patientCountLabel;
    private JLabel doctorCountLabel;
    private JLabel appointmentCountLabel;
    private JLabel waitingCountLabel;

    private JPanel appointmentsPanel;
    private JPanel content;
    private JPanel statsPanel;
    private JPanel lowerPanel;

    private JPanel overviewPanel;
    private JPanel patientCard;
    private JPanel doctorCard;
    private JPanel appointmentCard;
    private JPanel waitingCard;

    private JScrollPane scrollPane;

    private final RecordRegistry patientRegistry;
    private final HashTable<Doctor> doctorRegistry;
    private final AppointmentScheduler appointmentScheduler;

    public DashboardPanel() {

        patientRegistry = HospitalData.getPatientRegistry();
        doctorRegistry = HospitalData.getDoctorRegistry();
        appointmentScheduler = HospitalData.getAppointmentScheduler();

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        createDashboard();
    }

 private JPanel createStatsPanel() {

    JPanel panel =
            new JPanel(
                    new GridLayout(
                            1,
                            4,
                            18,
                            18
                    )
            );

    panel.setBackground(BACKGROUND);

    panel.setAlignmentX(
            Component.LEFT_ALIGNMENT
    );


    patientCard =
            createStatCardPanel(
                    "PATIENTS",
                    PRIMARY,
                    0
            );


    doctorCard =
            createStatCardPanel(
                    "DOCTORS",
                    GREEN,
                    1
            );


    appointmentCard =
            createStatCardPanel(
                    "APPOINTMENTS",
                    ORANGE,
                    2
            );


    waitingCard =
            createStatCardPanel(
                    "WAITING",
                    RED,
                    3
            );


    panel.add(patientCard);

    panel.add(doctorCard);

    panel.add(appointmentCard);

    panel.add(waitingCard);


    return panel;
}

private JPanel createStatCardPanel(
        String title,
        Color accent,
        int type
) {

    JPanel card =
            new JPanel(
                    new BorderLayout()
            );

    card.setBackground(CARD);

    card.setBorder(
            BorderFactory.createCompoundBorder(

                    BorderFactory.createLineBorder(
                            new Color(
                                    225,
                                    229,
                                    235
                            )
                    ),

                    new EmptyBorder(
                            18,
                            20,
                            18,
                            20
                    )
            )
    );


    JLabel titleLabel =
            new JLabel(title);

    titleLabel.setFont(
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    12
            )
    );

    titleLabel.setForeground(SECONDARY);


    JLabel valueLabel =
            new JLabel("0");

    valueLabel.setFont(
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    32
            )
    );

    valueLabel.setForeground(accent);


    card.add(
            titleLabel,
            BorderLayout.NORTH
    );

    card.add(
            valueLabel,
            BorderLayout.CENTER
    );


    // Label referanslarını kaydet

    if (type == 0) {
        patientCountLabel = valueLabel;
    }

    else if (type == 1) {
        doctorCountLabel = valueLabel;
    }

    else if (type == 2) {
        appointmentCountLabel = valueLabel;
    }

    else if (type == 3) {
        waitingCountLabel = valueLabel;
    }


    return card;
}

private JPanel createLowerPanel() {

    JPanel panel =
            new JPanel(
                    new GridLayout(
                            1,
                            2,
                            20,
                            0
                    )
            );

    panel.setBackground(BACKGROUND);

    panel.setAlignmentX(
            Component.LEFT_ALIGNMENT
    );


    appointmentsPanel =
            createAppointmentsPanel();


    overviewPanel =
            createOverviewPanel();


    panel.add(appointmentsPanel);

    panel.add(overviewPanel);


    return panel;
}

private void updateResponsiveLayout() {

    if (
            statsPanel == null
                    || lowerPanel == null
    ) {
        return;
    }


    int width =
            getWidth();


    // =========================================
    // STAT CARDS
    // =========================================

    statsPanel.removeAll();


    if (width >= 1100) {

        statsPanel.setLayout(
                new GridLayout(
                        1,
                        4,
                        18,
                        18
                )
        );


        statsPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        130
                )
        );

    }

    else if (width >= 750) {

        statsPanel.setLayout(
                new GridLayout(
                        2,
                        2,
                        18,
                        18
                )
        );


        statsPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        270
                )
        );

    }

    else {

        statsPanel.setLayout(
                new GridLayout(
                        4,
                        1,
                        0,
                        15
                )
        );


        statsPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        520
                )
        );
    }


    addStatCards();


    // =========================================
    // LOWER PANELS
    // =========================================

    lowerPanel.removeAll();


    if (width >= 900) {

        // Büyük ekran:
        // Appointment | System Overview

        lowerPanel.setLayout(
                new GridLayout(
                        1,
                        2,
                        20,
                        0
                )
        );


        lowerPanel.add(
                appointmentsPanel
        );


        lowerPanel.add(
                overviewPanel
        );


        lowerPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        400
                )
        );

    }

    else {

        // Küçük ekran:
        // Appointment
        //
        // System Overview

        lowerPanel.setLayout(
                new BoxLayout(
                        lowerPanel,
                        BoxLayout.Y_AXIS
                )
        );


        appointmentsPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        overviewPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        appointmentsPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        350
                )
        );


        overviewPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        500
                )
        );


        lowerPanel.add(
                appointmentsPanel
        );


        lowerPanel.add(
                Box.createVerticalStrut(20)
        );


        lowerPanel.add(
                overviewPanel
        );


        lowerPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        900
                )
        );
    }


    statsPanel.revalidate();
    statsPanel.repaint();


    lowerPanel.revalidate();
    lowerPanel.repaint();


    content.revalidate();
    content.repaint();
}

private void addStatCards() {

    statsPanel.add(patientCard);

    statsPanel.add(doctorCard);

    statsPanel.add(appointmentCard);

    statsPanel.add(waitingCard);
}

    private void createDashboard() {

    content = new JPanel();
    content.setLayout(
            new BoxLayout(
                    content,
                    BoxLayout.Y_AXIS
            )
    );

    content.setBackground(BACKGROUND);

    content.setBorder(
            new EmptyBorder(
                    30,
                    35,
                    30,
                    35
            )
    );


    // =========================
    // HEADER
    // =========================

    JLabel title =
            new JLabel(
                    "Hospital Management System"
            );

    title.setFont(
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    28
            )
    );

    title.setForeground(TEXT);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);


    JLabel subtitle =
            new JLabel(
                    "Welcome to the hospital administration dashboard"
            );

    subtitle.setFont(
            new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    14
            )
    );

    subtitle.setForeground(SECONDARY);
    subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

    content.add(title);

    content.add(
            Box.createVerticalStrut(5)
    );

    content.add(subtitle);

    content.add(
            Box.createVerticalStrut(25)
    );


    // =========================
    // STATISTICS
    // =========================

    statsPanel =
            createStatsPanel();

    content.add(statsPanel);

    content.add(
            Box.createVerticalStrut(25)
    );


    // =========================
    // LOWER CONTENT
    // =========================

    lowerPanel =
            createLowerPanel();

    content.add(lowerPanel);


    // =========================
    // SCROLL
    // =========================

    scrollPane =
            new JScrollPane(content);

    scrollPane.setBorder(null);

    scrollPane.getVerticalScrollBar()
            .setUnitIncrement(16);


    add(
            scrollPane,
            BorderLayout.CENTER
    );


    // Responsive kontrolü

    addComponentListener(
            new java.awt.event.ComponentAdapter() {

                @Override
                public void componentResized(
                        java.awt.event.ComponentEvent e
                ) {

                    updateResponsiveLayout();
                }
            }
    );


    SwingUtilities.invokeLater(
            this::updateResponsiveLayout
    );


    refresh();
}

    // =========================
    // STAT CARD
    // =========================

   /*private JLabel createStatCard(
            JPanel parent,
            String title,
            String value,
            Color accent
    ) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 229, 235)
                        ),
                        new EmptyBorder(18, 20, 18, 20)
                )
        );

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        titleLabel.setForeground(SECONDARY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 32)
        );
        valueLabel.setForeground(accent);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        parent.add(card);

        return valueLabel;
    }*/

    // =========================
    // APPOINTMENTS
    // =========================

    private JPanel createAppointmentsPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 229, 235)
                        ),
                        new EmptyBorder(20, 20, 20, 20)
                )
        );

        JLabel title = new JLabel("Upcoming Appointments");
        title.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );
        title.setForeground(TEXT);

        panel.add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(
                new BoxLayout(listPanel, BoxLayout.Y_AXIS)
        );
        listPanel.setBackground(CARD);

        List<Appointment> appointments =
                appointmentScheduler.getAllAppointments();

        if (appointments.isEmpty()) {

            JLabel emptyLabel =
                    new JLabel("No appointments scheduled.");

            emptyLabel.setFont(
                    new Font("Segoe UI", Font.PLAIN, 14)
            );
            emptyLabel.setForeground(SECONDARY);

            listPanel.add(Box.createVerticalStrut(20));
            listPanel.add(emptyLabel);

        } else {

            int count = 0;

            for (Appointment appointment : appointments) {

                if (count >= 6) {
                    break;
                }

                JPanel appointmentCard =
                        createAppointmentRow(appointment);

                listPanel.add(appointmentCard);
                listPanel.add(Box.createVerticalStrut(8));

                count++;
            }
        }

        JScrollPane scroll =
                new JScrollPane(listPanel);

        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAppointmentRow(
            Appointment appointment
    ) {

        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(249, 250, 251));

        row.setBorder(
                new EmptyBorder(10, 12, 10, 12)
        );

        Patient patient = appointment.getPatient();
        Doctor doctor = appointment.getDoctor();

        JLabel patientLabel =
                new JLabel(patient.getName());

        patientLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );
        patientLabel.setForeground(TEXT);

        JLabel doctorLabel =
                new JLabel("Dr. " + doctor.getName());

        doctorLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );
        doctorLabel.setForeground(SECONDARY);

        JPanel info = new JPanel();
        info.setLayout(
                new BoxLayout(info, BoxLayout.Y_AXIS)
        );
        info.setBackground(row.getBackground());

        info.add(patientLabel);
        info.add(Box.createVerticalStrut(3));
        info.add(doctorLabel);

        JLabel timeLabel =
                new JLabel(
                        appointment.getDate()
                                + "  "
                                + appointment.getTime()
                );

        timeLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        timeLabel.setForeground(PRIMARY);

        row.add(info, BorderLayout.CENTER);
        row.add(timeLabel, BorderLayout.EAST);

        return row;
    }

    // =========================
    // SYSTEM OVERVIEW
    // =========================

    private JPanel createOverviewPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 229, 235)
                        ),
                        new EmptyBorder(20, 20, 20, 20)
                )
        );

        JLabel title =
                new JLabel("System Overview");

        title.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );
        title.setForeground(TEXT);

        panel.add(title, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(
                new BoxLayout(infoPanel, BoxLayout.Y_AXIS)
        );
        infoPanel.setBackground(CARD);

        addOverviewItem(
                infoPanel,
                "Registered Departments",
                String.valueOf(
                        HospitalData.getDepartments().size()
                )
        );

        addOverviewItem(
                infoPanel,
                "Patient Registry Capacity",
                String.valueOf(
                        patientRegistry.getCapacity()
                )
        );

        addOverviewItem(
                infoPanel,
                "Registry Load Factor",
                String.format(
                        "%.2f",
                        patientRegistry.getLoadFactor()
                )
        );

        addOverviewItem(
                infoPanel,
                "Waiting Queue",
                String.valueOf(
                        appointmentScheduler.getQueueSize()
                )
        );

        addOverviewItem(
                infoPanel,
                "Emergency Capacity",
                "20 patients"
        );

        addOverviewItem(
                infoPanel,
                "System Date",
                LocalDate.now().toString()
        );

        panel.add(infoPanel, BorderLayout.CENTER);

        return panel;
    }

    private void addOverviewItem(
            JPanel parent,
            String title,
            String value
    ) {

        JPanel row = new JPanel(
                new BorderLayout()
        );

        row.setBackground(CARD);
        row.setBorder(
                new EmptyBorder(10, 0, 10, 0)
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );
        titleLabel.setForeground(SECONDARY);

        JLabel valueLabel =
                new JLabel(value);

        valueLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );
        valueLabel.setForeground(TEXT);

        row.add(titleLabel, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);

        parent.add(row);
    }

    // =========================
    // REFRESH
    // =========================

    public void refresh() {

        if (patientCountLabel == null) {
            return;
        }

        int patientCount =
                patientRegistry.getPatientCount();

        int doctorCount =
                doctorRegistry.getAllValues().size();

        int appointmentCount =
                appointmentScheduler
                        .getScheduledAppointmentsCount();

        int waitingCount =
                appointmentScheduler.getQueueSize();

        patientCountLabel.setText(
                String.valueOf(patientCount)
        );

        doctorCountLabel.setText(
                String.valueOf(doctorCount)
        );

        appointmentCountLabel.setText(
                String.valueOf(appointmentCount)
        );

        waitingCountLabel.setText(
                String.valueOf(waitingCount)
        );

        refreshAppointments();
    }

    private void refreshAppointments() {

    if (
            appointmentsPanel == null
                    || lowerPanel == null
    ) {
        return;
    }


    JPanel newAppointmentsPanel =
            createAppointmentsPanel();


    Container parent =
            appointmentsPanel.getParent();


    if (parent == null) {
        return;
    }


    int index = -1;


    Component[] components =
            parent.getComponents();


    for (int i = 0; i < components.length; i++) {

        if (components[i] == appointmentsPanel) {

            index = i;

            break;
        }
    }


    if (index == -1) {
        return;
    }


    parent.remove(
            appointmentsPanel
    );


    parent.add(
            newAppointmentsPanel,
            index
    );


    appointmentsPanel =
            newAppointmentsPanel;


    updateResponsiveLayout();


    parent.revalidate();
    parent.repaint();
}
}