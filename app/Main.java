package app;

import models.*;
import services.*;
import structures.*;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Hospital Management System - Main Entry Point
 */
public class Main {

    private static final long studentID = 220315022;

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private static Doctor drSmith;
    private static Doctor drJohnson;
    private static Doctor drWilliams;

    private static HashTable<Doctor> doctorRegistry;


    public static void main(String[] args) {

        String studentName = "Elif Sude Yılmaz";

        printHeader("HOSPITAL MANAGEMENT SYSTEM");

        System.out.println("\n   Developer: " + studentName);
        System.out.println("   Student ID: " + studentID);
        System.out.println(
                "   System Time: "
                        + LocalDateTime.now().format(TIME_FORMAT)
        );


        int emergencyCapacity =
                (int) (studentID % 100) + 10;

        int uniqueSuffix =
                (int) (studentID % 100);


        System.out.println(
                "\n   SYSTEM CONFIGURATION (Based on Student ID):"
        );

        System.out.println(
                "   - Emergency Room Capacity: "
                        + emergencyCapacity
                        + " patients"
        );

        System.out.println(
                "   - Unique ID Suffix: "
                        + uniqueSuffix
        );


        System.out.println(
                "\n   Initializing hospital services with ADT implementations..."
        );


        RecordRegistry patientRegistry =
                new RecordRegistry(studentID);

        EmergencyTriage emergencySystem =
                new EmergencyTriage(emergencyCapacity);

        AppointmentScheduler appointmentSystem =
                new AppointmentScheduler();

        ActionHistoryManager historyManager =
                new ActionHistoryManager();

        AVLTree nameIndex =
                new AVLTree();


        // =========================
        // DOCTOR HASH TABLE
        // =========================

        doctorRegistry =
                new HashTable<>(50);


        System.out.println(
                "   Setting up medical departments and staff..."
        );


        // =========================
        // DEPARTMENTS
        // =========================

        Department cardiology =
                new Department(
                        "Cardiology",
                        "Heart Center - Floor 3"
                );


        Department neurology =
                new Department(
                        "Neurology",
                        "Brain & Nerve Center - Floor 4"
                );


        Department orthopedics =
                new Department(
                        "Orthopedics",
                        "Bone & Joint Center - Floor 2"
                );


        // =========================
        // DOCTORS
        // =========================

        drSmith =
                new Doctor(
                        "DOC-NEU-001",
                        "Dr. Michael Smith",
                        "Neurologist",
                        neurology
                );


        drJohnson =
                new Doctor(
                        "DOC-HRT-002",
                        "Dr. Sarah Johnson",
                        "Cardiologist",
                        cardiology
                );


        drWilliams =
                new Doctor(
                        "DOC-ORT-001",
                        "Dr. Robert Williams",
                        "Orthopedic Surgeon",
                        orthopedics
                );


        // =========================
        // ADD DOCTORS TO HASH TABLE
        // =========================

        doctorRegistry.put(
                drSmith.getId(),
                drSmith
        );

        doctorRegistry.put(
                drJohnson.getId(),
                drJohnson
        );

        doctorRegistry.put(
                drWilliams.getId(),
                drWilliams
        );


        // =========================
        // HOSPITAL TREE
        // =========================

        System.out.println(
                "   Building hospital organizational structure..."
        );


        Tree hospitalTree =
                new Tree(
                        "Hospital Organization Chart"
                );


        hospitalTree.addDepartment(
                neurology,
                drSmith
        );

        hospitalTree.addDepartment(
                cardiology,
                drJohnson
        );

        hospitalTree.addDepartment(
                orthopedics,
                drWilliams
        );

        hospitalTree.addEmergencyUnit(
                emergencyCapacity
        );


        Scanner scanner =
                new Scanner(System.in);


        printSeparator(60);

        System.out.println(
                "          SYSTEM READY - ALL ADTS ACTIVE AND INTEGRATED"
        );

        printSeparator(60);


        boolean systemActive = true;


        while (systemActive) {

            try {

                displayMainMenu();


                System.out.print(
                        "\n   Please enter your choice (0-16): "
                );


                int choice =
                        scanner.nextInt();

                scanner.nextLine();


                switch (choice) {

                    case 0:

                        exitSystem(
                                patientRegistry,
                                historyManager
                        );

                        systemActive = false;

                        break;


                    // =========================
                    // PATIENT MANAGEMENT
                    // =========================

                    case 1:

                        scheduleRegularAppointment(
                                scanner,
                                appointmentSystem,
                                patientRegistry,
                                nameIndex,
                                historyManager,
                                emergencySystem
                        );

                        break;


                    case 2:

                        admitEmergencyPatient(
                                scanner,
                                emergencySystem,
                                patientRegistry,
                                nameIndex,
                                historyManager,
                                studentID,
                                appointmentSystem
                        );

                        break;


                    case 3:

                        findPatientById(
                                scanner,
                                patientRegistry
                        );

                        break;


                    case 4:

                        findPatientByName(
                                scanner,
                                nameIndex
                        );

                        break;


                    case 5:

                        deletePatientRecord(
                                scanner,
                                patientRegistry,
                                historyManager,
                                emergencySystem,
                                appointmentSystem,
                                nameIndex
                        );

                        break;


                    // =========================
                    // DOCTOR MANAGEMENT
                    // =========================

                    case 6:

                        findDoctorById(scanner);

                        break;


                    case 7:

                        findDoctorByName(
                                scanner,
                                hospitalTree
                        );

                        break;


                    // =========================
                    // SYSTEM TOOLS
                    // =========================

                    case 8:

                        undoLastAction(
                                historyManager,
                                patientRegistry,
                                emergencySystem,
                                appointmentSystem,
                                nameIndex
                        );

                        break;


                    case 9:

                        viewHospitalStructure(
                                hospitalTree,
                                scanner
                        );

                        break;


                    case 10:

                        appointmentSystem.displayQueue();

                        break;


                    case 11:

                        appointmentSystem.displayAllAppointments();

                        break;


                    case 12:

                        viewEmergencyStatus(
                                emergencySystem
                        );

                        break;


                    // =========================
                    // MEDICAL RECORDS
                    // =========================

                    case 13:

                        addMedicalNote(
                                scanner,
                                patientRegistry,
                                historyManager,
                                emergencySystem,
                                appointmentSystem
                        );

                        break;


                    case 14:

                        manageDoctorOffice(
                                scanner,
                                historyManager,
                                patientRegistry,
                                emergencySystem,
                                appointmentSystem
                        );

                        break;


                    case 15:

                        treatNextEmergency(
                                emergencySystem,
                                scanner,
                                historyManager,
                                patientRegistry,
                                appointmentSystem
                        );

                        break;


                    case 16:

                        showSystemDashboard(
                                studentID,
                                patientRegistry,
                                appointmentSystem,
                                emergencySystem,
                                historyManager,
                                nameIndex,
                                hospitalTree
                        );

                        break;


                    default:

                        System.out.println(
                                "\n   [ALERT] Invalid option selected."
                        );

                        System.out.println(
                                "   Please choose a number between 0 and 16."
                        );
                }


                if (choice != 0) {

                    System.out.println(
                            "\n   Press Enter to continue..."
                    );

                    scanner.nextLine();
                }

            }

            catch (InputMismatchException e) {

                System.out.println(
                        "\n   [ERROR] Invalid input format detected."
                );

                System.out.println(
                        "   Please enter a valid numeric choice."
                );

                scanner.nextLine();
            }

            catch (Exception e) {

                System.out.println(
                        "\n   [SYSTEM ERROR] Unexpected error occurred."
                );

                System.out.println(
                        "   Error details: "
                                + e.getMessage()
                );

                System.out.println(
                        "   System has recovered and is ready to continue."
                );
            }
        }


        scanner.close();


        System.out.println(
                "\n   Thank you for using the Hospital Management System!"
        );

        System.out.println(
                "   Developed by: "
                        + studentName
                        + " (ID: "
                        + studentID
                        + ")"
        );
    }


