package app;

import models.Department;
import models.Doctor;
import services.AppointmentScheduler;
import services.EmergencyTriage;
import services.RecordRegistry;
import structures.HashTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Hastane Yönetim Sistemi'nin merkezi veri yöneticisidir.
 *
 * Bu sınıf, Swing kullanıcı arayüzü tarafından kullanılan
 * ana hastane servislerine merkezi erişim sağlar.
 *
 * Hastane içerisindeki:
 * - Hastalar
 * - Doktorlar
 * - Bölümler
 * - Randevular
 * - Acil servis
 *
 * gibi temel sistem bileşenleri burada yönetilir.
 */
public final class HospitalData {

    // =========================================================
    // SİSTEM KİMLİĞİ
    // =========================================================

    /**
     * Ders projesi kapsamında kullanılan öğrenci numarası.
     *
     * Bu değer mevcut veri yapılarındaki kapasite hesaplamalarında
     * kullanılmaktadır.
     */
    private static final long STUDENT_ID = 220315022L;


    // =========================================================
    // ACİL SERVİS KONFİGÜRASYONU
    // =========================================================

    /**
     * Acil servis maksimum hasta kapasitesi.
     *
     * Mevcut proje kuralı korunmuştur.
     */
    private static final int EMERGENCY_CAPACITY =
            (int) (STUDENT_ID % 100) + 10;


    // =========================================================
    // RANDEVU SİSTEMİ
    // =========================================================

    private static final AppointmentScheduler appointmentScheduler =
            new AppointmentScheduler();


    // =========================================================
    // ACİL SERVİS SİSTEMİ
    // =========================================================

    private static final EmergencyTriage emergencySystem =
            new EmergencyTriage(EMERGENCY_CAPACITY);


    // =========================================================
    // HASTA SİSTEMİ
    // =========================================================

    private static final RecordRegistry patientRegistry =
            new RecordRegistry(STUDENT_ID);


    // =========================================================
    // DOKTOR SİSTEMİ
    // =========================================================

    /**
     * Doktor kayıtları custom HashTable içerisinde tutulmaktadır.
     *
     * Key   : Doktor ID
     * Value : Doctor
     */
    private static final HashTable<Doctor> doctorRegistry =
            new HashTable<>(50);


    // =========================================================
    // BÖLÜM SİSTEMİ
    // =========================================================

    private static final List<Department> departments =
            new ArrayList<>();


    // =========================================================
    // INITIALIZATION
    // =========================================================

    static {
        initializeHospitalData();
    }


    /**
     * Private constructor.
     *
     * HospitalData nesnesi oluşturulmaz.
     * Sınıf merkezi/static veri sağlayıcı olarak kullanılır.
     */
    private HospitalData() {
    }


    /**
     * Hastane başlangıç verilerini oluşturur.
     */
    private static void initializeHospitalData() {

        initializeDepartments();

        initializeDoctors();
    }


    // =========================================================
    // BÖLÜMLER
    // =========================================================

