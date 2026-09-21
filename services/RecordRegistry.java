package services;

import java.util.List;

import models.Patient;
import structures.HashTable;

/**
 * Service class for managing patient records using a Hash Table.
 *
 * The internal Patient ID is used as the primary HashTable key.
 * T.C. Kimlik No is used as the user-facing identifier for
 * patient search, update and deletion operations.
 *
 * Uses a unique student ID to determine the initial capacity
 * of the data structure.
 */
public class RecordRegistry {

    private HashTable<Patient> table;
    private int patientCount;
    private long studentID;

    /**
     * Initializes the registry with a capacity based on the student ID.
     *
     * @param studentID Unique identifier used for configuration
     */
    public RecordRegistry(long studentID) {
        int capacity = (int) (studentID % 1000) + 100;

        this.table = new HashTable<>(capacity);
        this.patientCount = 0;
        this.studentID = studentID;
    }

    // ==================== CREATE ====================

    /**
     * Adds a new patient to the registry.
     *
     * Internal Patient ID must be unique.
     * T.C. Kimlik No must also be unique.
     *
     * @param p Patient object to add
     * @return true if added successfully, false otherwise
     */
    public boolean add(Patient p) {

        if (p == null) {
            return false;
        }

        // Check internal Patient ID
        if (table.contains(p.getId())) {
            System.out.println(
                "   [REGISTRY] Patient already exists: " + p.getId()
            );
            return false;
        }

        // Check T.C. Kimlik No
        if (p.getNationalId() != null
                && !p.getNationalId().trim().isEmpty()
                && containsNationalId(p.getNationalId())) {

            System.out.println(
                "   [REGISTRY] A patient with this TC Identity Number already exists: "
                + p.getNationalId()
            );

            return false;
        }

        table.put(p.getId(), p);
        patientCount++;

        System.out.println(
            "   [REGISTRY] Added patient: "
            + p.getName()
            + " (" + p.getId() + ")"
            + " | Total patients: "
            + patientCount
        );

        return true;
    }

    // ==================== READ ====================

    /**
     * Retrieves a patient record by internal Patient ID.
     *
     * This method is intended for internal system operations.
     *
     * @param id Internal Patient ID
     * @return Patient object if found, null otherwise
     */
    public Patient get(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        return table.get(id);
    }

    /**
     * Retrieves a patient by T.C. Kimlik No.
     *
     * This is the main method that should be used by the UI
     * for patient-related operations.
     *
     * @param nationalId T.C. Kimlik No
     * @return Patient object if found, null otherwise
     */
    public Patient getByNationalId(String nationalId) {

        if (nationalId == null || nationalId.trim().isEmpty()) {
            return null;
        }

        List<Patient> patients = table.getAllValues();

        for (Patient patient : patients) {

            if (patient != null
                    && nationalId.equals(patient.getNationalId())) {

                return patient;
            }
        }

        return null;
    }

    /**
     * Checks whether a patient exists using internal Patient ID.
     *
     * @param id Internal Patient ID
     * @return true if patient exists, false otherwise
     */
    public boolean contains(String id) {

        if (id == null || id.trim().isEmpty()) {
            return false;
        }

        return table.get(id) != null;
    }

    /**
     * Checks whether a patient exists using T.C. Kimlik No.
     *
     * @param nationalId T.C. Kimlik No
     * @return true if patient exists, false otherwise
     */
    public boolean containsNationalId(String nationalId) {
        return getByNationalId(nationalId) != null;
    }

    // ==================== UPDATE ====================

    /**
     * Updates an existing patient using internal Patient ID.
     *
     * This method is kept for internal system compatibility.
     *
     * @param id Internal Patient ID
     * @param updatedPatient Updated patient data
     * @return true if updated successfully, false otherwise
     */
    public boolean update(String id, Patient updatedPatient) {

        if (id == null || updatedPatient == null) {
            return false;
        }

        if (!table.contains(id)) {
            System.out.println(
                "   [REGISTRY] Patient not found for update: " + id
            );

            return false;
        }

        table.put(id, updatedPatient);

        System.out.println(
            "   [REGISTRY] Updated record for: "
            + updatedPatient.getName()
        );

        return true;
    }

