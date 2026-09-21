package services;

import models.Appointment;
import models.AppointmentStatus;
import models.Doctor;
import models.Patient;
import structures.Queue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Appointment Scheduler with time slot and appointment lifecycle management.
 *
 * ADT Usage:
 * - Queue (FIFO): Maintains the active hospital waiting line.
 * - HashMap: Provides O(1) average appointment lookup by Patient ID.
 *
 * Appointment records are NEVER deleted when:
 * - patient is called
 * - appointment is completed
 * - appointment is cancelled
 * - patient becomes a no-show
 *
 * This allows the system to maintain appointment history.
 */
public class AppointmentScheduler {

    private Queue appointmentQueue;

    /**
     * Appointment records.
     *
     * Current project architecture uses Patient ID as the key.
     */
    private Map<String, Appointment> scheduledAppointments;

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm");

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Grace period before an unattended appointment
     * is automatically considered NO_SHOW.
     *
     * Example:
     * 10:00 appointment
     * 10:00 - 10:14 → may remain WAITING
     * 10:15+ → NO_SHOW if patient has not progressed
     */
    private static final long NO_SHOW_GRACE_PERIOD_MINUTES = 15;

    /**
     * Creates a new AppointmentScheduler.
     */
    public AppointmentScheduler() {

        this.appointmentQueue =
                new Queue();

        this.scheduledAppointments =
                new HashMap<>();
    }

    // ============================================================
    // APPOINTMENT BOOKING
    // ============================================================

    /**
     * FUNCTIONAL REQUIREMENT 2:
     * Books an appointment with a specific time slot.
     *
     * @param patient Patient to book
     * @param doctor Assigned doctor
     * @param timeStr Time string (HH:mm)
     * @param date Date string (yyyy-MM-dd)
     * @return true if booked successfully
     */
    public boolean bookAppointmentWithTime(
            Patient patient,
            Doctor doctor,
            String timeStr,
            String date) {

        if (patient == null || doctor == null) {
            return false;
        }

        if (patient.getId() == null
                || patient.getId().trim().isEmpty()) {
            return false;
        }

        if (timeStr == null
                || timeStr.trim().isEmpty()) {
            return false;
        }

        if (date == null
                || date.trim().isEmpty()) {
            return false;
        }

        String patientId =
                patient.getId().trim();

        String cleanTime =
                timeStr.trim();

        String cleanDate =
                date.trim();

        /*
         * Current architecture allows one active appointment
         * per patient.
         */
        if (scheduledAppointments.containsKey(patientId)) {

            Appointment existing =
                    scheduledAppointments.get(patientId);

            if (existing != null
                    && !existing.isFinished()) {

                System.out.println(
                        "   [APPOINTMENT] Patient already has "
                                + "an active appointment: "
                                + patient.getName()
                );

                return false;
            }

            /*
             * If the previous appointment is finished,
             * allow a new appointment.
             */
            scheduledAppointments.remove(patientId);
        }

        try {

            LocalTime time =
                    LocalTime.parse(
                            cleanTime,
                            TIME_FORMAT
                    );

            LocalDate parsedDate =
                    LocalDate.parse(
                            cleanDate,
                            DATE_FORMAT
                    );

            // Prevent booking in the past.
            LocalDateTime appointmentDateTime =
                    LocalDateTime.of(
                            parsedDate,
                            time
                    );

            if (appointmentDateTime.isBefore(
                    LocalDateTime.now())) {

                System.out.println(
                        "   [APPOINTMENT] Cannot book "
                                + "an appointment in the past."
                );

                return false;
            }

            /*
             * Prevent doctor double-booking.
             */
            if (isDoctorSlotOccupied(
                    doctor,
                    cleanDate,
                    time,
                    null)) {

                System.out.println(
                        "   [APPOINTMENT] Doctor already has "
                                + "an appointment at this time."
                );

                return false;
            }

            Appointment appointment =
                    new Appointment(
                            patient,
                            doctor,
                            time,
                            cleanDate
                    );

            /*
             * Store appointment record.
             *
             * IMPORTANT:
             * Do NOT put it directly into the waiting queue.
             * It only enters the queue when its time arrives.
             */
            scheduledAppointments.put(
                    patientId,
                    appointment
            );

            System.out.println(
                    "   [APPOINTMENT] Time slot booked:"
            );

            System.out.println(
                    "     Patient: "
                            + patient.getName()
            );

            System.out.println(
                    "     Doctor: "
                            + doctor.getName()
            );

            System.out.println(
                    "     Time: "
                            + time.format(TIME_FORMAT)
            );

            System.out.println(
                    "     Date: "
                            + cleanDate
            );

            System.out.println(
                    "     Status: SCHEDULED"
            );

            /*
             * Synchronize in case the appointment time
             * is immediately due.
             */
            synchronizeWaitingQueue();

            return true;

        } catch (DateTimeParseException e) {

            System.out.println(
                    "   [ERROR] Invalid appointment date/time."
            );

            System.out.println(
                    "   Time format: HH:mm (e.g., 14:30)"
            );

            System.out.println(
                    "   Date format: yyyy-MM-dd "
                            + "(e.g., 2026-09-04)"
            );

            return false;
        }
    }

