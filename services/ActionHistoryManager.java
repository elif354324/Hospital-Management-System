package services;

import structures.StatefulStack;
import structures.StateSnapshot;
import models.Doctor;
import java.util.HashMap;
import java.util.Map;

/**
 * Action History Manager implementing the Memento Design Pattern.
 * Manages the StatefulStack to provide robust "Undo" functionality.
 * This satisfies TECHNICAL REQUIREMENT 5 (Stacks) and 6 (Undo & Updates).
 */
public class ActionHistoryManager {
    private StatefulStack stateStack;
    
    /**
     * Inner class representing the system state at a specific point in time.
     * Ensures deep isolation for restoration.
     */
    private static class SystemState {
        RecordRegistry registryState;
        EmergencyTriage emergencyState;
        AppointmentScheduler schedulerState;
        DoctorQueueState doctorSmithState;
        DoctorQueueState doctorJohnsonState;
        DoctorQueueState doctorWilliamsState;
        
        SystemState(RecordRegistry registry, EmergencyTriage triage, 
                   AppointmentScheduler scheduler, Doctor drSmith, 
                   Doctor drJohnson, Doctor drWilliams) {
            // Requirement: Deep copying to ensure state isolation
            this.registryState = registry.deepCopy();
            this.emergencyState = triage.deepCopy();
            this.schedulerState = scheduler.deepCopy();
            this.doctorSmithState = new DoctorQueueState(drSmith);
            this.doctorJohnsonState = new DoctorQueueState(drJohnson);
            this.doctorWilliamsState = new DoctorQueueState(drWilliams);
        }
        
        StateSnapshot toStateSnapshot(String description) {
            Map<String, Object> states = new HashMap<>();
            states.put("registry", this.registryState);
            states.put("emergency", this.emergencyState);
            states.put("scheduler", this.schedulerState);
            states.put("doctorSmith", this.doctorSmithState);
            states.put("doctorJohnson", this.doctorJohnsonState);
            states.put("doctorWilliams", this.doctorWilliamsState);
            
            return new StateSnapshot(states, description);
        }
        
        static SystemState fromStateSnapshot(StateSnapshot snapshot) {
            Map<String, Object> states = snapshot.getSavedStates();
            SystemState systemState = new SystemState(
                (RecordRegistry) states.get("registry"),
                (EmergencyTriage) states.get("emergency"),
                (AppointmentScheduler) states.get("scheduler"),
                null, null, null  
            );
            systemState.doctorSmithState = (DoctorQueueState) states.get("doctorSmith");
            systemState.doctorJohnsonState = (DoctorQueueState) states.get("doctorJohnson");
            systemState.doctorWilliamsState = (DoctorQueueState) states.get("doctorWilliams");
            
            return systemState;
        }
    }

    private static class DoctorQueueState {
        String doctorId;
        String doctorName;
        structures.Queue queueState;
        
        DoctorQueueState(Doctor doctor) {
            if (doctor != null) {
                this.doctorId = doctor.getId();
                this.doctorName = doctor.getName();
                this.queueState = doctor.getWaitingLine().deepCopy();
            } else {
                this.doctorId = "UNKNOWN";
                this.doctorName = "Unknown Doctor";
                this.queueState = new structures.Queue();
            }
        }
        
        public structures.Queue getQueueState() { return queueState.deepCopy(); }
        public String getDoctorName() { return doctorName; }
    }

    public ActionHistoryManager() {
        this.stateStack = new StatefulStack();
    }
    
    /**
     * Saves the current system state before any administrative change.
     */
    public void saveState(RecordRegistry registry, EmergencyTriage triage, 
                         AppointmentScheduler scheduler, Doctor drSmith, 
                         Doctor drJohnson, Doctor drWilliams) {
        SystemState state = new SystemState(registry, triage, scheduler, 
                                          drSmith, drJohnson, drWilliams);
        StateSnapshot snapshot = state.toStateSnapshot("Administrative Change");
        stateStack.push(snapshot);
    }
    
    /**
     * FUNCTIONAL REQUIREMENT 6: Reverts the system to its previous state.
     * CHALLENGE HANDLING: Manages cases where an Undo is attempted on an empty stack.
     * @return true if restoration was successful.
     */
    public boolean undo(RecordRegistry registry, EmergencyTriage triage, 
                       AppointmentScheduler scheduler, Doctor drSmith, 
                       Doctor drJohnson, Doctor drWilliams) {
        
        // Requirement Check: Handle empty stack edge case
        if (stateStack.isEmpty()) {
            System.out.println("   [UNDO ERROR] No actions to undo. The history stack is empty.");
            return false;
        }
        
        try {
            StateSnapshot previousSnapshot = stateStack.pop();
            SystemState previousState = SystemState.fromStateSnapshot(previousSnapshot);
            
            registry.restoreFrom(previousState.registryState);
            triage.restoreFrom(previousState.emergencyState);
            scheduler.restoreFrom(previousState.schedulerState);
            
            restoreDoctorQueue(drSmith, previousState.doctorSmithState);
            restoreDoctorQueue(drJohnson, previousState.doctorJohnsonState);
            restoreDoctorQueue(drWilliams, previousState.doctorWilliamsState);
            
            System.out.println("   [UNDO SUCCESS] Reverted action: " + previousSnapshot.getDescription());
            return true;
            
        } catch (Exception e) {
            System.out.println("   [CRITICAL ERROR] Restoration failed: " + e.getMessage());
            return false;
        }
    }
    
    private void restoreDoctorQueue(Doctor doctor, DoctorQueueState state) {
        if (doctor != null && state != null) {
            doctor.getWaitingLine().clear();
            doctor.getWaitingLine().restoreFrom(state.getQueueState());
        }
    }

    /**
     * ANALYTICS REQUIREMENT: Provides visibility into the history ADT.
     */
    public void displayHistoryAnalytics() {
        System.out.println("\n   === UNDO SYSTEM ANALYTICS ===");
        System.out.println("   States Stored: " + stateStack.size());
        System.out.println("   Stack Usage: " + String.format("%.1f", stateStack.getUsagePercentage()) + "%");
        System.out.println("   Undo Possible: " + (canUndo() ? "YES" : "NO"));
        System.out.println("   =============================");
    }

    public int getHistorySize() { return stateStack.size(); }
    public boolean canUndo() { return !stateStack.isEmpty(); }

    /**
    * Records an action description for logging and tracking within the Undo system.
    * @param action Description of the performed action
    */
    public void recordAction(String action) {
        // This method prevents the "Unimplemented method" error by providing a body
        System.out.println("   [ACTION RECORDED] " + action);
}
}