package edu.uph.m24si2.lapakumkmapp;

public class UserModel {
    private String nama;
    private String email;
    private String phone;
    private String password;
    private String role; // "UMKM" or "ADMIN"

    public UserModel(String nama, String email, String phone, String password, String role) {
        this.nama = nama;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }

    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}