    /**
     * Standard booking for backward compatibility.
     */
    public void bookAppointment(Patient patient) {

        if (patient == null) {
            return;
        }

        /*
         * This legacy method does not contain doctor/date/time
         * information, so it only adds the patient to the
         * existing FIFO queue.
         */
        if (!appointmentQueue.contains(patient.getId())) {

            appointmentQueue.enqueue(patient);

            System.out.println(
                    "   [APPOINTMENT] Booked for: "
                            + patient.getName()
                            + " | Queue position: "
                            + appointmentQueue.size()
            );
        }
    }

    // ============================================================
    // WAITING QUEUE SYNCHRONIZATION
    // ============================================================

    /**
     * Synchronizes the active waiting queue with appointment
     * date/time and lifecycle status.
     *
     * This method should be called:
     * - when Dashboard opens
     * - when Appointment page opens
     * - after booking
     * - after updating an appointment
     * - after application restart
     *
     * Rules:
     *
     * Future appointment:
     *     SCHEDULED
     *
     * Today's appointment whose time has arrived:
     *     WAITING
     *
     * Today's appointment that passed the grace period
     * without progressing:
     *     NO_SHOW
     *
     * Completed/cancelled/no-show:
     *     Never enters waiting queue.
     */
    public void synchronizeWaitingQueue() {

        /*
         * Rebuild the active queue from appointment records.
         *
         * This prevents stale queue entries after restart,
         * update, cancel, or lifecycle changes.
         */
        appointmentQueue.clear();

        LocalDateTime now =
                LocalDateTime.now();

        for (Appointment appointment :
                scheduledAppointments.values()) {

            if (appointment == null) {
                continue;
            }

            if (appointment.getPatient() == null) {
                continue;
            }

            if (appointment.getDoctor() == null) {
                continue;
            }

            AppointmentStatus status =
                    appointment.getStatus();

            /*
             * Final states never enter the queue.
             */
            if (appointment.isFinished()) {
                continue;
            }

            /*
             * Future appointment.
             *
             * IMPORTANT:
             * Never downgrade an appointment that has already
             * progressed in its lifecycle.
             */
            if (appointment.getDateTime().isAfter(now)) {

                if (status == AppointmentStatus.SCHEDULED) {
                    continue;
                }

                continue;
            }

            /*
             * If appointment is already CALLED, ARRIVED or
             * IN_PROGRESS, do not put it back into the
             * waiting queue.
             */
            if (status == AppointmentStatus.CALLED
                    || status == AppointmentStatus.ARRIVED
                    || status == AppointmentStatus.IN_PROGRESS) {

                continue;
            }

            /*
             * Appointment time has arrived.
             *
             * If the appointment is still SCHEDULED/WAITING
             * and exceeds the grace period, mark NO_SHOW.
             */
            if (appointment.isPastGracePeriod(
                    NO_SHOW_GRACE_PERIOD_MINUTES)) {

                appointment.setStatus(
                        AppointmentStatus.NO_SHOW
                );

                System.out.println(
                        "   [APPOINTMENT] Automatically marked "
                                + "as NO_SHOW: "
                                + appointment.getPatient().getName()
                );

                continue;
            }

            /*
             * Appointment is currently eligible for waiting.
             */
            if (appointment.canEnterWaitingQueue()) {

                appointment.setStatus(
                        AppointmentStatus.WAITING
                );

                Patient patient =
                        appointment.getPatient();

                /*
                 * Prevent duplicate patient entries.
                 */
                if (!appointmentQueue.contains(
                        patient.getId())) {

                    appointmentQueue.enqueue(
                            patient
                    );
                }

                /*
                 * Keep doctor's runtime waiting line synchronized.
                 */
                Doctor doctor =
                        appointment.getDoctor();

                if (!doctor.getWaitingLine()
                        .contains(patient.getId())) {

                    doctor.addPatientToQueue(
                            patient
                    );
                }
            }
        }
    }

