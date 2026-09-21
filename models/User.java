package models;

public class User {

    private String username;
    private String password;
    private UserRole role;

    // Sadece DOCTOR kullanıcıları için
    private String doctorId;


    public User(
            String username,
            String password,
            UserRole role
    ) {

        this.username = username;
        this.password = password;
        this.role = role;
        this.doctorId = null;
    }


    public User(
            String username,
            String password,
            UserRole role,
            String doctorId
    ) {

        this.username = username;
        this.password = password;
        this.role = role;
        this.doctorId = doctorId;
    }


    // =========================
    // GETTERS
    // =========================

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public UserRole getRole() {
        return role;
    }


    public String getDoctorId() {
        return doctorId;
    }


    // =========================
    // SETTERS
    // =========================

    public void setPassword(String password) {
        this.password = password;
    }


    public void setRole(UserRole role) {
        this.role = role;
    }


    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }


    public boolean isDoctor() {

        return role == UserRole.DOCTOR;
    }


    @Override
    public String toString() {

        return username
                + " | "
                + role;
    }
}