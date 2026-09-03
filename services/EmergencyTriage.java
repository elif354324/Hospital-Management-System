package services;

import models.Patient;
import structures.Heap;

/**
 * Emergency Room triage system using Max-Heap for priority-based patient management
 * Prioritizes patients by severity (10 = most critical)
 * 
 * ADT Usage: MyHeap (Max-Heap) for priority queue
 * Time Complexity: O(log n) insert, O(log n) extract, O(n) remove
 */
public class EmergencyTriage {
    private Heap heap;
    private int capacity;
    private int totalTreated;
    
    /**
     * Creates a new EmergencyTriage system with specified capacity
     * @param capacity Maximum number of emergency patients
     */
    public EmergencyTriage(int capacity) { 
        this.heap = new Heap(capacity);
        this.capacity = capacity;
        this.totalTreated = 0;
    }
    
    /**
     * Admits a patient to the emergency room
     * @param p Patient to admit
     * @return true if admitted successfully, false if at capacity
     */
    public boolean admit(Patient p) { 
        if (heap.size() >= capacity) {
            System.out.println("   [EMERGENCY] Emergency room at full capacity! Cannot admit: " + p.getName());
            return false;
        }
        
        heap.insert(p);
        System.out.println("   [EMERGENCY] Admitted: " + p.getName() + 
                         " | Severity: " + p.getSeverity() + "/10" +
                         " | ER patients: " + heap.size() + "/" + capacity);
        return true;
    }
    
    /**
     * Treats the next patient with highest priority (highest severity)
     * @return The treated patient, or null if no patients
     */
    public Patient treatNext() {
        Patient next = heap.extractMax();
        if (next != null) {
            totalTreated++;
            System.out.println("   [TREATMENT] Treating patient: " + next.getName() +
                             " | Severity: " + next.getSeverity() + "/10" +
                             " | Total treated today: " + totalTreated);
        } else {
            System.out.println("   [EMERGENCY] No patients to treat.");
        }
        return next;
    }
    
    /**
     * Removes a specific patient from emergency room
     * @param patientId Patient ID to remove
     * @return true if removed successfully, false if not found
     */
    public boolean removePatient(String patientId) {
        boolean removed = heap.removeById(patientId);
        if (removed) {
            System.out.println("   [EMERGENCY] Removed patient from emergency room: " + patientId);
        }
        return removed;
    }
    
    /**
     * Views next patient without removing from heap
     * @return Next patient to treat or null if empty
     */
    public Patient peekNext() {
        return heap.peekMax();
    }
    
    /**
     * Displays all emergency patients
     */
    public void display() { 
        System.out.println("\n");
        System.out.println("   =========================================");
        System.out.println("          EMERGENCY ROOM STATUS");
        System.out.println("   =========================================");
        
        if (!hasPatients()) {
            System.out.println("\n   No patients in emergency room.");
            System.out.println("   Status: READY for new admissions");
        } else {
            heap.display();
        }
        System.out.println("   ====================================================================");
    }
    
    // ==================== QUERY METHODS ====================
    
    /**
     * Returns number of patients in emergency room
     * @return Patient count
     */
    public int getPatientCount() {
        return heap.size();
    }
    
    /**
     * Returns emergency room capacity
     * @return Maximum capacity
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * Checks if there are emergency patients waiting
     * @return true if patients exist, false otherwise
     */
    public boolean hasPatients() {
        return heap.size() > 0;
    }
    
    /**
     * Returns the current occupancy percentage
     * @return Occupancy percentage (0-100)
     */
    public double getOccupancyPercentage() {
        return (heap.size() * 100.0) / capacity;
    }
    
    /**
     * Checks if emergency room is at critical capacity (>90%)
     * @return true if critical capacity reached
     */
    public boolean isCriticalCapacity() {
        return getOccupancyPercentage() > 90.0;
    }
    
    /**
     * Returns total patients treated today
     * @return Total treated count
     */
    public int getTotalTreated() {
        return totalTreated;
    }
    
    // ==================== STATE MANAGEMENT ====================
    
    /**
     * Creates a deep copy of the emergency triage system
     * @return New EmergencyTriage with same state
     */
    public EmergencyTriage deepCopy() {
        EmergencyTriage copy = new EmergencyTriage(this.capacity);
        copy.heap = this.heap.deepCopy();
        copy.totalTreated = this.totalTreated;
        return copy;
    }
    
    /**
     * Restores from a copy
     * @param source Source to restore from
     */
    public void restoreFrom(EmergencyTriage source) {
        this.heap = source.heap.deepCopy();
        this.capacity = source.capacity;
        this.totalTreated = source.totalTreated;
        System.out.println("   [RESTORE] Emergency triage state restored.");
    }
}