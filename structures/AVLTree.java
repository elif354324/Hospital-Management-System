package structures;

import models.Patient;
import java.util.ArrayList;
import java.util.List;

/**
 * AVL Tree implementation for patient name indexing
 * Self-balancing binary search tree with O(log n) operations
 * 
 * ADT Characteristics:
 * - Self-balancing binary search tree
 * - Height difference ≤ 1 for all nodes
 * - O(log n) search, insert, remove operations
 * - Handles duplicate names with patient ID secondary key
 * - Guaranteed O(log n) height
 */
public class AVLTree {
    /**
     * AVL Tree Node with height tracking
     */
    private class AVLNode {
        Patient patient;
        AVLNode left, right;
        int height;
        
        AVLNode(Patient patient) {
            this.patient = patient;
            this.height = 1;  // New node is initially at height 1
        }
        
        /**
         * Creates a deep copy of the node
         */
        AVLNode deepCopy() {
            AVLNode copy = new AVLNode(this.patient.deepCopy());
            copy.height = this.height;
            return copy;
        }
    }
    
    private AVLNode root;
    private int nodeCount;
    
    /**
     * Creates an empty AVL Tree
     */
    public AVLTree() {
        root = null;
        nodeCount = 0;
    }
    
    // ==================== CORE AVL OPERATIONS ====================
    
    /**
     * Returns the height of a node (handles null)
     */
    private int height(AVLNode node) {
        return node == null ? 0 : node.height;
    }
    
    /**
     * Returns balance factor of a node
     * Positive = left heavy, Negative = right heavy
     */
    private int getBalance(AVLNode node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }
    
    /**
     * Updates height of a node based on children's heights
     */
    private void updateHeight(AVLNode node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }
    
    /**
     * Right rotation for left-left case
     */
    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;
        
        // Perform rotation
        x.right = y;
        y.left = T2;
        
        // Update heights
        updateHeight(y);
        updateHeight(x);
        