    /**
     * Hastanedeki klinik ve tıbbi birimleri oluşturur.
     */
    private static void initializeDepartments() {

        // -----------------------------------------------------
        // DAHİLİ TIP BİRİMLERİ
        // -----------------------------------------------------

        Department internalMedicine =
                new Department(
                        "İç Hastalıkları",
                        "Dahili Tıp Merkezi - 2. Kat"
                );


        Department cardiology =
                new Department(
                        "Kardiyoloji",
                        "Kalp ve Damar Merkezi - 3. Kat"
                );


        Department neurology =
                new Department(
                        "Nöroloji",
                        "Beyin ve Sinir Hastalıkları Merkezi - 4. Kat"
                );


        Department pulmonology =
                new Department(
                        "Göğüs Hastalıkları",
                        "Göğüs Hastalıkları Merkezi - 3. Kat"
                );


        Department dermatology =
                new Department(
                        "Dermatoloji",
                        "Cilt Hastalıkları Merkezi - 2. Kat"
                );


        // -----------------------------------------------------
        // CERRAHİ TIP BİRİMLERİ
        // -----------------------------------------------------

        Department generalSurgery =
                new Department(
                        "Genel Cerrahi",
                        "Cerrahi Tıp Merkezi - 3. Kat"
                );


        Department orthopedics =
                new Department(
                        "Ortopedi ve Travmatoloji",
                        "Kas ve İskelet Sistemi Merkezi - 2. Kat"
                );


        Department neurosurgery =
                new Department(
                        "Beyin ve Sinir Cerrahisi",
                        "Nöroşirürji Merkezi - 4. Kat"
                );


        Department urology =
                new Department(
                        "Üroloji",
                        "Üroloji Merkezi - 2. Kat"
                );


        Department gynecology =
                new Department(
                        "Kadın Hastalıkları ve Doğum",
                        "Kadın ve Doğum Merkezi - 1. Kat"
                );


        // -----------------------------------------------------
        // DİĞER TIBBİ BİRİMLER
        // -----------------------------------------------------

        Department ophthalmology =
                new Department(
                        "Göz Hastalıkları",
                        "Göz Sağlığı Merkezi - 1. Kat"
                );


        Department ent =
                new Department(
                        "Kulak Burun Boğaz",
                        "KBB Merkezi - 2. Kat"
                );


        Department pediatrics =
                new Department(
                        "Çocuk Sağlığı ve Hastalıkları",
                        "Çocuk Sağlığı Merkezi - 1. Kat"
                );


        Department psychiatry =
                new Department(
                        "Psikiyatri",
                        "Ruh Sağlığı Merkezi - 4. Kat"
                );


        Department radiology =
                new Department(
                        "Radyoloji",
                        "Görüntüleme Merkezi - Zemin Kat"
                );


        // -----------------------------------------------------
        // BÖLÜMLERİ SİSTEME EKLE
        // -----------------------------------------------------

        departments.add(internalMedicine);

        departments.add(cardiology);

        departments.add(neurology);

        departments.add(pulmonology);

        departments.add(dermatology);

        departments.add(generalSurgery);

        departments.add(orthopedics);

        departments.add(neurosurgery);

        departments.add(urology);

        departments.add(gynecology);

        departments.add(ophthalmology);

        departments.add(ent);

        departments.add(pediatrics);

        departments.add(psychiatry);

        departments.add(radiology);
    }


    // =========================================================
    // DOKTORLAR
    // =========================================================

