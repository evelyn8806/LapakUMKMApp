package edu.uph.m24si2.lapakumkmapp;

import java.io.Serializable;
import java.util.UUID;

public class RentalRequest implements Serializable {
    public enum Status {
        MENUNGGU_PEMBAYARAN("Menunggu pembayaran"),
        MENUNGGU_PERSETUJUAN("Menunggu persetujuan"),
        AKTIF("Aktif"),
        SELESAI("Selesai");

        private String label;
        Status(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    private String id;
    private String eventName;
    private String eventLocation;
    private String eventPrice;
    private int eventImage;
    private Status status;
    private long timestamp;

    public RentalRequest(String eventName, String eventLocation, String eventPrice, int eventImage, Status status) {
        this.id = UUID.randomUUID().toString();
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventPrice = eventPrice;
        this.eventImage = eventImage;
        this.status = status;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public String getEventName() { return eventName; }
    public String getEventLocation() { return eventLocation; }
    public String getEventPrice() { return eventPrice; }
    public int getEventImage() { return eventImage; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public long getTimestamp() { return timestamp; }
}