    // =====================================================
    // UI & REPORTING METHODS
    // =====================================================

    private static void displayMainMenu() {

        System.out.println("\n");

        printHeader("HOSPITAL MAIN MENU");


        System.out.println("\n   PATIENT MANAGEMENT");

        System.out.println(
                "   1. Schedule Regular Appointment"
        );

        System.out.println(
                "   2. Admit Emergency Patient"
        );

        System.out.println(
                "   3. Find Patient by ID"
        );

        System.out.println(
                "   4. Find Patient by Name"
        );

        System.out.println(
                "   5. Delete Patient Record"
        );


        System.out.println("\n   DOCTOR MANAGEMENT");

        System.out.println(
                "   6. Find Doctor by ID"
        );

        System.out.println(
                "   7. Find Doctor by Name"
        );


        System.out.println("\n   SYSTEM TOOLS");

        System.out.println(
                "   8. Undo Last Action"
        );

        System.out.println(
                "   9. View Hospital Structure"
        );

        System.out.println(
                "   10. View Appointment Queue"
        );

        System.out.println(
                "   11. View All Scheduled Appointments"
        );

        System.out.println(
                "   12. View Emergency Room Status"
        );


        System.out.println(
                "\n   MEDICAL RECORDS & PROCESSING"
        );

        System.out.println(
                "   13. Add Medical Note"
        );

        System.out.println(
                "   14. Manage Doctor's Office"
        );

        System.out.println(
                "   15. Treat Next Emergency Case"
        );


        System.out.println(
                "\n   SYSTEM INFORMATION"
        );

        System.out.println(
                "   16. System Dashboard"
        );

        System.out.println(
                "   0. Exit System"
        );


        printSeparator(40);
    }


