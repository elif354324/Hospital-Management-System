package ui;

import app.HospitalData;
import models.Department;
import models.Doctor;
import structures.HashTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorPanel extends JPanel {

private JTextField idField;
private JTextField nameField;
private JTextField specialtyField;

private JComboBox<String> departmentBox;

private JTextField searchField;

private JTable doctorTable;
private DefaultTableModel tableModel;

private HashTable<Doctor> doctorRegistry;

public DoctorPanel() {

    doctorRegistry = HospitalData.getDoctorRegistry();

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
            new EmptyBorder(
                    25,
                    30,
                    15,
                    30
            )
    );

    JLabel title = new JLabel("Doctor Management");

    title.setFont(
            new Font(
                    "Arial",
                    Font.BOLD,
                    30
            )
    );

    JLabel subtitle =
            new JLabel(
                    "Manage hospital doctors and departments"
            );

    subtitle.setForeground(Color.GRAY);

    subtitle.setFont(
            new Font(
                    "Arial",
                    Font.PLAIN,
                    15
            )
    );

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
            createControlSection(),
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

// =========================================
// CONTROL SECTION
// =========================================

private JPanel createControlSection() {

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

    panel.add(createDoctorForm());
    panel.add(createSearchPanel());

    return panel;
}

// =========================================
// ADD / UPDATE / DELETE DOCTOR FORM
// =========================================

private JPanel createDoctorForm() {

    JPanel card = new JPanel();

    card.setLayout(
            new BoxLayout(
                    card,
                    BoxLayout.Y_AXIS
            )
    );

    card.setBackground(Color.WHITE);

    card.setBorder(
            BorderFactory.createCompoundBorder(

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
            )
    );

    JLabel title =
            new JLabel("Add New Doctor");

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

    // Doctor ID

    card.add(
            new JLabel("Doctor ID")
    );

    idField = new JTextField();

    prepareField(idField);

    card.add(idField);

    card.add(
            Box.createVerticalStrut(10)
    );

    // Doctor Name

    card.add(
            new JLabel("Doctor Name")
    );

    nameField = new JTextField();

    prepareField(nameField);

    card.add(nameField);

    card.add(
            Box.createVerticalStrut(10)
    );

    // Specialty

    card.add(
            new JLabel("Specialty")
    );

    specialtyField = new JTextField();

    prepareField(specialtyField);

    card.add(specialtyField);

    card.add(
            Box.createVerticalStrut(10)
    );

    // Department

    card.add(
            new JLabel("Department")
    );

    departmentBox = new JComboBox<>();

    loadDepartments();

    departmentBox.setMaximumSize(
            new Dimension(
                    Integer.MAX_VALUE,
                    35
            )
    );

    card.add(departmentBox);

    card.add(
            Box.createVerticalStrut(20)
    );

    JPanel buttonPanel =
            new JPanel(
                    new GridLayout(
                            2,
                            2,
                            10,
                            10
                    )
            );

    buttonPanel.setBackground(Color.WHITE);

    JButton addButton =
            new JButton("Add Doctor");

    JButton updateButton =
            new JButton("Update Doctor");

    JButton deleteButton =
            new JButton("Delete Doctor");

    JButton clearButton =
            new JButton("Clear");

    addButton.addActionListener(
            e -> addDoctor()
    );

    updateButton.addActionListener(
            e -> updateDoctor()
    );

    deleteButton.addActionListener(
            e -> deleteDoctor()
    );

    clearButton.addActionListener(
            e -> clearFields()
    );

    buttonPanel.add(addButton);
    buttonPanel.add(updateButton);
    buttonPanel.add(deleteButton);
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
            new BoxLayout(
                    card,
                    BoxLayout.Y_AXIS
            )
    );

    card.setBackground(Color.WHITE);

    card.setBorder(
            BorderFactory.createCompoundBorder(

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
            )
    );

    JLabel title =
            new JLabel("Search Doctor");

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
            new JLabel("Doctor ID")
    );

    searchField = new JTextField();

    prepareField(searchField);

    card.add(searchField);

    card.add(
            Box.createVerticalStrut(20)
    );

    JButton searchButton =
            new JButton("Search Doctor");

    JButton refreshButton =
            new JButton("Refresh List");

    searchButton.setMaximumSize(
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
            e -> searchDoctor()
    );

    refreshButton.addActionListener(
            e -> refreshTable()
    );

    card.add(searchButton);

    card.add(
            Box.createVerticalStrut(10)
    );

    card.add(refreshButton);

    return card;
}

// =========================================
// DOCTOR TABLE
// =========================================

