package structures;

import java.util.ArrayList;
import java.util.List;
import models.Department;
import models.Doctor;

/**
 * General Tree implementation for hospital organizational structure
 * Supports n-ary tree structure for hierarchical organization display
 * Features dynamic queue size updates and professional display formatting
 * * ADT Characteristics:
 * - General tree (n-ary tree) structure
 * - Each node can have unlimited children nodes
 * - Used for hierarchical organization display
 * - O(n) traversal complexity for display operations
 * - Dynamic updates for real-time queue information
 */
public class Tree {
    private String label;
    private List<Tree> children;
    private int level;
    private Doctor associatedDoctor; // Reference for dynamic doctor queue updates
    private Department associatedDepartment; // Reference for department information
    
    /**
     * Creates a new tree node with the given label
     * @param label The label/text for this node
     */
    public Tree(String label) {
        this.label = label;
        this.children = new ArrayList<>();
        this.level = 0; // Root level initialization
        this.associatedDoctor = null;
        this.associatedDepartment = null;
    }

    /**
     * Represents a node in a General Tree used to model the hospital hierarchy
     * (Hospital → Departments → Doctors).
     */
    public class TreeNode<T> {
        T data;
        LinkedList<TreeNode<T>> children;
    }
    
    /**
     * Creates a new tree node with label and level for internal use
     * @param label Node label text
     * @param level Node depth in tree hierarchy
     */
    private Tree(String label, int level) {
        this.label = label;
        this.children = new ArrayList<>();
        this.level = level;
        this.associatedDoctor = null;
        this.associatedDepartment = null;
    }
    
    /**
     * Creates a new tree node with doctor reference for dynamic updates
     * @param label Node label text
     * @param level Node depth in tree
     * @param doctor Doctor reference for real-time queue updates
     */
    private Tree(String label, int level, Doctor doctor) {
        this.label = label;
        this.children = new ArrayList<>();
        this.level = level;
        this.associatedDoctor = doctor;
        this.associatedDepartment = null;
    }
    
    // ==================== TREE STRUCTURE OPERATIONS ====================
    
    /**
     * FUNCTIONAL REQUIREMENT 4: Searching within the hierarchy.
     * Recursively searches for a node (Department or Doctor) by its name.
     * This fulfills the requirement for hierarchical searching.
     * @param targetName The name of the department or doctor to find.
     * @return The MyTree node containing the target, or null if not found.
     */
    public Tree findNode(String targetName) {
        // Check if current node label matches or if the associated doctor/department name matches
        if (this.label.contains(targetName) || 
           (associatedDoctor != null && associatedDoctor.getName().equalsIgnoreCase(targetName)) ||
           (associatedDepartment != null && associatedDepartment.getName().equalsIgnoreCase(targetName))) {
            return this;
        }

        // Recursively search through all children nodes
        for (Tree child : children) {
            Tree found = child.findNode(targetName);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * Adds a child node to this node
     * @param child The child node to add
     */
    public void addChild(Tree child) {
        child.level = this.level + 1;
        children.add(child);
    }
    
    /**
     * Creates and adds a child with the given label
     * @param label Label for the new child node
     * @return The newly created child node
     */
    public Tree addChild(String label) {
        Tree child = new Tree(label, this.level + 1);
        children.add(child);
        return child;
    }
    
    /**
     * Creates and adds a child with doctor reference
     * @param label Label for the new child node
     * @param doctor Doctor reference for dynamic queue updates
     * @return The newly created child node
     */
    public Tree addChild(String label, Doctor doctor) {
        Tree child = new Tree(label, this.level + 1, doctor);
        children.add(child);
        return child;
    }
    
    /**
     * Returns list of children nodes
     * @return List of child nodes (defensive copy for encapsulation)
     */
    public List<Tree> getChildren() {
        return new ArrayList<>(children);
    }
    
    /**
     * Returns node label
     * @return Node label text
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * Returns node level (depth) in tree
     * @return Level in tree (0 for root, increasing downward)
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Checks if node is leaf (has no children)
     * @return true if leaf node, false otherwise
     */
    public boolean isLeaf() {
        return children.isEmpty();
    }
    
    /**
     * Returns associated doctor reference
     * @return Doctor reference or null if not associated
     */
    public Doctor getAssociatedDoctor() {
        return associatedDoctor;
    }
    
    /**
     * Returns associated department reference
     * @return Department reference or null if not associated
     */
    public Department getAssociatedDepartment() {
        return associatedDepartment;
    }
    
    // ==================== DISPLAY AND VISUALIZATION METHODS ====================
    
    /**
     * Recursively displays tree structure with proper indentation
     * Shows dynamic queue sizes for doctors with professional formatting
     * Fixed duplicate "Patients waiting" display issue
     * @param node Current node to display
     * @param indent Indentation string for hierarchical display
     */
    private void displayRecursive(Tree node, String indent) {
        String connector = node.isLeaf() ? "└── " : "├── ";
        
        // Initialize display label with node's base label
        String displayLabel = node.getLabel();
        
        // If this node has an associated doctor, enhance display with current queue size
        if (node.getAssociatedDoctor() != null) {
            Doctor doctor = node.getAssociatedDoctor();
            
            // Extract doctor information without duplicate queue display
            // Doctor.toString() already contains queue information, so we use it directly
            displayLabel = doctor.toString();
        }
        
        System.out.println(indent + connector + displayLabel);
        
        // Recursively display children with increased indentation
        if (!node.isLeaf()) {
            String childIndent = indent + "    ";
            for (Tree child : node.getChildren()) {
                displayRecursive(child, childIndent);
            }
        }
    }
    
    /**
     * Displays the complete hospital organizational structure
     * Shows dynamic queue sizes for all doctors with real-time updates
     */
    public void displayStructure() {
        System.out.println("\n   " + "=".repeat(50));
        System.out.println("        HOSPITAL ORGANIZATIONAL STRUCTURE");
        System.out.println("   " + "=".repeat(50));
        
        displayRecursive(this, "");
        
        System.out.println("   " + "=".repeat(50));
    }
    
    // ==================== HOSPITAL-SPECIFIC METHODS ====================
    
    /**
     * Helper method to add a medical department with its doctors
     * Stores doctor references for dynamic queue updates
     * @param department The department to add
     * @param doctors Array of doctors working in this department
     */
    public void addDepartment(Department department, Doctor... doctors) {
        Tree deptNode = addChild(department.toString());
        deptNode.associatedDepartment = department;
        
        for (Doctor doctor : doctors) {
            // Store doctor reference for dynamic queue updates
            Tree doctorNode = deptNode.addChild(doctor.toString(), doctor);
        }
    }
    
    /**
     * Helper method to add emergency unit with capacity information
     * @param capacity Maximum patient capacity of emergency unit
     */
    public void addEmergencyUnit(int capacity) {
        Tree emergencyNode = addChild(
            "Emergency Unit (Capacity: " + capacity + " patients)"
        );
        emergencyNode.addChild("Status: OPERATIONAL");
        emergencyNode.addChild("Triage: Max-Heap Priority System");
    }
}