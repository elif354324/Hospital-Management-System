package structures;

import java.util.ArrayList;
import java.util.List;

/**
 * Stateful Stack implementation for managing system state snapshots
 * Custom ADT used by ActionHistoryManager for undo functionality
 * Implements Last-In-First-Out (LIFO) ordering with fixed capacity
 * Features circular buffer behavior for overflow management
 * 
 * ADT Characteristics:
 * - Last-In-First-Out (LIFO) ordering principle
 * - Stores StateSnapshot objects containing complete system states
 * - Fixed capacity with efficient circular buffer overflow handling
 * - O(1) time complexity for push/pop operations
 * - Memory efficient array-based implementation
 * - Comprehensive state management capabilities
 */
public class StatefulStack {
    private StateSnapshot[] stack;
    private int top;
    private int capacity;
    private static final int DEFAULT_CAPACITY = 100;
    
    /**
     * Creates a StatefulStack with default capacity
     * Default capacity optimized for hospital system state management
     */
    public StatefulStack() {
        this(DEFAULT_CAPACITY);
    }
    
    /**
     * Creates a StatefulStack with specified capacity
     * @param capacity Maximum number of snapshots to store
     * @throws IllegalArgumentException if capacity is less than minimum required
     */
    public StatefulStack(int capacity) {
        if (capacity < 10) {
            throw new IllegalArgumentException("Minimum capacity is 10 snapshots");
        }
        this.capacity = capacity;
        this.stack = new StateSnapshot[this.capacity];
        this.top = -1; // Stack empty initialization
    }
    
    // ==================== CORE STACK OPERATIONS ====================
    
    /**
     * Pushes a state snapshot onto the stack
     * Implements circular buffer behavior when stack reaches capacity
     * Time Complexity: O(1) - constant time operation
     * @param snapshot StateSnapshot to push onto stack
     * @return true if pushed successfully, false on error
     * @throws NullPointerException if snapshot parameter is null
     */
    public boolean push(StateSnapshot snapshot) {
        if (snapshot == null) {
            throw new NullPointerException("Cannot push null snapshot onto stack");
        }
        
        if (top < capacity - 1) {
            // Normal push operation
            stack[++top] = snapshot;
            return true;
        } else {
            // Stack full - implement circular buffer behavior
            System.out.println("   [STACK] Stack at capacity, overwriting oldest snapshot.");
            
            // Shift all elements down (remove oldest, preserve newest)
            for (int i = 0; i < capacity - 1; i++) {
                stack[i] = stack[i + 1];
            }
            stack[capacity - 1] = snapshot;
            // top index remains at capacity - 1
            
            return true;
        }
    }
    
    /**
     * Pops the most recent state snapshot from the stack
     * Returns null if stack is empty
     * Time Complexity: O(1) - constant time operation
     * @return Most recent StateSnapshot, or null if stack empty
     */
    public StateSnapshot pop() {
        if (top == -1) {
            return null;
        }
        
        StateSnapshot snapshot = stack[top];
        stack[top] = null; // Clear reference for garbage collection
        top--;
        
        return snapshot;
    }
    
    /**
     * Peeks at the most recent state snapshot without removing
     * Non-destructive read operation
     * Time Complexity: O(1) - constant time operation
     * @return Most recent StateSnapshot, or null if stack empty
     */
    public StateSnapshot peek() {
        return (top != -1) ? stack[top] : null;
    }
    
    /**
     * Checks if stack is empty
     * Time Complexity: O(1) - constant time operation
     * @return true if stack is empty, false otherwise
     */
    public boolean isEmpty() {
        return top == -1;
    }
    
    /**
     * Returns number of snapshots currently in stack
     * Time Complexity: O(1) - constant time operation
     * @return Current stack size (0 to capacity)
     */
    public int size() {
        return top + 1;
    }
    
    /**
     * Clears all snapshots from the stack
     * Releases all references for garbage collection
     * Time Complexity: O(n) - linear in current size
     */
    public void clear() {
        for (int i = 0; i <= top; i++) {
            stack[i] = null;
        }
        top = -1;
        System.out.println("   [STACK] All snapshots cleared. Stack reset to empty state.");
    }
    
