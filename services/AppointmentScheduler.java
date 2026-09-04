package services;

import models.*;
import structures.Queue;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Appointment Scheduler with time slot management.
 * * ADT Usage: 
 * - MyQueue (FIFO): Ensures standard waiting room order as per requirements.
 * - HashMap: Provides O(1) instantaneous lookup for appointment details by Patient ID.
 */
public class AppointmentScheduler {
    private Queue appointmentQueue;
    private Map<String, Appointment> scheduledAppointments; // Patient ID -> Appointment
    private static final DateTimeFormatter TIME_FORMAT = 
        DateTimeFormatter.ofPattern("HH:mm");
    
    /**
     * Creates a new AppointmentScheduler.
     * Initializes the FIFO queue and the hash-based lookup table.
     */
    public AppointmentScheduler() {
        this.appointmentQueue = new Queue();
        this.scheduledAppointments = new HashMap<>();
    }
    
    /**
     * FUNCTIONAL REQUIREMENT 2: Books appointment with specific time slot.
     * Challenges: Managing time-based data within a FIFO structure.
     * * @param patient Patient to book
     * @param doctor Assigned doctor
     * @param timeStr Time string (HH:mm)
     * @param date Date string
     * @return true if booked successfully
     */
    public boolean bookAppointmentWithTime(Patient patient, Doctor doctor, String timeStr, String date) {
        try {
            // Requirement: Handle time management for scheduled visits
            LocalTime time = LocalTime.parse(timeStr, TIME_FORMAT);
            Appointment appointment = new Appointment(patient, doctor, time, date);
            
            // ADT: Enqueue for standard waiting line (FIFO)
            appointmentQueue.enqueue(patient);
            
            // ADT: Instant lookup for analytics and directory search
            scheduledAppointments.put(patient.getId(), appointment);
            
            System.out.println("   [APPOINTMENT] Time slot booked:");
            System.out.println("     Patient: " + patient.getName());
            System.out.println("     Doctor: " + doctor.getName());
            System.out.println("     Time: " + timeStr);
            System.out.println("     Date: " + date);
            return true;
            
        } catch (DateTimeParseException e) {
            System.out.println("   [ERROR] Invalid time format. Use HH:mm (e.g., 14:30)");
            return false;
        }
    }
    
    /**
     * Standard booking for backward compatibility.
     * @param patient Patient to book
     */
    public void bookAppointment(Patient patient) {
        appointmentQueue.enqueue(patient);
        System.out.println("   [APPOINTMENT] Booked for: " + patient.getName() +
                         " | Queue position: " + appointmentQueue.size());
    }
    
    /**
     * FUNCTIONAL REQUIREMENT 5: Retrieval of appointment details by patient ID.
     * Time Complexity: O(1) average case using Hash Map.
     * * @param patientId Patient ID
     * @return Appointment details or null if not found
     */
    public Appointment getAppointmentDetails(String patientId) {
        return scheduledAppointments.get(patientId);
    }
    
    /**
     * Calls the next patient from the FIFO queue.
     * Ensures that once treated, the record is updated across all structures.
     * * @return Next patient or null if queue empty
     */
    public Patient callNextPatient() {

    Patient nextPatient =
            appointmentQueue.dequeue();

    if (nextPatient != null) {

        Appointment appointment =
                scheduledAppointments.remove(
                        nextPatient.getId()
                );

        if (appointment != null
                && appointment.getDoctor() != null) {

            appointment.getDoctor()
                    .removePatientFromQueue(
                            nextPatient.getId()
                    );
        }

        System.out.println(
                "   [APPOINTMENT] Calling next patient: "
                        + nextPatient.getName()
        );

    } else {

        System.out.println(
                "   [APPOINTMENT] No patients in queue."
        );
    }

    return nextPatient;
}
    
    /**
     * Views next patient without removing from queue (O(1)).
     * @return Next patient or null if queue empty
     */
    public Patient peekNextPatient() {
        return appointmentQueue.peek();
    }
    
    /**
     * FUNCTIONAL REQUIREMENT 5: Generates status reports for the waiting line.
     */
    public void displayQueue() {
        System.out.println("\n   === APPOINTMENT QUEUE (FIFO Status Report) ===");
        
        if (appointmentQueue.isEmpty()) {
            System.out.println("   No patients currently in the waiting line.");
            System.out.println("   Next available slot: Immediate intake available.");
        } else {
            System.out.println("   Total Waiting: " + appointmentQueue.size() + " patients");
            System.out.println("   " + "-".repeat(60));
            
            Patient nextPatient = appointmentQueue.peek();
            if (nextPatient != null) {
                Appointment appointment = scheduledAppointments.get(nextPatient.getId());
                System.out.println("   UP NEXT: " + nextPatient.getName());
                
                if (appointment != null) {
                    System.out.println("   Details: Dr. " + appointment.getDoctor().getName() + 
                                       " at " + appointment.getTime().format(TIME_FORMAT));
                }
            }
            
            // Analytic calculation for hospital management
            System.out.println("   Estimated wait time: " + 
                             (appointmentQueue.size() * 15) + " minutes");
        }
        System.out.println("   ===============================================");
    }
    
