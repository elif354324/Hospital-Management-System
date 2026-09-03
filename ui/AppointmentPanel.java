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
                new JTextField("09:00");

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
                new JTextField("2026-09-03");

        prepareField(dateField);

        card.add(dateField);


        card.add(
                Box.createVerticalStrut(15)
        );


        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                10,
                                0
                        )
                );

        buttonPanel.setBackground(Color.WHITE);


        JButton scheduleButton =
                new JButton("Schedule");

        JButton clearButton =
                new JButton("Clear");


        scheduleButton.addActionListener(
                e -> scheduleAppointment()
        );


        clearButton.addActionListener(
                e -> clearFields()
        );


        buttonPanel.add(scheduleButton);

        buttonPanel.add(clearButton);


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

        String date =
                dateField.getText().trim();


        if (
                patientId.isEmpty()
                        || patientName.isEmpty()
                        || time.isEmpty()
                        || date.isEmpty()
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

                                date
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

                            appointment.getDate()
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

        timeField.setText("09:00");

        dateField.setText("2026-09-03");


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
}