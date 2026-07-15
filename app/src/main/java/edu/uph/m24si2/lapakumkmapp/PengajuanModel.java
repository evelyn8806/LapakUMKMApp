package edu.uph.m24si2.lapakumkmapp;

public class PengajuanModel {
    private String id;
    private String namaUmkm;
    private String namaEvent;
    private String tanggal;
    private String status; // "Menunggu", "Disetujui", "Ditolak", "Diproses"
    private String userEmail;
    private String userName;

    public PengajuanModel(String id, String namaUmkm, String namaEvent, String tanggal, String status) {
        this.id = id;
        this.namaUmkm = namaUmkm;
        this.namaEvent = namaEvent;
        this.tanggal = tanggal;
        this.status = status;
        this.userEmail = "admin@umkmapp.com"; 
        this.userName = "Admin";
    }

    public PengajuanModel(String id, String namaUmkm, String namaEvent, String tanggal, String status, String userEmail, String userName) {
        this.id = id;
        this.namaUmkm = namaUmkm;
        this.namaEvent = namaEvent;
        this.tanggal = tanggal;
        this.status = status;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public String getId() { return id; }
    public String getNamaUmkm() { return namaUmkm; }
    public String getNamaEvent() { return namaEvent; }
    public String getTanggal() { return tanggal; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
