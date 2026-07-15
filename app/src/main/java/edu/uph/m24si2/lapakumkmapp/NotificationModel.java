package edu.uph.m24si2.lapakumkmapp;

public class NotificationModel {
    private String title;
    private String message;
    private long timestamp;

    public NotificationModel(String title, String message) {
        this.title = title;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public long getTimestamp() { return timestamp; }
}
