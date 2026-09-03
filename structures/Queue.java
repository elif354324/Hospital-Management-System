package structures;

import models.Patient;

/**
 * FIFO Queue implementation using linked list
 * Used for appointment scheduling and doctor waiting lines
 * 
 * ADT Characteristics:
 * - First-In-First-Out (FIFO) ordering
 * - O(1) enqueue and dequeue operations
 * - Dynamic size (no fixed capacity)
 * - Linked list implementation
 */
public class Queue {
    /**
     * Node for linked list implementation
     */
    private class Node {
        Patient data;
        Node next;
        
        Node(Patient data) {
            this.data = data;
            this.next = null;
        }
        
        /**
         * Creates a deep copy of the node
         */
        Node deepCopy() {
            return new Node(this.data.deepCopy());
        }
    }
    
    private Node front;
    private Node rear;
    private int size;
    
    /**
     * Creates an empty queue
     */
    public Queue() {
        front = rear = null;
        size = 0;
    }
    
    // ==================== CORE QUEUE OPERATIONS ====================
    
    /**
     * Adds a patient to the end of the queue
     * Time Complexity: O(1)
     * @param patient Patient to enqueue
     */
    public void enqueue(Patient patient) {
        Node newNode = new Node(patient);
        
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }
    
    /**
     * Removes and returns the patient at the front of the queue
     * Time Complexity: O(1)
     * @return Patient at front, or null if queue empty
     */
    public Patient dequeue() {
        if (front == null) return null;
        
        Patient patient = front.data;
        front = front.next;
        
        if (front == null) {
            rear = null;
        }
        
        size--;
        return patient;
    }
    
    /**
     * Returns the patient at the front without removing
     * Time Complexity: O(1)
     * @return Patient at front, or null if queue empty
     */
    public Patient peek() {
        return (front != null) ? front.data : null;
    }
    
    /**
     * Removes a specific patient from the queue by ID
     * Time Complexity: O(n)
     * @param patientId Patient ID to remove
     * @return true if removed successfully
     */
    public boolean removePatient(String patientId) {
        if (front == null) return false;
        
        // Check if it's the front patient
        if (front.data.getId().equals(patientId)) {
            dequeue();
            return true;
        }
        
        Node current = front;
        while (current.next != null && !current.next.data.getId().equals(patientId)) {
            current = current.next;
        }
        
        if (current.next != null) {
            // Found the patient to remove
            if (current.next == rear) {
                rear = current; // Update rear if removing last patient
            }
            current.next = current.next.next;
            size--;
            return true;
        }
        
        return false;
    }
    
    /**
     * Clears all patients from the queue
     * Used for system state restoration
     */
    public void clear() {
        front = rear = null;
        size = 0;
    }
    
    /**
     * Restores queue from a saved copy
     * Used for undo functionality
     * @param source Source queue to restore from
     */
    public void restoreFrom(Queue source) {
        // Clear current queue
        this.clear();
        
        if (source.isEmpty()) {
            return;
        }
        
        // Deep copy all nodes from source
        Node sourceCurrent = source.front;
        Node lastCopied = null;
        
        while (sourceCurrent != null) {
            Node newNode = sourceCurrent.deepCopy();
            
            if (lastCopied == null) {
                this.front = newNode;
            } else {
                lastCopied.next = newNode;
            }
            
            lastCopied = newNode;
            sourceCurrent = sourceCurrent.next;
        }
        
        this.rear = lastCopied;
        this.size = source.size;
    }
    
    /**
     * Checks if queue is empty
     * @return true if queue is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Returns number of patients in queue
     * @return Queue size
     */
    public int size() {
        return size;
    }
    
    // ==================== DISPLAY METHODS ====================
    
    /**
     * Displays all patients in the queue
     */
    public void display() {
        if (size == 0) {
            System.out.println("   [QUEUE] Queue is empty.");
            return;
        }
        
        System.out.println("\n   === FIFO QUEUE (First to Last) ===");
        System.out.println("   Size: " + size + " patients");
        System.out.println("   Next: " + (front != null ? front.data.getName() : "None"));
        System.out.println("   " + "─".repeat(50));
        
        Node current = front;
        int position = 1;
        
        while (current != null) {
            Patient patient = current.data;
            System.out.printf("   %2d. %-20s | ID: %-12s | Priority: %d/10\n",
                            position, patient.getName(), patient.getId(), 
                            patient.getSeverity());
            current = current.next;
            position++;
        }
        
        System.out.println("   ===================================");
    }
    
    /**
     * Checks if a specific patient is in the queue
     * Time Complexity: O(n)
     * @param patientId Patient ID to search for
     * @return true if patient found
     */
    public boolean contains(String patientId) {
        Node current = front;
        while (current != null) {
            if (current.data.getId().equals(patientId)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
    
    // ==================== STATE MANAGEMENT ====================
    
    /**
     * Creates a deep copy of the queue
     * @return New MyQueue with same patients
     */
    public Queue deepCopy() {
        Queue copy = new Queue();
        
        if (this.front == null) {
            return copy;
        }
        
        // Deep copy the linked list
        Node current = this.front;
        Node copyCurrent = null;
        Node copyPrev = null;
        
        while (current != null) {
            Node newNode = current.deepCopy();
            
            if (copyPrev == null) {
                copy.front = newNode;
            } else {
                copyPrev.next = newNode;
            }
            
            copyPrev = newNode;
            copyCurrent = newNode;
            current = current.next;
        }
        
        copy.rear = copyPrev;
        copy.size = this.size;
        
        return copy;
    }
    
    /**
     * Returns queue statistics
     * @return Formatted statistics string
     */
    public String getStatistics() {
        return String.format(
            "Queue: %d patients | Next: %s | Status: %s",
            size,
            peek() != null ? peek().getName() : "None",
            isEmpty() ? "IDLE" : "ACTIVE"
        );
    }
}