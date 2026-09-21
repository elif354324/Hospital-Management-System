package models;

import structures.LinkedList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Patient represents a patient registered in the
 * Hospital Management System.
 *
 * Patient data is divided into two logical groups:
 *
 * 1. Personal Data
 *    - Identity
 *    - Contact
 *    - Emergency contact
 *
 * 2. Clinical Data
 *    - Symptoms
 *    - Allergies
 *    - Chronic diseases
 *    - Medical history
 *    - Emergency severity
 *
 * IMPORTANT:
 * Patient objects must NOT be sent directly to the AI service.
 * The ClinicalDataSanitizer will later extract only the
 * minimum necessary clinical information.
 *
 * ADT Usage:
 * - Custom LinkedList is used for medical history.
 * - Custom LinkedList is used for allergies.
 * - Custom LinkedList is used for chronic diseases.
 * - Custom LinkedList is used for symptoms.
 */
public class Patient {

    // =========================================================
    // PERSONAL DATA
    // =========================================================

    /**
     * Internal hospital patient ID.
     *
     * Example:
     * HST-000001
     */
    private String id;

    private String name;

    /**
     * Turkish Republic Identity Number.
     *
     * This is highly sensitive personal information.
     * It must never be sent to the AI service.
     */
    private String nationalId;

    private LocalDate birthDate;

    private String gender;

    private String phone;

    private String email;

    private String address;


    // =========================================================
    // EMERGENCY CONTACT
    // =========================================================

    private String emergencyContactName;

    private String emergencyContactPhone;

    private String emergencyContactRelation;


    // =========================================================
    // CLINICAL DATA
    // =========================================================

    /**
     * Blood group.
     *
     * Example:
     * A Rh+
     * 0 Rh-
     */
    private String bloodGroup;


    /**
     * Emergency severity.
     *
     * Scale:
     * 1  = Low
     * 10 = Critical
     */
    private int severity;


    /**
     * Current symptoms reported by the patient.
     *
     * Example:
     * - Göğüs ağrısı
     * - Nefes darlığı
     * - Baş dönmesi
     */
    private LinkedList<String> symptoms;


    /**
     * Known allergies.
     */
    private LinkedList<String> allergies;


    /**
     * Known chronic diseases.
     */
    private LinkedList<String> chronicDiseases;


    /**
     * Medical history.
     */
    private LinkedList<String> medicalHistory;


    // =========================================================
    // SYSTEM INFORMATION
    // =========================================================

    /**
     * Date/time when the patient was registered.
     */
    private LocalDateTime registrationDate;


    // =========================================================
    // FORMATTERS
    // =========================================================

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern(
                    "dd.MM.yyyy HH:mm:ss"
            );


    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern(
                    "dd.MM.yyyy"
            );


    // =========================================================
    // CONSTRUCTORS
    // =========================================================

    /**
     * Creates a standard patient.
     *
     * This constructor is kept for compatibility
     * with the existing system.
     */
    public Patient(
            String id,
            String name) {

        initializeDefaults(
                id,
                name
        );

        addHistory(
                "Hasta sisteme kaydedildi.",
                true
        );
    }


    /**
     * Creates a patient with emergency severity.
     *
     * This constructor is used by the emergency system.
     */
    public Patient(
            String id,
            String name,
            int severity) {

        initializeDefaults(
                id,
                name
        );

        this.severity =
                clampSeverity(severity);

        addHistory(
                "Acil servis kaydı oluşturuldu. Öncelik seviyesi: "
                        + this.severity,
                true
        );
    }


    /**
     * Existing complete patient constructor.
     *
     * Kept for compatibility with existing code.
     */
    public Patient(
            String id,
            String name,
            int age,
            String gender,
            String phone,
            int severity) {

        initializeDefaults(
                id,
                name
        );

        this.birthDate =
                calculateBirthDateFromAge(age);

        this.gender =
                safeValue(gender);

        this.phone =
                safeValue(phone);

        this.severity =
                clampSeverity(severity);

        addHistory(
                "Hasta sisteme kaydedildi.",
                true
        );
    }


    /**
     * Initializes common patient fields.
     */
    private void initializeDefaults(
            String id,
            String name) {

        this.id =
                safeValue(id);

        this.name =
                safeValue(name);

        this.nationalId = "";

        this.birthDate = null;

        this.gender = "";

        this.phone = "";

        this.email = "";

        this.address = "";

        this.emergencyContactName = "";

        this.emergencyContactPhone = "";

        this.emergencyContactRelation = "";

        this.bloodGroup = "";

        this.severity = 1;

        this.symptoms =
                new LinkedList<>();

        this.allergies =
                new LinkedList<>();

        this.chronicDiseases =
                new LinkedList<>();

        this.medicalHistory =
                new LinkedList<>();

        this.registrationDate =
                LocalDateTime.now();
    }


    // =========================================================
    // PERSONAL DATA - GETTERS
    // =========================================================

    public String getId() {

        return id;
    }


    public String getName() {

        return name;
    }


    public String getNationalId() {

        return nationalId;
    }


    public LocalDate getBirthDate() {

        return birthDate;
    }