    private static void printHeader(String title) {

        System.out.println(
                "\n   " + "=".repeat(title.length() + 4)
        );

        System.out.println(
                "     " + title
        );

        System.out.println(
                "   " + "=".repeat(title.length() + 4)
        );
    }


    private static void printSeparator(int length) {

        System.out.println(
                "   " + "-".repeat(length)
        );
    }


    private static void exitSystem(
            RecordRegistry patientRegistry,
            ActionHistoryManager historyManager
    ) {

        System.out.println("\n");

        printSeparator(50);

        System.out.println(
                "   Thank you for using Hospital Management System"
        );

        System.out.println(
                "   System shutting down gracefully..."
        );

        System.out.println(
                "   Final Time: "
                        + LocalDateTime.now().format(TIME_FORMAT)
        );

        System.out.println(
                "   Final System Statistics:"
        );

        System.out.println(
                "   - Total Patients Registered: "
                        + patientRegistry.getPatientCount()
        );

        System.out.println(
                "   - Actions Recorded in History: "
                        + historyManager.getHistorySize()
        );

        System.out.println(
                "   - Hash Table Load Factor: "
                        + String.format(
                        "%.2f",
                        patientRegistry.getLoadFactor()
                )
        );

        printSeparator(50);
    }


    // =====================================================
    // DOCTOR MANAGEMENT
    // =====================================================

    private static void findDoctorById(
            Scanner scanner
    ) {

        System.out.println("\n");

        printHeader("DOCTOR SEARCH BY ID");


        System.out.print(
                "\n   Enter Doctor ID: "
        );

        String id =
                scanner.nextLine().trim();


        Doctor doc =
                doctorRegistry.get(id);


        if (doc != null) {

            System.out.println(
                    "\n   [DOCTOR FOUND - HashTable Lookup]"
            );

            System.out.println(
                    "   Name: " + doc.getName()
            );

            System.out.println(
                    "   ID: " + doc.getId()
            );

            System.out.println(
                    "   Specialty: " + doc.getSpecialty()
            );

            System.out.println(
                    "   Department: "
                            + doc.getDepartment().getName()
            );

            System.out.println(
                    "   Patients Waiting: "
                            + doc.getQueueSize()
            );

        } else {

            System.out.println(
                    "\n   [NOT FOUND] No doctor found with ID: "
                            + id
            );
        }
    }


