package models;
import structures.Queue;

/**
 * Doctor class represents a medical doctor in the hospital
 * Manages patient waiting line using FIFO queue
 * 
 * ADT Usage: MyQueue (FIFO) for patient waiting line
 * Time Complexity: O(1) for enqueue/dequeue operations
 */
public class Doctor {
    private String id;
    private String name;
    private Department department;
    private Queue waitingLine;
    
    /**
     * Creates a new doctor
     * @param id Unique doctor ID
     * @param name Doctor's full name
     * @param department Assigned department
     */
    public Doctor(String id, String name, Department department) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.waitingLine = new Queue();
        department.addDoctor(); // Increment department doctor count
    }
    
    // ==================== GETTER METHODS ====================
    
    /**
     * Returns doctor ID
     * @return Unique doctor ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Returns doctor name
     * @return Doctor's full name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns assigned department
     * @return Department object
     */
    public Department getDepartment() {
        return department;
    }
    
    /**
     * Returns waiting line queue
     * @return MyQueue of waiting patients
     */
    public Queue getWaitingLine() {
        return waitingLine;
    }
    
    // ==================== PATIENT MANAGEMENT ====================
    
    /**
     * Adds patient to doctor's waiting line
     * @param patient Patient to add
     */
    public void addPatientToQueue(Patient patient) {
        waitingLine.enqueue(patient);
        System.out.println("   [DOCTOR] " + name + " added " + patient.getName() + 
                         " to waiting line. Total waiting: " + getQueueSize());
    }
    
    /**
     * Sees next patient in waiting line (FIFO)
     * @return Next patient or null if empty
     */
    public Patient seeNextPatient() {
        Patient nextPatient = waitingLine.dequeue();
        if (nextPatient != null) {
            System.out.println("   [DOCTOR] " + name + " is now examining: " + 
                             nextPatient.getName());
            nextPatient.addHistory("Consultation with " + name);
        }
        return nextPatient;
    }
    
    /**
     * Returns number of patients waiting
     * @return Queue size
     */
    public int getQueueSize() {
        return waitingLine.size();
    }
    
    /**
     * Checks if doctor has patients waiting
     * @return true if patients are waiting
     */
    public boolean hasPatientsWaiting() {
        return waitingLine.size() > 0;
    }
    
    /**
     * Clears the doctor's waiting queue
     * Used for system state restoration
     */
    public void clearQueue() {
        waitingLine.clear();
        System.out.println("   [DOCTOR] " + name + "'s waiting queue cleared.");
    }
    
    /**
     * Returns doctor information as string
     * @return Formatted doctor info
     */
    @Override
    public String toString() {
        return String.format("%s (%s) - %s | Patients waiting: %d", 
                           name, id, department.getName(), getQueueSize());
    }
}