    /**
     * Sistemin başlangıç doktorlarını oluşturur.
     *
     * Doktorlar ilgili bölümlerle ilişkilendirilir ve
     * custom HashTable içerisine kaydedilir.
     */
    private static void initializeDoctors() {

        // -----------------------------------------------------
        // BÖLÜMLERİ BUL
        // -----------------------------------------------------

        Department internalMedicine =
                findDepartmentByName(
                        "İç Hastalıkları"
                );


        Department cardiology =
                findDepartmentByName(
                        "Kardiyoloji"
                );


        Department neurology =
                findDepartmentByName(
                        "Nöroloji"
                );


        Department pulmonology =
                findDepartmentByName(
                        "Göğüs Hastalıkları"
                );


        Department dermatology =
                findDepartmentByName(
                        "Dermatoloji"
                );


        Department generalSurgery =
                findDepartmentByName(
                        "Genel Cerrahi"
                );


        Department orthopedics =
                findDepartmentByName(
                        "Ortopedi ve Travmatoloji"
                );


        Department neurosurgery =
                findDepartmentByName(
                        "Beyin ve Sinir Cerrahisi"
                );


        Department urology =
                findDepartmentByName(
                        "Üroloji"
                );


        Department gynecology =
                findDepartmentByName(
                        "Kadın Hastalıkları ve Doğum"
                );


        Department ophthalmology =
                findDepartmentByName(
                        "Göz Hastalıkları"
                );


        Department ent =
                findDepartmentByName(
                        "Kulak Burun Boğaz"
                );


        Department pediatrics =
                findDepartmentByName(
                        "Çocuk Sağlığı ve Hastalıkları"
                );


        Department psychiatry =
                findDepartmentByName(
                        "Psikiyatri"
                );


        Department radiology =
                findDepartmentByName(
                        "Radyoloji"
                );


        // =====================================================
        // DOKTORLAR
        // =====================================================

        Doctor drAhmetYilmaz =
                new Doctor(
                        "DOC-ICD-001",
                        "Dr. Ahmet Yılmaz",
                        "İç Hastalıkları Uzmanı",
                        internalMedicine
                );


        Doctor drZeynepKaya =
                new Doctor(
                        "DOC-KAR-001",
                        "Dr. Zeynep Kaya",
                        "Kardiyoloji Uzmanı",
                        cardiology
                );


        Doctor drMehmetDemir =
                new Doctor(
                        "DOC-NEU-001",
                        "Dr. Mehmet Demir",
                        "Nöroloji Uzmanı",
                        neurology
                );


        Doctor drElifSahin =
                new Doctor(
                        "DOC-GOG-001",
                        "Dr. Elif Şahin",
                        "Göğüs Hastalıkları Uzmanı",
                        pulmonology
                );


        Doctor drBurakAydin =
                new Doctor(
                        "DOC-DER-001",
                        "Dr. Burak Aydın",
                        "Dermatoloji Uzmanı",
                        dermatology
                );


        Doctor drSelinCelik =
                new Doctor(
                        "DOC-GEN-001",
                        "Dr. Selin Çelik",
                        "Genel Cerrahi Uzmanı",
                        generalSurgery
                );


        Doctor drEmreArslan =
                new Doctor(
                        "DOC-ORT-001",
                        "Dr. Emre Arslan",
                        "Ortopedi ve Travmatoloji Uzmanı",
                        orthopedics
                );


        Doctor drDeryaKoc =
                new Doctor(
                        "DOC-BSC-001",
                        "Dr. Derya Koç",
                        "Beyin ve Sinir Cerrahisi Uzmanı",
                        neurosurgery
                );


        Doctor drMuratOzkan =
                new Doctor(
                        "DOC-URO-001",
                        "Dr. Murat Özkan",
                        "Üroloji Uzmanı",
                        urology
                );


        Doctor drEceKaraca =
                new Doctor(
                        "DOC-KDO-001",
                        "Dr. Ece Karaca",
                        "Kadın Hastalıkları ve Doğum Uzmanı",
                        gynecology
                );


        Doctor drCanYildiz =
                new Doctor(
                        "DOC-GOZ-001",
                        "Dr. Can Yıldız",
                        "Göz Hastalıkları Uzmanı",
                        ophthalmology
                );


        Doctor drMerveKilic =
                new Doctor(
                        "DOC-KBB-001",
                        "Dr. Merve Kılıç",
                        "Kulak Burun Boğaz Uzmanı",
                        ent
                );


        Doctor drHakanPolat =
                new Doctor(
                        "DOC-CPS-001",
                        "Dr. Hakan Polat",
                        "Çocuk Sağlığı ve Hastalıkları Uzmanı",
                        pediatrics
                );


        Doctor drAsliGunes =
                new Doctor(
                        "DOC-PSI-001",
                        "Dr. Aslı Güneş",
                        "Psikiyatri Uzmanı",
                        psychiatry
                );


        Doctor drOkanErsoy =
                new Doctor(
                        "DOC-RAD-001",
                        "Dr. Okan Ersoy",
                        "Radyoloji Uzmanı",
                        radiology
                );


        // =====================================================
        // DOKTORLARI REGISTRY'YE EKLE
        // =====================================================

        doctorRegistry.put(
                drAhmetYilmaz.getId(),
                drAhmetYilmaz
        );


        doctorRegistry.put(
                drZeynepKaya.getId(),
                drZeynepKaya
        );


        doctorRegistry.put(
                drMehmetDemir.getId(),
                drMehmetDemir
        );


        doctorRegistry.put(
                drElifSahin.getId(),
                drElifSahin
        );


        doctorRegistry.put(
                drBurakAydin.getId(),
                drBurakAydin
        );


        doctorRegistry.put(
                drSelinCelik.getId(),
                drSelinCelik
        );


        doctorRegistry.put(
                drEmreArslan.getId(),
                drEmreArslan
        );


        doctorRegistry.put(
                drDeryaKoc.getId(),
                drDeryaKoc
        );


        doctorRegistry.put(
                drMuratOzkan.getId(),
                drMuratOzkan
        );


        doctorRegistry.put(
                drEceKaraca.getId(),
                drEceKaraca
        );


        doctorRegistry.put(
                drCanYildiz.getId(),
                drCanYildiz
        );


        doctorRegistry.put(
                drMerveKilic.getId(),
                drMerveKilic
        );


        doctorRegistry.put(
                drHakanPolat.getId(),
                drHakanPolat
        );


        doctorRegistry.put(
                drAsliGunes.getId(),
                drAsliGunes
        );


        doctorRegistry.put(
                drOkanErsoy.getId(),
                drOkanErsoy
        );
    }