    /**
     * Comprehensive search/report for all scheduled time slots.
     */
    public void displayAllAppointments() {
        System.out.println("\n   === ANALYTICS: ALL SCHEDULED TIME SLOTS ===");
        
        if (scheduledAppointments.isEmpty()) {
            System.out.println("   No appointments recorded in the system.");
        } else {
            System.out.println("   Scheduled Records: " + scheduledAppointments.size());
            System.out.println("   " + "-".repeat(60));
            
            int count = 1;
            for (Appointment appointment : scheduledAppointments.values()) {
                System.out.printf("   %2d. %-20s | Dr. %-15s | %s | %s\n",
                                count++,
                                appointment.getPatient().getName(),
                                appointment.getDoctor().getName(),
                                appointment.getTime().format(TIME_FORMAT),
                                appointment.getDate());
            }
        }
        System.out.println("   ======================================");
    }
    
    // ==================== QUERY METHODS ====================
    
    public int getQueueSize() { return appointmentQueue.size(); }
    public int getScheduledAppointmentsCount() { return scheduledAppointments.size(); }
    public boolean hasWaitingPatients() { return appointmentQueue.size() > 0; }
    public boolean isEmpty() { return appointmentQueue.isEmpty(); }
    public boolean hasAppointment(String patientId) { return scheduledAppointments.containsKey(patientId); }

    public boolean updateAppointment(
        String patientId,
        Doctor newDoctor,
        String newTimeStr,
        String newDate) {

    if (patientId == null || patientId.trim().isEmpty()) {
        return false;
    }

    Appointment appointment =
            scheduledAppointments.get(patientId);

    if (appointment == null) {
        return false;
    }

    try {
        LocalTime newTime =
                LocalTime.parse(newTimeStr, TIME_FORMAT);

        Doctor oldDoctor =
                appointment.getDoctor();

        // Eski doktorun bekleme kuyruğundan çıkar
        if (oldDoctor != null) {
            oldDoctor.removePatientFromQueue(patientId);
        }

        // Randevu bilgilerini güncelle
        appointment.setDoctor(newDoctor);
        appointment.setTime(newTime);
        appointment.setDate(newDate);

        // Yeni doktorun kuyruğuna ekle
        if (newDoctor != null) {
            Patient patient =
                    appointment.getPatient();

            newDoctor.addPatientToQueue(patient);
        }

        System.out.println(
                "   [APPOINTMENT] Appointment updated for: "
                        + appointment.getPatient().getName()
        );

        return true;

    } catch (DateTimeParseException e) {

        System.out.println(
                "   [ERROR] Invalid time format. Use HH:mm"
        );

        return false;
    }
}

    public boolean cancelAppointment(String patientId) {

        if (patientId == null || patientId.trim().isEmpty()) {
            return false;
        }

        Appointment appointment =
                scheduledAppointments.get(patientId);

        if (appointment == null) {
            return false;
        }

        // Ana appointment kaydından çıkar
        scheduledAppointments.remove(patientId);

        // Ana FIFO kuyruğundan çıkar
        appointmentQueue.removePatient(patientId);

        // Doktorun kendi bekleme kuyruğundan çıkar
        Doctor doctor =
                appointment.getDoctor();

        if (doctor != null) {
            doctor.removePatientFromQueue(patientId);
        }

        System.out.println(
                "   [APPOINTMENT] Cancelled appointment for: "
                        + appointment.getPatient().getName()
        );

        return true;
    }

    public List<Appointment> getAllAppointments() {
    return new ArrayList<>(
            scheduledAppointments.values()
    );
    }
    
    // ==================== STATE MANAGEMENT (UNDO SUPPORT) ====================
    
    /**
     * TECHNICAL REQUIREMENT 6: Support for Undo via State Preservation.
     * Creates a deep copy of the scheduler state.
     * * @return New AppointmentScheduler with same state
     */
    public AppointmentScheduler deepCopy() {
        AppointmentScheduler copy = new AppointmentScheduler();
        copy.appointmentQueue = this.appointmentQueue.deepCopy();
        
        // Deep copy the hash map to ensure Memento isolation
        for (Map.Entry<String, Appointment> entry : this.scheduledAppointments.entrySet()) {
            copy.scheduledAppointments.put(entry.getKey(), entry.getValue().deepCopy());
        }
        
        return copy;
    }
    
    /**
     * Restores state after an administrative error is undone.
     * @param source Source scheduler to restore from
     */
    public void restoreFrom(AppointmentScheduler source) {
        this.appointmentQueue = source.appointmentQueue.deepCopy();
        this.scheduledAppointments.clear();
        for (Map.Entry<String, Appointment> entry : source.scheduledAppointments.entrySet()) {
            this.scheduledAppointments.put(entry.getKey(), entry.getValue().deepCopy());
        }
        System.out.println("   [RESTORE] Appointment scheduler state synchronized.");
    }
}