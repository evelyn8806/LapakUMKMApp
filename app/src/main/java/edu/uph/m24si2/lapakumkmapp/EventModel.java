package edu.uph.m24si2.lapakumkmapp;

public class EventModel {
    private String nama;
    private String kategori;
    private String lokasi;
    private String harga;
    private int numericHarga;
    private int gambar;
    private String deskripsi;
    private String kota;
    private double latitude;
    private double longitude;

    public EventModel(String nama, String kategori, String lokasi, String harga, int numericHarga, int gambar, String deskripsi, String kota, double latitude, double longitude) {
        this.nama = nama;
        this.kategori = kategori;
        this.lokasi = lokasi;
        this.harga = harga;
        this.numericHarga = numericHarga;
        this.gambar = gambar;
        this.deskripsi = deskripsi;
        this.kota = kota;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNama() { return nama; }
    public String getKategori() { return kategori; }
    public String getLokasi() { return lokasi; }
    public String getHarga() { return harga; }
    public int getNumericHarga() { return numericHarga; }
    public int getGambar() { return gambar; }
    public String getDeskripsi() { return deskripsi; }
    public String getKota() { return kota; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
