package services;

import models.Patient;
import structures.HashTable;

/**
 * Service class for managing patient records using a Hash Table.
 * Provides functionality to add, retrieve, update, and delete patient records.
 * Uses a unique student ID to determine the initial capacity of the data structure.
 */
public class RecordRegistry {
    private HashTable<Patient> table;
    private int patientCount;
    private long studentID;

    /**
     * Initializes the registry with a capacity based on the student ID.
     * @param studentID Unique identifier used for configuration
     */
    public RecordRegistry(long studentID) {
        int capacity = (int)(studentID % 1000) + 100;
        this.table = new HashTable<>(capacity);
        this.patientCount = 0;
        this.studentID = studentID;
    }

    /**
     * Adds a new patient to the registry.
     * @param p The patient object to add
     * @return true if added successfully, false if ID already exists
     */
    public boolean add(Patient p) {
        if (table.contains(p.getId())) {
            System.out.println("   [REGISTRY] Patient already exists: " + p.getId());
            return false;
        }

        table.put(p.getId(), p);
        patientCount++;
        System.out.println("   [REGISTRY] Added patient: " + p.getName() +
                         " (" + p.getId() + ")" +
                         " | Total patients: " + patientCount);
        return true;
    }

    /**
     * Retrieves a patient record by ID.
     * @param id The patient ID to search for
     * @return The Patient object if found, null otherwise
     */
    public Patient get(String id) {
        return table.get(id);
    }

    /**
     * Deletes a patient record from the registry.
     * @param id The ID of the patient to remove
     * @return true if deleted successfully, false if not found
     */
    public boolean delete(String id) {
        Patient removed = table.remove(id);
        if (removed != null) {
            patientCount--;
            System.out.println("   [REGISTRY] Deleted patient: " + removed.getName() +
                             " (" + id + ")" +
                             " | Remaining patients: " + patientCount);
            return true;
        }
        System.out.println("   [REGISTRY] Patient not found for deletion: " + id);
        return false;
    }

    /**
     * Updates an existing patient record.
     * @param id The ID of the patient to update
     * @param updatedPatient The new patient data
     * @return true if updated successfully, false if ID not found
     */
    public boolean update(String id, Patient updatedPatient) {
        if (!table.contains(id)) {
            System.out.println("   [REGISTRY] Patient not found for update: " + id);
            return false;
        }

        table.put(id, updatedPatient);
        System.out.println("   [REGISTRY] Updated record for: " + updatedPatient.getName());
        return true;
    }

    public boolean contains(String id) {
        return table.get(id) != null;
    }

    // ==================== REPORTING METHODS ====================

    public void generateRegistryReport() {
        System.out.println("\n   === PATIENT REGISTRY ANALYTICS ===");
        System.out.println("   Student ID Modifier: " + studentID);
        System.out.println("   Total Registered Patients: " + patientCount);
        System.out.println("   Internal Table Capacity: " + getCapacity());
        System.out.println("   Current Load Factor: " + String.format("%.2f", getLoadFactor()));
        System.out.println("   Status: " + (needsRehash() ? "REHASH RECOMMENDED" : "STABLE"));
        System.out.println("   ==================================");
    }

    // ==================== QUERY METHODS ====================

    public int getPatientCount() { return patientCount; }
    public int getCapacity() { return table.getCapacity(); }
    public double getLoadFactor() { return table.getLoadFactor(); }
    public long getStudentID() { return studentID; }
    public boolean isEmpty() { return patientCount == 0; }
    public boolean needsRehash() { return getLoadFactor() > 0.75; }

    // ==================== STATE MANAGEMENT ====================

    /**
     * Creates a deep copy of the registry for undo functionality.
     * @return A new RecordRegistry with the same state
     */
    public RecordRegistry deepCopy() {
        RecordRegistry copy = new RecordRegistry(this.studentID);
        copy.table = this.table.deepCopy();
        copy.patientCount = this.patientCount;
        return copy;
    }

    public void restoreFrom(RecordRegistry source) {
        this.table = source.table.deepCopy();
        this.patientCount = source.patientCount;
        this.studentID = source.studentID;
        System.out.println("   [RESTORE] Patient registry synchronized. Count: " + patientCount);
    }
}