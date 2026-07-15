package edu.uph.m24si2.lapakumkmapp;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static UserManager instance;
    private List<UserModel> listUsers;

    private UserManager() {
        listUsers = new ArrayList<>();
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
