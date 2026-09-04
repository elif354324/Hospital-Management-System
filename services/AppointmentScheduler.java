package services;

import models.*;
import structures.Queue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Appointment Scheduler with time slot management.
 *
 * ADT Usage:
 * - Queue (FIFO): Ensures standard waiting room order.
 * - HashMap: Provides O(1) average lookup for appointment details by Patient ID.
 */
public class AppointmentScheduler {

    private Queue appointmentQueue;
    private Map<String, Appointment> scheduledAppointments;

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm");

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Creates a new AppointmentScheduler.
     */
    public AppointmentScheduler() {
        this.appointmentQueue = new Queue();
        this.scheduledAppointments = new HashMap<>();
    }

    /**
     * FUNCTIONAL REQUIREMENT 2:
     * Books appointment with specific time slot.
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

        if (patient.getId() == null || patient.getId().trim().isEmpty()) {
            return false;
        }

        if (timeStr == null || timeStr.trim().isEmpty()) {
            return false;
        }

        if (date == null || date.trim().isEmpty()) {
            return false;
        }

        // Prevent duplicate appointment for the same patient.
        if (scheduledAppointments.containsKey(patient.getId())) {
            return false;
        }

        try {
            LocalTime time =
                    LocalTime.parse(timeStr.trim(), TIME_FORMAT);

            LocalDate.parse(date.trim(), DATE_FORMAT);

            Appointment appointment =
                    new Appointment(
                            patient,
                            doctor,
                            time,
                            date.trim()
                    );

            // FIFO waiting queue.
            appointmentQueue.enqueue(patient);

            // O(1) average appointment lookup.
            scheduledAppointments.put(
                    patient.getId(),
                    appointment
            );

            System.out.println(
                    "   [APPOINTMENT] Time slot booked:"
            );

            System.out.println(
                    "     Patient: " + patient.getName()
            );

            System.out.println(
                    "     Doctor: " + doctor.getName()
            );

            System.out.println(
                    "     Time: " + time.format(TIME_FORMAT)
            );

            System.out.println(
                    "     Date: " + date
            );

            return true;

        } catch (DateTimeParseException e) {

            System.out.println(
                    "   [ERROR] Invalid appointment date/time."
            );

            System.out.println(
                    "   Time format: HH:mm (e.g., 14:30)"
            );

            System.out.println(
                    "   Date format: yyyy-MM-dd (e.g., 2026-09-04)"
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

        appointmentQueue.enqueue(patient);

        System.out.println(
                "   [APPOINTMENT] Booked for: "
                        + patient.getName()
                        + " | Queue position: "
                        + appointmentQueue.size()
        );
    }

    /**
     * FUNCTIONAL REQUIREMENT 5:
     * Retrieval of appointment details by patient ID.
     *
     * Time Complexity: O(1) average case.
     */
    public Appointment getAppointmentDetails(String patientId) {

        if (patientId == null) {
            return null;
        }

        return scheduledAppointments.get(
                patientId.trim()
        );
    }

    /**
     * Calls the next patient from the FIFO queue.
     *
     * @return Next patient or null if queue is empty
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
     * Views next patient without removing from queue.
     *
     * @return Next patient or null if queue is empty
     */
    public Patient peekNextPatient() {
        return appointmentQueue.peek();
    }

