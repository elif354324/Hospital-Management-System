package structures;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

/**
 * StateSnapshot class represents a saved state of the hospital system
 * Implements Memento Pattern for comprehensive undo functionality
 * Stores complete system state for restoration and rollback operations
 * 
 * Design Pattern: Memento Pattern implementation
 * Purpose: Store complete system state for restoration
 * Contains: System states map, description, timestamp, metadata
 * Serialization: Supports serialization for persistence
 */
public class StateSnapshot implements Serializable {
    private Map<String, Object> savedStates;
    private String description;
    private LocalDateTime timestamp;
    private static final DateTimeFormatter TIME_FORMAT = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Creates a new StateSnapshot with system states and description
     * @param savedStates Map of system component names to their state objects
     * @param description Human-readable description of the action/snapshot
     * @throws IllegalArgumentException if savedStates is null or empty
     */
    public StateSnapshot(Map<String, Object> savedStates, String description) {
        if (savedStates == null || savedStates.isEmpty()) {
            throw new IllegalArgumentException("Saved states cannot be null or empty");
        }
        
        // Create defensive copy to ensure state isolation
        this.savedStates = new HashMap<>(savedStates);
        this.description = (description != null) ? description : "Unnamed Snapshot";
        this.timestamp = LocalDateTime.now();
    }
    
    // ==================== GETTER AND QUERY METHODS ====================
    
    /**
     * Returns the saved states map as a defensive copy
     * Ensures external code cannot modify internal state
     * @return Copy of the saved states map
     */
    public Map<String, Object> getSavedStates() {
        return new HashMap<>(savedStates);
    }
    
    /**
     * Returns the action description for this snapshot
     * @return Description of the saved action/state
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Returns the timestamp when snapshot was created
     * @return Creation timestamp as LocalDateTime
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    /**
     * Returns formatted timestamp string for display
     * @return Formatted timestamp in yyyy-MM-dd HH:mm:ss format
     */
    public String getFormattedTimestamp() {
        return timestamp.format(TIME_FORMAT);
    }
    
    /**
     * Returns number of system components saved in this snapshot
     * @return Count of saved system states
     */
    public int getSystemCount() {
        return savedStates.size();
    }
    
    /**
     * Returns array of system names saved in this snapshot
     * @return Array of system component names
     */
    public String[] getSavedSystemNames() {
        return savedStates.keySet().toArray(new String[0]);
    }
    
    /**
     * Checks if a specific system component is saved in this snapshot
     * @param systemName Name of system component to check
     * @return true if system state is saved, false otherwise
     */
    public boolean containsSystem(String systemName) {
        return savedStates.containsKey(systemName);
    }
    
    /**
     * Gets the saved state of a specific system component
     * @param systemName Name of system component
     * @return Saved state object or null if not found
     */
    public Object getSystemState(String systemName) {
        return savedStates.get(systemName);
    }
    
    /**
     * Calculates age of snapshot in seconds
     * @return Age in seconds since creation
     */
    public long getAgeInSeconds() {
        return Duration.between(timestamp, LocalDateTime.now()).getSeconds();
    }
    
    /**
     * Returns formatted age string for user display
     * @return Age as "X seconds/minutes/hours ago"
     */
    public String getFormattedAge() {
        long seconds = getAgeInSeconds();
        
        if (seconds < 60) {
            return seconds + " seconds ago";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + " minute" + (minutes != 1 ? "s" : "") + " ago";
        } else {
            long hours = seconds / 3600;
            return hours + " hour" + (hours != 1 ? "s" : "") + " ago";
        }
    }
    
    // ==================== DISPLAY AND UTILITY METHODS ====================
    
    /**
     * Returns snapshot summary for logging and display
     * @return Formatted summary string
     */
    @Override
    public String toString() {
        return String.format(
            "Snapshot: '%s' | Systems: %d | Time: %s | Age: %s",
            description, getSystemCount(), getFormattedTimestamp(), getFormattedAge()
        );
    }
    
    /**
     * Returns detailed snapshot information for debugging
     * @return Multi-line detailed information string
     */
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append("\n=== STATE SNAPSHOT DETAILS ===\n");
        info.append("Description: ").append(description).append("\n");
        info.append("Timestamp: ").append(getFormattedTimestamp()).append("\n");
        info.append("Age: ").append(getFormattedAge()).append("\n");
        info.append("Systems Saved: ").append(getSystemCount()).append("\n");
        
        if (!savedStates.isEmpty()) {
            info.append("Saved Systems:\n");
            for (Map.Entry<String, Object> entry : savedStates.entrySet()) {
                String systemName = entry.getKey();
                Object state = entry.getValue();
                String className = (state != null) ? 
                    state.getClass().getSimpleName() : "null";
                info.append("  - ").append(systemName)
                    .append(" (").append(className).append(")\n");
            }
        }
        
        info.append("==============================\n");
        return info.toString();
    }
    
    /**
     * Creates a defensive copy of the snapshot
     * Note: This creates shallow copies of state objects
     * For true deep copy, each state object must implement deep copy
     * @return New StateSnapshot with same data
     */
    public StateSnapshot deepCopy() {
        // Create new map with same entries (shallow copy of states)
        Map<String, Object> copiedStates = new HashMap<>();
        for (Map.Entry<String, Object> entry : savedStates.entrySet()) {
            // Note: This is a shallow copy of state objects
            // For production, each state object should implement deep copy
            copiedStates.put(entry.getKey(), entry.getValue());
        }
        
        return new StateSnapshot(copiedStates, this.description);
    }
    
    /**
     * Validates the snapshot for completeness and consistency
     * @return true if snapshot is valid, false otherwise
     */
    public boolean isValid() {
        return savedStates != null && 
               !savedStates.isEmpty() && 
               description != null && 
               timestamp != null;
    }
    
    /**
     * Returns metadata about the snapshot
     * @return Map containing snapshot metadata
     */
    public Map<String, String> getMetadata() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("description", description);
        metadata.put("timestamp", getFormattedTimestamp());
        metadata.put("age", getFormattedAge());
        metadata.put("systemCount", String.valueOf(getSystemCount()));
        return metadata;
    }
}