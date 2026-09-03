package ui;

import app.HospitalData;
import models.Patient;
import services.RecordRegistry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientPanel extends JPanel {

    private JTextField idField;
    private JTextField nameField;
    private JTextField severityField;

    private JTextField searchField;

    private JTable patientTable;
    private DefaultTableModel tableModel;

    private RecordRegistry patientRegistry;


    public PatientPanel() {

        patientRegistry = HospitalData.getPatientRegistry();

        setLayout(new BorderLayout());

        setBackground(Color.WHITE);

        createHeader();

        createMainContent();

        refreshTable();
    }


    // =========================================
    // HEADER
    // =========================================

    private void createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(Color.WHITE);

        header.setBorder(
                new EmptyBorder(25, 30, 15, 30)
        );


        JLabel title =
                new JLabel("Patient Management");

        title.setFont(
                new Font("Arial", Font.BOLD, 30)
        );


        JLabel subtitle =
                new JLabel(
                        "Register, search, update and manage hospital patients"
                );

        subtitle.setFont(
                new Font("Arial", Font.PLAIN, 15)
        );

        subtitle.setForeground(Color.GRAY);


        JPanel textPanel = new JPanel();

        textPanel.setBackground(Color.WHITE);

        textPanel.setLayout(
                new BoxLayout(textPanel, BoxLayout.Y_AXIS)
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


        add(header, BorderLayout.NORTH);
    }


    // =========================================
    // MAIN CONTENT
    // =========================================

    private void createMainContent() {

        JPanel content = new JPanel(
                new BorderLayout(20, 20)
        );

        content.setBackground(
                new Color(245, 247, 250)
        );

        content.setBorder(
                new EmptyBorder(20, 30, 30, 30)
        );


        content.add(
                createControlSection(),
                BorderLayout.NORTH
        );


        content.add(
                createTableSection(),
                BorderLayout.CENTER
        );


        add(content, BorderLayout.CENTER);
    }


    // =========================================
    // TOP CONTROL SECTION
    // =========================================

    private JPanel createControlSection() {

        JPanel panel = new JPanel(
                new GridLayout(1, 2, 20, 0)
        );

        panel.setBackground(
                new Color(245, 247, 250)
        );


        panel.add(
                createPatientForm()
        );

        panel.add(
                createSearchPanel()
        );


        return panel;
    }


    // =========================================
    // PATIENT FORM
    // =========================================

    private JPanel createPatientForm() {

        JPanel card = new JPanel();

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setBackground(Color.WHITE);

        card.setBorder(
                createCardBorder()
        );


        JLabel title =
                new JLabel("Patient Information");

        title.setFont(
                new Font("Arial", Font.BOLD, 20)
        );


        card.add(title);

        card.add(
                Box.createVerticalStrut(20)
        );


        card.add(
                new JLabel("Patient ID")
        );

        idField = new JTextField();

        prepareTextField(idField);

        card.add(idField);


        card.add(
                Box.createVerticalStrut(10)
        );


        card.add(
                new JLabel("Patient Name")
        );

        nameField = new JTextField();

        prepareTextField(nameField);

        card.add(nameField);


        card.add(
                Box.createVerticalStrut(10)
        );


        card.add(
                new JLabel("Severity (1-10)")
        );

        severityField = new JTextField("1");

        prepareTextField(severityField);

        card.add(severityField);


        card.add(
                Box.createVerticalStrut(20)
        );


        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(1, 3, 10, 0)
                );

        buttonPanel.setBackground(Color.WHITE);


        JButton addButton =
                new JButton("Add");

        JButton updateButton =
                new JButton("Update");

        JButton clearButton =
                new JButton("Clear");


        addButton.addActionListener(
                e -> addPatient()
        );


        updateButton.addActionListener(
                e -> updatePatient()
        );


        clearButton.addActionListener(
                e -> clearFields()
        );


        buttonPanel.add(addButton);

        buttonPanel.add(updateButton);

        buttonPanel.add(clearButton);


        card.add(buttonPanel);


        return card;
    }


    // =========================================
    // SEARCH PANEL
    // =========================================

    private JPanel createSearchPanel() {

        JPanel card = new JPanel();

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setBackground(Color.WHITE);

        card.setBorder(
                createCardBorder()
        );


        JLabel title =
                new JLabel("Search & Actions");

        title.setFont(
                new Font("Arial", Font.BOLD, 20)
        );


        card.add(title);

        card.add(
                Box.createVerticalStrut(20)
        );


        card.add(
                new JLabel("Patient ID")
        );


        searchField = new JTextField();

        prepareTextField(searchField);

        card.add(searchField);


        card.add(
                Box.createVerticalStrut(20)
        );


        JButton searchButton =
                new JButton("Search Patient");

        JButton deleteButton =
                new JButton("Delete Patient");

        JButton refreshButton =
                new JButton("Refresh List");


        searchButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        deleteButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        refreshButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );


        searchButton.addActionListener(
                e -> searchPatient()
        );

        deleteButton.addActionListener(
                e -> deletePatient()
        );

        refreshButton.addActionListener(
                e -> refreshTable()
        );


        card.add(searchButton);

        card.add(
                Box.createVerticalStrut(10)
        );

        card.add(deleteButton);

        card.add(
                Box.createVerticalStrut(10)
        );

        card.add(refreshButton);


        return card;
    }


    // =========================================
    // TABLE
    // =========================================

    private JScrollPane createTableSection() {

        String[] columns = {

                "Patient ID",

                "Patient Name",

                "Severity"
        };


        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };


        patientTable =
                new JTable(tableModel);


        patientTable.setRowHeight(32);

        patientTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );


        patientTable.getTableHeader().setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );


        patientTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );


        patientTable.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        loadSelectedPatient();
                    }
                });


        JScrollPane scrollPane =
                new JScrollPane(patientTable);


        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Registered Patients"
                )
        );


        return scrollPane;
    }


    // =========================================
    // ADD PATIENT
    // =========================================

    private void addPatient() {

        String id =
                idField.getText().trim();

        String name =
                nameField.getText().trim();

        String severityText =
                severityField.getText().trim();


        if (id.isEmpty() || name.isEmpty()) {

            showMessage(
                    "Patient ID and Name are required.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        int severity;

        try {

            severity =
                    Integer.parseInt(severityText);

        }

        catch (NumberFormatException e) {

            showMessage(
                    "Severity must be a number between 1 and 10.",
                    "Invalid Severity",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        if (severity < 1 || severity > 10) {

            showMessage(
                    "Severity must be between 1 and 10.",
                    "Invalid Severity",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        Patient patient =
                new Patient(
                        id,
                        name,
                        severity
                );


        boolean success =
                patientRegistry.add(patient);


        if (success) {

            refreshTable();

            clearFields();


            showMessage(
                    "Patient registered successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        }

        else {

            showMessage(
                    "A patient with this ID already exists.",
                    "Duplicate Patient",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================
    // SEARCH PATIENT
    // =========================================

    private void searchPatient() {

        String id =
                searchField.getText().trim();


        if (id.isEmpty()) {

            showMessage(
                    "Enter a Patient ID to search.",
                    "Search",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        Patient patient =
                patientRegistry.get(id);


        if (patient == null) {

            showMessage(
                    "Patient not found.",
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }


        idField.setText(
                patient.getId()
        );

        nameField.setText(
                patient.getName()
        );

        severityField.setText(
                String.valueOf(
                        patient.getSeverity()
                )
        );


        highlightPatient(
                patient.getId()
        );


        showMessage(
                "Patient found successfully.",
                "Search Result",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    // =========================================
    // UPDATE PATIENT
    // =========================================

    private void updatePatient() {

        String id =
                idField.getText().trim();

        String name =
                nameField.getText().trim();

        String severityText =
                severityField.getText().trim();


        if (id.isEmpty()) {

            showMessage(
                    "Select or search a patient first.",
                    "Update",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        Patient existingPatient =
                patientRegistry.get(id);


        if (existingPatient == null) {

            showMessage(
                    "Patient not found.",
                    "Update",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        int severity;

        try {

            severity =
                    Integer.parseInt(severityText);

        }

        catch (NumberFormatException e) {

            showMessage(
                    "Invalid severity value.",
                    "Update Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        if (severity < 1 || severity > 10) {

            showMessage(
                    "Severity must be between 1 and 10.",
                    "Update Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        existingPatient.setName(name);

        existingPatient.setSeverity(severity);


        patientRegistry.update(
                id,
                existingPatient
        );


        refreshTable();


        showMessage(
                "Patient updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    // =========================================
    // DELETE PATIENT
    // =========================================

    private void deletePatient() {

        String id =
                searchField.getText().trim();


        if (id.isEmpty()) {

            id =
                    idField.getText().trim();
        }


        if (id.isEmpty()) {

            showMessage(
                    "Select a patient or enter an ID.",
                    "Delete",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        int confirmation =
                JOptionPane.showConfirmDialog(

                        this,

                        "Are you sure you want to delete patient:\n"
                                + id + "?",

                        "Confirm Delete",

                        JOptionPane.YES_NO_OPTION,

                        JOptionPane.WARNING_MESSAGE
                );


        if (confirmation !=
                JOptionPane.YES_OPTION) {

            return;
        }


        boolean success =
                patientRegistry.delete(id);


        if (success) {

            refreshTable();

            clearFields();

            searchField.setText("");


            showMessage(
                    "Patient deleted successfully.",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE
            );

        }

        else {

            showMessage(
                    "Patient not found.",
                    "Delete Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================
    // REFRESH TABLE
    // =========================================

    private void refreshTable() {

        tableModel.setRowCount(0);


        List<Patient> patients =
                patientRegistry.getAllPatients();


        for (Patient patient : patients) {

            tableModel.addRow(

                    new Object[]{

                            patient.getId(),

                            patient.getName(),

                            patient.getSeverity()
                    }
            );
        }
    }


    // =========================================
    // SELECT TABLE ROW
    // =========================================

    private void loadSelectedPatient() {

        int row =
                patientTable.getSelectedRow();


        if (row == -1) {

            return;
        }


        String id =
                tableModel
                        .getValueAt(row, 0)
                        .toString();


        Patient patient =
                patientRegistry.get(id);


        if (patient != null) {

            idField.setText(
                    patient.getId()
            );

            nameField.setText(
                    patient.getName()
            );

            severityField.setText(
                    String.valueOf(
                            patient.getSeverity()
                    )
            );


            searchField.setText(
                    patient.getId()
            );
        }
    }


    // =========================================
    // HIGHLIGHT PATIENT
    // =========================================

    private void highlightPatient(
            String patientId
    ) {

        for (
                int i = 0;

                i < tableModel.getRowCount();

                i++
        ) {

            String id =
                    tableModel
                            .getValueAt(i, 0)
                            .toString();


            if (id.equals(patientId)) {

                patientTable.setRowSelectionInterval(
                        i,
                        i
                );

                break;
            }
        }
    }


    // =========================================
    // CLEAR
    // =========================================

    private void clearFields() {

        idField.setText("");

        nameField.setText("");

        severityField.setText("1");
    }


    // =========================================
    // HELPER METHODS
    // =========================================

    private void prepareTextField(
            JTextField field
    ) {

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        35
                )
        );
    }


    private EmptyBorder createCardBorder() {

        return new EmptyBorder(
                20,
                20,
                20,
                20
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