    private static void findDoctorByName(
            Scanner scanner,
            Tree hospitalTree
    ) {

        System.out.println("\n");

        printHeader("DOCTOR SEARCH BY NAME");


        System.out.print(
                "\n   Enter Doctor Name: "
        );

        String name =
                scanner.nextLine().trim();


        Tree node =
                hospitalTree.findNode(name);


        if (
                node != null
                        && node.getAssociatedDoctor() != null
        ) {

            Doctor doc =
                    node.getAssociatedDoctor();


            System.out.println(
                    "\n   [DOCTOR FOUND - Tree Search]"
            );

            System.out.println(
                    "   Name: " + doc.getName()
            );

            System.out.println(
                    "   ID: " + doc.getId()
            );

            System.out.println(
                    "   Specialty: "
                            + doc.getSpecialty()
            );

            System.out.println(
                    "   Department: "
                            + doc.getDepartment().getName()
            );

            System.out.println(
                    "   Patients Waiting: "
                            + doc.getQueueSize()
            );

        } else {

            System.out.println(
                    "\n   [NOT FOUND] No doctor found with name: "
                            + name
            );
        }
    }


    // =====================================================
    // ADMINISTRATIVE WORKFLOWS
    // =====================================================

    private static void scheduleRegularAppointment(
            Scanner scanner,
            AppointmentScheduler appointmentSystem,
            RecordRegistry patientRegistry,
            AVLTree nameIndex,
            ActionHistoryManager historyManager,
            EmergencyTriage emergencySystem
    ) {

        System.out.println("\n");

        printHeader("SCHEDULE REGULAR APPOINTMENT");


        System.out.print(
                "\n   Patient's Full Name: "
        );

        String name =
                scanner.nextLine().trim();


        System.out.print(
                "   Patient ID: "
        );

        String id =
                scanner.nextLine().trim();


        System.out.print(
                "   Appointment Time (HH:mm, e.g., 14:30): "
        );

        String time =
                scanner.nextLine().trim();


        System.out.print(
                "   Appointment Date (YYYY-MM-DD): "
        );

        String date =
                scanner.nextLine().trim();


        System.out.println(
                "\n   Select Specialized Doctor:"
        );

        System.out.println(
                "   1. "
                        + drSmith.getName()
                        + " ("
                        + drSmith.getSpecialty()
                        + ")"
        );

        System.out.println(
                "   2. "
                        + drJohnson.getName()
                        + " ("
                        + drJohnson.getSpecialty()
                        + ")"
        );

        System.out.println(
                "   3. "
                        + drWilliams.getName()
                        + " ("
                        + drWilliams.getSpecialty()
                        + ")"
        );


        System.out.print(
                "   Choice (1-3): "
        );


        int doctorChoice =
                scanner.nextInt();

        scanner.nextLine();


        Doctor selectedDoctor = null;


        switch (doctorChoice) {

            case 1:
                selectedDoctor = drSmith;
                break;

            case 2:
                selectedDoctor = drJohnson;
                break;

            case 3:
                selectedDoctor = drWilliams;
                break;

            default:

                System.out.println(
                        "   [ERROR] Invalid doctor selection."
                );

                return;
        }


        historyManager.saveState(
                patientRegistry,
                emergencySystem,
                appointmentSystem,
                drSmith,
                drJohnson,
                drWilliams
        );


        Patient patient =
                new Patient(id, name);


        patientRegistry.add(patient);

        nameIndex.insert(patient);


        boolean booked =
                appointmentSystem.bookAppointmentWithTime(
                        patient,
                        selectedDoctor,
                        time,
                        date
                );


        if (booked) {

            selectedDoctor.addPatientToQueue(patient);

            historyManager.recordAction(
                    "Scheduled appointment for: "
                            + name
                            + " at "
                            + time
            );

            System.out.println(
                    "\n   [SUCCESS] Appointment scheduled with time slot!"
            );

        } else {

            appointmentSystem.bookAppointment(patient);

            selectedDoctor.addPatientToQueue(patient);

            historyManager.recordAction(
                    "Scheduled appointment for: "
                            + name
            );
        }
    }


