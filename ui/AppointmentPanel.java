package ui;

import javax.swing.border.CompoundBorder;
import app.HospitalData;
import models.Appointment;
import models.Doctor;
import models.Patient;
import services.AppointmentScheduler;
import services.RecordRegistry;
import structures.HashTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentPanel extends JPanel {

    private JTextField patientIdField;
    private JTextField patientNameField;

    private JComboBox<String> doctorBox;

    private JTextField timeField;
    private JTextField dateField;

    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    private JLabel queueLabel;
    private JLabel nextPatientLabel;

    private AppointmentScheduler appointmentScheduler;
    private RecordRegistry patientRegistry;
    private HashTable<Doctor> doctorRegistry;


    public AppointmentPanel() {

        appointmentScheduler =
                HospitalData.getAppointmentScheduler();

        patientRegistry =
                HospitalData.getPatientRegistry();

        doctorRegistry =
                HospitalData.getDoctorRegistry();


        setLayout(new BorderLayout());

        setBackground(Color.WHITE);


        createHeader();

        createMainContent();

        refreshTable();

        refreshQueueInformation();
    }


    // =====================================
    // HEADER
    // =====================================

    private void createHeader() {

        JPanel header = new JPanel();

        header.setLayout(new BorderLayout());

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
                new JLabel("Appointment Management");

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );


        JLabel subtitle =
                new JLabel(
                        "Schedule and manage patient appointments"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        subtitle.setForeground(Color.GRAY);


        JPanel textPanel = new JPanel();

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


    // =====================================
    // MAIN CONTENT
    // =====================================

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
                createTableSection(),
                BorderLayout.CENTER
        );


        add(
                content,
                BorderLayout.CENTER
        );
    }


    // =====================================
    // TOP SECTION
    // =====================================

    private JPanel createTopSection() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
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
                createAppointmentForm()
        );


        panel.add(
                createQueuePanel()
        );


        return panel;
    }


    // =====================================
    // APPOINTMENT FORM
    // =====================================

    private JPanel createAppointmentForm() {

        JPanel card = new JPanel();

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
                new JLabel("Schedule Appointment");

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );


        card.add(title);

        card.add(
                Box.createVerticalStrut(15)
        );


        // PATIENT ID

        card.add(
                new JLabel("Patient ID")
        );

        patientIdField =
                new JTextField();

        prepareField(patientIdField);

        card.add(patientIdField);


        card.add(
                Box.createVerticalStrut(8)
        );


        // PATIENT NAME

        card.add(
                new JLabel("Patient Name")
        );

        patientNameField =
                new JTextField();

        prepareField(patientNameField);

        card.add(patientNameField);


        card.add(
                Box.createVerticalStrut(8)
        );


        // DOCTOR

        card.add(
                new JLabel("Select Doctor")
        );

        doctorBox =
                new JComboBox<>();

        loadDoctors();

        doctorBox.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        35
                )
        );

        card.add(doctorBox);


        card.add(
                Box.createVerticalStrut(8)
        );


        // TIME

        card.add(
                new JLabel("Appointment Time (HH:mm)")
        );

        timeField =
                new JTextField();

        prepareField(timeField);

        card.add(timeField);


        card.add(
                Box.createVerticalStrut(8)
        );


        // DATE

        card.add(
                new JLabel("Appointment Date (YYYY-MM-DD)")
        );

        dateField =
                new JTextField();

        prepareField(dateField);

        card.add(dateField);


        card.add(
                Box.createVerticalStrut(15)
        );


        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                10,
                                0
                        )
                );

        buttonPanel.setBackground(Color.WHITE);


        JButton scheduleButton =
                new JButton("Schedule");

        JButton cancelButton =
                new JButton("Cancel");

        JButton clearButton =
                new JButton("Clear");

        JButton updateButton =
        new JButton("Update");


        scheduleButton.addActionListener(
                e -> scheduleAppointment()
        );

        cancelButton.addActionListener(
                e -> cancelAppointment()
        );

        updateButton.addActionListener(
                e -> updateAppointment()
        );


        clearButton.addActionListener(
                e -> clearFields()
        );


        buttonPanel.add(scheduleButton);

        buttonPanel.add(cancelButton);

        buttonPanel.add(clearButton);

        buttonPanel.add(updateButton);

        card.add(buttonPanel);


        return card;
    }


    // =====================================
    // QUEUE PANEL
    // =====================================

    private JPanel createQueuePanel() {

        JPanel card = new JPanel();

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
                new JLabel("Appointment Queue");

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


        queueLabel =
                new JLabel(
                        "Patients Waiting: 0"
                );

        queueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );


        nextPatientLabel =
                new JLabel(
                        "Next Patient: None"
                );

        nextPatientLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );


        card.add(queueLabel);

        card.add(
                Box.createVerticalStrut(15)
        );

        card.add(nextPatientLabel);


        card.add(
                Box.createVerticalStrut(30)
        );


        JButton callNextButton =
                new JButton("Call Next Patient");


        callNextButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        45
                )
        );


        callNextButton.addActionListener(
                e -> callNextPatient()
        );


        card.add(callNextButton);


        card.add(
                Box.createVerticalStrut(10)
        );


        JButton refreshButton =
                new JButton("Refresh Queue");


        refreshButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );


        refreshButton.addActionListener(
                e -> {

                    refreshTable();

                    refreshQueueInformation();
                }
        );


        card.add(refreshButton);


        return card;
    }


    // =====================================
    // TABLE
    // =====================================

    private JScrollPane createTableSection() {

        String[] columns = {

                "Patient ID",

                "Patient Name",

                "Doctor",

                "Department",

                "Time",

                "Date"
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


        appointmentTable =
                new JTable(tableModel);

        appointmentTable.getSelectionModel().addListSelectionListener(
        e -> {

            if (e.getValueIsAdjusting()) {
                return;
            }

            int selectedRow =
                    appointmentTable.getSelectedRow();

            if (selectedRow == -1) {
                return;
            }

            String patientId =
                    tableModel.getValueAt(
                            selectedRow,
                            0
                    ).toString();

            String patientName =
                    tableModel.getValueAt(
                            selectedRow,
                            1
                    ).toString();

            String doctorName =
                    tableModel.getValueAt(
                            selectedRow,
                            2
                    ).toString();

            String time =
                    tableModel.getValueAt(
                            selectedRow,
                            4
                    ).toString();

            String date =
                    tableModel.getValueAt(
                            selectedRow,
                            5
                    ).toString();

            patientIdField.setText(patientId);
            patientNameField.setText(patientName);
            timeField.setText(time);
            dateField.setText(date);

            for (int i = 0; i < doctorBox.getItemCount(); i++) {

                String doctorItem =
                        doctorBox.getItemAt(i);

                if (doctorItem.contains(doctorName)) {
                    doctorBox.setSelectedIndex(i);
                    break;
                }
            }
        }
);    


        appointmentTable.setRowHeight(32);


        appointmentTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                14
                        )
                );


        JScrollPane scrollPane =
                new JScrollPane(
                        appointmentTable
                );


        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Scheduled Appointments"
                )
        );


        return scrollPane;
    }


    // =====================================
    // LOAD DOCTORS
    // =====================================

    private void loadDoctors() {

        List<Doctor> doctors =
                doctorRegistry.getAllValues();


        for (Doctor doctor : doctors) {

            doctorBox.addItem(
                    doctor.getId()
                            + " | "
                            + doctor.getName()
            );
        }
    }


    // =====================================
    // SCHEDULE APPOINTMENT
    // =====================================

    private void scheduleAppointment() {

        String patientId =
                patientIdField.getText().trim();

        String patientName =
                patientNameField.getText().trim();

        String time =
                timeField.getText().trim();

        String newDate =
                dateField.getText().trim();

        if (newDate.isEmpty()) {

    JOptionPane.showMessageDialog(
            this,
            "Please enter an appointment date.",
            "Missing Date",
            JOptionPane.WARNING_MESSAGE
    );

    return;
}


try {

    java.time.LocalDate parsedDate =
            java.time.LocalDate.parse(
                    newDate,
                    java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")
            );

    newDate =
            parsedDate.toString();

} catch (java.time.format.DateTimeParseException e) {

    JOptionPane.showMessageDialog(
            this,
            "Invalid date format.\nPlease use: gg.aa.yyyy\nExample: 03.09.2026",
            "Invalid Date",
            JOptionPane.ERROR_MESSAGE
    );

    return;
}

        try {

    java.time.LocalDate parsedDate =
            java.time.LocalDate.parse(
                    newDate,
                    java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")
            );

    newDate =
            parsedDate.toString();

} catch (java.time.format.DateTimeParseException e) {

    JOptionPane.showMessageDialog(
            this,
            "Invalid date format.\nPlease use: gg.aa.yyyy\nExample: 03.09.2026",
            "Invalid Date",
            JOptionPane.ERROR_MESSAGE
    );

    return;
}

        if (time.isEmpty()) {

    JOptionPane.showMessageDialog(
            this,
            "Please enter an appointment time.",
            "Missing Time",
            JOptionPane.WARNING_MESSAGE
    );

    return;
}


if (newDate.isEmpty()) {

    JOptionPane.showMessageDialog(
            this,
            "Please enter an appointment date.",
            "Missing Date",
            JOptionPane.WARNING_MESSAGE
    );

    return;
}

        try {

    java.time.LocalDate parsedDate =
            java.time.LocalDate.parse(
                    newDate,
                    java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")
            );

    newDate =
            parsedDate.toString();

} catch (java.time.format.DateTimeParseException e) {

    JOptionPane.showMessageDialog(
            this,
            "Please enter an appointment date.",
            "Missing Date",
            JOptionPane.ERROR_MESSAGE
    );

    return;
}


        if (
                patientId.isEmpty()
                        || patientName.isEmpty()
                        || time.isEmpty()
                        || newDate.isEmpty()
        ) {

            showMessage(
                    "Please fill in all appointment information.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        if (
                doctorBox.getSelectedItem()
                        == null
        ) {

            showMessage(
                    "Please select a doctor.",
                    "Doctor Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        Doctor selectedDoctor =
                getSelectedDoctor();


        if (selectedDoctor == null) {

            showMessage(
                    "Selected doctor could not be found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // Eğer hasta zaten varsa onu kullan

        Patient patient =
                patientRegistry.get(patientId);


        // Yoksa yeni hasta oluştur

        if (patient == null) {

            patient =
                    new Patient(
                            patientId,
                            patientName
                    );


            patientRegistry.add(patient);
        }


        // Aynı hastanın tekrar randevusu var mı?

        if (
                appointmentScheduler
                        .hasAppointment(patientId)
        ) {

            showMessage(
                    "This patient already has a scheduled appointment.",
                    "Duplicate Appointment",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        boolean success =
                appointmentScheduler
                        .bookAppointmentWithTime(

                                patient,

                                selectedDoctor,

                                time,

                                newDate
                        );


        if (success) {

            // Doktorun kendi FIFO kuyruğuna ekle

            selectedDoctor.addPatientToQueue(
                    patient
            );


            refreshTable();

            refreshQueueInformation();

            clearFields();


            showMessage(
                    "Appointment scheduled successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        }

        else {

            showMessage(
                    "Invalid time format.\n\n"
                            + "Please use HH:mm\n"
                            + "Example: 14:30",

                    "Invalid Time",

                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

        private void cancelAppointment() {

        int selectedRow =
                appointmentTable.getSelectedRow();

        if (selectedRow == -1) {

                showMessage(
                        "Please select an appointment from the table first.",
                        "Cancel Appointment",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
        }

        String patientId =
                tableModel.getValueAt(
                        selectedRow,
                        0
                ).toString();

        String patientName =
                tableModel.getValueAt(
                        selectedRow,
                        1
                ).toString();

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,

                        "Are you sure you want to cancel this appointment?\n\n"
                                + "Patient: "
                                + patientName
                                + "\nID: "
                                + patientId,

                        "Confirm Cancellation",

                        JOptionPane.YES_NO_OPTION,

                        JOptionPane.WARNING_MESSAGE
                );

        if (confirmation != JOptionPane.YES_OPTION) {
                return;
        }

        boolean removed =
                appointmentScheduler
                        .cancelAppointment(patientId);

        if (removed) {

                refreshTable();

                refreshQueueInformation();

                clearFields();

                showMessage(
                        "Appointment cancelled successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

        } else {

                showMessage(
                        "Appointment could not be cancelled.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
        }
        }

        private void updateAppointment() {

    int selectedRow =
            appointmentTable.getSelectedRow();

    if (selectedRow == -1) {
        showMessage(
                "Please select an appointment from the table first.",
                "Update Appointment",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    String patientId =
            tableModel.getValueAt(
                    selectedRow,
                    0
            ).toString();

    String patientName =
            tableModel.getValueAt(
                    selectedRow,
                    1
            ).toString();

        String selectedDoctorText =
                (String) doctorBox.getSelectedItem();

        if (selectedDoctorText == null || selectedDoctorText.trim().isEmpty()) {
        showMessage(
                "Please select a doctor.",
                "Update Appointment",
                JOptionPane.WARNING_MESSAGE
        );
        return;
        }

        String selectedDoctorId =
                selectedDoctorText.split("\\|")[0].trim();

        Doctor selectedDoctor =
                HospitalData.getDoctorRegistry().get(selectedDoctorId);

    String time =
            timeField.getText().trim();

    String date =
            dateField.getText().trim();

    if (time.isEmpty() || date.isEmpty()) {
        showMessage(
                "Please enter appointment time and date.",
                "Update Appointment",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    int confirmation =
            JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to update this appointment?\n\n"
                            + "Patient: "
                            + patientName
                            + "\nDoctor: "
                            + selectedDoctor.getName()
                            + "\nTime: "
                            + time
                            + "\nDate: "
                            + date,
                    "Confirm Update",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

    if (confirmation != JOptionPane.YES_OPTION) {
        return;
    }

    boolean updated =
            appointmentScheduler.updateAppointment(
                    patientId,
                    selectedDoctor,
                    time,
                    date
            );

    if (updated) {

        refreshTable();
        refreshQueueInformation();
        clearFields();

        showMessage(
                "Appointment updated successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

    } else {

        showMessage(
                "Appointment could not be updated.\n"
                        + "Please check the time format (HH:mm).",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}


    // =====================================
    // CALL NEXT PATIENT
    // =====================================

    private void callNextPatient() {

        Patient nextPatient =
                appointmentScheduler.callNextPatient();


        if (nextPatient == null) {

            showMessage(
                    "There are no patients waiting.",
                    "Queue Empty",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }


        showMessage(
                "Calling next patient:\n\n"

                        + nextPatient.getName()

                        + "\nID: "
                        + nextPatient.getId(),

                "Next Patient",

                JOptionPane.INFORMATION_MESSAGE
        );


        refreshTable();

        refreshQueueInformation();
    }


    // =====================================
    // GET SELECTED DOCTOR
    // =====================================

    private Doctor getSelectedDoctor() {

        String selected =
                (String)
                        doctorBox.getSelectedItem();


        if (selected == null) {

            return null;
        }


        String doctorId =
                selected.split("\\|")[0].trim();


        return doctorRegistry.get(
                doctorId
        );
    }


    // =====================================
    // REFRESH TABLE
    // =====================================

    private void refreshTable() {

        tableModel.setRowCount(0);


        List<Appointment> appointments =
                appointmentScheduler
                        .getAllAppointments();


        for (
                Appointment appointment :
                appointments
        ) {

            Patient patient =
                    appointment.getPatient();

            Doctor doctor =
                    appointment.getDoctor();


            tableModel.addRow(

                    new Object[]{

                            patient.getId(),

                            patient.getName(),

                            doctor.getName(),

                            doctor.getDepartment().getName(),

                            appointment.getTime(),

                            formatDateForDisplay(
        appointment.getDate()
            )
                    }
            );
        }
    }


    // =====================================
    // REFRESH QUEUE
    // =====================================

    private void refreshQueueInformation() {

        int queueSize =
                appointmentScheduler.getQueueSize();


        queueLabel.setText(
                "Patients Waiting: "
                        + queueSize
        );


        Patient nextPatient =
                appointmentScheduler
                        .peekNextPatient();


        if (nextPatient == null) {

            nextPatientLabel.setText(
                    "Next Patient: None"
            );

        }

        else {

            nextPatientLabel.setText(
                    "Next Patient: "
                            + nextPatient.getName()
            );
        }
    }


    // =====================================
    // CLEAR
    // =====================================

    private void clearFields() {

        patientIdField.setText("");

        patientNameField.setText("");

        timeField.setText("");

        dateField.setText("");


        if (
                doctorBox.getItemCount() > 0
        ) {

            doctorBox.setSelectedIndex(0);
        }
    }


    // =====================================
    // HELPERS
    // =====================================

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


    private CompoundBorder createCardBorder() {

        return new CompoundBorder(

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

    private String formatDateForDisplay(String date) {

    if (date == null || date.trim().isEmpty()) {
        return "";
    }

    try {

        java.time.LocalDate parsedDate =
                java.time.LocalDate.parse(date);

        return parsedDate.format(
                java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")
        );

    } catch (java.time.format.DateTimeParseException e) {

        return date;
    }
}

}