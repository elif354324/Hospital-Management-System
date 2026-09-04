package models;

import structures.Queue;

/**
 * Doctor class represents a medical doctor in the hospital.
 * Manages patient waiting line using FIFO queue.
 */
public class Doctor {

    private String id;
    private String name;
    private String specialty;
    private Department department;
    private Queue waitingLine;


    /**
     * Creates a new doctor.
     *
     * @param id Unique doctor ID
     * @param name Doctor's full name
     * @param specialty Doctor's medical specialty
     * @param department Assigned department
     */
    public Doctor(
            String id,
            String name,
            String specialty,
            Department department
    ) {

        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.department = department;

        this.waitingLine = new Queue();

        department.addDoctor();
    }


    // =========================================
    // GETTERS
    // =========================================

    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getSpecialty() {
        return specialty;
    }


    public Department getDepartment() {
        return department;
    }


    public Queue getWaitingLine() {
        return waitingLine;
    }


    // =========================================
    // SETTERS
    // =========================================

    public void setName(String name) {

        this.name = name;
    }


    public void setSpecialty(String specialty) {

        this.specialty = specialty;
    }


    public void setDepartment(
        Department department
) {

    if (department == null) {
        return;
    }

    if (this.department == department) {
        return;
    }

    if (this.department != null) {
        this.department.removeDoctor();
    }

    this.department = department;

    this.department.addDoctor();
}


    // =========================================
    // PATIENT MANAGEMENT
    // =========================================

    public void addPatientToQueue(
            Patient patient
    ) {

        waitingLine.enqueue(patient);

        System.out.println(
                "   [DOCTOR] "
                        + name
                        + " added "
                        + patient.getName()
                        + " to waiting line. Total waiting: "
                        + getQueueSize()
        );
    }


    public Patient seeNextPatient() {

        Patient nextPatient =
                waitingLine.dequeue();


        if (nextPatient != null) {

            System.out.println(
                    "   [DOCTOR] "
                            + name
                            + " is now examining: "
                            + nextPatient.getName()
            );


            nextPatient.addHistory(
                    "Consultation with " + name
            );
        }


        return nextPatient;
    }


    public int getQueueSize() {

        return waitingLine.size();
    }


    public boolean hasPatientsWaiting() {

        return waitingLine.size() > 0;
    }


    public void clearQueue() {

        waitingLine.clear();

        System.out.println(
                "   [DOCTOR] "
                        + name
                        + "'s waiting queue cleared."
        );
    }

    public boolean removePatientFromQueue(String patientId) {

    if (patientId == null || patientId.trim().isEmpty()) {
        return false;
    }

    return waitingLine.removePatient(patientId);
    }


    // =========================================
    // TO STRING
    // =========================================

    @Override
    public String toString() {

        return String.format(
                "%s (%s) - %s | %s | Patients waiting: %d",
                name,
                id,
                specialty,
                department.getName(),
                getQueueSize()
        );
    }
}