    // =====================================================
    // EMERGENCY SYSTEM
    // =====================================================

    private static void admitEmergencyPatient(
            Scanner scanner,
            EmergencyTriage emergencySystem,
            RecordRegistry patientRegistry,
            AVLTree nameIndex,
            ActionHistoryManager historyManager,
            long studentID,
            AppointmentScheduler appointmentSystem
    ) {

        System.out.println("\n");

        printHeader("EMERGENCY ROOM ADMISSION");


        System.out.print(
                "\n   Patient's Full Name: "
        );

        String name =
                scanner.nextLine().trim();


        System.out.print(
                "   Emergency Severity (1-10, 10=Critical): "
        );

        int severity =
                scanner.nextInt();

        scanner.nextLine();


        if (severity < 1 || severity > 10) {

            severity = 5;
        }


        String emergencyId =
                "EMG-"
                        + System.currentTimeMillis() % 10000
                        + "-"
                        + (studentID % 100);


        Patient patient =
                new Patient(
                        emergencyId,
                        name,
                        severity
                );


        historyManager.saveState(
                patientRegistry,
                emergencySystem,
                appointmentSystem,
                drSmith,
                drJohnson,
                drWilliams
        );


        boolean admitted =
                emergencySystem.admit(patient);


        if (admitted) {

            patientRegistry.add(patient);

            nameIndex.insert(patient);

            patient.addHistory(
                    "Emergency admission - Severity: "
                            + severity
            );

            historyManager.recordAction(
                    "Admitted emergency patient: "
                            + name
                            + " (Severity: "
                            + severity
                            + ")"
            );

            System.out.println(
                    "\n   [SUCCESS] Emergency patient admitted to triage system!"
            );

        } else {

            System.out.println(
                    "\n   [ERROR] Emergency room at full capacity."
            );
        }
    }


    private static void deletePatientRecord(
            Scanner scanner,
            RecordRegistry patientRegistry,
            ActionHistoryManager historyManager,
            EmergencyTriage emergencySystem,
            AppointmentScheduler appointmentSystem,
            AVLTree nameIndex
    ) {

        System.out.println("\n");

        printHeader("DELETE PATIENT RECORD");


        System.out.print(
                "\n   Enter Patient ID to delete: "
        );

        String id =
                scanner.nextLine().trim();


        historyManager.saveState(
                patientRegistry,
                emergencySystem,
                appointmentSystem,
                drSmith,
                drJohnson,
                drWilliams
        );


        Patient patient =
                patientRegistry.get(id);


        if (patient != null) {

            patientRegistry.delete(id);

            nameIndex.removeByNameAndId(
                    patient.getName(),
                    id
            );


            if (id.startsWith("EMG-")) {

                emergencySystem.removePatient(id);
            }


            drSmith.getWaitingLine().removePatient(id);

            drJohnson.getWaitingLine().removePatient(id);

            drWilliams.getWaitingLine().removePatient(id);


            System.out.println(
                    "\n   [SUCCESS] Patient '"
                            + patient.getName()
                            + "' deleted from all systems."
            );


            historyManager.recordAction(
                    "Deleted patient: "
                            + id
            );

        } else {

            System.out.println(
                    "\n   [ERROR] Patient not found: "
                            + id
            );
        }
    }


