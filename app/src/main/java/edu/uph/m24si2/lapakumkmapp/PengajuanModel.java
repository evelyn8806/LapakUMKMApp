package edu.uph.m24si2.lapakumkmapp;

import java.io.Serializable;

public class PengajuanModel implements Serializable {
    private String id;
    private String namaUmkm;
    private String namaEvent;
    private String tanggal;
    private String status; // "Menunggu", "Disetujui", "Ditolak", "Diproses"
    private String userEmail;
    private String userName;
    private String rentalRequestId;
    
    // Additional fields for review
    private String jenisUsaha;
    private String deskripsiUsaha;
    private String nib;
    private String ktpUri;
    private String nibUri;

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

    public PengajuanModel(String id, String namaUmkm, String namaEvent, String tanggal, String status, String userEmail, String userName, String rentalRequestId) {
        this.id = id;
        this.namaUmkm = namaUmkm;
        this.namaEvent = namaEvent;
        this.tanggal = tanggal;
        this.status = status;
        this.userEmail = userEmail;
        this.userName = userName;
        this.rentalRequestId = rentalRequestId;
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
    public String getRentalRequestId() { return rentalRequestId; }
    public void setRentalRequestId(String rentalRequestId) { this.rentalRequestId = rentalRequestId; }

    public String getJenisUsaha() { return jenisUsaha; }
    public void setJenisUsaha(String jenisUsaha) { this.jenisUsaha = jenisUsaha; }
    public String getDeskripsiUsaha() { return deskripsiUsaha; }
    public void setDeskripsiUsaha(String deskripsiUsaha) { this.deskripsiUsaha = deskripsiUsaha; }
    public String getNib() { return nib; }
    public void setNib(String nib) { this.nib = nib; }
    public String getKtpUri() { return ktpUri; }
    public void setKtpUri(String ktpUri) { this.ktpUri = ktpUri; }
    public String getNibUri() { return nibUri; }
    public void setNibUri(String nibUri) { this.nibUri = nibUri; }
}