        return x;
    }
    
    /**
     * Left rotation for right-right case
     */
    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;
        
        // Perform rotation
        y.left = x;
        x.right = T2;
        
        // Update heights
        updateHeight(x);
        updateHeight(y);
        
        return y;
    }
    
    /**
     * Performs appropriate rotations to balance the tree
     */
    private AVLNode balance(AVLNode node) {
        if (node == null) return null;
        
        // Update height
        updateHeight(node);
        
        // Check balance factor
        int balance = getBalance(node);
        
        // Left Left Case
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rightRotate(node);
        }
        
        // Left Right Case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        
        // Right Right Case
        if (balance < -1 && getBalance(node.right) <= 0) {
            return leftRotate(node);
        }
        
        // Right Left Case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        
        return node;
    }
    
    // ==================== PUBLIC OPERATIONS ====================
    
    /**
     * Inserts a patient into the AVL Tree
     * Time Complexity: O(log n)
     * @param patient Patient to insert
     */
    public void insert(Patient patient) {
        root = insertRec(root, patient);
        nodeCount++;
    }
    
    private AVLNode insertRec(AVLNode node, Patient patient) {
        // Standard BST insertion
        if (node == null) {
            return new AVLNode(patient);
        }
        
        // Compare by name first, then by ID if names are equal
        int nameComparison = patient.getName().compareToIgnoreCase(node.patient.getName());
        
        if (nameComparison < 0) {
            node.left = insertRec(node.left, patient);
        } else if (nameComparison > 0) {
            node.right = insertRec(node.right, patient);
        } else {
            // Names are equal, compare by ID
            int idComparison = patient.getId().compareTo(node.patient.getId());
            if (idComparison < 0) {
                node.left = insertRec(node.left, patient);
            } else if (idComparison > 0) {
                node.right = insertRec(node.right, patient);
            } else {
                // Same patient (same ID), update and don't increase count
                nodeCount--; // Adjust count since we're replacing
                node.patient = patient;
                return node;
            }
        }
        
        // Balance the tree
        return balance(node);
    }
    
    /**
     * NEW: Remove patient by name and ID
     * @param name Patient name
     * @param id Patient ID
     * @return true if removed successfully
     */
    public boolean removeByNameAndId(String name, String id) {
        if (root == null) return false;
        
        boolean[] removed = new boolean[1];
        root = removeRec(root, name, id, removed);
        
        if (removed[0]) {
            nodeCount--;
        }
        
        return removed[0];
    }
    
    /**
    * Recursively removes a specific patient from the AVL tree using
    * both name and ID to ensure exact matching.
    *
    * After the deletion, the method updates node heights and
    * rebalances the tree to preserve AVL properties.
    */
    private AVLNode removeRec(AVLNode node, String name, String id, boolean[] removed) {
        if (node == null) return null;
        
        int nameComp = name.compareToIgnoreCase(node.patient.getName());
        
        if (nameComp < 0) {
            node.left = removeRec(node.left, name, id, removed);
        } else if (nameComp > 0) {
            node.right = removeRec(node.right, name, id, removed);
        } else {
            // Names match, check ID
            int idComp = id.compareTo(node.patient.getId());
            if (idComp < 0) {
                node.left = removeRec(node.left, name, id, removed);
            } else if (idComp > 0) {
                node.right = removeRec(node.right, name, id, removed);
            } else {
                // Found the exact patient
                removed[0] = true;
                
                // Node with only one child or no child
                if (node.left == null || node.right == null) {
                    AVLNode temp = (node.left != null) ? node.left : node.right;
                    
                    // No child case
                    if (temp == null) {
                        node = null;
                    } else {
                        // One child case
                        node = temp;
                    }
                } else {
                    // Node with two children: get inorder successor
                    AVLNode temp = minValueNode(node.right);
                    node.patient = temp.patient;
                    node.right = removeRec(node.right, temp.patient.getName(), temp.patient.getId(), new boolean[1]);
                }
            }
        }
        
        if (node == null) return null;
        
        // Update height and balance
        updateHeight(node);
        return balance(node);
    }
    
    /**
    * Finds the node with the minimum value in an AVL tree.
    * Used during deletion to locate the in-order successor.
    */
    private AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }
    
    /**
     * Searches for patients by name
     * Returns all patients with matching name
     * Time Complexity: O(log n + m) where m = number of matches
     * @param name Name to search for
     * @return List of patients with matching name
     */
    public List<Patient> searchAllByName(String name) {
        List<Patient> results = new ArrayList<>();
        searchAllByNameRec(root, name, results);
        return results;
    }
    
    /**
    * Recursively searches the AVL tree for all patients with the given name.
    * Handles duplicate names by exploring both subtrees when a match is found.
    */
    private void searchAllByNameRec(AVLNode node, String name, List<Patient> results) {
        if (node == null) {
            return;
        }
        
        int comparison = name.compareToIgnoreCase(node.patient.getName());
        
        if (comparison == 0) {
            results.add(node.patient);
            // Search both sides for additional matches (duplicate names)
            searchAllByNameRec(node.left, name, results);
            searchAllByNameRec(node.right, name, results);
        } else if (comparison < 0) {
            searchAllByNameRec(node.left, name, results);
        } else {
            searchAllByNameRec(node.right, name, results);
        }
    }
    
    /**
     * Performs in-order traversal (sorted by name)
     * @return List of patients in sorted order
     */
    public List<Patient> inOrderTraversal() {
        List<Patient> result = new ArrayList<>();
        inOrderRec(root, result);
        return result;
    }
    
    /**
    * Performs an in-order traversal of the AVL tree.
    * This traversal returns patients sorted by name.
    */
    private void inOrderRec(AVLNode node, List<Patient> result) {
        if (node != null) {
            inOrderRec(node.left, result);
            result.add(node.patient);
            inOrderRec(node.right, result);
        }
    }
    
    // ==================== QUERY METHODS ====================
    
    /**
     * Returns the total number of nodes in the tree
     * @return Node count
     */
    public int getNodeCount() {
        return nodeCount;
    }
    
    /**
     * Returns the height of the tree
     * @return Tree height
     */
    public int getHeight() {
        return height(root);
    }
    
    /**
     * Checks if the tree is balanced (AVL property)
     * @return true if balanced, false otherwise
     */
    public boolean isBalanced() {
        return isBalancedRec(root);
    }
    
    /**
    * Recursively checks whether the AVL tree is balanced.
    * Ensures that the height difference of subtrees is at most one.
    */
    private boolean isBalancedRec(AVLNode node) {
        if (node == null) return true;
        
        int balance = getBalance(node);
        return Math.abs(balance) <= 1 && 
               isBalancedRec(node.left) && 
               isBalancedRec(node.right);
    }
    
    /**
     * Checks if tree is empty
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }
    
    /**
     * Returns balance report
     * @return String describing tree balance
     */
    public String getBalanceReport() {
        if (root == null) return "EMPTY TREE";
        
        int balance = getBalance(root);
        if (Math.abs(balance) <= 1) {
            return "PERFECTLY BALANCED (balance: " + balance + ")";
        } else if (Math.abs(balance) <= 2) {
            return "SLIGHTLY IMBALANCED (balance: " + balance + ")";
        } else {
            return "SERIOUSLY IMBALANCED (balance: " + balance + ")";
        }
    }
    
    // ==================== STATE MANAGEMENT ====================
    
    /**
     * Creates a deep copy of the AVL tree
     * @return New MyAVLTree with same structure
     */
    public AVLTree deepCopy() {
        AVLTree copy = new AVLTree();
        copy.root = deepCopyRec(this.root);
        copy.nodeCount = this.nodeCount;
        return copy;
    }
    
    /**
    * Creates a deep copy of the AVL tree starting from the given node.
    * This method is used to safely preserve tree states for the undo mechanism.
    */
    private AVLNode deepCopyRec(AVLNode node) {
        if (node == null) return null;
        
        AVLNode copy = node.deepCopy();
        copy.left = deepCopyRec(node.left);
        copy.right = deepCopyRec(node.right);
        return copy;
    }
    
    /**
     * Restores tree from a copy
     * @param source Source tree to restore from
     */
    public void restoreFromCopy(AVLTree source) {
        this.root = deepCopyRec(source.root);
        this.nodeCount = source.nodeCount;
    }
}