    // ============================================================
    // APPOINTMENT RETRIEVAL
    // ============================================================

    /**
     * FUNCTIONAL REQUIREMENT 5:
     * Retrieves appointment details by patient ID.
     *
     * Time Complexity:
     * O(1) average case.
     */
    public Appointment getAppointmentDetails(
            String patientId) {

        if (patientId == null) {
            return null;
        }

        synchronizeWaitingQueue();

        return scheduledAppointments.get(
                patientId.trim()
        );
    }

    // ============================================================
    // CALL NEXT PATIENT
    // ============================================================

    /**
     * Calls the next patient from the active FIFO queue.
     *
     * IMPORTANT:
     * The appointment is NOT deleted.
     *
     * Its status changes:
     *
     * WAITING → CALLED
     */
    public Patient callNextPatient() {

        synchronizeWaitingQueue();

        Patient nextPatient =
                appointmentQueue.dequeue();

        if (nextPatient == null) {

            System.out.println(
                    "   [APPOINTMENT] "
                            + "No patients currently waiting."
            );

            return null;
        }

        Appointment appointment =
                scheduledAppointments.get(
                        nextPatient.getId()
                );

        if (appointment == null) {

            System.out.println(
                    "   [APPOINTMENT] Appointment record "
                            + "could not be found."
            );

            return null;
        }

        /*
         * Do NOT remove the appointment.
         */
        appointment.setStatus(
                AppointmentStatus.CALLED
        );

        Doctor doctor =
                appointment.getDoctor();

        if (doctor != null) {

            doctor.removePatientFromQueue(
                    nextPatient.getId()
            );
        }

        System.out.println(
                "   [APPOINTMENT] Calling patient: "
                        + nextPatient.getName()
        );

        if (doctor != null) {

            System.out.println(
                    "   [APPOINTMENT] Doctor: "
                            + doctor.getName()
            );
        }

        System.out.println(
                "   [APPOINTMENT] Status: CALLED"
        );

        return nextPatient;
    }

    // ============================================================
    // PEEK
    // ============================================================

    /**
     * Views the next patient without removing them.
     */
    public Patient peekNextPatient() {

        synchronizeWaitingQueue();

        return appointmentQueue.peek();
    }

    // ============================================================
    // QUEUE REPORT
    // ============================================================

