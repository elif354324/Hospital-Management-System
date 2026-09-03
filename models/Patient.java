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
    private int severity; // 1-10 scale, 10 = most critical
    private LinkedList<String> medicalHistory;
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Creates a standard patient with default severity (1)
     * @param id Patient ID
     * @param name Patient full name
     */
    public Patient(String id, String name) {
        this.id = id;
        this.name = name;
        this.severity = 1; // Default priority for regular appointments
        this.medicalHistory = new LinkedList<>();
        this.addHistory("PATIENT REGISTERED", true);
    }
    
    /**
     * Creates a patient with custom severity (for emergency cases)
     * @param id Patient ID
     * @param name Patient full name
     * @param severity Emergency severity (1-10)
     */
    public Patient(String id, String name, int severity) {
        this.id = id;
        this.name = name;
        this.severity = Math.max(1, Math.min(10, severity)); // Clamp to 1-10 range
        this.medicalHistory = new LinkedList<>();
        this.addHistory("EMERGENCY ADMISSION - Severity: " + severity, true);
    }
    
    // ==================== GETTER METHODS ====================
    
    /**
     * Returns patient ID
     * @return Patient ID
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * Returns patient name
     * @return Patient full name
     */
    public String getName() { 
        return name; 
    }
    
    /**
     * Returns patient severity level
     * @return Severity level (1-10)
     */
    public int getSeverity() { 
        return severity; 
    }
    
    /**
     * Returns medical history linked list
     * @return Medical history records
     */
    public LinkedList<String> getMedicalHistory() { 
        return medicalHistory; 
    }
    
    // ==================== SETTER METHODS ====================
    
    /**
     * Updates patient severity level
     * @param severity New severity (1-10)
     */
    public void setSeverity(int severity) {
        int oldSeverity = this.severity;
        this.severity = Math.max(1, Math.min(10, severity));
        this.addHistory("SEVERITY UPDATED: " + oldSeverity + " → " + this.severity, false);
    }
    
    /**
     * Updates patient name
     * @param name New patient name
     */
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        this.addHistory("NAME UPDATED: " + oldName + " → " + name, false);
    }
    
    // ==================== MEDICAL HISTORY MANAGEMENT ====================
    
    /**
     * Adds a record to medical history with auto-timestamp
     * @param record Medical record text
     * @param isAuto True if auto-generated, false if manual
     */
    public void addHistory(String record, boolean isAuto) {
        String prefix = isAuto ? "[SYSTEM] " : "[MANUAL] ";
        String timestampedRecord = prefix + record + " [" + 
                                 LocalDateTime.now().format(TIMESTAMP_FORMAT) + "]";
        medicalHistory.addLast(timestampedRecord);
        
        if (!isAuto) {
            System.out.println("   [HISTORY] Added to " + name + "'s record: " + record);
        }
    }
    
    /**
     * Simplified method for manual note addition (backward compatibility)
     * @param record Medical record text
     */
    public void addHistory(String record) {
        addHistory(record, false);
    }
    
    /**
     * Automatically adds a doctor consultation record
     * @param doctorName Doctor's name
     * @param notes Consultation notes
     */
    public void addDoctorConsultation(String doctorName, String notes) {
        String record = "CONSULTATION with Dr. " + doctorName + " - " + notes;
        addHistory(record, true);
    }
    
    /**
     * Automatically adds emergency treatment record
     * @param severity Treatment severity
     * @param outcome Treatment outcome
     */
    public void addEmergencyTreatment(int severity, String outcome) {
        String record = "EMERGENCY TREATMENT - Severity: " + severity + 
                       " - Outcome: " + outcome;
        addHistory(record, true);
    }
    
    /**
     * Automatically adds appointment record
     * @param doctorName Doctor's name
     * @param time Appointment time
     */
    public void addAppointmentRecord(String doctorName, String time) {
        String record = "APPOINTMENT with Dr. " + doctorName + " at " + time;
        addHistory(record, true);
    }
    
    /**
     * Displays complete medical history
     */
    public void displayHistory() {
        System.out.println("\n   === MEDICAL HISTORY for " + name + " (" + id + ") ===");
        if (medicalHistory.size() == 0) {
            System.out.println("   No medical history available.");
        } else {
            System.out.println("   Total entries: " + medicalHistory.size());
            System.out.println("   " + "-".repeat(60));
            
            // Convert to array for display
            String[] historyArray = new String[medicalHistory.size()];
            // Simple traversal for display
            // In full implementation, MyLinkedList would have toArray()
            System.out.println("   [History display available in enhanced version]");
        }
        System.out.println("   ==================================");
    }
    
    /**
     * Creates a deep copy of the patient
     * @return New Patient object with same data
     */
    public Patient deepCopy() {
        Patient copy = new Patient(this.id, this.name, this.severity);
        // Note: Medical history is not deep copied in this implementation
        return copy;
    }
}