    /**
     * Returns age calculated from birth date.
     */
    public int getAge() {

        if (birthDate == null) {

            return 0;
        }

        return java.time.Period
                .between(
                        birthDate,
                        LocalDate.now()
                )
                .getYears();
    }


    public String getGender() {

        return gender;
    }


    public String getPhone() {

        return phone;
    }


    public String getEmail() {

        return email;
    }


    public String getAddress() {

        return address;
    }


    // =========================================================
    // EMERGENCY CONTACT - GETTERS
    // =========================================================

    public String getEmergencyContactName() {

        return emergencyContactName;
    }


    public String getEmergencyContactPhone() {

        return emergencyContactPhone;
    }


    public String getEmergencyContactRelation() {

        return emergencyContactRelation;
    }


    // =========================================================
    // CLINICAL DATA - GETTERS
    // =========================================================

    public String getBloodGroup() {

        return bloodGroup;
    }


    public int getSeverity() {

        return severity;
    }


    public LinkedList<String> getSymptoms() {

        return symptoms;
    }


    public LinkedList<String> getAllergies() {

        return allergies;
    }


    public LinkedList<String> getChronicDiseases() {

        return chronicDiseases;
    }


    public LinkedList<String> getMedicalHistory() {

        return medicalHistory;
    }


    public LocalDateTime getRegistrationDate() {

        return registrationDate;
    }


    // =========================================================
    // PERSONAL DATA - SETTERS
    // =========================================================

    public void setName(
            String name) {

        String oldName =
                this.name;

        this.name =
                safeValue(name);

        addHistory(
                "Hasta adı güncellendi: "
                        + oldName
                        + " → "
                        + this.name,
                false
        );
    }


    /**
     * Sets Turkish Republic Identity Number.
     *
     * IMPORTANT:
     * This value must never be sent to AI.
     */
    public void setNationalId(
            String nationalId) {

        this.nationalId =
                safeValue(nationalId);
    }


    public void setBirthDate(
            LocalDate birthDate) {

        this.birthDate =
                birthDate;
    }


    /**
     * Compatibility method.
     *
     * Existing code may still call setAge().
     *
     * New code should prefer setBirthDate().
     */
    public void setAge(
            int age) {

        if (age < 0) {

            age = 0;
        }

        this.birthDate =
                calculateBirthDateFromAge(age);
    }


    public void setGender(
            String gender) {

        this.gender =
                safeValue(gender);
    }


    public void setPhone(
            String phone) {

        this.phone =
                safeValue(phone);
    }


    public void setEmail(
            String email) {

        this.email =
                safeValue(email);
    }


    public void setAddress(
            String address) {

        this.address =
                safeValue(address);
    }


    // =========================================================
    // EMERGENCY CONTACT - SETTERS
    // =========================================================

    public void setEmergencyContactName(
            String name) {

        this.emergencyContactName =
                safeValue(name);
    }


    public void setEmergencyContactPhone(
            String phone) {

        this.emergencyContactPhone =
                safeValue(phone);
    }


    public void setEmergencyContactRelation(
            String relation) {

        this.emergencyContactRelation =
                safeValue(relation);
    }


    // =========================================================
    // CLINICAL DATA - SETTERS
    // =========================================================

    public void setBloodGroup(
            String bloodGroup) {

        this.bloodGroup =
                safeValue(bloodGroup);
    }


    public void setSeverity(
            int severity) {

        int oldSeverity =
                this.severity;

        this.severity =
                clampSeverity(severity);

        addHistory(
                "Acil durum öncelik seviyesi güncellendi: "
                        + oldSeverity
                        + " → "
                        + this.severity,
                false
        );
    }


    // =========================================================
    // SYMPTOMS
    // =========================================================

    /**
     * Adds a symptom to the patient's current symptom list.
     */
    public void addSymptom(
            String symptom) {

        if (isBlank(symptom)) {

            return;
        }

        symptoms.addLast(
                symptom.trim()
        );
    }


    /**
     * Adds multiple symptoms as a single clinical event.
     *
     * The actual LinkedList implementation remains
     * responsible for storage.
     */
    public void clearSymptoms() {

        symptoms =
                new LinkedList<>();
    }


    // =========================================================
    // ALLERGIES
    // =========================================================

    public void addAllergy(
            String allergy) {

        if (isBlank(allergy)) {

            return;
        }

        allergies.addLast(
                allergy.trim()
        );
    }


    public void clearAllergies() {

        allergies =
                new LinkedList<>();
    }


    // =========================================================
    // CHRONIC DISEASES
    // =========================================================

    public void addChronicDisease(
            String disease) {

        if (isBlank(disease)) {

            return;
        }

        chronicDiseases.addLast(
                disease.trim()
        );
    }


    public void clearChronicDiseases() {

        chronicDiseases =
                new LinkedList<>();
    }


    // =========================================================
    // MEDICAL HISTORY
    // =========================================================