    /**
     * FUNCTIONAL REQUIREMENT 5:
     * Generates status reports for the waiting line.
     */
    public void displayQueue() {

        synchronizeWaitingQueue();

        System.out.println(
                "\n   === APPOINTMENT QUEUE "
                        + "(FIFO Status Report) ==="
        );

        if (appointmentQueue.isEmpty()) {

            System.out.println(
                    "   No patients currently in "
                            + "the waiting line."
            );

            System.out.println(
                    "   Next available slot: "
                            + "Immediate intake available."
            );

        } else {

            System.out.println(
                    "   Total Waiting: "
                            + appointmentQueue.size()
                            + " patients"
            );

            System.out.println(
                    "   " + "-".repeat(60)
            );

            Patient nextPatient =
                    appointmentQueue.peek();

            if (nextPatient != null) {

                Appointment appointment =
                        scheduledAppointments.get(
                                nextPatient.getId()
                        );

                System.out.println(
                        "   UP NEXT: "
                                + nextPatient.getName()
                );

                if (appointment != null) {

                    Doctor doctor =
                            appointment.getDoctor();

                    System.out.println(
                            "   Details: Dr. "
                                    + (doctor != null
                                    ? doctor.getName()
                                    : "Unassigned")
                                    + " at "
                                    + appointment.getTime()
                                    .format(TIME_FORMAT)
                    );

                    System.out.println(
                            "   Status: "
                                    + appointment.getStatus()
                    );
                }
            }

            System.out.println(
                    "   Estimated wait time: "
                            + (appointmentQueue.size() * 15)
                            + " minutes"
            );
        }

        System.out.println(
                "   ==============================================="
        );
    }

    // ============================================================
    // ALL APPOINTMENTS REPORT
    // ============================================================

    /**
     * Comprehensive report for all appointment records.
     *
     * Historical appointments are intentionally included.
     */
    public void displayAllAppointments() {

        synchronizeWaitingQueue();

        System.out.println(
                "\n   === ANALYTICS: ALL APPOINTMENT "
                        + "TIME SLOTS ==="
        );

        if (scheduledAppointments.isEmpty()) {

            System.out.println(
                    "   No appointments recorded in the system."
            );

        } else {

            System.out.println(
                    "   Appointment Records: "
                            + scheduledAppointments.size()
            );

            System.out.println(
                    "   " + "-".repeat(75)
            );

            int count = 1;

            for (Appointment appointment :
                    scheduledAppointments.values()) {

                String patientName =
                        appointment.getPatient() != null
                                ? appointment.getPatient().getName()
                                : "Unknown";

                String doctorName =
                        appointment.getDoctor() != null
                                ? appointment.getDoctor().getName()
                                : "Unknown";

                System.out.printf(
                        "   %2d. %-20s | Dr. %-18s | %s | %s | %s%n",
                        count++,
                        patientName,
                        doctorName,
                        appointment.getTime()
                                .format(TIME_FORMAT),
                        appointment.getDate(),
                        appointment.getStatus()
                );
            }
        }

        System.out.println(
                "   ==============================================="
        );
    }

    // ============================================================
    // QUERY METHODS
    // ============================================================

    /**
     * Returns the number of patients currently waiting.
     */
    public int getQueueSize() {

        synchronizeWaitingQueue();

        return appointmentQueue.size();
    }

    /**
     * Returns total appointment records.
     *
     * Includes historical records.
     */
    public int getScheduledAppointmentsCount() {

        return scheduledAppointments.size();
    }

    /**
     * Returns true if at least one patient is currently waiting.
     */
    public boolean hasWaitingPatients() {

        synchronizeWaitingQueue();

        return !appointmentQueue.isEmpty();
    }

    /**
     * Returns true if the waiting queue is empty.
     */
    public boolean isEmpty() {

        synchronizeWaitingQueue();

        return appointmentQueue.isEmpty();
    }

    /**
     * Checks whether the patient has an appointment record.
     *
     * Historical records also count as appointments.
     */
    public boolean hasAppointment(String patientId) {

        if (patientId == null) {
            return false;
        }

        return scheduledAppointments.containsKey(
                patientId.trim()
        );
    }

    // ============================================================
    // APPOINTMENT UPDATE
    // ============================================================

