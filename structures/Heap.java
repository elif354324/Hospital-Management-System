package structures;

import models.Patient;

/**
 * Max-Heap implementation for priority-based patient management
 * Highest severity patients are treated first (severity 10 = highest)
 * 
 * ADT Characteristics:
 * - Complete binary tree stored in array
 * - Max-heap property: parent >= children
 * - O(log n) insert and extract operations
 * - O(1) peek at maximum element
 * - O(n) search for specific patient
 */
public class Heap {
    private Patient[] heap;
    private int size;
    private int capacity;
    
    /**
     * Creates a new Max-Heap with specified capacity
     * @param capacity Maximum number of patients
     */
    public Heap(int capacity) {
        this.capacity = Math.max(capacity, 10); // Minimum capacity 10
        this.heap = new Patient[this.capacity];
        this.size = 0;
    }
    
    // ==================== CORE HEAP OPERATIONS ====================
    
    /**
     * Inserts a patient into the heap
     * Time Complexity: O(log n)
     * @param p Patient to insert
     * @return true if inserted successfully, false if heap full
     */
    public boolean insert(Patient p) {
        if (size >= capacity) {
            System.out.println("   [HEAP] Heap is at full capacity!");
            return false;
        }
        
        // Add at the end
        heap[size] = p;
        size++;
        
        // Bubble up to maintain heap property
        bubbleUp(size - 1);
        
        return true;
    }
    
    /**
     * Extracts the patient with highest severity
     * Time Complexity: O(log n)
     * @return Patient with highest severity, or null if heap empty
     */
    public Patient extractMax() {
        if (size == 0) return null;
        
        Patient maxPatient = heap[0];
        
        // Move last element to root
        heap[0] = heap[size - 1];
        heap[size - 1] = null; // Clear the last position
        size--;
        
        // Bubble down to maintain heap property
        if (size > 0) {
            bubbleDown(0);
        }
        
        return maxPatient;
    }
    
    /**
     * Returns the patient with highest severity without removing
     * Time Complexity: O(1)
     * @return Maximum patient or null if heap empty
     */
    public Patient peekMax() {
        return size > 0 ? heap[0] : null;
    }
    
    /**
     * NEW: Removes a specific patient by ID from the heap
     * Time Complexity: O(n) for search + O(log n) for heapify = O(n)
     * @param patientId Patient ID to remove
     * @return true if removed successfully, false if not found
     */
    public boolean removeById(String patientId) {
        if (size == 0) {
            return false;
        }
        
        // Find the index of patient with given ID
        int indexToRemove = -1;
        for (int i = 0; i < size; i++) {
            if (heap[i] != null && heap[i].getId().equals(patientId)) {
                indexToRemove = i;
                break;
            }
        }
        
        if (indexToRemove == -1) {
            return false; // Patient not found
        }
        
        // Replace with last element
        heap[indexToRemove] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        
        // Restore heap property
        if (indexToRemove < size) {
            // Try bubbling up first
            bubbleUp(indexToRemove);
            // Then bubble down if needed
            bubbleDown(indexToRemove);
        }
        
        return true;
    }
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Bubbles up an element to maintain heap property
     */
    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            
            if (heap[index].getSeverity() > heap[parent].getSeverity()) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }
    
    /**
     * Bubbles down an element to maintain heap property
     */
    private void bubbleDown(int index) {
        while (index < size) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int largest = index;
            
            if (left < size && heap[left].getSeverity() > heap[largest].getSeverity()) {
                largest = left;
            }
            
            if (right < size && heap[right].getSeverity() > heap[largest].getSeverity()) {
                largest = right;
            }
            
            if (largest != index) {
                swap(index, largest);
                index = largest;
            } else {
                break;
            }
        }
    }
    
    /**
     * Swaps two elements in the heap
     */
    private void swap(int i, int j) {
        Patient temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    
    // ==================== DISPLAY METHODS ====================
    
    /**
     * Display heap contents efficiently
     * Time Complexity: O(n) - no sorting or extracting
     */
    public void display() {
        if (size == 0) {
            System.out.println("   Emergency room is empty.");
            return;
        }
        
        System.out.println("\n   === EMERGENCY ROOM (Priority Order) ===");
        System.out.println("   Next to treat: " + 
                         (peekMax() != null ? peekMax().getName() + 
                         " (Severity: " + peekMax().getSeverity() + "/10)" : "None"));
        System.out.println("   Total patients: " + size + "/" + capacity);
        System.out.println("   " + "-".repeat(50));
        
        // Simple display without sorting
        for (int i = 0; i < size; i++) {
            Patient p = heap[i];
            String position = (i == 0) ? "Next" : "Waiting";
            System.out.printf("   %-10s: %-20s | Severity: %2d/10 | ID: %s\n",
                            position, p.getName(), p.getSeverity(), p.getId());
        }
    }
    
    // ==================== QUERY METHODS ====================
    
    /**
     * Returns number of patients in heap
     */
    public int size() {
        return size;
    }
    
    /**
     * Checks if heap is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Returns heap capacity
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * Checks if heap property is maintained
     * For debugging purposes
     */
    public boolean isHeapValid() {
        for (int i = 0; i < size; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            
            if (left < size && heap[i].getSeverity() < heap[left].getSeverity()) {
                return false;
            }
            if (right < size && heap[i].getSeverity() < heap[right].getSeverity()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Searches for a patient by ID
     * Time Complexity: O(n)
     * @param patientId Patient ID to search for
     * @return true if patient found in heap
     */
    public boolean contains(String patientId) {
        for (int i = 0; i < size; i++) {
            if (heap[i] != null && heap[i].getId().equals(patientId)) {
                return true;
            }
        }
        return false;
    }
    
    // ==================== STATE MANAGEMENT ====================
    
    /**
     * Creates a deep copy of the heap
     */
    public Heap deepCopy() {
        Heap copy = new Heap(this.capacity);
        copy.size = this.size;
        
        // Deep copy patients
        for (int i = 0; i < this.size; i++) {
            copy.heap[i] = this.heap[i].deepCopy();
        }
        
        return copy;
    }
}