    // ==================== DISPLAY AND QUERY METHODS ====================
    
    /**
     * Displays the last N actions stored in the stack
     * Shows action descriptions with chronological order
     * Time Complexity: O(n) where n is display count
     * @param n Number of recent actions to display
     */
    public void displayLastActions(int n) {
        if (isEmpty()) {
            System.out.println("   No actions recorded in history stack.");
            return;
        }
        
        int displayCount = Math.min(n, size());
        System.out.println("   Recent " + displayCount + " actions from history:");
        
        for (int i = 0; i < displayCount; i++) {
            int index = top - i;
            if (index >= 0 && stack[index] != null) {
                System.out.printf("   %2d. %s (Saved: %s)\n", 
                                i + 1, 
                                stack[index].getDescription(),
                                stack[index].getFormattedAge());
            }
        }
    }
    
    /**
     * Displays comprehensive stack statistics and status
     * Shows capacity, usage, and operational metrics
     * Time Complexity: O(1) - constant time operation
     */
    public void displayStats() {
        System.out.println("\n   === STATEFUL STACK STATISTICS ===");
        System.out.println("   Capacity: " + capacity + " snapshots");
        System.out.println("   Current Size: " + size() + " snapshots");
        System.out.println("   Available Space: " + (capacity - size()) + " slots");
        System.out.println("   Usage Percentage: " + 
                         String.format("%.1f", getUsagePercentage()) + "%");
        System.out.println("   Status: " + (isEmpty() ? "EMPTY" : "ACTIVE"));
        
        if (!isEmpty()) {
            StateSnapshot recent = peek();
            System.out.println("   Most Recent Action: " + recent.getDescription());
            System.out.println("   Timestamp: " + recent.getFormattedTimestamp());
            System.out.println("   Snapshot Age: " + recent.getFormattedAge());
        }
        
        System.out.println("   Implementation: Array-based with Circular Buffer");
        System.out.println("   Time Complexity: O(1) push/pop operations");
        System.out.println("   ===================================");
    }
    
    /**
     * Returns a list of all snapshot descriptions in chronological order
     * Useful for logging and debugging purposes
     * Time Complexity: O(n) - linear in stack size
     * @return List of action descriptions from oldest to newest
     */
    public List<String> getAllActionDescriptions() {
        List<String> descriptions = new ArrayList<>();
        for (int i = 0; i <= top; i++) {
            if (stack[i] != null) {
                descriptions.add(stack[i].getDescription());
            }
        }
        return descriptions;
    }
    
    /**
     * Checks if stack contains a snapshot with specific description
     * Implements linear search through stack array
     * Time Complexity: O(n) - linear search
     * @param description Description text to search for
     * @return true if description found, false otherwise
     */
    public boolean contains(String description) {
        for (int i = 0; i <= top; i++) {
            if (stack[i] != null && 
                stack[i].getDescription().equals(description)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns the maximum capacity of the stack
     * Time Complexity: O(1) - constant time operation
     * @return Maximum number of snapshots stack can hold
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * Returns current usage percentage of stack capacity
     * Time Complexity: O(1) - constant time operation
     * @return Usage percentage from 0 to 100
     */
    public double getUsagePercentage() {
        return (size() * 100.0) / capacity;
    }
    
    /**
     * Checks if stack is at critical capacity (>90% full)
     * Time Complexity: O(1) - constant time operation
     * @return true if stack usage exceeds 90%, false otherwise
     */
    public boolean isCriticalCapacity() {
        return getUsagePercentage() > 90.0;
    }
    
    /**
     * Returns formatted statistics string for display purposes
     * Time Complexity: O(1) - constant time operation
     * @return Formatted string with stack statistics
     */
    public String getStatistics() {
        StateSnapshot recent = peek();
        return String.format(
            "State Stack: %d/%d snapshots | Recent: %s | Status: %s",
            size(), capacity,
            recent != null ? recent.getDescription() : "No actions",
            isEmpty() ? "IDLE" : "ACTIVE"
        );
    }
}