    /**
     * FUNCTIONAL REQUIREMENT 5:
     * Generates status reports for the waiting line.
     */
    public void displayQueue() {

        System.out.println(
                "\n   === APPOINTMENT QUEUE (FIFO Status Report) ==="
        );

        if (appointmentQueue.isEmpty()) {

            System.out.println(
                    "   No patients currently in the waiting line."
            );

            System.out.println(
                    "   Next available slot: Immediate intake available."
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

                    System.out.println(
                            "   Details: Dr. "
                                    + appointment.getDoctor().getName()
                                    + " at "
                                    + appointment.getTime()
                                    .format(TIME_FORMAT)
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

    /**
     * Comprehensive report for all scheduled appointments.
     */
    public void displayAllAppointments() {

        System.out.println(
                "\n   === ANALYTICS: ALL SCHEDULED TIME SLOTS ==="
        );

        if (scheduledAppointments.isEmpty()) {

            System.out.println(
                    "   No appointments recorded in the system."
            );

        } else {

            System.out.println(
                    "   Scheduled Records: "
                            + scheduledAppointments.size()
            );

            System.out.println(
                    "   " + "-".repeat(60)
            );

            int count = 1;

            for (Appointment appointment :
                    scheduledAppointments.values()) {

                System.out.printf(
                        "   %2d. %-20s | Dr. %-15s | %s | %s%n",
                        count++,
                        appointment.getPatient().getName(),
                        appointment.getDoctor().getName(),
                        appointment.getTime().format(TIME_FORMAT),
                        appointment.getDate()
                );
            }
        }

        System.out.println(
                "   ======================================"
        );
    }

    // ==================== QUERY METHODS ====================

    public int getQueueSize() {
        return appointmentQueue.size();
    }

    public int getScheduledAppointmentsCount() {
        return scheduledAppointments.size();
    }

    public boolean hasWaitingPatients() {
        return appointmentQueue.size() > 0;
    }

    public boolean isEmpty() {
        return appointmentQueue.isEmpty();
    }

    public boolean hasAppointment(String patientId) {

        if (patientId == null) {
            return false;
        }

        return scheduledAppointments.containsKey(
                patientId.trim()
        );
    }

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

        patientId = patientId.trim();
        newTimeStr = newTimeStr.trim();
        newDate = newDate.trim();

        Appointment appointment =
                scheduledAppointments.get(patientId);

        if (appointment == null) {
            return false;
        }

        try {

            LocalTime newTime =
                    LocalTime.parse(
                            newTimeStr,
                            TIME_FORMAT
                    );

            LocalDate.parse(
                    newDate,
                    DATE_FORMAT
            );

            Doctor oldDoctor =
                    appointment.getDoctor();

            // Remove from old doctor's queue.
            if (oldDoctor != null) {

                oldDoctor.removePatientFromQueue(
                        patientId
                );
            }

            // Update appointment information.
            appointment.setDoctor(newDoctor);
            appointment.setTime(newTime);
            appointment.setDate(newDate);

            // Add patient to new doctor's queue.
            Patient patient =
                    appointment.getPatient();

            newDoctor.addPatientToQueue(patient);

            System.out.println(
                    "   [APPOINTMENT] Appointment updated for: "
                            + patient.getName()
            );

            return true;

        } catch (DateTimeParseException e) {

            System.out.println(
                    "   [ERROR] Invalid appointment date/time."
            );

            return false;
        }
    }

    /**
     * Cancels an appointment.
     *
     * @param patientId Patient ID
     * @return true if cancelled successfully
     */
    public boolean cancelAppointment(String patientId) {

        if (patientId == null
                || patientId.trim().isEmpty()) {
            return false;
        }

        patientId = patientId.trim();

        Appointment appointment =
                scheduledAppointments.get(patientId);

        if (appointment == null) {
            return false;
        }

        // Remove from appointment lookup.
        scheduledAppointments.remove(patientId);

        // Remove from main FIFO queue.
        appointmentQueue.removePatient(patientId);

        // Remove from doctor's queue.
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

        return true;
    }

    /**
     * Returns all scheduled appointments.
     */
    public List<Appointment> getAllAppointments() {

        return new ArrayList<>(
                scheduledAppointments.values()
        );
    }

    // ==================== STATE MANAGEMENT ====================

    /**
     * TECHNICAL REQUIREMENT 6:
     * Support for Undo via State Preservation.
     */
    public AppointmentScheduler deepCopy() {

        AppointmentScheduler copy =
                new AppointmentScheduler();

        copy.appointmentQueue =
                this.appointmentQueue.deepCopy();

        for (Map.Entry<String, Appointment> entry :
                this.scheduledAppointments.entrySet()) {

            copy.scheduledAppointments.put(
                    entry.getKey(),
                    entry.getValue().deepCopy()
            );
        }

        return copy;
    }

    /**
     * Restores state after an administrative error is undone.
     */
    public void restoreFrom(
            AppointmentScheduler source) {

        if (source == null) {
            return;
        }

        this.appointmentQueue =
                source.appointmentQueue.deepCopy();

        this.scheduledAppointments.clear();

        for (Map.Entry<String, Appointment> entry :
                source.scheduledAppointments.entrySet()) {

            this.scheduledAppointments.put(
                    entry.getKey(),
                    entry.getValue().deepCopy()
            );
        }

        System.out.println(
                "   [RESTORE] Appointment scheduler state synchronized."
        );
    }
}