    /**
     * Updates an existing appointment.
     *
     * @param patientId Patient ID
     * @param newDoctor New doctor
     * @param newTimeStr New time (HH:mm)
     * @param newDate New date (yyyy-MM-dd)
     * @return true if updated successfully
     */
    public boolean updateAppointment(
            String patientId,
            Doctor newDoctor,
            String newTimeStr,
            String newDate) {

        if (patientId == null
                || patientId.trim().isEmpty()) {
            return false;
        }

        if (newDoctor == null) {
            return false;
        }

        if (newTimeStr == null
                || newTimeStr.trim().isEmpty()) {
            return false;
        }

        if (newDate == null
                || newDate.trim().isEmpty()) {
            return false;
        }

        patientId =
                patientId.trim();

        newTimeStr =
                newTimeStr.trim();

        newDate =
                newDate.trim();

        Appointment appointment =
                scheduledAppointments.get(
                        patientId
                );

        if (appointment == null) {
            return false;
        }

        /*
         * Finished appointments should not be edited.
         */
        if (appointment.isFinished()) {

            System.out.println(
                    "   [APPOINTMENT] Finished appointments "
                            + "cannot be modified."
            );

            return false;
        }

        try {

            LocalTime newTime =
                    LocalTime.parse(
                            newTimeStr,
                            TIME_FORMAT
                    );

            LocalDate parsedDate =
                    LocalDate.parse(
                            newDate,
                            DATE_FORMAT
                    );

            LocalDateTime newDateTime =
                    LocalDateTime.of(
                            parsedDate,
                            newTime
                    );

            /*
             * Do not move appointment into the past.
             */
            if (newDateTime.isBefore(
                    LocalDateTime.now())) {

                System.out.println(
                        "   [APPOINTMENT] Cannot update "
                                + "appointment to a past time."
                );

                return false;
            }

            /*
             * Prevent doctor double-booking.
             */
            if (isDoctorSlotOccupied(
                    newDoctor,
                    newDate,
                    newTime,
                    appointment)) {

                System.out.println(
                        "   [APPOINTMENT] New doctor slot "
                                + "is already occupied."
                );

                return false;
            }

            Doctor oldDoctor =
                    appointment.getDoctor();

            /*
             * Remove patient from old doctor's runtime queue.
             */
            if (oldDoctor != null) {

                oldDoctor.removePatientFromQueue(
                        patientId
                );
            }

            /*
             * Remove patient from main waiting queue.
             */
            appointmentQueue.removePatient(
                    patientId
            );

            /*
             * Update appointment information.
             */
            appointment.setDoctor(
                    newDoctor
            );

            appointment.setTime(
                    newTime
            );

            appointment.setDate(
                    newDate
            );

            /*
             * Reset lifecycle to SCHEDULED.
             */
            appointment.setStatus(
                    AppointmentStatus.SCHEDULED
            );

            /*
             * Recalculate active waiting queue.
             */
            synchronizeWaitingQueue();

            System.out.println(
                    "   [APPOINTMENT] Appointment updated for: "
                            + appointment.getPatient().getName()
            );

            return true;

        } catch (DateTimeParseException e) {

            System.out.println(
                    "   [ERROR] Invalid appointment date/time."
            );

            return false;
        }
    }

    // ============================================================
    // CANCEL APPOINTMENT
    // ============================================================

    /**
     * Cancels an appointment.
     *
     * IMPORTANT:
     * The appointment record is NOT deleted.
     *
     * Status becomes CANCELLED so the appointment
     * remains available in historical records.
     */
    public boolean cancelAppointment(
            String patientId) {

        if (patientId == null
                || patientId.trim().isEmpty()) {

            return false;
        }

        patientId =
                patientId.trim();

        Appointment appointment =
                scheduledAppointments.get(
                        patientId
                );

        if (appointment == null) {
            return false;
        }

        if (appointment.isFinished()) {

            return false;
        }

        /*
         * Change lifecycle status instead of deleting.
         */
        appointment.setStatus(
                AppointmentStatus.CANCELLED
        );

        /*
         * Remove from active waiting queue.
         */
        appointmentQueue.removePatient(
                patientId
        );

        /*
         * Remove from doctor's runtime queue.
         */
        Doctor doctor =
                appointment.getDoctor();

        if (doctor != null) {

            doctor.removePatientFromQueue(
                    patientId
            );
        }

        System.out.println(
                "   [APPOINTMENT] Cancelled appointment for: "
                        + appointment.getPatient().getName()
        );

        System.out.println(
                "   [APPOINTMENT] Historical record preserved."
        );

        return true;
    }