private JScrollPane createTableSection() {

    String[] columns = {

            "Doctor ID",
            "Doctor Name",
            "Specialty",
            "Department",
            "Patients Waiting"
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

    doctorTable =
            new JTable(tableModel);

    doctorTable.setRowHeight(32);

    doctorTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
    );

    doctorTable.getTableHeader()
            .setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            14
                    )
            );

    doctorTable.getSelectionModel()
            .addListSelectionListener(e -> {

                if (!e.getValueIsAdjusting()) {

                    loadSelectedDoctor();
                }
            });

    JScrollPane scrollPane =
            new JScrollPane(
                    doctorTable
            );

    scrollPane.setBorder(
            BorderFactory.createTitledBorder(
                    "Hospital Doctors"
            )
    );

    return scrollPane;
}

// =========================================
// LOAD DEPARTMENTS
// =========================================

private void loadDepartments() {

    List<Department> departments =
            HospitalData.getDepartments();

    for (Department department : departments) {

        if (department != null) {

            departmentBox.addItem(
                    department.getName()
            );
        }
    }
}

// =========================================
// ADD DOCTOR
// =========================================

private void addDoctor() {

    String id =
            idField.getText().trim();

    String name =
            nameField.getText().trim();

    String specialty =
            specialtyField.getText().trim();

    String departmentName =
            (String) departmentBox.getSelectedItem();

    if (
            id.isEmpty()
                    || name.isEmpty()
                    || specialty.isEmpty()
    ) {

        showMessage(
                "Doctor ID, Name, and Specialty are required.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    if (departmentName == null
            || departmentName.trim().isEmpty()) {

        showMessage(
                "Please select a department.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    Doctor existingDoctor =
            doctorRegistry.get(id);

    if (existingDoctor != null) {

        showMessage(
                "A doctor with this ID already exists.",
                "Duplicate Doctor",
                JOptionPane.ERROR_MESSAGE
        );

        return;
    }

    Department selectedDepartment =
            findDepartment(departmentName);

    if (selectedDepartment == null) {

        showMessage(
                "Department not found.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        return;
    }

    Doctor doctor =
            new Doctor(
                    id,
                    name,
                    specialty,
                    selectedDepartment
            );

    doctorRegistry.put(
            doctor.getId(),
            doctor
    );

    boolean success =
            doctorRegistry.get(
                    doctor.getId()
            ) != null;

    if (success) {

        refreshTable();

        clearFields();

        showMessage(
                "Doctor added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

    } else {

        showMessage(
                "Could not add doctor.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

// =========================================
// SEARCH DOCTOR
// =========================================

private void searchDoctor() {

    String id =
            searchField.getText().trim();

    if (id.isEmpty()) {

        showMessage(
                "Please enter a Doctor ID.",
                "Search",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    Doctor doctor =
            doctorRegistry.get(id);

    if (doctor == null) {

        showMessage(
                "Doctor not found.",
                "Search Result",
                JOptionPane.INFORMATION_MESSAGE
        );

        return;
    }

    idField.setText(
            doctor.getId()
    );

    nameField.setText(
            doctor.getName()
    );

    specialtyField.setText(
            doctor.getSpecialty()
    );

    if (doctor.getDepartment() != null) {

        departmentBox.setSelectedItem(
                doctor.getDepartment().getName()
        );
    }

    highlightDoctor(
            doctor.getId()
    );

    String departmentName =
            doctor.getDepartment() != null
                    ? doctor.getDepartment().getName()
                    : "Not assigned";

    showMessage(
            "Doctor found!\n\n"
                    + "Name: "
                    + doctor.getName()
                    + "\nID: "
                    + doctor.getId()
                    + "\nSpecialty: "
                    + doctor.getSpecialty()
                    + "\nDepartment: "
                    + departmentName
                    + "\nPatients Waiting: "
                    + doctor.getQueueSize(),

            "Doctor Details",

            JOptionPane.INFORMATION_MESSAGE
    );
}

// =========================================
// REFRESH TABLE
// =========================================

private void refreshTable() {

    if (tableModel == null) {
        return;
    }

    tableModel.setRowCount(0);

    List<Doctor> doctors =
            doctorRegistry.getAllValues();

    if (doctors == null) {
        return;
    }

    for (Doctor doctor : doctors) {

        if (doctor == null) {
            continue;
        }

        String departmentName =
                doctor.getDepartment() != null
                        ? doctor.getDepartment().getName()
                        : "Not assigned";

        tableModel.addRow(
                new Object[]{

                        doctor.getId(),

                        doctor.getName(),

                        doctor.getSpecialty(),

                        departmentName,

                        doctor.getQueueSize()
                }
        );
    }
}

// =========================================
// SELECT DOCTOR
// =========================================

private void loadSelectedDoctor() {

    int row =
            doctorTable.getSelectedRow();

    if (row == -1) {
        return;
    }

    int modelRow =
            doctorTable.convertRowIndexToModel(row);

    String id =
            tableModel
                    .getValueAt(
                            modelRow,
                            0
                    )
                    .toString();

    Doctor doctor =
            doctorRegistry.get(id);

    if (doctor == null) {
        return;
    }

    idField.setText(
            doctor.getId()
    );

    nameField.setText(
            doctor.getName()
    );

    specialtyField.setText(
            doctor.getSpecialty()
    );

    if (doctor.getDepartment() != null) {

        departmentBox.setSelectedItem(
                doctor.getDepartment().getName()
        );
    }

    searchField.setText(
            doctor.getId()
    );
}

// =========================================
// FIND DEPARTMENT
// =========================================

private Department findDepartment(
        String name
) {

    if (name == null) {
        return null;
    }

    for (
            Department department :
            HospitalData.getDepartments()
    ) {

        if (
                department != null
                        && department.getName() != null
                        && department.getName()
                        .equalsIgnoreCase(name)
        ) {

            return department;
        }
    }

    return null;
}

// =========================================
// HIGHLIGHT DOCTOR
// =========================================

private void highlightDoctor(
        String doctorId
) {

    if (doctorId == null) {
        return;
    }

    for (
            int i = 0;
            i < tableModel.getRowCount();
            i++
    ) {

        Object value =
                tableModel.getValueAt(i, 0);

        if (value != null
                && value.toString()
                .equals(doctorId)) {

            doctorTable.setRowSelectionInterval(
                    i,
                    i
            );

            doctorTable.scrollRectToVisible(
                    doctorTable
                            .getCellRect(
                                    i,
                                    0,
                                    true
                            )
            );

            break;
        }
    }
}

// =========================================
// UPDATE DOCTOR
// =========================================

private void updateDoctor() {

    String id =
            idField.getText().trim();

    String name =
            nameField.getText().trim();

    String specialty =
            specialtyField.getText().trim();

    String departmentName =
            (String) departmentBox.getSelectedItem();

    // Check Doctor ID

    if (id.isEmpty()) {

        showMessage(
                "Please select or search for a doctor first.",
                "Update Doctor",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    // Check required fields

    if (
            name.isEmpty()
                    || specialty.isEmpty()
    ) {

        showMessage(
                "Doctor Name and Specialty are required.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    // Check department

    if (departmentName == null
            || departmentName.trim().isEmpty()) {

        showMessage(
                "Please select a department.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    // Find doctor

    Doctor doctor =
            doctorRegistry.get(id);

    if (doctor == null) {

        showMessage(
                "Doctor not found.",
                "Update Doctor",
                JOptionPane.ERROR_MESSAGE
        );

        return;
    }

    // Find selected department

    Department selectedDepartment =
            findDepartment(departmentName);

    if (selectedDepartment == null) {

        showMessage(
                "Department not found.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        return;
    }

    // Update doctor information

    doctor.setName(name);

    doctor.setSpecialty(specialty);

    doctor.setDepartment(
            selectedDepartment
    );

    refreshTable();

    highlightDoctor(id);

    showMessage(
            "Doctor information updated successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE
    );
}

// =========================================
// DELETE DOCTOR
// =========================================

private void deleteDoctor() {

    String id =
            idField.getText().trim();

    if (id.isEmpty()) {

        showMessage(
                "Please select or search for a doctor first.",
                "Delete Doctor",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    Doctor doctor =
            doctorRegistry.get(id);

    if (doctor == null) {

        showMessage(
                "Doctor not found.",
                "Delete Doctor",
                JOptionPane.ERROR_MESSAGE
        );

        return;
    }

    String departmentName =
            doctor.getDepartment() != null
                    ? doctor.getDepartment().getName()
                    : "Not assigned";

    int confirmation =
            JOptionPane.showConfirmDialog(
                    this,

                    "Are you sure you want to remove this doctor?\n\n"
                            + "Doctor: "
                            + doctor.getName()
                            + "\nID: "
                            + doctor.getId()
                            + "\nDepartment: "
                            + departmentName,

                    "Confirm Doctor Removal",

                    JOptionPane.YES_NO_OPTION,

                    JOptionPane.WARNING_MESSAGE
            );

    if (
            confirmation != JOptionPane.YES_OPTION
    ) {

        return;
    }

    Doctor removed =
            doctorRegistry.remove(id);

    if (removed != null) {

        if (removed.getDepartment() != null) {

            removed.getDepartment()
                    .removeDoctor();
        }

        refreshTable();

        clearFields();

        searchField.setText("");

        showMessage(
                "Doctor removed successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

    } else {

        showMessage(
                "Could not remove doctor.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

// =========================================
// CLEAR
// =========================================

private void clearFields() {

    idField.setText("");

    nameField.setText("");

    specialtyField.setText("");

    searchField.setText("");

    doctorTable.clearSelection();

    if (
            departmentBox.getItemCount() > 0
    ) {

        departmentBox.setSelectedIndex(0);
    }
}

// =========================================
// HELPERS
// =========================================

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

// =========================================
// PUBLIC REFRESH
// =========================================

public void refresh() {

    refreshTable();
}


}
