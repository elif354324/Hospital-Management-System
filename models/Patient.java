package models;

import structures.LinkedList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Patient class represents a hospital patient
 * Uses MyLinkedList for medical history records
 *
 * ADT Usage: MyLinkedList for medical history
 * Time Complexity: O(1) for addLast, O(n) for traversal
 */
public class Patient {

    private String id;
    private String name;
    private int age;
    private String gender;
    private String phone;
    private int severity; // 1-10 scale, 10 = most critical

    private LinkedList<String> medicalHistory;

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Creates a standard patient with default values.
     */
    public Patient(String id, String name) {
        this.id = id;
        this.name = name;
        this.age = 0;
        this.gender = "";
        this.phone = "";
        this.severity = 1;

        this.medicalHistory = new LinkedList<>();
        this.addHistory("PATIENT REGISTERED", true);
    }

    /**
     * Creates a patient with custom severity.
     */
    public Patient(String id, String name, int severity) {
        this.id = id;
        this.name = name;
        this.age = 0;
        this.gender = "";
        this.phone = "";
        this.severity = Math.max(1, Math.min(10, severity));

        this.medicalHistory = new LinkedList<>();
        this.addHistory(
                "EMERGENCY ADMISSION - Severity: " + severity,
                true
        );
    }

    /**
     * Creates a complete patient.
     */
    public Patient(
            String id,
            String name,
            int age,
            String gender,
            String phone,
            int severity) {

        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.severity = Math.max(1, Math.min(10, severity));

        this.medicalHistory = new LinkedList<>();
        this.addHistory("PATIENT REGISTERED", true);
    }

    // ==================== GETTERS ====================

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public int getSeverity() {
        return severity;
    }

    public LinkedList<String> getMedicalHistory() {
        return medicalHistory;
    }

    // ==================== SETTERS ====================

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        this.addHistory(
                "NAME UPDATED: " + oldName + " → " + name,
                false
        );
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSeverity(int severity) {
        int oldSeverity = this.severity;

        this.severity = Math.max(1, Math.min(10, severity));

        this.addHistory(
                "SEVERITY UPDATED: " + oldSeverity +
                " → " + this.severity,
                false
        );
    }

    // ==================== MEDICAL HISTORY ====================

    public void addHistory(String record, boolean isAuto) {

        String prefix = isAuto
                ? "[SYSTEM] "
                : "[MANUAL] ";

        String timestampedRecord =
                prefix + record + " [" +
                LocalDateTime.now().format(TIMESTAMP_FORMAT) +
                "]";

        medicalHistory.addLast(timestampedRecord);

        if (!isAuto) {
            System.out.println(
                    "   [HISTORY] Added to " +
                    name +
                    "'s record: " +
                    record
            );
        }
    }

    public void addHistory(String record) {
        addHistory(record, false);
    }

    public void addDoctorConsultation(
            String doctorName,
            String notes) {

        String record =
                "CONSULTATION with Dr. " +
                doctorName +
                " - " +
                notes;

        addHistory(record, true);
    }

    public void addEmergencyTreatment(
            int severity,
            String outcome) {

        String record =
                "EMERGENCY TREATMENT - Severity: " +
                severity +
                " - Outcome: " +
                outcome;

        addHistory(record, true);
    }

    public void addAppointmentRecord(
            String doctorName,
            String time) {

        String record =
                "APPOINTMENT with Dr. " +
                doctorName +
                " at " +
                time;

        addHistory(record, true);
    }

    public void displayHistory() {

        System.out.println(
                "\n   === MEDICAL HISTORY for " +
                name +
                " (" +
                id +
                ") ==="
        );

        if (medicalHistory.size() == 0) {

            System.out.println(
                    "   No medical history available."
            );

        } else {

            System.out.println(
                    "   Total entries: " +
                    medicalHistory.size()
            );

            System.out.println(
                    "   " + "-".repeat(60)
            );

            System.out.println(
                    "   [History display available in enhanced version]"
            );
        }

        System.out.println(
                "   =================================="
        );
    }

    /**
     * Creates a deep copy of the patient.
     */
    public Patient deepCopy() {

        Patient copy = new Patient(
                this.id,
                this.name,
                this.age,
                this.gender,
                this.phone,
                this.severity
        );

        return copy;
    }
}
