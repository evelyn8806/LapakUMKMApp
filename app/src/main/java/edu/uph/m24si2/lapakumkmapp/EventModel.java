package edu.uph.m24si2.lapakumkmapp;

public class EventModel {
    private String nama;
    private String kategori;
    private String lokasi;
    private String harga;
    private int gambar;
    private String deskripsi;

    public EventModel(String nama, String kategori, String lokasi, String harga, int gambar, String deskripsi) {
        this.nama = nama;
        this.kategori = kategori;
        this.lokasi = lokasi;
        this.harga = harga;
        this.gambar = gambar;
        this.deskripsi = deskripsi;
    }

    public String getNama() { return nama; }
    public String getKategori() { return kategori; }
    public String getLokasi() { return lokasi; }
    public String getHarga() { return harga; }
    public int getGambar() { return gambar; }
    public String getDeskripsi() { return deskripsi; }
}