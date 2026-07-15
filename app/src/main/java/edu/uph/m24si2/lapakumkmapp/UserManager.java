package edu.uph.m24si2.lapakumkmapp;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static UserManager instance;
    private List<UserModel> listUsers;

    private UserManager() {
        listUsers = new ArrayList<>();
        // Default users
        listUsers.add(new UserModel("Admin Lapak", "admin@umkm.com", "081122334455", "admin123", "ADMIN"));
        listUsers.add(new UserModel("Budi Santoso", "budi@gmail.com", "081234567890", "user123", "UMKM"));
        listUsers.add(new UserModel("Siti Aminah", "siti@gmail.com", "081298765432", "user123", "UMKM"));
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void addUser(UserModel user) {
        // Simple check to avoid duplicates by email
        for (UserModel u : listUsers) {
            if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
                return;
            }
        }
        listUsers.add(user);
    }

    public List<UserModel> getListUsers() {
        return listUsers;
    }

    public UserModel login(String email, String password) {
        for (UserModel u : listUsers) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}
