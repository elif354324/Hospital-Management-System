package services;

import models.User;
import models.UserRole;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {

    private Map<String, User> users;

    private User currentUser;


    public AuthenticationService() {

        users = new HashMap<>();

        initializeDefaultUsers();
    }


    private void initializeDefaultUsers() {

        users.put(
                "admin",
                new User(
                        "admin",
                        "admin123",
                        UserRole.ADMIN
                )
        );


        users.put(
                "reception",
                new User(
                        "reception",
                        "reception123",
                        UserRole.RECEPTIONIST
                )
        );


        // Default doctor account
        users.put(
                "drsmith",
                new User(
                        "drsmith",
                        "doctor123",
                        UserRole.DOCTOR,
                        "DOC-NEU-001"
                )
        );
    }


    // =========================
    // LOGIN
    // =========================

    public boolean login(
            String username,
            String password
    ) {

        if (username == null
                || password == null) {

            return false;
        }


        User user =
                users.get(
                        username.trim()
                );


        if (user == null) {

            return false;
        }


        if (!user.getPassword()
                .equals(password)) {

            return false;
        }


        currentUser = user;

        return true;
    }


    // =========================
    // LOGOUT
    // =========================

    public void logout() {

        currentUser = null;
    }


    // =========================
    // CURRENT USER
    // =========================

    public User getCurrentUser() {

        return currentUser;
    }


    public boolean isLoggedIn() {

        return currentUser != null;
    }


    // =========================
    // ROLE CHECKS
    // =========================

    public boolean isAdmin() {

        return isLoggedIn()
                && currentUser.getRole()
                == UserRole.ADMIN;
    }


    public boolean isDoctor() {

        return isLoggedIn()
                && currentUser.getRole()
                == UserRole.DOCTOR;
    }


    public boolean isReceptionist() {

        return isLoggedIn()
                && currentUser.getRole()
                == UserRole.RECEPTIONIST;
    }


    // =========================
    // USER MANAGEMENT
    // =========================

    public boolean addUser(User user) {

        if (user == null
                || user.getUsername() == null
                || user.getUsername().trim().isEmpty()) {

            return false;
        }


        String username =
                user.getUsername().trim();


        if (users.containsKey(username)) {

            return false;
        }


        users.put(
                username,
                user
        );


        return true;
    }


    public User getUser(
            String username
    ) {

        if (username == null) {

            return null;
        }


        return users.get(
                username.trim()
        );
    }


    public Map<String, User> getUsers() {

        return new HashMap<>(
                users
        );
    }
}