    // =========================================================
    // HASTA SİSTEMİ
    // =========================================================

    public static RecordRegistry getPatientRegistry() {

        return patientRegistry;
    }


    // =========================================================
    // RANDEVU SİSTEMİ
    // =========================================================

    public static AppointmentScheduler getAppointmentScheduler() {

        return appointmentScheduler;
    }


    // =========================================================
    // ACİL SERVİS SİSTEMİ
    // =========================================================

    public static EmergencyTriage getEmergencySystem() {

        return emergencySystem;
    }


    public static int getEmergencyCapacity() {

        return EMERGENCY_CAPACITY;
    }


    // =========================================================
    // DOKTOR SİSTEMİ
    // =========================================================

    public static HashTable<Doctor> getDoctorRegistry() {

        return doctorRegistry;
    }


    /**
     * Doktor ID kullanarak doktor bulur.
     *
     * Ortalama zaman karmaşıklığı: O(1)
     */
    public static Doctor getDoctorById(
            String id) {

        if (id == null ||
                id.trim().isEmpty()) {

            return null;
        }

        return doctorRegistry.get(
                id.trim()
        );
    }


    /**
     * Sistemde kayıtlı tüm doktorları döndürür.
     */
    public static List<Doctor> getAllDoctors() {

        return new ArrayList<>(
                doctorRegistry.getAllValues()
        );
    }


    // =========================================================
    // BÖLÜM SİSTEMİ
    // =========================================================

    /**
     * Sistemdeki tüm bölümleri döndürür.
     *
     * Dışarıdan listenin değiştirilmesini engellemek için
     * unmodifiable liste döndürülür.
     */
    public static List<Department> getDepartments() {

        return Collections.unmodifiableList(
                departments
        );
    }


    /**
     * Bölüm adına göre bölüm bulur.
     */
    public static Department findDepartmentByName(
            String name) {

        if (name == null) {

            return null;
        }


        for (Department department :
                departments) {

            if (department.getName()
                    .equalsIgnoreCase(
                            name.trim()
                    )) {

                return department;
            }
        }


        return null;
    }


    // =========================================================
    // SİSTEM BİLGİLERİ
    // =========================================================

    public static long getStudentId() {

        return STUDENT_ID;
    }


    /**
     * Dashboard ve sistem bilgileri için
     * kısa sistem özeti oluşturur.
     */
    public static String getSystemSummary() {

        return "Hastalar: "
                + patientRegistry.getPatientCount()

                + " | Doktorlar: "
                + doctorRegistry.size()

                + " | Randevular: "
                + appointmentScheduler
                        .getScheduledAppointmentsCount()

                + " | Acil Servis: "
                + emergencySystem
                        .getCurrentPatientCount();
    }
}