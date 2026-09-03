package models;

/**
 * Department class represents a medical department in the hospital
 * Contains information about department name, location, and doctor count
 */
public class Department {
    private String name;
    private String location;
    private int doctorCount;
    
    /**
     * Creates a department with given name and default location
     * @param name Department name (e.g., "Cardiology")
     */
    public Department(String name) {
        this.name = name;
        this.location = "Main Building - Floor 1";
        this.doctorCount = 0;
    }
    
    /**
     * Creates a department with given name and location
     * @param name Department name
     * @param location Physical location in hospital
     */
    public Department(String name, String location) {
        this.name = name;
        this.location = location;
        this.doctorCount = 0;
    }
    
    // ==================== GETTER METHODS ====================
    
    /**
     * Returns department name
     * @return Department name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns department location
     * @return Physical location
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * Returns number of doctors in department
     * @return Doctor count
     */
    public int getDoctorCount() {
        return doctorCount;
    }
    
    // ==================== DEPARTMENT MANAGEMENT ====================
    
    /**
     * Adds a doctor to the department
     */
    public void addDoctor() {
        doctorCount++;
        System.out.println("   [DEPARTMENT] Added doctor to " + name + 
                         ". Total doctors: " + doctorCount);
    }
    
    /**
     * Removes a doctor from the department
     * @return true if doctor removed, false if no doctors
     */
    public boolean removeDoctor() {
        if (doctorCount > 0) {
            doctorCount--;
            System.out.println("   [DEPARTMENT] Removed doctor from " + name + 
                             ". Remaining doctors: " + doctorCount);
            return true;
        }
        return false;
    }
    
    /**
     * Checks if department has available doctors
     * @return true if at least one doctor available
     */
    public boolean hasDoctors() {
        return doctorCount > 0;
    }
    
    /**
     * Returns department information as string
     * @return Formatted department info
     */
    @Override
    public String toString() {
        return String.format("%s Department | Location: %s | Doctors: %d", 
                           name, location, doctorCount);
    }
}