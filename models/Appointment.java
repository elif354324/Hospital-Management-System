package models;

import java.time.LocalTime;

/**
 * Appointment class with time slot management
 * Represents a scheduled appointment with specific time
 */
public class Appointment {
    private Patient patient;
    private Doctor doctor;
    private LocalTime appointmentTime;
    private String date;
    
    /**
     * Creates a new appointment with time slot
     * @param patient The patient
     * @param doctor The doctor
     * @param time Appointment time
     * @param date Appointment date
     */
    public Appointment(Patient patient, Doctor doctor, LocalTime time, String date) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentTime = time;
        this.date = date;
    }
    
    // ==================== GETTER METHODS ====================
    
    /**
     * Returns appointment patient
     * @return Patient object
     */
    public Patient getPatient() { 
        return patient; 
    }
    
    /**
     * Returns appointment doctor
     * @return Doctor object
     */
    public Doctor getDoctor() { 
        return doctor; 
    }
    
    /**
     * Returns appointment time
     * @return LocalTime object
     */
    public LocalTime getTime() { 
        return appointmentTime; 
    }
    
    /**
     * Returns appointment date
     * @return Date string
     */
    public String getDate() { 
        return date; 
    }

    // ==================== SETTER METHODS ====================

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    /**
     * Returns appointment information
     * @return Formatted appointment info
     */
    @Override
    public String toString() {
        return String.format("%s with %s at %s on %s",
                           patient.getName(), 
                           doctor.getName(), // Doctor name already includes "Dr."
                           appointmentTime.toString(), 
                           date);
    }
    
    /**
     * Creates a deep copy of the appointment
     * @return New Appointment object
     */
    public Appointment deepCopy() {
        return new Appointment(
            this.patient.deepCopy(),
            this.doctor, // Note: Doctor is not deep copied
            this.appointmentTime,
            this.date
        );
    }
}