    // ============================================================
    // LIFECYCLE METHODS
    // ============================================================

    /**
     * Marks an appointment as ARRIVED.
     *
     * CALLED → ARRIVED
     */
    public boolean markPatientArrived(
            String patientId) {

        Appointment appointment =
                scheduledAppointments.get(
                        patientId
                );

        if (appointment == null) {
            return false;
        }

        if (appointment.getStatus()
                != AppointmentStatus.CALLED) {

            return false;
        }

        appointment.setStatus(
                AppointmentStatus.ARRIVED
        );

        System.out.println(
                "   [APPOINTMENT] Patient arrived: "
                        + appointment.getPatient().getName()
        );

        return true;
    }

    /**
     * Starts a consultation.
     *
     * ARRIVED → IN_PROGRESS
     */
    public boolean startAppointment(
            String patientId) {

        Appointment appointment =
                scheduledAppointments.get(
                        patientId
                );

        if (appointment == null) {
            return false;
        }

        if (appointment.getStatus()
                != AppointmentStatus.ARRIVED) {

            return false;
        }

        appointment.setStatus(
                AppointmentStatus.IN_PROGRESS
        );

        System.out.println(
                "   [APPOINTMENT] Consultation started: "
                        + appointment.getPatient().getName()
        );

        return true;
    }

    /**
     * Completes a consultation.
     *
     * IN_PROGRESS → COMPLETED
     */
    public boolean completeAppointment(
            String patientId) {

        Appointment appointment =
                scheduledAppointments.get(
                        patientId
                );

        if (appointment == null) {
            return false;
        }

        if (appointment.getStatus()
                != AppointmentStatus.IN_PROGRESS) {

            return false;
        }

        appointment.setStatus(
                AppointmentStatus.COMPLETED
        );

        Doctor doctor =
                appointment.getDoctor();

        if (doctor != null) {

            doctor.removePatientFromQueue(
                    patientId
            );
        }

        System.out.println(
                "   [APPOINTMENT] Appointment completed: "
                        + appointment.getPatient().getName()
        );

        return true;
    }

    /**
     * Explicitly marks an appointment as NO_SHOW.
     */
    public boolean markNoShow(
            String patientId) {

        Appointment appointment =
                scheduledAppointments.get(
                        patientId
                );

        if (appointment == null) {
            return false;
        }

        if (appointment.isFinished()) {
            return false;
        }

        appointment.setStatus(
                AppointmentStatus.NO_SHOW
        );

        appointmentQueue.removePatient(
                patientId
        );

        Doctor doctor =
                appointment.getDoctor();

        if (doctor != null) {

            doctor.removePatientFromQueue(
                    patientId
            );
        }

        System.out.println(
                "   [APPOINTMENT] Marked as NO_SHOW: "
                        + appointment.getPatient().getName()
        );

        return true;
    }

    // ============================================================
    // APPOINTMENT COLLECTION
    // ============================================================

    /**
     * Returns all appointment records.
     *
     * Includes:
     * - Scheduled
     * - Waiting
     * - Called
     * - Arrived
     * - In progress
     * - Completed
     * - No-show
     * - Cancelled
     */
    public List<Appointment> getAllAppointments() {

        synchronizeWaitingQueue();

        return new ArrayList<>(
                scheduledAppointments.values()
        );
    }

