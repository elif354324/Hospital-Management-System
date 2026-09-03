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

    private void createDashboard() {

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BACKGROUND);
        content.setBorder(new EmptyBorder(30, 35, 30, 35));

        // =========================
        // HEADER
        // =========================

        JLabel title = new JLabel("Hospital Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "Welcome to the hospital administration dashboard"
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(SECONDARY);

        content.add(title);
        content.add(Box.createVerticalStrut(5));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(25));

        // =========================
        // STAT CARDS
        // =========================

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 18, 0));
        statsPanel.setBackground(BACKGROUND);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        patientCountLabel =
                createStatCard(
                        statsPanel,
                        "PATIENTS",
                        "0",
                        PRIMARY
                );

        doctorCountLabel =
                createStatCard(
                        statsPanel,
                        "DOCTORS",
                        "0",
                        GREEN
                );

        appointmentCountLabel =
                createStatCard(
                        statsPanel,
                        "APPOINTMENTS",
                        "0",
                        ORANGE
                );

        waitingCountLabel =
                createStatCard(
                        statsPanel,
                        "WAITING",
                        "0",
                        RED
                );

        content.add(statsPanel);
        content.add(Box.createVerticalStrut(25));

        // =========================
        // LOWER CONTENT
        // =========================

        JPanel lowerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        lowerPanel.setBackground(BACKGROUND);

        // Appointments
        appointmentsPanel = createAppointmentsPanel();
        lowerPanel.add(appointmentsPanel);

        // System overview
        JPanel overviewPanel = createOverviewPanel();
        lowerPanel.add(overviewPanel);

        content.add(lowerPanel);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        refresh();
    }

    // =========================
    // STAT CARD
    // =========================

    private JLabel createStatCard(
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
    }

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

        if (appointmentsPanel == null) {
            return;
        }

        Container parent =
                appointmentsPanel.getParent();

        if (parent == null) {
            return;
        }

        int index =
                java.util.Arrays.asList(
                        parent.getComponents()
                ).indexOf(appointmentsPanel);

        JPanel newPanel =
                createAppointmentsPanel();

        parent.remove(index);
        parent.add(newPanel, index);

        appointmentsPanel = newPanel;

        parent.revalidate();
        parent.repaint();
    }
}