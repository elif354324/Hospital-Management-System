package structures;

/**
 * Singly Linked List implementation with comprehensive functionality
 * Used for patient medical history tracking with efficient operations
 * Supports generic data types and deep copy functionality
 * 
 * ADT Characteristics:
 * - Dynamic size with efficient memory usage
 * - O(1) insertion at beginning and end
 * - O(n) search and random access operations
 * - O(1) removal from beginning
 * - Complete deep copy support for state management
 * - Generic type support for flexibility
 */
public class LinkedList<T> {
    /**
     * Node class for linked list implementation
     * Stores data and reference to next node
     * Supports generic data types
     */
    private class Node {
        T data;
        Node next;
        
        /**
         * Creates a new node with specified data
         * @param data Element to store in node
         */
        Node(T data) {
            this.data = data;
            this.next = null;
        }
        
        /**
         * Creates a deep copy of the node
         * Note: For true deep copy, T should implement Cloneable interface
         * @return New node with copied data reference
         */
        Node deepCopy() {
            // Creates a new node with same data reference
            // For true deep copy, T should implement proper cloning
            return new Node(this.data);
        }
    }
    
    private Node head;
    private Node tail;
    private int size;
    
    /**
     * Creates an empty linked list
     * Initializes all pointers to null and size to zero
     */
    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }
    
    // ==================== CORE OPERATIONS ====================
    
    /**
     * Adds element to the end of the list
     * Uses tail pointer for O(1) time complexity
     * @param data Element to add to the list
     */
    public void addLast(T data) {
        Node newNode = new Node(data);
        
        if (tail == null) {
            // First element in list
            head = tail = newNode;
        } else {
            // Append to end of list
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }
    
    /**
     * Adds element to the beginning of the list
     * O(1) time complexity operation
     * @param data Element to add to the beginning
     */
    public void addFirst(T data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
        
        if (tail == null) {
            // First element in list
            tail = head;
        }
        size++;
    }
    
    /**
     * Removes and returns the first element
     * O(1) time complexity operation
     * @return First element or null if list empty
     */
    public T removeFirst() {
        if (head == null) return null;
        
        T data = head.data;
        head = head.next;
        
        if (head == null) {
            // List is now empty
            tail = null;
        }
        size--;
        
        return data;
    }
    
    /**
     * Returns the first element without removing
     * O(1) time complexity operation
     * @return First element or null if list empty
     */
    public T getFirst() {
        return (head != null) ? head.data : null;
    }
    
    /**
     * Returns the last element without removing
     * Uses tail pointer for O(1) time complexity
     * @return Last element or null if list empty
     */
    public T getLast() {
        return (tail != null) ? tail.data : null;
    }
    
    // ==================== QUERY AND UTILITY METHODS ====================
    
    /**
     * Checks if list is empty
     * @return true if list is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Returns number of elements in list
     * @return Current size of the list
     */
    public int size() {
        return size;
    }
    
    /**
     * Clears all elements from the list
     * Resets list to empty state
     */
    public void clear() {
        head = tail = null;
        size = 0;
    }
    
    /**
     * Checks if list contains specific element
     * Implements linear search through list
     * Time Complexity: O(n)
     * @param data Element to search for
     * @return true if element found, false otherwise
     */
    public boolean contains(T data) {
        Node current = head;
        while (current != null) {
            if (current.data.equals(data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
    
    // ==================== DEEP COPY FUNCTIONALITY ====================
    
    /**
     * Creates a deep copy of the linked list
     * Copies all nodes and their data references
     * Time Complexity: O(n) where n is list size
     * @return New MyLinkedList with copied elements
     */
    public LinkedList<T> deepCopy() {
        LinkedList<T> copy = new LinkedList<>();
        
        if (this.head == null) {
            return copy; // Return empty list
        }
        
        Node current = this.head;
        Node copyPrev = null;
        
        while (current != null) {
            Node newNode = current.deepCopy();
            
            if (copyPrev == null) {
                // First node in copy
                copy.head = newNode;
            } else {
                // Link to previous node
                copyPrev.next = newNode;
            }
            
            copyPrev = newNode;
            current = current.next;
        }
        
        copy.tail = copyPrev;
        copy.size = this.size;
        
        return copy;
    }
    
    // ==================== DISPLAY AND CONVERSION METHODS ====================
    
    /**
     * Displays all elements in the list
     * Shows elements in order from first to last
     */
    public void display() {
        if (size == 0) {
            System.out.println("   [LIST] List is empty.");
            return;
        }
        
        Node current = head;
        int count = 1;
        
        System.out.println("   Linked List Contents (" + size + " elements):");
        while (current != null) {
            System.out.println("   " + count + ". " + current.data);
            current = current.next;
            count++;
        }
    }
    
    /**
     * Converts linked list to array
     * Time Complexity: O(n)
     * @return Array containing all elements in list order
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        T[] array = (T[]) new Object[size];
        Node current = head;
        int index = 0;
        
        while (current != null) {
            array[index++] = current.data;
            current = current.next;
        }
        
        return array;
    }
    
    /**
     * Returns list statistics for monitoring
     * @return Formatted statistics string
     */
    public String getStatistics() {
        return String.format(
            "LinkedList: %d elements | Empty: %s | Memory: Efficient",
            size, isEmpty() ? "YES" : "NO"
        );
    }
}