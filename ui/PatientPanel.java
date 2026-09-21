package ui;

import app.HospitalData;
import models.Patient;
import services.RecordRegistry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PatientPanel extends JPanel {

    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color CARD_BACKGROUND = Color.WHITE;
    private final Color PRIMARY = new Color(37, 99, 235);
    private final Color SUCCESS = new Color(22, 163, 74);
    private final Color DANGER = new Color(220, 38, 38);
    private final Color SECONDARY = new Color(107, 114, 128);
    private final Color TEXT = new Color(31, 41, 55);
    private final Color MUTED = new Color(107, 114, 128);
    private final Color BORDER = new Color(229, 231, 235);

    private final RecordRegistry patientRegistry;

    // ============================================================
    // PATIENT INFORMATION
    // ============================================================

    private JTextField nationalIdField;
    private JTextField nameField;
    private JTextField birthDateField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;
    private JTextField severityField;

    private JComboBox<String> genderBox;
    private JComboBox<String> bloodGroupBox;

    // ============================================================
    // SEARCH
    // ============================================================

    private JTextField searchField;

    // ============================================================
    // TABLE
    // ============================================================

    private JTable patientTable;
    private DefaultTableModel tableModel;

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public PatientPanel() {

        patientRegistry = HospitalData.getPatientRegistry();

        setLayout(new BorderLayout(20, 20));

        setBackground(BACKGROUND);

        setBorder(
                new EmptyBorder(
                        24,
                        24,
                        24,
                        24
                )
        );

        buildHeader();
        buildMainContent();

        refreshTable();
    }

    // ============================================================
    // HEADER
    // ============================================================

    private void buildHeader() {

        JPanel headerPanel =
                new JPanel();

        headerPanel.setLayout(
                new BoxLayout(
                        headerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        headerPanel.setOpaque(false);

        JLabel title =
                new JLabel("Hasta Yönetimi");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(TEXT);

        JLabel subtitle =
                new JLabel(
                        "Hasta kayıtlarını görüntüleyin ve yönetin."
                );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(MUTED);

        headerPanel.add(title);

        headerPanel.add(
                Box.createVerticalStrut(5)
        );

        headerPanel.add(subtitle);

        add(
                headerPanel,
                BorderLayout.NORTH
        );
    }

    // ============================================================
    // MAIN CONTENT
    // ============================================================

    private void buildMainContent() {

        JPanel contentPanel =
                new JPanel();

        contentPanel.setLayout(
                new BoxLayout(
                        contentPanel,
                        BoxLayout.Y_AXIS
                )
        );

        contentPanel.setBackground(
                BACKGROUND
        );

        // ========================================================
        // ÜST BÖLÜM
        // ========================================================

        JPanel topPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                20,
                                0
                        )
                );

        topPanel.setOpaque(false);

        topPanel.add(
                buildPatientInformationPanel()
        );

        topPanel.add(
                buildSearchAndActionsPanel()
        );

        topPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        contentPanel.add(topPanel);

        contentPanel.add(
                Box.createVerticalStrut(20)
        );

        // ========================================================
        // KAYITLI HASTALAR TABLOSU
        // ========================================================

        JPanel tablePanel =
                buildPatientTablePanel();

        tablePanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        tablePanel.setPreferredSize(
                new Dimension(
                        0,
                        350
                )
        );

        tablePanel.setMinimumSize(
                new Dimension(
                        0,
                        300
                )
        );

        tablePanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        450
                )
        );

        contentPanel.add(tablePanel);

        contentPanel.add(
                Box.createVerticalStrut(20)
        );

        // ========================================================
        // TÜM SAYFAYI SCROLL YAP
        // ========================================================

        JScrollPane pageScroll =
                new JScrollPane(
                        contentPanel
                );

        pageScroll.setBorder(null);

        pageScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        pageScroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        pageScroll.getVerticalScrollBar()
                .setUnitIncrement(16);

        pageScroll.getVerticalScrollBar()
                .setBlockIncrement(80);

        pageScroll.getViewport()
                .setBackground(BACKGROUND);

        add(
                pageScroll,
                BorderLayout.CENTER
        );
    }

    // ============================================================
    // PATIENT INFORMATION PANEL
    // ============================================================

    private JPanel buildPatientInformationPanel() {

        JPanel card =
                createCardPanel();

        card.setLayout(
                new BorderLayout(
                        0,
                        15
                )
        );

        JLabel title =
                new JLabel("Hasta Bilgileri");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        title.setForeground(TEXT);

        card.add(
                title,
                BorderLayout.NORTH
        );

        JPanel form =
                new JPanel();

        form.setLayout(
                new BoxLayout(
                        form,
                        BoxLayout.Y_AXIS
                )
        );

        form.setOpaque(false);

        // --------------------------------------------------------
        // T.C. KİMLİK NO
        // --------------------------------------------------------

        nationalIdField =
                createTextField();

        form.add(
                createFormField(
                        "T.C. Kimlik No",
                        nationalIdField
                )
        );

        form.add(
                Box.createVerticalStrut(10)
        );

        // --------------------------------------------------------
        // AD SOYAD
        // --------------------------------------------------------

        nameField =
                createTextField();

        form.add(
                createFormField(
                        "Ad Soyad",
                        nameField
                )
        );

        form.add(
                Box.createVerticalStrut(10)
        );

        // --------------------------------------------------------
        // DOĞUM TARİHİ
        // --------------------------------------------------------

        birthDateField =
                createTextField();

        birthDateField.setToolTipText(
                "Örnek: 15.04.2004"
        );

        form.add(
                createFormField(
                        "Doğum Tarihi",
                        birthDateField
                )
        );

        form.add(
                Box.createVerticalStrut(10)
        );

        // --------------------------------------------------------
        // CİNSİYET
        // --------------------------------------------------------

        genderBox =
                new JComboBox<>(
                        new String[]{
                                "Seçiniz",
                                "Kadın",
                                "Erkek",
                                "Belirtilmemiş"
                        }
                );

        styleComboBox(genderBox);

        form.add(
                createFormField(
                        "Cinsiyet",
                        genderBox
                )
        );

        form.add(
                Box.createVerticalStrut(10)
        );

        // --------------------------------------------------------
        // TELEFON
        // --------------------------------------------------------

        phoneField =
                createTextField();

        form.add(
                createFormField(
                        "Telefon",
                        phoneField
                )
        );

        form.add(
                Box.createVerticalStrut(10)
        );

        // --------------------------------------------------------
        // E-POSTA
        // --------------------------------------------------------

        emailField =
                createTextField();

        form.add(
                createFormField(
                        "E-posta",
                        emailField
                )
        );

        form.add(
                Box.createVerticalStrut(10)
        );

        // --------------------------------------------------------
        // ADRES
        // --------------------------------------------------------

        addressField =
                createTextField();

        form.add(
                createFormField(
                        "Adres",
                        addressField
                )
        );

        form.add(
                Box.createVerticalStrut(10)
        );

        // --------------------------------------------------------
        // KAN GRUBU
        // --------------------------------------------------------

        bloodGroupBox =
                new JComboBox<>(
                        new String[]{
                                "Seçiniz",
                                "A+",
                                "A-",
                                "B+",
                                "B-",
                                "AB+",
                                "AB-",
                                "0+",
                                "0-"
                        }
                );

        styleComboBox(bloodGroupBox);

        form.add(
                createFormField(
                        "Kan Grubu",
                        bloodGroupBox
                )
        );

        form.add(
                Box.createVerticalStrut(10)
        );

        // --------------------------------------------------------
        // ŞİDDET
        // --------------------------------------------------------

        severityField =
                createTextField();

        severityField.setText("1");

        form.add(
                createFormField(
                        "Öncelik Şiddeti (1-10)",
                        severityField
                )
        );

        card.add(
                form,
                BorderLayout.CENTER
        );

        // --------------------------------------------------------
        // BUTTONS
        // --------------------------------------------------------

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                8,
                                0
                        )
                );

        buttonPanel.setOpaque(false);

        JButton addButton =
                createButton(
                        "Hasta Ekle",
                        SUCCESS
                );

        JButton updateButton =
                createButton(
                        "Güncelle",
                        PRIMARY
                );

        JButton clearButton =
                createButton(
                        "Temizle",
                        SECONDARY
                );

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(clearButton);

        card.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        addButton.addActionListener(
                e -> addPatient()
        );

        updateButton.addActionListener(
                e -> updatePatient()
        );

        clearButton.addActionListener(
                e -> clearForm()
        );

        return card;
    }

    // ============================================================
    // SEARCH & ACTIONS PANEL
    // ============================================================

    private JPanel buildSearchAndActionsPanel() {

        JPanel card =
                createCardPanel();

        card.setLayout(
                new BorderLayout(
                        0,
                        15
                )
        );

        JLabel title =
                new JLabel("Arama ve İşlemler");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        title.setForeground(TEXT);

        card.add(
                title,
                BorderLayout.NORTH
        );

        JPanel content =
                new JPanel();

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setOpaque(false);

        // --------------------------------------------------------
        // SEARCH FIELD
        // --------------------------------------------------------

        searchField =
                createTextField();

        /*
         * SADECE ARAMA ALANINI NORMAL BOYUTTA TUTUYORUZ.
         *
         * Genişlik : 250 px
         * Yükseklik: 36 px
         */
        searchField.setPreferredSize(
                new Dimension(
                        250,
                        36
                )
        );

        searchField.setMinimumSize(
                new Dimension(
                        250,
                        36
                )
        );

        searchField.setMaximumSize(
                new Dimension(
                        250,
                        36
                )
        );

        searchField.setToolTipText(
                "11 haneli T.C. Kimlik No"
        );

        /*
         * createFormField() normalde BoxLayout tarafından
         * genişletilebildiği için arama alanını ayrı bir
         * panel içinde sabitliyoruz.
         */
        JPanel searchFieldPanel =
                createFormField(
                        "T.C. Kimlik No ile Ara",
                        searchField
                );

        searchFieldPanel.setPreferredSize(
                new Dimension(
                        250,
                        61
                )
        );

        searchFieldPanel.setMinimumSize(
                new Dimension(
                        250,
                        61
                )
        );

        searchFieldPanel.setMaximumSize(
                new Dimension(
                        250,
                        61
                )
        );

        searchFieldPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        content.add(
                searchFieldPanel
        );

        content.add(
                Box.createVerticalStrut(15)
        );

        // --------------------------------------------------------
        // SEARCH BUTTON
        // --------------------------------------------------------

        JButton searchButton =
                createButton(
                        "Hasta Ara",
                        PRIMARY
                );

        content.add(
                searchButton
        );

        content.add(
                Box.createVerticalStrut(10)
        );

        // --------------------------------------------------------
        // DELETE BUTTON
        // --------------------------------------------------------

        JButton deleteButton =
                createButton(
                        "Hastayı Sil",
                        DANGER
                );

        content.add(
                deleteButton
        );

        content.add(
                Box.createVerticalStrut(10)
        );

        // --------------------------------------------------------
        // REFRESH BUTTON
        // --------------------------------------------------------

        JButton refreshButton =
                createButton(
                        "Listeyi Yenile",
                        SECONDARY
                );

        content.add(
                refreshButton
        );

        content.add(
                Box.createVerticalGlue()
        );

        // --------------------------------------------------------
        // INFORMATION
        // --------------------------------------------------------

        JLabel info =
                new JLabel(
                        "<html>"
                                + "<b>Bilgi:</b><br>"
                                + "Hasta arama, güncelleme ve silme "
                                + "işlemleri T.C. Kimlik No üzerinden "
                                + "gerçekleştirilir."
                                + "</html>"
                );

        info.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        info.setForeground(MUTED);

        content.add(info);

        card.add(
                content,
                BorderLayout.CENTER
        );

        // --------------------------------------------------------
        // ACTION LISTENERS
        // --------------------------------------------------------

        searchButton.addActionListener(
                e -> searchPatient()
        );

        deleteButton.addActionListener(
                e -> deletePatient()
        );

        refreshButton.addActionListener(
                e -> {

                    refreshTable();
                    clearSearch();

                }
        );

        searchField.addActionListener(
                e -> searchPatient()
        );

        return card;
    }

    // ============================================================
    // PATIENT TABLE
    // ============================================================

    private JPanel buildPatientTablePanel() {

        JPanel card =
                createCardPanel();

        card.setLayout(
                new BorderLayout(
                        0,
                        12
                )
        );

        JLabel title =
                new JLabel("Kayıtlı Hastalar");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        title.setForeground(TEXT);

        card.add(
                title,
                BorderLayout.NORTH
        );

        String[] columns = {

                "T.C. Kimlik No",
                "Hasta",
                "Yaş",
                "Cinsiyet",
                "Telefon",
                "Kan Grubu",
                "Şiddet"

        };

        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        patientTable =
                new JTable(tableModel);

        patientTable.setRowHeight(34);

        patientTable.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        patientTable.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                13
                        )
                );

        patientTable.getTableHeader()
                .setForeground(TEXT);

        patientTable.getTableHeader()
                .setBackground(
                        new Color(
                                243,
                                244,
                                246
                        )
                );

        patientTable.setGridColor(BORDER);

        patientTable.setShowVerticalLines(false);

        patientTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        /*
         * TABLONUN KENDİ SCROLL'U
         */
        JScrollPane tableScroll =
                new JScrollPane(
                        patientTable
                );

        tableScroll.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        tableScroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        tableScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        card.add(
                tableScroll,
                BorderLayout.CENTER
        );

        // ========================================================
        // TABLE SELECTION
        // ========================================================

        patientTable.getSelectionModel()
                .addListSelectionListener(
                        e -> {

                            if (!e.getValueIsAdjusting()) {

                                loadSelectedPatient();
                            }
                        }
                );

        return card;
    }

    // ============================================================
    // ADD PATIENT
    // ============================================================

    private void addPatient() {

        String nationalId =
                nationalIdField
                        .getText()
                        .trim();

        String name =
                nameField
                        .getText()
                        .trim();

        if (!validateRequiredFields()) {
            return;
        }

        if (!validateNationalId(
                nationalId
        )) {
            return;
        }

        if (patientRegistry
                .containsNationalId(
                        nationalId
                )) {

            showError(
                    "Bu T.C. Kimlik No ile kayıtlı bir hasta zaten mevcut."
            );

            return;
        }

        LocalDate birthDate =
                parseBirthDate();

        if (birthDate == null) {
            return;
        }

        int severity =
                parseSeverity();

        if (severity == -1) {
            return;
        }

        // Internal ID is generated automatically.
        // User never enters this ID.
        String internalId =
                generatePatientId();

        Patient patient =
                new Patient(
                        internalId,
                        name,
                        severity
                );

        patient.setNationalId(
                nationalId
        );

        patient.setBirthDate(
                birthDate
        );

        patient.setGender(
                getSelectedValue(
                        genderBox
                )
        );

        patient.setPhone(
                phoneField
                        .getText()
                        .trim()
        );

        patient.setEmail(
                emailField
                        .getText()
                        .trim()
        );

        patient.setAddress(
                addressField
                        .getText()
                        .trim()
        );

        patient.setBloodGroup(
                getSelectedValue(
                        bloodGroupBox
                )
        );

        boolean added =
                patientRegistry.add(
                        patient
                );

        if (!added) {

            showError(
                    "Hasta kaydı oluşturulamadı."
            );

            return;
        }

        showSuccess(
                "Hasta başarıyla kaydedildi."
        );

        clearForm();
        refreshTable();
    }

    // ============================================================
    // UPDATE PATIENT
    // ============================================================

    private void updatePatient() {

        String nationalId =
                nationalIdField
                        .getText()
                        .trim();

        if (!validateNationalId(
                nationalId
        )) {
            return;
        }

        Patient existingPatient =
                patientRegistry
                        .getByNationalId(
                                nationalId
                        );

        if (existingPatient == null) {

            showError(
                    "Bu T.C. Kimlik No ile kayıtlı hasta bulunamadı."
            );

            return;
        }

        if (!validateRequiredFields()) {
            return;
        }

        LocalDate birthDate =
                parseBirthDate();

        if (birthDate == null) {
            return;
        }

        int severity =
                parseSeverity();

        if (severity == -1) {
            return;
        }

        /*
         * Keep the internal ID of the existing patient.
         * User never sees or enters it.
         */
        Patient updatedPatient =
                new Patient(
                        existingPatient.getId(),
                        nameField
                                .getText()
                                .trim(),
                        severity
                );

        updatedPatient.setNationalId(
                nationalId
        );

        updatedPatient.setBirthDate(
                birthDate
        );

        updatedPatient.setGender(
                getSelectedValue(
                        genderBox
                )
        );

        updatedPatient.setPhone(
                phoneField
                        .getText()
                        .trim()
        );

        updatedPatient.setEmail(
                emailField
                        .getText()
                        .trim()
        );

        updatedPatient.setAddress(
                addressField
                        .getText()
                        .trim()
        );

        updatedPatient.setBloodGroup(
                getSelectedValue(
                        bloodGroupBox
                )
        );

        boolean updated =
                patientRegistry
                        .updateByNationalId(
                                nationalId,
                                updatedPatient
                        );

        if (!updated) {

            showError(
                    "Hasta bilgileri güncellenemedi."
            );

            return;
        }

        showSuccess(
                "Hasta bilgileri başarıyla güncellendi."
        );

        clearForm();
        refreshTable();
    }

    // ============================================================
    // DELETE PATIENT
    // ============================================================

    private void deletePatient() {

        String nationalId =
                searchField
                        .getText()
                        .trim();

        if (!validateNationalId(
                nationalId
        )) {
            return;
        }

        Patient patient =
                patientRegistry
                        .getByNationalId(
                                nationalId
                        );

        if (patient == null) {

            showError(
                    "Bu T.C. Kimlik No ile kayıtlı hasta bulunamadı."
            );

            return;
        }

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        "Hasta kaydı silinecek.\n\n"
                                + "Hasta: "
                                + patient.getName()
                                + "\n"
                                + "T.C. Kimlik No: "
                                + maskNationalId(
                                nationalId
                        )
                                + "\n\n"
                                + "Devam etmek istiyor musunuz?",
                        "Hasta Kaydını Sil",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        boolean deleted =
                patientRegistry
                        .deleteByNationalId(
                                nationalId
                        );

        if (!deleted) {

            showError(
                    "Hasta kaydı silinemedi."
            );

            return;
        }

        showSuccess(
                "Hasta kaydı başarıyla silindi."
        );

        clearForm();
        clearSearch();
        refreshTable();
    }

    // ============================================================
    // SEARCH PATIENT
    // ============================================================

    private void searchPatient() {

        String nationalId =
                searchField
                        .getText()
                        .trim();

        if (!validateNationalId(
                nationalId
        )) {
            return;
        }

        Patient patient =
                patientRegistry
                        .getByNationalId(
                                nationalId
                        );

        if (patient == null) {

            showError(
                    "Bu T.C. Kimlik No ile kayıtlı hasta bulunamadı."
            );

            return;
        }

        loadPatientIntoFields(
                patient
        );

        highlightPatient(
                patient
        );
    }

    // ============================================================
    // LOAD SELECTED PATIENT
    // ============================================================

    private void loadSelectedPatient() {

        int selectedRow =
                patientTable
                        .getSelectedRow();

        if (selectedRow < 0) {
            return;
        }

        List<Patient> patients =
                patientRegistry
                        .getAllPatients();

        if (selectedRow >= patients.size()) {
            return;
        }

        Patient patient =
                patients.get(
                        selectedRow
                );

        if (patient != null) {

            loadPatientIntoFields(
                    patient
            );
        }
    }

    // ============================================================
    // LOAD PATIENT INTO FORM
    // ============================================================

    private void loadPatientIntoFields(
            Patient patient) {

        if (patient == null) {
            return;
        }

        nationalIdField.setText(
                safeValue(
                        patient.getNationalId()
                )
        );

        nameField.setText(
                safeValue(
                        patient.getName()
                )
        );

        if (patient.getBirthDate() != null) {

            birthDateField.setText(
                    patient.getBirthDate()
                            .format(
                                    dateFormatter
                            )
            );

        } else {

            birthDateField.setText("");
        }

        selectComboValue(
                genderBox,
                patient.getGender()
        );

        phoneField.setText(
                safeValue(
                        patient.getPhone()
                )
        );

        emailField.setText(
                safeValue(
                        patient.getEmail()
                )
        );

        addressField.setText(
                safeValue(
                        patient.getAddress()
                )
        );

        selectComboValue(
                bloodGroupBox,
                patient.getBloodGroup()
        );

        severityField.setText(
                String.valueOf(
                        patient.getSeverity()
                )
        );
    }

    // ============================================================
    // REFRESH TABLE
    // ============================================================

    private void refreshTable() {

        if (tableModel == null) {
            return;
        }

        tableModel.setRowCount(0);

        List<Patient> patients =
                patientRegistry
                        .getAllPatients();

        for (Patient patient : patients) {

            if (patient == null) {
                continue;
            }

            Object[] row = {

                    maskNationalId(
                            patient.getNationalId()
                    ),

                    maskName(
                            patient.getName()
                    ),

                    patient.getAge(),

                    safeValue(
                            patient.getGender()
                    ),

                    maskPhone(
                            patient.getPhone()
                    ),

                    safeValue(
                            patient.getBloodGroup()
                    ),

                    patient.getSeverity()
            };

            tableModel.addRow(row);
        }
    }

    // ============================================================
    // HIGHLIGHT PATIENT
    // ============================================================

    private void highlightPatient(
            Patient targetPatient) {

        if (targetPatient == null) {
            return;
        }

        List<Patient> patients =
                patientRegistry
                        .getAllPatients();

        for (int i = 0;
             i < patients.size();
             i++) {

            Patient patient =
                    patients.get(i);

            if (patient != null
                    && patient.getId()
                    .equals(
                            targetPatient.getId()
                    )) {

                patientTable
                        .setRowSelectionInterval(
                                i,
                                i
                        );

                patientTable
                        .scrollRectToVisible(
                                patientTable
                                        .getCellRect(
                                                i,
                                                0,
                                                true
                                        )
                        );

                break;
            }
        }
    }

    // ============================================================
    // VALIDATION
    // ============================================================

    private boolean validateRequiredFields() {

        if (nationalIdField
                .getText()
                .trim()
                .isEmpty()) {

            showError(
                    "T.C. Kimlik No boş bırakılamaz."
            );

            nationalIdField.requestFocus();

            return false;
        }

        if (nameField
                .getText()
                .trim()
                .isEmpty()) {

            showError(
                    "Ad Soyad boş bırakılamaz."
            );

            nameField.requestFocus();

            return false;
        }

        return true;
    }

    private boolean validateNationalId(
            String nationalId) {

        if (nationalId == null
                || nationalId.trim().isEmpty()) {

            showError(
                    "T.C. Kimlik No giriniz."
            );

            return false;
        }

        if (!nationalId.matches(
                "\\d{11}"
        )) {

            showError(
                    "T.C. Kimlik No 11 haneli olmalıdır."
            );

            return false;
        }

        if (nationalId.charAt(0) == '0') {

            showError(
                    "T.C. Kimlik No 0 ile başlayamaz."
            );

            return false;
        }

        return true;
    }

    private LocalDate parseBirthDate() {

        String value =
                birthDateField
                        .getText()
                        .trim();

        if (value.isEmpty()) {

            showError(
                    "Doğum tarihi giriniz.\n"
                            + "Örnek: 15.04.2004"
            );

            birthDateField.requestFocus();

            return null;
        }

        try {

            LocalDate date =
                    LocalDate.parse(
                            value,
                            dateFormatter
                    );

            if (date.isAfter(
                    LocalDate.now()
            )) {

                showError(
                        "Doğum tarihi gelecekte olamaz."
                );

                return null;
            }

            return date;

        } catch (
                DateTimeParseException e) {

            showError(
                    "Doğum tarihi geçersiz.\n"
                            + "Format: GG.AA.YYYY\n"
                            + "Örnek: 15.04.2004"
            );

            return null;
        }
    }

    private int parseSeverity() {

        String value =
                severityField
                        .getText()
                        .trim();

        try {

            int severity =
                    Integer.parseInt(
                            value
                    );

            if (severity < 1
                    || severity > 10) {

                showError(
                        "Şiddet değeri 1 ile 10 arasında olmalıdır."
                );

                return -1;
            }

            return severity;

        } catch (
                NumberFormatException e) {

            showError(
                    "Şiddet değeri sayısal olmalıdır."
            );

            return -1;
        }
    }

    // ============================================================
    // INTERNAL ID
    // ============================================================

    private String generatePatientId() {

        int nextNumber =
                patientRegistry
                        .getPatientCount()
                        + 1;

        String id;

        do {

            id = String.format(
                    "PAT-%04d",
                    nextNumber
            );

            nextNumber++;

        } while (
                patientRegistry.contains(id)
        );

        return id;
    }

    // ============================================================
    // CLEAR
    // ============================================================

    private void clearForm() {

        nationalIdField.setText("");
        nameField.setText("");
        birthDateField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");

        severityField.setText("1");

        genderBox.setSelectedIndex(0);
        bloodGroupBox.setSelectedIndex(0);

        patientTable.clearSelection();
    }

    private void clearSearch() {

        searchField.setText("");
    }

    // ============================================================
    // FORM FIELD
    // ============================================================

    private JPanel createFormField(
            String labelText,
            JComponent component) {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BorderLayout(
                        0,
                        5
                )
        );

        panel.setOpaque(false);

        JLabel label =
                new JLabel(labelText);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        label.setForeground(TEXT);

        panel.add(
                label,
                BorderLayout.NORTH
        );

        panel.add(
                component,
                BorderLayout.CENTER
        );

        return panel;
    }

    // ============================================================
    // UI HELPERS
    // ============================================================

    private JPanel createCardPanel() {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                CARD_BACKGROUND
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                18,
                                18,
                                18,
                                18
                        )
                )
        );

        return panel;
    }

    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        field.setPreferredSize(
                new Dimension(
                        0,
                        36
                )
        );

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        36
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );

        return field;
    }

    private JButton createButton(
            String text,
            Color color) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                color
        );

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setPreferredSize(
                new Dimension(
                        120,
                        38
                )
        );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        38
                )
        );

        return button;
    }

    private void styleComboBox(
            JComboBox<String> comboBox) {

        comboBox.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        comboBox.setPreferredSize(
                new Dimension(
                        0,
                        36
                )
        );

        comboBox.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        36
                )
        );
    }

    // ============================================================
    // COMBO HELPERS
    // ============================================================

    private String getSelectedValue(
            JComboBox<String> comboBox) {

        Object value =
                comboBox.getSelectedItem();

        if (value == null) {
            return "";
        }

        String text =
                value.toString();

        if ("Seçiniz".equals(text)) {
            return "";
        }

        return text;
    }

    private void selectComboValue(
            JComboBox<String> comboBox,
            String value) {

        if (value == null
                || value.trim().isEmpty()) {

            comboBox.setSelectedIndex(0);

            return;
        }

        comboBox.setSelectedItem(
                value
        );

        if (comboBox.getSelectedIndex()
                == -1) {

            comboBox.setSelectedIndex(0);
        }
    }

    // ============================================================
    // DATA MASKING
    // ============================================================

    private String maskNationalId(
            String nationalId) {

        if (nationalId == null
                || nationalId.length() != 11) {

            return "***********";
        }

        return nationalId.charAt(0)
                + "*********"
                + nationalId.charAt(10);
    }

    private String maskName(
            String name) {

        if (name == null
                || name.trim().isEmpty()) {

            return "********";
        }

        String[] words =
                name.trim()
                        .split("\\s+");

        StringBuilder result =
                new StringBuilder();

        for (int i = 0;
             i < words.length;
             i++) {

            String word = words[i];

            if (word.length() <= 2) {

                result.append(word);

            } else {

                result.append(
                        word.substring(
                                0,
                                2
                        )
                );

                for (int j = 2;
                     j < word.length();
                     j++) {

                    result.append("*");
                }
            }

            if (i < words.length - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }

    private String maskPhone(
            String phone) {

        if (phone == null
                || phone.trim().isEmpty()) {

            return "-";
        }

        String cleanPhone =
                phone.trim();

        if (cleanPhone.length() <= 4) {
            return "****";
        }

        int visibleStart = 2;
        int visibleEnd = 2;

        StringBuilder result =
                new StringBuilder();

        result.append(
                cleanPhone.substring(
                        0,
                        visibleStart
                )
        );

        int maskLength =
                cleanPhone.length()
                        - visibleStart
                        - visibleEnd;

        for (int i = 0;
             i < maskLength;
             i++) {

            result.append("*");
        }

        result.append(
                cleanPhone.substring(
                        cleanPhone.length()
                                - visibleEnd
                )
        );

        return result.toString();
    }

    private String safeValue(
            String value) {

        if (value == null
                || value.trim().isEmpty()) {

            return "-";
        }

        return value;
    }

    // ============================================================
    // MESSAGES
    // ============================================================

    private void showError(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Hata",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showSuccess(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Başarılı",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}