    /**
     * Updates a patient using T.C. Kimlik No.
     *
     * This is the preferred method for UI operations.
     *
     * @param nationalId T.C. Kimlik No
     * @param updatedPatient Updated patient data
     * @return true if updated successfully, false otherwise
     */
    public boolean updateByNationalId(
            String nationalId,
            Patient updatedPatient) {

        if (nationalId == null
                || nationalId.trim().isEmpty()
                || updatedPatient == null) {

            return false;
        }

        Patient existingPatient = getByNationalId(nationalId);

        if (existingPatient == null) {

            System.out.println(
                "   [REGISTRY] Patient not found for TC update: "
                + nationalId
            );

            return false;
        }

        /*
         * Prevent changing the patient's TC number to a TC number
         * that belongs to another patient.
         */
        String updatedNationalId = updatedPatient.getNationalId();

        if (updatedNationalId != null
                && !updatedNationalId.equals(nationalId)
                && containsNationalId(updatedNationalId)) {

            System.out.println(
                "   [REGISTRY] Another patient already uses this TC Identity Number: "
                + updatedNationalId
            );

            return false;
        }

        return update(existingPatient.getId(), updatedPatient);
    }

    // ==================== DELETE ====================

    /**
     * Deletes a patient using internal Patient ID.
     *
     * This method is kept for internal system compatibility.
     *
     * @param id Internal Patient ID
     * @return true if deleted successfully, false otherwise
     */
    public boolean delete(String id) {

        if (id == null || id.trim().isEmpty()) {
            return false;
        }

        Patient removed = table.remove(id);

        if (removed != null) {

            patientCount--;

            System.out.println(
                "   [REGISTRY] Deleted patient: "
                + removed.getName()
                + " (" + id + ")"
                + " | Remaining patients: "
                + patientCount
            );

            return true;
        }

        System.out.println(
            "   [REGISTRY] Patient not found for deletion: " + id
        );

        return false;
    }

    /**
     * Deletes a patient using T.C. Kimlik No.
     *
     * This is the preferred method for UI operations.
     *
     * @param nationalId T.C. Kimlik No
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteByNationalId(String nationalId) {

        if (nationalId == null || nationalId.trim().isEmpty()) {
            return false;
        }

        Patient patient = getByNationalId(nationalId);

        if (patient == null) {

            System.out.println(
                "   [REGISTRY] Patient not found for TC deletion: "
                + nationalId
            );

            return false;
        }

        return delete(patient.getId());
    }

    // ==================== QUERY METHODS ====================

    /**
     * Returns the total number of registered patients.
     */
    public int getPatientCount() {
        return patientCount;
    }

    /**
     * Returns the internal HashTable capacity.
     */
    public int getCapacity() {
        return table.getCapacity();
    }

    /**
     * Returns the current load factor.
     */
    public double getLoadFactor() {
        return table.getLoadFactor();
    }

    /**
     * Returns the student ID used for configuration.
     */
    public long getStudentID() {
        return studentID;
    }

    /**
     * Checks whether the registry is empty.
     */
    public boolean isEmpty() {
        return patientCount == 0;
    }

    /**
     * Checks whether the HashTable should be rehashed.
     */
    public boolean needsRehash() {
        return getLoadFactor() > 0.75;
    }

    /**
     * Returns all registered patients.
     */
    public List<Patient> getAllPatients() {
        return table.getAllValues();
    }

    // ==================== REPORTING ====================

    /**
     * Generates a registry analytics report.
     */
    public void generateRegistryReport() {

        System.out.println("\n   === PATIENT REGISTRY ANALYTICS ===");

        System.out.println(
            "   Student ID Modifier: " + studentID
        );

        System.out.println(
            "   Total Registered Patients: " + patientCount
        );

        System.out.println(
            "   Internal Table Capacity: " + getCapacity()
        );

        System.out.println(
            "   Current Load Factor: "
            + String.format("%.2f", getLoadFactor())
        );

        System.out.println(
            "   Status: "
            + (needsRehash()
                ? "REHASH RECOMMENDED"
                : "STABLE")
        );

        System.out.println(
            "   =================================="
        );
    }

    // ==================== STATE MANAGEMENT ====================

    /**
     * Creates a deep copy of the registry for undo functionality.
     *
     * @return A new RecordRegistry with the same state
     */
    public RecordRegistry deepCopy() {

        RecordRegistry copy =
            new RecordRegistry(this.studentID);

        copy.table = this.table.deepCopy();
        copy.patientCount = this.patientCount;

        return copy;
    }

    /**
     * Restores the registry from another registry state.
     *
     * @param source Source registry
     */
    public void restoreFrom(RecordRegistry source) {

        if (source == null) {
            return;
        }

        this.table = source.table.deepCopy();
        this.patientCount = source.patientCount;
        this.studentID = source.studentID;

        System.out.println(
            "   [RESTORE] Patient registry synchronized. Count: "
            + patientCount
        );
    }
}