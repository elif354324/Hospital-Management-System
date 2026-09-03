package ui;

import app.HospitalData;
import models.Patient;
import services.EmergencyTriage;
import services.RecordRegistry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EmergencyPanel extends JPanel {

    private JTextField nameField;
    private JTextField severityField;
    private JTextField removeIdField;

    private JLabel patientCountLabel;
    private JLabel capacityLabel;
    private JLabel occupancyLabel;
    private JLabel nextPatientLabel;
    private JLabel treatedLabel;

    private JProgressBar occupancyBar;

    private EmergencyTriage emergencySystem;
    private RecordRegistry patientRegistry;


    public EmergencyPanel() {

        emergencySystem =
                HospitalData.getEmergencySystem();

        patientRegistry =
                HospitalData.getPatientRegistry();


        setLayout(new BorderLayout());

        setBackground(Color.WHITE);


        createHeader();

        createMainContent();

        refreshEmergencyInformation();
    }


    // =========================================
    // HEADER
    // =========================================

    private void createHeader() {

        JPanel header =
                new JPanel(new BorderLayout());

        header.setBackground(Color.WHITE);

        header.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        15,
                        30
                )
        );


        JLabel title =
                new JLabel("Emergency Triage");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );


        JLabel subtitle =
                new JLabel(
                        "Priority-based emergency patient management using Max Heap"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        subtitle.setForeground(Color.GRAY);


        JPanel textPanel =
                new JPanel();

        textPanel.setBackground(Color.WHITE);

        textPanel.setLayout(
                new BoxLayout(
                        textPanel,
                        BoxLayout.Y_AXIS
                )
        );


        textPanel.add(title);

        textPanel.add(
                Box.createVerticalStrut(5)
        );

        textPanel.add(subtitle);


        header.add(
                textPanel,
                BorderLayout.WEST
        );


        add(
                header,
                BorderLayout.NORTH
        );
    }


    // =========================================
    // MAIN CONTENT
    // =========================================

    private void createMainContent() {

        JPanel content =
                new JPanel(
                        new BorderLayout(
                                20,
                                20
                        )
                );


        content.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );


        content.setBorder(
                new EmptyBorder(
                        20,
                        30,
                        30,
                        30
                )
        );


        content.add(
                createTopSection(),
                BorderLayout.NORTH
        );


        content.add(
                createStatusSection(),
                BorderLayout.CENTER
        );


        add(
                content,
                BorderLayout.CENTER
        );
    }


    // =========================================
    // TOP SECTION
    // =========================================

    private JPanel createTopSection() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                20,
                                0
                        )
                );


        panel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );


        panel.add(
                createAdmissionCard()
        );


        panel.add(
                createTreatmentCard()
        );


        return panel;
    }


    // =========================================
    // EMERGENCY ADMISSION
    // =========================================

    private JPanel createAdmissionCard() {

        JPanel card =
                new JPanel();


        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );


        card.setBackground(Color.WHITE);


        card.setBorder(
                createCardBorder()
        );


        JLabel title =
                new JLabel(
                        "Emergency Admission"
                );


        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );


        card.add(title);


        card.add(
                Box.createVerticalStrut(20)
        );


        card.add(
                new JLabel("Patient Name")
        );


        nameField =
                new JTextField();


        prepareField(nameField);


        card.add(nameField);


        card.add(
                Box.createVerticalStrut(15)
        );


        card.add(
                new JLabel(
                        "Severity (1 - 10)"
                )
        );


        severityField =
                new JTextField("5");


        prepareField(severityField);


        card.add(severityField);


        card.add(
                Box.createVerticalStrut(20)
        );


        JButton admitButton =
                new JButton(
                        "🚨 Admit Emergency Patient"
                );


        admitButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        45
                )
        );


        admitButton.addActionListener(
                e -> admitEmergencyPatient()
        );


        card.add(admitButton);


        card.add(
                Box.createVerticalStrut(10)
        );


        JLabel info =
                new JLabel(
                        "Severity 10 = Highest Priority"
                );


        info.setForeground(Color.GRAY);


        card.add(info);


        return card;
    }


    // =========================================
    // TREATMENT CARD
    // =========================================

    private JPanel createTreatmentCard() {

        JPanel card =
                new JPanel();


        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );


        card.setBackground(Color.WHITE);


        card.setBorder(
                createCardBorder()
        );


        JLabel title =
                new JLabel(
                        "Priority Treatment"
                );


        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );


        card.add(title);


        card.add(
                Box.createVerticalStrut(25)
        );


        nextPatientLabel =
                new JLabel(
                        "Next Patient: None"
                );


        nextPatientLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );


        card.add(nextPatientLabel);


        card.add(
                Box.createVerticalStrut(25)
        );


        JButton treatButton =
                new JButton(
                        "Treat Highest Priority Patient"
                );


        treatButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        45
                )
        );


        treatButton.addActionListener(
                e -> treatNextPatient()
        );


        card.add(treatButton);


        card.add(
                Box.createVerticalStrut(15)
        );


        card.add(
                new JLabel(
                        "Remove Patient by ID"
                )
        );


        removeIdField =
                new JTextField();


        prepareField(removeIdField);


        card.add(removeIdField);


        card.add(
                Box.createVerticalStrut(10)
        );


        JButton removeButton =
                new JButton(
                        "Remove Patient"
                );


        removeButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );


        removeButton.addActionListener(
                e -> removeEmergencyPatient()
        );


        card.add(removeButton);


        return card;
    }


    // =========================================
    // STATUS SECTION
    // =========================================

    private JPanel createStatusSection() {

        JPanel container =
                new JPanel(
                        new BorderLayout(
                                20,
                                20
                        )
                );


        container.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );


        JPanel statistics =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                20,
                                0
                        )
                );


        statistics.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );


        statistics.add(
                createPatientCountCard()
        );


        statistics.add(
                createCapacityCard()
        );


        statistics.add(
                createTreatedCard()
        );


        container.add(
                statistics,
                BorderLayout.NORTH
        );


        container.add(
                createOccupancyCard(),
                BorderLayout.CENTER
        );


        return container;
    }


    // =========================================
    // PATIENT COUNT CARD
    // =========================================

    private JPanel createPatientCountCard() {

        JPanel card =
                createStatisticCard();


        JLabel title =
                new JLabel(
                        "CURRENT ER PATIENTS"
                );


        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );


        patientCountLabel =
                new JLabel("0");


        patientCountLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        38
                )
        );


        card.add(
                title,
                BorderLayout.NORTH
        );


        card.add(
                patientCountLabel,
                BorderLayout.CENTER
        );


        return card;
    }


    // =========================================
    // CAPACITY CARD
    // =========================================

    private JPanel createCapacityCard() {

        JPanel card =
                createStatisticCard();


        JLabel title =
                new JLabel(
                        "EMERGENCY CAPACITY"
                );


        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );


        capacityLabel =
                new JLabel("0");


        capacityLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        38
                )
        );


        card.add(
                title,
                BorderLayout.NORTH
        );


        card.add(
                capacityLabel,
                BorderLayout.CENTER
        );


        return card;
    }


    // =========================================
    // TREATED CARD
    // =========================================

    private JPanel createTreatedCard() {

        JPanel card =
                createStatisticCard();


        JLabel title =
                new JLabel(
                        "TOTAL TREATED"
                );


        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );


        treatedLabel =
                new JLabel("0");


        treatedLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        38
                )
        );


        card.add(
                title,
                BorderLayout.NORTH
        );


        card.add(
                treatedLabel,
                BorderLayout.CENTER
        );


        return card;
    }


    // =========================================
    // OCCUPANCY CARD
    // =========================================

    private JPanel createOccupancyCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );


        card.setBackground(Color.WHITE);


        card.setBorder(
                createCardBorder()
        );


        JLabel title =
                new JLabel(
                        "Emergency Room Occupancy"
                );


        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );


        occupancyLabel =
                new JLabel(
                        "0%"
                );


        occupancyLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );


        occupancyBar =
                new JProgressBar(
                        0,
                        100
                );


        occupancyBar.setStringPainted(true);


        card.add(
                title,
                BorderLayout.NORTH
        );


        card.add(
                occupancyBar,
                BorderLayout.CENTER
        );


        card.add(
                occupancyLabel,
                BorderLayout.SOUTH
        );


        return card;
    }


    // =========================================
    // ADMIT PATIENT
    // =========================================

    private void admitEmergencyPatient() {

        String name =
                nameField.getText().trim();


        String severityText =
                severityField.getText().trim();


        if (name.isEmpty()) {

            showMessage(
                    "Please enter the patient's name.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        int severity;


        try {

            severity =
                    Integer.parseInt(
                            severityText
                    );

        }

        catch (NumberFormatException e) {

            showMessage(
                    "Severity must be a number between 1 and 10.",
                    "Invalid Severity",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        if (
                severity < 1
                        || severity > 10
        ) {

            showMessage(
                    "Severity must be between 1 and 10.",
                    "Invalid Severity",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        String emergencyId =
                "EMG-"
                        + System.currentTimeMillis()
                        + "-"
                        + (int)
                        (Math.random() * 1000);


        Patient patient =
                new Patient(
                        emergencyId,
                        name,
                        severity
                );


        boolean admitted =
                emergencySystem.admit(patient);


        if (admitted) {

            patientRegistry.add(patient);


            patient.addHistory(
                    "Emergency admission - Severity: "
                            + severity
            );


            refreshEmergencyInformation();


            nameField.setText("");

            severityField.setText("5");


            showMessage(
                    "Emergency patient admitted successfully!\n\n"
                            + "Patient ID: "
                            + patient.getId()
                            + "\nSeverity: "
                            + severity
                            + "/10",

                    "Emergency Admission",

                    JOptionPane.INFORMATION_MESSAGE
            );

        }

        else {

            showMessage(
                    "Emergency room is at full capacity.",

                    "Capacity Full",

                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================
    // TREAT NEXT PATIENT
    // =========================================

    private void treatNextPatient() {

        Patient patient =
                emergencySystem.treatNext();


        if (patient == null) {

            showMessage(
                    "There are no emergency patients waiting.",

                    "Emergency Room Empty",

                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }


        refreshEmergencyInformation();


        showMessage(
                "🚨 Patient selected by Max Heap priority:\n\n"

                        + "Name: "
                        + patient.getName()

                        + "\nID: "
                        + patient.getId()

                        + "\nSeverity: "
                        + patient.getSeverity()
                        + "/10",

                "Treating Patient",

                JOptionPane.INFORMATION_MESSAGE
        );
    }


    // =========================================
    // REMOVE PATIENT
    // =========================================

    private void removeEmergencyPatient() {

        String id =
                removeIdField
                        .getText()
                        .trim();


        if (id.isEmpty()) {

            showMessage(
                    "Please enter a Patient ID.",

                    "Remove Patient",

                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        boolean removed =
                emergencySystem.removePatient(id);


        if (removed) {

            refreshEmergencyInformation();

            removeIdField.setText("");


            showMessage(
                    "Patient removed from emergency triage.",

                    "Success",

                    JOptionPane.INFORMATION_MESSAGE
            );

        }

        else {

            showMessage(
                    "Emergency patient not found.",

                    "Not Found",

                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================
    // REFRESH INFORMATION
    // =========================================

    private void refreshEmergencyInformation() {

        int patientCount =
                emergencySystem.getPatientCount();


        int capacity =
                emergencySystem.getCapacity();


        int treated =
                emergencySystem.getTotalTreated();


        int occupancy =
                (int)
                        emergencySystem
                                .getOccupancyPercentage();


        patientCountLabel.setText(
                String.valueOf(patientCount)
        );


        capacityLabel.setText(
                String.valueOf(capacity)
        );


        treatedLabel.setText(
                String.valueOf(treated)
        );


        occupancyLabel.setText(
                occupancy + "%"
        );


        occupancyBar.setValue(
                occupancy
        );


        occupancyBar.setString(
                occupancy + "% Full"
        );


        Patient nextPatient =
                emergencySystem.peekNext();


        if (nextPatient == null) {

            nextPatientLabel.setText(
                    "Next Patient: None"
            );

        }

        else {

            nextPatientLabel.setText(
                    "Next Priority: "
                            + nextPatient.getName()
                            + " (Severity "
                            + nextPatient.getSeverity()
                            + "/10)"
            );
        }
    }


    // =========================================
    // HELPERS
    // =========================================

    private JPanel createStatisticCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );


        card.setBackground(Color.WHITE);


        card.setBorder(
                createCardBorder()
        );


        return card;
    }


    private javax.swing.border.Border createCardBorder() {

        return BorderFactory.createCompoundBorder(

                BorderFactory.createLineBorder(
                        new Color(
                                220,
                                225,
                                230
                        )
                ),

                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );
    }


    private void prepareField(
            JTextField field
    ) {

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        35
                )
        );
    }


    private void showMessage(

            String message,

            String title,

            int type
    ) {

        JOptionPane.showMessageDialog(

                this,

                message,

                title,

                type
        );
    }
}