    /**
     * Returns today's appointments.
     *
     * Historical appointments from previous days
     * are not included.
     */
    public List<Appointment> getTodayAppointments() {

        synchronizeWaitingQueue();

        List<Appointment> result =
                new ArrayList<>();

        LocalDate today =
                LocalDate.now();

        for (Appointment appointment :
                scheduledAppointments.values()) {

            if (appointment == null) {
                continue;
            }

            if (appointment.getDateTime()
                    .toLocalDate()
                    .equals(today)) {

                result.add(appointment);
            }
        }

        return result;
    }

    /**
     * Returns only active waiting appointments.
     */
    public List<Appointment> getWaitingAppointments() {

        synchronizeWaitingQueue();

        List<Appointment> result =
                new ArrayList<>();

        for (Appointment appointment :
                scheduledAppointments.values()) {

            if (appointment == null) {
                continue;
            }

            if (appointment.getStatus()
                    == AppointmentStatus.WAITING) {

                result.add(appointment);
            }
        }

        return result;
    }

    // ============================================================
    // DOCTOR SLOT VALIDATION
    // ============================================================

    /**
     * Checks whether a doctor already has an appointment
     * at the specified date/time.
     *
     * @param doctor Doctor to check
     * @param date Appointment date
     * @param time Appointment time
     * @param ignoredAppointment Appointment being updated,
     *                            or null for new booking
     */
    private boolean isDoctorSlotOccupied(
            Doctor doctor,
            String date,
            LocalTime time,
            Appointment ignoredAppointment) {

        if (doctor == null) {
            return false;
        }

        for (Appointment existing :
                scheduledAppointments.values()) {

            if (existing == null) {
                continue;
            }

            /*
             * Ignore the appointment currently being updated.
             */
            if (existing == ignoredAppointment) {
                continue;
            }

            /*
             * Cancelled and no-show appointments do not
             * block a future slot.
             */
            if (existing.getStatus()
                    == AppointmentStatus.CANCELLED
                    || existing.getStatus()
                    == AppointmentStatus.NO_SHOW) {

                continue;
            }

            Doctor existingDoctor =
                    existing.getDoctor();

            if (existingDoctor == null) {
                continue;
            }

            if (!existingDoctor.getId()
                    .equals(doctor.getId())) {

                continue;
            }

            if (!existing.getDate()
                    .equals(date)) {

                continue;
            }

            if (!existing.getTime()
                    .equals(time)) {

                continue;
            }

            return true;
        }

        return false;
    }

    // ============================================================
    // STATE MANAGEMENT / UNDO
    // ============================================================

    /**
     * TECHNICAL REQUIREMENT 6:
     * Creates a copy of scheduler state.
     */
    public AppointmentScheduler deepCopy() {

        AppointmentScheduler copy =
                new AppointmentScheduler();

        /*
         * Copy appointment records.
         */
        for (Map.Entry<String, Appointment> entry :
                scheduledAppointments.entrySet()) {

            copy.scheduledAppointments.put(
                    entry.getKey(),
                    entry.getValue().deepCopy()
            );
        }

        /*
         * Rebuild queue from appointment state
         * instead of copying Patient deep copies.
         *
         * This keeps queue and appointment state synchronized.
         */
        copy.synchronizeWaitingQueue();

        return copy;
    }

    /**
     * Restores scheduler state.
     */
    public void restoreFrom(
            AppointmentScheduler source) {

        if (source == null) {
            return;
        }

        this.scheduledAppointments.clear();

        for (Map.Entry<String, Appointment> entry :
                source.scheduledAppointments.entrySet()) {

            this.scheduledAppointments.put(
                    entry.getKey(),
                    entry.getValue().deepCopy()
            );
        }

        /*
         * Queue is derived from appointment records.
         */
        this.appointmentQueue.clear();

        synchronizeWaitingQueue();

        System.out.println(
                "   [RESTORE] Appointment scheduler "
                        + "state synchronized."
        );
    }
}