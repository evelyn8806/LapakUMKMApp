package edu.uph.m24si2.lapakumkmapp;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static UserManager instance;
    private List<UserModel> listUsers;
    private static final String PREF_NAME = "UserManagerPrefs";
    private static final String KEY_USERS = "users_list";

    private UserManager() {
        listUsers = new ArrayList<>();
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void loadUsers(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String usersJson = prefs.getString(KEY_USERS, null);
        listUsers.clear();
        if (usersJson != null) {
            try {
                JSONArray jsonArray = new JSONArray(usersJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    listUsers.add(new UserModel(
                        obj.getString("nama"),
                        obj.getString("email"),
                        obj.getString("phone"),
                        obj.getString("password"),
                        obj.getString("role")
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveUsers(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        try {
            for (UserModel user : listUsers) {
                JSONObject obj = new JSONObject();
                obj.put("nama", user.getNama());
                obj.put("email", user.getEmail());
                obj.put("phone", user.getPhone());
                obj.put("password", user.getPassword());
                obj.put("role", user.getRole());
                jsonArray.put(obj);
            }
            prefs.edit().putString(KEY_USERS, jsonArray.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addUser(Context context, UserModel user) {
        // Simple check to avoid duplicates by email
        for (UserModel u : listUsers) {
            if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
                return;
            }
        }
        listUsers.add(user);
        saveUsers(context);
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
