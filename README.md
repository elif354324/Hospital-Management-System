# Hospital Management System

A Java Swing-based Hospital Management System developed to manage patients, doctors, appointments, departments, and emergency cases through a desktop graphical user interface.

## 📋 Project Overview

The Hospital Management System is designed to provide a simple and organized way to manage essential hospital operations.

The application allows users to:

* Manage patient records
* Manage doctor records
* Assign doctors to departments
* Schedule appointments
* Update and cancel appointments
* Manage waiting queues
* Handle emergency patients
* Search and display patient and doctor information
* Monitor appointment and patient information through a dashboard

The project also demonstrates the practical use of fundamental data structures and object-oriented programming concepts in Java.

## 🚀 Features

### Dashboard

* Overview of the hospital system
* Patient statistics
* Doctor statistics
* Appointment information
* Quick access to other management sections

### Patient Management

* Add new patients
* Search patients by ID
* Update patient information
* Delete patient records
* Display patient information in a table
* Patient severity/priority management

### Doctor Management

* Add new doctors
* Search doctors by ID
* Update doctor information
* Delete doctors
* Assign doctors to departments
* Display the number of patients waiting for each doctor

### Appointment Management

* Schedule appointments
* Select a doctor and patient
* Set appointment date and time
* Update appointments
* Cancel appointments
* Display scheduled appointments
* Manage appointment waiting queues
* Call the next patient

### Emergency Management

* Register emergency patients
* Manage emergency cases according to severity
* Prioritize patients using the appropriate queue structure

## 🛠️ Technologies Used

* **Java 21**
* **Java Swing**
* **Object-Oriented Programming (OOP)**
* **Custom Data Structures**
* **VS Code**
* **Git & GitHub**

## 🧩 Data Structures

The project uses custom implementations of several data structures rather than relying exclusively on Java's built-in collection classes.

### Hash Table

Used for efficient storage and retrieval of:

* Patients
* Doctors
* Appointments

### Queue

Used for:

* Doctor waiting queues
* Appointment processing
* Patient queue management

### Priority Queue

Used for emergency patient prioritization based on severity.

These structures demonstrate how different data structures can be applied to real-world hospital management operations.

## 🏗️ Project Structure

```text
HOSPITAL_MANAGEMENT_SYSTEM_PROJECT/
│
├── app/
│   ├── HospitalData.java
│   └── Main.java
│
├── models/
│   ├── Appointment.java
│   ├── Department.java
│   ├── Doctor.java
│   └── Patient.java
│
├── services/
│   ├── AppointmentScheduler.java
│   └── RecordRegistry.java
│
├── structures/
│   ├── HashTable.java
│   ├── Queue.java
│   └── PriorityQueue.java
│
├── ui/
│   ├── AppointmentPanel.java
│   ├── DashboardPanel.java
│   ├── DoctorPanel.java
│   ├── EmergencyPanel.java
│   ├── MainFrame.java
│   └── PatientPanel.java
│
├── .gitignore
└── README.md
```

## ▶️ How to Run

### Requirements

Make sure Java 21 or a compatible JDK is installed.

Check the installed Java version:

```bash
java -version
```

### Using VS Code

1. Open the project folder in VS Code.
2. Make sure Java 21 is configured.
3. Open `app/Main.java`.
4. Run the `Main.java` file using the **Run** button in VS Code.

Alternatively, compile and run the project from the terminal according to the project's source structure.

## 🖥️ User Interface

The application provides a graphical desktop interface consisting of:

* Dashboard
* Patient Management
* Doctor Management
* Appointment Management
* Emergency Management

The interface is designed with Java Swing and supports dynamic resizing for a more flexible desktop experience.

## 🔄 Main Workflow

```text
Patient Registration
        │
        ▼
Patient Management
        │
        ▼
Doctor Selection
        │
        ▼
Appointment Scheduling
        │
        ▼
Waiting Queue
        │
        ▼
Patient Called
        │
        ▼
Appointment Completed
```

Emergency cases follow a priority-based workflow:

```text
Emergency Patient
        │
        ▼
Severity Evaluation
        │
        ▼
Priority Queue
        │
        ▼
Highest Priority Patient
        │
        ▼
Emergency Treatment
```

## 🎯 Project Goals

The main goals of this project are:

* Applying object-oriented programming principles
* Implementing and using custom data structures
* Understanding data management in a real-world scenario
* Building a functional desktop GUI with Java Swing
* Connecting user interface components with application logic
* Practicing software organization and modular design

## 📌 Notes

This project was developed as an educational Java application. It focuses on demonstrating programming concepts, data structures, object-oriented design, and GUI development within a hospital management scenario.

## 👩‍💻 Developer

**Elif Sude Yılmaz**

---

⭐ If you find this project useful, feel free to explore the source code and experiment with the different management modules.