    private static void findPatientById(
            Scanner scanner,
            RecordRegistry patientRegistry
    ) {

        System.out.println("\n");

        printHeader("PATIENT SEARCH BY ID");


        System.out.print(
                "\n   Enter Patient ID: "
        );

        String id =
                scanner.nextLine().trim();


        Patient patient =
                patientRegistry.get(id);


        if (patient != null) {

            System.out.println(
                    "\n   [PATIENT FOUND - HashTable Lookup]"
            );

            System.out.println(
                    "   Name: "
                            + patient.getName()
            );

            System.out.println(
                    "   ID: "
                            + patient.getId()
            );

            System.out.println(
                    "   Priority Level: "
                            + patient.getSeverity()
            );

        } else {

            System.out.println(
                    "\n   [NOT FOUND] No patient found with ID: "
                            + id
            );
        }
    }


    private static void findPatientByName(
            Scanner scanner,
            AVLTree nameIndex
    ) {

        System.out.println("\n");

        printHeader("PATIENT SEARCH BY NAME");


        System.out.print(
                "\n   Enter Patient Name: "
        );

        String name =
                scanner.nextLine().trim();


        List<Patient> patients =
                nameIndex.searchAllByName(name);


        if (!patients.isEmpty()) {

            System.out.println(
                    "\n   [PATIENT FOUND - AVL Tree Search]"
            );


            for (
                    int i = 0;
                    i < patients.size();
                    i++
            ) {

                Patient patient =
                        patients.get(i);


                System.out.println(
                        "   "
                                + (i + 1)
                                + ". Name: "
                                + patient.getName()
                                + " | ID: "
                                + patient.getId()
                );
            }

        } else {

            System.out.println(
                    "\n   [NOT FOUND] No patient found with name: "
                            + name
            );
        }
    }


    private static void undoLastAction(
            ActionHistoryManager historyManager,
            RecordRegistry patientRegistry,
            EmergencyTriage emergencySystem,
            AppointmentScheduler appointmentSystem,
            AVLTree nameIndex
    ) {

        System.out.println("\n");

        printHeader("UNDO LAST ACTION");


        boolean success =
                historyManager.undo(
                        patientRegistry,
                        emergencySystem,
                        appointmentSystem,
                        drSmith,
                        drJohnson,
                        drWilliams
                );


        if (success) {

            System.out.println(
                    "\n   [SUCCESS] Last action undone using Stack ADT!"
            );

        } else {

            System.out.println(
                    "\n   [INFO] No actions to undo. History stack is empty."
            );
        }
    }


    private static void viewHospitalStructure(
            Tree hospitalTree,
            Scanner scanner
    ) {

        System.out.println(
                "\n   1. Full Organizational View"
        );

        System.out.println(
                "   2. Search within Organization"
        );

        System.out.print(
                "   Choice: "
        );


        int choice =
                scanner.nextInt();

        scanner.nextLine();


        if (choice == 2) {

            System.out.print(
                    "   Enter Department or Doctor Name: "
            );

            String target =
                    scanner.nextLine().trim();


            if (hospitalTree.findNode(target) != null) {

                System.out.println(
                        "   [RESULT] '"
                                + target
                                + "' is active in the hierarchy."
                );

            } else {

                System.out.println(
                        "   [RESULT] Not found."
                );
            }

        } else {

            hospitalTree.displayStructure();
        }
    }


    private static void viewEmergencyStatus(
            EmergencyTriage emergencySystem
    ) {

        emergencySystem.display();
    }


    private static void addMedicalNote(
            Scanner scanner,
            RecordRegistry patientRegistry,
            ActionHistoryManager historyManager,
            EmergencyTriage emergencySystem,
            AppointmentScheduler appointmentSystem
    ) {

        System.out.println("\n");

        printHeader("ADD MEDICAL NOTE");


        System.out.print(
                "\n   Patient ID: "
        );

        String id =
                scanner.nextLine().trim();


        Patient patient =
                patientRegistry.get(id);


        if (patient != null) {

            System.out.print(
                    "   Medical Note: "
            );

            String note =
                    scanner.nextLine().trim();


            historyManager.saveState(
                    patientRegistry,
                    emergencySystem,
                    appointmentSystem,
                    drSmith,
                    drJohnson,
                    drWilliams
            );


            patient.addHistory(
                    "Note: " + note
            );


            historyManager.recordAction(
                    "Added medical note for: "
                            + patient.getName()
            );


            System.out.println(
                    "\n   [SUCCESS] Medical note added."
            );

        } else {

            System.out.println(
                    "\n   [ERROR] Patient not found."
            );
        }
    }


