package ui;

import app.HospitalData;
import models.Patient;
import services.AIService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ClinicalAIPanel extends JPanel {

    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color CARD = Color.WHITE;
    private final Color PRIMARY = new Color(37, 99, 235);
    private final Color TEXT = new Color(31, 41, 55);

    private JComboBox<String> patientComboBox;
    private JTextArea complaintArea;
    private JTextArea resultArea;
    private JLabel statusLabel;
    private JButton analyzeButton;

    private final AIService aiService;

    public ClinicalAIPanel() {

        aiService = new AIService();

        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        loadPatients();
    }

    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel title = new JLabel("AI Clinical Pre-Assessment");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "AI destekli ön değerlendirme ve risk analizi"
        );

        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(107, 114, 128));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(subtitle);

        panel.add(textPanel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createMainContent() {

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setOpaque(false);

        mainPanel.add(createInputPanel());
        mainPanel.add(createResultPanel());

        return mainPanel;
    }

    private JPanel createInputPanel() {

        JPanel card = createCard();

        card.setLayout(new BorderLayout(15, 15));

        JLabel title = new JLabel("Patient Assessment");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(TEXT);

        card.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel patientLabel = new JLabel("Select Patient");
        patientLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        patientComboBox = new JComboBox<>();
        patientComboBox.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 38)
        );

        JLabel complaintLabel = new JLabel("Current Complaint / Symptoms");
        complaintLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        complaintArea = new JTextArea(8, 30);
        complaintArea.setLineWrap(true);
        complaintArea.setWrapStyleWord(true);
        complaintArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JScrollPane complaintScroll =
                new JScrollPane(complaintArea);

        complaintScroll.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 180)
        );

        analyzeButton = new JButton("AI PRE-ASSESSMENT");
        analyzeButton.setFont(
                new Font("SansSerif", Font.BOLD, 14)
        );
        analyzeButton.setForeground(Color.WHITE);
        analyzeButton.setBackground(PRIMARY);
        analyzeButton.setFocusPainted(false);
        analyzeButton.setBorderPainted(false);
        analyzeButton.setPreferredSize(
                new Dimension(200, 42)
        );

        analyzeButton.addActionListener(e ->
                performAssessment()
        );

        form.add(patientLabel);
        form.add(Box.createVerticalStrut(8));
        form.add(patientComboBox);

        form.add(Box.createVerticalStrut(20));

        form.add(complaintLabel);
        form.add(Box.createVerticalStrut(8));
        form.add(complaintScroll);

        form.add(Box.createVerticalStrut(20));

        form.add(analyzeButton);

        card.add(form, BorderLayout.CENTER);

        return card;
    }

    private JPanel createResultPanel() {

        JPanel card = createCard();

        card.setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("AI Assessment Result");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(TEXT);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );
        resultArea.setBackground(new Color(249, 250, 251));
        resultArea.setBorder(
                new EmptyBorder(15, 15, 15, 15)
        );

        JScrollPane scrollPane =
                new JScrollPane(resultArea);

        statusLabel = new JLabel(
                "Ready for assessment"
        );

        statusLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );
        statusLabel.setForeground(
                new Color(107, 114, 128)
        );

        card.add(title, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);
        card.add(statusLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createCard() {

        JPanel panel = new JPanel();

        panel.setBackground(CARD);
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(229, 231, 235)
                        ),
                        new EmptyBorder(20, 20, 20, 20)
                )
        );

        return panel;
    }

    private void loadPatients() {

        patientComboBox.removeAllItems();

        List<Patient> patients =
                HospitalData.getPatientRegistry()
                        .getAllPatients();

        if (patients.isEmpty()) {

            patientComboBox.addItem(
                    "No registered patients"
            );

            analyzeButton.setEnabled(false);

            return;
        }

        for (Patient patient : patients) {

            patientComboBox.addItem(
                    patient.getId()
                            + " - "
                            + patient.getName()
            );
        }
    }

    private void performAssessment() {

        if (patientComboBox.getSelectedItem() == null) {
            return;
        }

        String selected =
                patientComboBox
                        .getSelectedItem()
                        .toString();

        if (selected.equals("No registered patients")) {
            return;
        }

        String patientId =
                selected.split(" - ")[0];

        Patient patient =
                HospitalData
                        .getPatientRegistry()
                        .get(patientId);

        if (patient == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Patient record could not be found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        String complaint =
                complaintArea.getText().trim();

        if (complaint.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the patient's current complaint or symptoms.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        analyzeButton.setEnabled(false);
        statusLabel.setText("AI is analyzing...");
        resultArea.setText("Please wait...");

        String prompt = buildClinicalPrompt(
                patient,
                complaint
        );

        SwingWorker<String, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected String doInBackground()
                            throws Exception {

                        return aiService.ask(prompt);
                    }

                    @Override
                    protected void done() {

                        analyzeButton.setEnabled(true);

                        try {

                            String result = get();

                            resultArea.setText(
                                    result
                                            + "\n\n"
                                            + "----------------------------------"
                                            + "\n"
                                            + "⚠ AI-generated information. "
                                            + "This is NOT a medical diagnosis."
                            );

                            statusLabel.setText(
                                    "Assessment completed"
                            );

                        } catch (Exception ex) {

                            resultArea.setText(
                                    "AI service could not be reached.\n\n"
                                    + ex.getMessage()
                            );

                            statusLabel.setText(
                                    "Assessment failed"
                            );
                        }
                    }
                };

        worker.execute();
    }

    private String buildClinicalPrompt(
            Patient patient,
            String complaint
    ) {

        return """
                You are a clinical decision-support assistant
                inside a hospital management system.

                Your task is NOT to diagnose the patient.

                Analyze the provided patient information and
                current complaint and produce a cautious
                clinical pre-assessment for a healthcare
                professional.

                Patient Information:
                ID: %s
                Name: %s
                Age: %d
                Gender: %s
                Severity: %d

                Current Complaint:
                %s

                Provide your response using these sections:

                1. Risk Level
                2. Key Findings
                3. Possible Clinical Areas to Consider
                4. Warning Signs
                5. Recommended Next Step

                Do not claim certainty.
                Do not provide a definitive diagnosis.
                Clearly state when urgent professional
                evaluation may be appropriate.

                Medical history should be considered if provided.
                """.formatted(
                patient.getId(),
                patient.getName(),
                patient.getAge(),
                patient.getGender(),
                patient.getSeverity(),
                complaint
        );
    }

    public void refresh() {
        loadPatients();
    }
}