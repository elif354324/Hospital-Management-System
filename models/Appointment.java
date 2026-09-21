package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a hospital appointment.
 *
 * Appointment stores:
 * - Patient
 * - Doctor
 * - Date
 * - Time
 * - Current appointment status
 * - Important lifecycle timestamps
 *
 * Appointment lifecycle:
 *
 * SCHEDULED
 *      ↓
 * WAITING
 *      ↓
 * CALLED
 *      ↓
 * ARRIVED
 *      ↓
 * IN_PROGRESS
 *      ↓
 * COMPLETED
 *
 * Alternative endings:
 * SCHEDULED / WAITING → NO_SHOW
 * SCHEDULED / WAITING → CANCELLED
 */
public class Appointment {

    private Patient patient;
    private Doctor doctor;
    private LocalTime appointmentTime;
    private String date;

    private AppointmentStatus status;

    // ==================== LIFECYCLE TIMESTAMPS ====================

    private LocalDateTime calledAt;
    private LocalDateTime arrivedAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Creates a new appointment.
     *
     * New appointments always start as SCHEDULED.
     */
    public Appointment(
            Patient patient,
            Doctor doctor,
            LocalTime time,
            String date) {

        this.patient = patient;
        this.doctor = doctor;
        this.appointmentTime = time;
        this.date = date;

        this.status = AppointmentStatus.SCHEDULED;
    }

    // ==================== GETTERS ====================

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalTime getTime() {
        return appointmentTime;
    }

    public String getDate() {
        return date;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public LocalDateTime getCalledAt() {
        return calledAt;
    }

    public LocalDateTime getArrivedAt() {
        return arrivedAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    // ==================== SETTERS ====================

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // ==================== STATUS MANAGEMENT ====================

    /**
     * Changes the appointment status.
     *
     * Lifecycle timestamps are automatically recorded
     * when the corresponding state is reached.
     */
    public void setStatus(AppointmentStatus status) {

        if (status == null) {
            return;
        }

        this.status = status;

        LocalDateTime now = LocalDateTime.now();

        switch (status) {

            case CALLED:
                if (this.calledAt == null) {
                    this.calledAt = now;
                }
                break;

            case ARRIVED:
                if (this.arrivedAt == null) {
                    this.arrivedAt = now;
                }
                break;

            case IN_PROGRESS:
                if (this.startedAt == null) {
                    this.startedAt = now;
                }
                break;

            case COMPLETED:
                if (this.completedAt == null) {
                    this.completedAt = now;
                }
                break;

            default:
                break;
        }
    }

    // ==================== DATE / TIME ====================

    /**
     * Returns the complete appointment date and time.
     */
    public LocalDateTime getDateTime() {

        LocalDate appointmentDate =
                LocalDate.parse(date, DATE_FORMAT);

        return LocalDateTime.of(
                appointmentDate,
                appointmentTime
        );
    }

    /**
     * Checks whether the appointment belongs to today.
     */
    public boolean isToday() {

        return getDateTime()
                .toLocalDate()
                .equals(LocalDate.now());
    }

    /**
     * Checks whether the appointment time has arrived.
     */
    public boolean isTimeReached() {

        return !getDateTime()
                .isAfter(LocalDateTime.now());
    }

    /**
     * Checks whether the appointment is in the past.
     */
    public boolean isPast() {

        return getDateTime()
                .isBefore(LocalDateTime.now());
    }

    /**
     * Returns how many minutes have passed since the
     * scheduled appointment time.
     */
    public long getMinutesPastAppointmentTime() {

        if (!isPast()) {
            return 0;
        }

        return java.time.Duration.between(
                getDateTime(),
                LocalDateTime.now()
        ).toMinutes();
    }

    // ==================== WAITING QUEUE ====================

    /**
     * Determines whether this appointment can currently
     * enter the hospital waiting queue.
     *
     * Rules:
     * - Must be today's appointment.
     * - Appointment time must have arrived.
     * - Status must still be SCHEDULED or WAITING.
     */
    public boolean canEnterWaitingQueue() {

        if (!isToday()) {
            return false;
        }

        if (!isTimeReached()) {
            return false;
        }

        return status == AppointmentStatus.SCHEDULED
                || status == AppointmentStatus.WAITING;
    }

    /**
     * Determines whether the appointment has become stale.
     *
     * The scheduler uses a grace period before marking
     * an unattended appointment as NO_SHOW.
     */
    public boolean isPastGracePeriod(long gracePeriodMinutes) {

        if (gracePeriodMinutes < 0) {
            return false;
        }

        if (!isPast()) {
            return false;
        }

        return getMinutesPastAppointmentTime()
                >= gracePeriodMinutes;
    }

    // ==================== COMPLETION ====================

    /**
     * Returns true if the appointment has reached a final state.
     */
    public boolean isFinished() {

        return status == AppointmentStatus.COMPLETED
                || status == AppointmentStatus.NO_SHOW
                || status == AppointmentStatus.CANCELLED;
    }

    /**
     * Returns true if the appointment is still active.
     */
    public boolean isActive() {

        return !isFinished();
    }

    // ==================== DISPLAY ====================

    @Override
    public String toString() {

        String patientName =
                patient != null
                        ? patient.getName()
                        : "Unknown Patient";

        String doctorName =
                doctor != null
                        ? doctor.getName()
                        : "Unknown Doctor";

        return String.format(
                "%s with %s at %s on %s [%s]",
                patientName,
                doctorName,
                appointmentTime,
                date,
                status
        );
    }

    // ==================== DEEP COPY ====================

    /**
     * Creates a deep copy of this appointment.
     */
    public Appointment deepCopy() {

        Patient copiedPatient =
                patient != null
                        ? patient.deepCopy()
                        : null;

        Appointment copy =
                new Appointment(
                        copiedPatient,
                        this.doctor,
                        this.appointmentTime,
                        this.date
                );

        copy.status = this.status;
        copy.calledAt = this.calledAt;
        copy.arrivedAt = this.arrivedAt;
        copy.startedAt = this.startedAt;
        copy.completedAt = this.completedAt;

        return copy;
    }
}