    /**
     * Adds a record to the patient's medical history.
     *
     * @param record medical record
     * @param isAuto true if generated by the system
     */
    public void addHistory(
            String record,
            boolean isAuto) {

        if (isBlank(record)) {

            return;
        }

        String prefix =
                isAuto
                        ? "[SİSTEM] "
                        : "[MANUEL] ";


        String timestampedRecord =
                prefix
                        + record.trim()
                        + " ["
                        + LocalDateTime.now()
                        .format(TIMESTAMP_FORMAT)
                        + "]";


        medicalHistory.addLast(
                timestampedRecord
        );
    }


    public void addHistory(
            String record) {

        addHistory(
                record,
                false
        );
    }


    /**
     * Adds a doctor consultation record.
     */
    public void addDoctorConsultation(
            String doctorName,
            String notes) {

        String record =
                "Doktor muayenesi: "
                        + safeValue(doctorName)
                        + " - "
                        + safeValue(notes);


        addHistory(
                record,
                true
        );
    }


    /**
     * Adds an emergency treatment record.
     */
    public void addEmergencyTreatment(
            int severity,
            String outcome) {

        String record =
                "Acil servis müdahalesi - Öncelik: "
                        + clampSeverity(severity)
                        + " - Sonuç: "
                        + safeValue(outcome);


        addHistory(
                record,
                true
        );
    }


    /**
     * Adds an appointment record.
     */
    public void addAppointmentRecord(
            String doctorName,
            String time) {

        String record =
                "Randevu - "
                        + safeValue(doctorName)
                        + " - "
                        + safeValue(time);


        addHistory(
                record,
                true
        );
    }


    // =========================================================
    // MEDICAL HISTORY DISPLAY
    // =========================================================

    public void displayHistory() {

        System.out.println();

        System.out.println(
                "=== HASTA TIBBİ GEÇMİŞİ ==="
        );

        System.out.println(
                "Hasta: "
                        + name
        );

        System.out.println(
                "Hasta No: "
                        + id
        );

        System.out.println(
                "Toplam kayıt: "
                        + medicalHistory.size()
        );

        System.out.println(
                "------------------------------------------"
        );

        System.out.println(
                "Tıbbi geçmiş görüntüleme işlemi UI "
                        + "üzerinden gerçekleştirilecektir."
        );

        System.out.println(
                "=========================================="
        );
    }


    // =========================================================
    // CLINICAL AI SUPPORT
    // =========================================================

    /**
     * Returns whether the patient currently has
     * clinical data that may be useful for AI-assisted
     * clinical evaluation.
     *
     * IMPORTANT:
     * This method does NOT expose personal information.
     *
     * The actual AI request will be created later by
     * ClinicalDataSanitizer.
     */
    public boolean hasClinicalData() {

        return symptoms.size() > 0
                || allergies.size() > 0
                || chronicDiseases.size() > 0
                || severity > 1;
    }


    // =========================================================
    // UTILITY METHODS
    // =========================================================

    private static int clampSeverity(
            int severity) {

        return Math.max(
                1,
                Math.min(
                        10,
                        severity
                )
        );
    }


    private static String safeValue(
            String value) {

        if (value == null) {

            return "";
        }

        return value.trim();
    }


    private static boolean isBlank(
            String value) {

        return value == null
                || value.trim().isEmpty();
    }


    /**
     * Compatibility helper for the old age-based model.
     *
     * This is intentionally approximate because an exact
     * birth date cannot be reconstructed from age alone.
     */
    private static LocalDate calculateBirthDateFromAge(
            int age) {

        if (age <= 0) {

            return null;
        }

        return LocalDate.now()
                .minusYears(age);
    }


    // =========================================================
    // DEEP COPY
    // =========================================================

    /**
     * Creates a copy of the patient.
     *
     * NOTE:
     * The custom LinkedList implementation does not expose
     * its complete traversal API here. Therefore the base
     * patient data is copied first.
     *
     * Once the LinkedList traversal/copy API is standardized,
     * the complete clinical history will also be deep-copied.
     */
    public Patient deepCopy() {
    Patient copy = new Patient(
            this.id,
            this.name
    );

    // Kişisel bilgiler
    copy.setNationalId(this.nationalId);
    copy.setBirthDate(this.birthDate);
    copy.setGender(this.gender);
    copy.setPhone(this.phone);
    copy.setEmail(this.email);
    copy.setAddress(this.address);

    // Klinik bilgiler
    copy.setSeverity(this.severity);
    copy.setBloodGroup(this.bloodGroup);

    // Acil durum iletişim bilgileri
    copy.setEmergencyContactName(this.emergencyContactName);
    copy.setEmergencyContactPhone(this.emergencyContactPhone);
    copy.setEmergencyContactRelation(this.emergencyContactRelation);

    // Klinik listelerin deep copy'si
    copy.symptoms = this.symptoms.deepCopy();
    copy.allergies = this.allergies.deepCopy();
    copy.chronicDiseases = this.chronicDiseases.deepCopy();
    copy.medicalHistory = this.medicalHistory.deepCopy();

    // Sistem bilgisi
    copy.registrationDate = this.registrationDate;

    return copy;
}

    // =========================================================
    // STRING REPRESENTATION
    // =========================================================

    @Override
    public String toString() {

        return id
                + " | "
                + name
                + " | "
                + getAge()
                + " yaş | "
                + gender;
    }
}