    private static void manageDoctorOffice(
            Scanner scanner,
            ActionHistoryManager historyManager,
            RecordRegistry patientRegistry,
            EmergencyTriage emergencySystem,
            AppointmentScheduler appointmentSystem
    ) {

        System.out.println(
                "\n   Select Doctor (1-3): "
        );

        System.out.println(
                "   1. Dr. Smith | 2. Dr. Johnson | 3. Dr. Williams"
        );


        int choice =
                scanner.nextInt();

        scanner.nextLine();


        Doctor selected =
                (choice == 2)
                        ? drJohnson
                        : (choice == 3)
                        ? drWilliams
                        : drSmith;


        selected.getWaitingLine().display();


        if (!selected.getWaitingLine().isEmpty()) {

            System.out.print(
                    "\n   See next patient? (y/n): "
            );


            if (
                    scanner.nextLine()
                            .trim()
                            .equalsIgnoreCase("y")
            ) {

                historyManager.saveState(
                        patientRegistry,
                        emergencySystem,
                        appointmentSystem,
                        drSmith,
                        drJohnson,
                        drWilliams
                );


                Patient next =
                        selected.seeNextPatient();


                next.addHistory(
                        "Examined by "
                                + selected.getName()
                );


                historyManager.recordAction(
                        selected.getName()
                                + " examined "
                                + next.getName()
                );


                System.out.println(
                        "\n   [ACTION] Examining: "
                                + next.getName()
                );
            }
        }
    }


    private static void treatNextEmergency(
            EmergencyTriage emergencySystem,
            Scanner scanner,
            ActionHistoryManager historyManager,
            RecordRegistry patientRegistry,
            AppointmentScheduler appointmentSystem
    ) {

        if (emergencySystem.hasPatients()) {

            historyManager.saveState(
                    patientRegistry,
                    emergencySystem,
                    appointmentSystem,
                    drSmith,
                    drJohnson,
                    drWilliams
            );


            Patient p =
                    emergencySystem.treatNext();


            p.addHistory(
                    "Emergency treatment completed."
            );


            historyManager.recordAction(
                    "Treated emergency: "
                            + p.getName()
            );


            System.out.println(
                    "\n   [TREATMENT] Completed for: "
                            + p.getName()
            );

        } else {

            System.out.println(
                    "   [INFO] ER is empty."
            );
        }
    }


    private static void showSystemDashboard(
            long studentID,
            RecordRegistry patientRegistry,
            AppointmentScheduler appointmentSystem,
            EmergencyTriage emergencySystem,
            ActionHistoryManager historyManager,
            AVLTree nameIndex,
            Tree hospitalTree
    ) {

        System.out.println("\n");

        printHeader(
                "SYSTEM DASHBOARD & ADT PERFORMANCE REPORT"
        );


        System.out.println(
                "\n   SYSTEM OVERVIEW"
        );

        System.out.println(
                "   - Student ID: "
                        + studentID
        );

        System.out.println(
                "   - Registered Patients: "
                        + patientRegistry.getPatientCount()
        );

        System.out.println(
                "   - ER Occupancy: "
                        + String.format(
                        "%.1f",
                        emergencySystem.getOccupancyPercentage()
                )
                        + "%"
        );

        System.out.println(
                "   - Hash Table Load Factor: "
                        + String.format(
                        "%.2f",
                        patientRegistry.getLoadFactor()
                )
        );

        System.out.println(
                "   - AVL Tree Height: "
                        + nameIndex.getHeight()
        );

        System.out.println(
                "   - Actions Stored (Stack): "
                        + historyManager.getHistorySize()
        );
    }
}