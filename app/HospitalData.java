package app;

import models.Department;
import models.Doctor;
import services.RecordRegistry;
import structures.HashTable;
import services.AppointmentScheduler;
import java.util.ArrayList;
import java.util.List;
import services.EmergencyTriage;

public class HospitalData {

    private static final long STUDENT_ID = 220315022L;
    private static final AppointmentScheduler appointmentScheduler =
        new AppointmentScheduler();

    // =========================
    // EMERGENCY SYSTEM
    // =========================

    private static final EmergencyTriage emergencySystem =
            new EmergencyTriage(20);


    // =========================
    // PATIENT SYSTEM
    // =========================

    private static final RecordRegistry patientRegistry =
            new RecordRegistry(STUDENT_ID);


    // =========================
    // DOCTOR SYSTEM
    // =========================

    private static final HashTable<Doctor> doctorRegistry =
            new HashTable<>(50);


    private static final List<Department> departments =
            new ArrayList<>();


    static {

        initializeDoctors();
    }


    private static void initializeDoctors() {

        Department cardiology =
                new Department(
                        "Cardiology",
                        "Heart Center - Floor 3"
                );


        Department neurology =
                new Department(
                        "Neurology",
                        "Brain & Nerve Center - Floor 4"
                );


        Department orthopedics =
                new Department(
                        "Orthopedics",
                        "Bone & Joint Center - Floor 2"
                );


        departments.add(cardiology);
        departments.add(neurology);
        departments.add(orthopedics);


        Doctor drSmith =
                new Doctor(
                        "DOC-NEU-001",
                        "Dr. Michael Smith",
                        neurology
                );


        Doctor drJohnson =
                new Doctor(
                        "DOC-HRT-002",
                        "Dr. Sarah Johnson",
                        cardiology
                );


        Doctor drWilliams =
                new Doctor(
                        "DOC-ORT-001",
                        "Dr. Robert Williams",
                        orthopedics
                );


        doctorRegistry.put(
                drSmith.getId(),
                drSmith
        );


        doctorRegistry.put(
                drJohnson.getId(),
                drJohnson
        );


        doctorRegistry.put(
                drWilliams.getId(),
                drWilliams
        );
    }


    // =========================
    // GETTERS
    // =========================

    public static RecordRegistry getPatientRegistry() {

        return patientRegistry;
    }

    public static AppointmentScheduler getAppointmentScheduler() {

    return appointmentScheduler;
}

    public static HashTable<Doctor> getDoctorRegistry() {

        return doctorRegistry;
    }


    public static List<Department> getDepartments() {

        return departments;
    }

    public static EmergencyTriage getEmergencySystem(){
      return emergencySystem;
    }
}