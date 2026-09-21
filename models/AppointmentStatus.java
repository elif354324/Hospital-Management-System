package models;

/**
 * Represents the lifecycle of a hospital appointment.
 */
public enum AppointmentStatus {

    /**
     * Appointment exists but its time has not arrived yet.
     */
    SCHEDULED,

    /**
     * Appointment time has arrived and patient is waiting.
     */
    WAITING,

    /**
     * Patient has been called by the doctor/staff.
     */
    CALLED,

    /**
     * Patient arrived after being called.
     */
    ARRIVED,

    /**
     * Doctor is currently examining the patient.
     */
    IN_PROGRESS,

    /**
     * Consultation has been completed.
     */
    COMPLETED,

    /**
     * Patient did not attend the appointment.
     */
    NO_SHOW,

    /**
     * Appointment was cancelled.
     */
    CANCELLED
}