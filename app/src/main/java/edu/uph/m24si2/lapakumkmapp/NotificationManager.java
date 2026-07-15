package edu.uph.m24si2.lapakumkmapp;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager {
    private static NotificationManager instance;
    private List<NotificationModel> notifications;

    private NotificationManager() {
        notifications = new ArrayList<>();
    }

    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public void addNotification(String title, String message) {
        notifications.add(0, new NotificationModel(title, message));
    }

    public List<NotificationModel> getNotifications() {
        return notifications;
    }
}
