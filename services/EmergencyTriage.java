package services;

import models.Patient;
import structures.Heap;

/**

* Emergency Room triage system using Max-Heap for priority-based
* patient management.
*
* Patients with higher severity values have higher priority.
* Severity: 10 = most critical.
*
* ADT Usage:
* * Heap (Max-Heap): Priority-based emergency patient management
*
* Time Complexity:
* * Insert: O(log n)
* * Extract Max: O(log n)
* * Remove: O(n)
    */
    public class EmergencyTriage {

  private Heap heap;
  private int capacity;
  private int totalTreated;

  /**

  * Creates a new EmergencyTriage system.
  *
  * @param capacity Maximum number of emergency patients
    */
    public EmergencyTriage(int capacity) {

    this.capacity = Math.max(1, capacity);
    this.heap = new Heap(this.capacity);
    this.totalTreated = 0;
    }

  // ==================== CORE OPERATIONS ====================

  /**

  * Admits a patient to the emergency room.
  *
  * @param patient Patient to admit
  * @return true if admitted successfully, false otherwise
    */
    public boolean admit(Patient patient) {

    if (patient == null) {

     System.out.println(
             "   [EMERGENCY] Cannot admit a null patient."
     );

     return false;

    }

    if (heap.size() >= capacity) {

     System.out.println(
             "   [EMERGENCY] Emergency room is at full capacity!"
     );

     System.out.println(
             "   Cannot admit: "
                     + patient.getName()
     );

     return false;

    }

    heap.insert(patient);

    System.out.println(
    "   [EMERGENCY] Admitted: "
    + patient.getName()
    );

    System.out.println(
    "   Severity: "
    + patient.getSeverity()
    + "/10"
    );

    System.out.println(
    "   ER Patients: "
    + heap.size()
    + "/"
    + capacity
    );

    return true;
    }

  /**

  * Treats the patient with the highest severity.
  *
  * @return Treated patient, or null if ER is empty
    */
    public Patient treatNext() {

    Patient nextPatient =
    heap.extractMax();

    if (nextPatient != null) {

     totalTreated++;


     System.out.println(
             "   [TREATMENT] Treating patient: "
                     + nextPatient.getName()
     );

     System.out.println(
             "   Severity: "
                     + nextPatient.getSeverity()
                     + "/10"
     );

     System.out.println(
             "   Total Treated: "
                     + totalTreated
     );

    } else {

     System.out.println(
             "   [EMERGENCY] No patients waiting for treatment."
     );

    }

    return nextPatient;
    }

  /**

  * Removes a specific patient from the emergency room.
  *
  * @param patientId Patient ID
  * @return true if removed successfully
    */
    public boolean removePatient(String patientId) {

    if (patientId == null
    || patientId.trim().isEmpty()) {

     return false;

    }

    boolean removed =
    heap.removeById(patientId.trim());

    if (removed) {

     System.out.println(
             "   [EMERGENCY] Removed patient: "
                     + patientId
     );

    }

    return removed;
    }

  /**

  * Views the highest-priority patient without removing them.
  *
  * @return Next emergency patient or null
    */
    public Patient peekNext() {

    return heap.peekMax();
    }

  // ==================== DISPLAY ====================

  /**

  * Displays emergency room status.
    */
    public void display() {

    System.out.println("\n");

    System.out.println(
    "   ========================================="
    );

    System.out.println(
    "          EMERGENCY ROOM STATUS"
    );

    System.out.println(
    "   ========================================="
    );

    System.out.println(
    "   Capacity: "
    + getPatientCount()
    + "/"
    + capacity
    );

    System.out.println(
    "   Occupancy: "
    + String.format(
    "%.1f",
    getOccupancyPercentage()
    )
    + "%"
    );

    if (isCriticalCapacity()) {

     System.out.println(
             "   Status: CRITICAL CAPACITY"
     );


    } else if (!hasPatients()) {

     System.out.println(
             "   Status: READY FOR NEW ADMISSIONS"
     );

    } else {

     System.out.println(
             "   Status: ACTIVE"
     );

    }

    System.out.println(
    "   -----------------------------------------"
    );

    if (!hasPatients()) {

     System.out.println(
             "   No patients currently in the emergency room."
     );
    

    } else {

     heap.display();

    }

    System.out.println(
    "   ========================================="
    );
    }

  // ==================== QUERY METHODS ====================

  /**

  * Returns the number of patients currently in the ER.
    */
    public int getPatientCount() {

    return heap.size();
    }

  /**

  * Alias for GUI/dashboard usage.
    */
    public int getCurrentPatientCount() {

    return getPatientCount();
    }

  /**

  * Returns maximum ER capacity.
    */
    public int getCapacity() {

    return capacity;
    }

  /**

  * Checks whether patients are waiting.
    */
    public boolean hasPatients() {

    return heap.size() > 0;
    }

  /**

  * Checks whether the ER is empty.
    */
    public boolean isEmpty() {

    return heap.size() == 0;
    }

  /**

  * Returns the current occupancy percentage.
    */
    public double getOccupancyPercentage() {

    if (capacity <= 0) {
    return 0.0;
    }

    return (heap.size() * 100.0)
    / capacity;
    }

  /**

  * Checks whether ER capacity is critical.
    */
    public boolean isCriticalCapacity() {

    return getOccupancyPercentage() >= 90.0;
    }

  /**

  * Checks whether the emergency room is completely full.
    */
    public boolean isFull() {

    return heap.size() >= capacity;
    }

  /**

  * Returns total number of patients treated.
    */
    public int getTotalTreated() {

    return totalTreated;
    }

  // ==================== STATE MANAGEMENT ====================

  /**

  * Creates a deep copy for undo functionality.
  *
  * @return Independent EmergencyTriage copy
    */
    public EmergencyTriage deepCopy() {

    EmergencyTriage copy =
    new EmergencyTriage(this.capacity);

    copy.heap =
    this.heap.deepCopy();

    copy.totalTreated =
    this.totalTreated;

    return copy;
    }

  /**

  * Restores emergency triage state.
  *
  * @param source Source state
    */
    public void restoreFrom(EmergencyTriage source) {

    if (source == null) {
    return;
    }

    this.heap =
    source.heap.deepCopy();

    this.capacity =
    source.capacity;

    this.totalTreated =
    source.totalTreated;

    System.out.println(
    "   [RESTORE] Emergency triage state restored."
    );
    }
    }
