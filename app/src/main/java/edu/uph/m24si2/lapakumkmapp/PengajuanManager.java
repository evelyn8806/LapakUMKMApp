package edu.uph.m24si2.lapakumkmapp;

import java.util.ArrayList;
import java.util.List;

public class PengajuanManager {
    private static PengajuanManager instance;
    private List<PengajuanModel> listPengajuan;

    private PengajuanManager() {
        listPengajuan = new ArrayList<>();
    }

    public static synchronized PengajuanManager getInstance() {
        if (instance == null) {
            instance = new PengajuanManager();
        }
        return instance;
    }

    public List<PengajuanModel> getListPengajuan() {
        return listPengajuan;
    }

    public List<PengajuanModel> getListPengajuanByUser(String email) {
        List<PengajuanModel> userList = new ArrayList<>();
        for (PengajuanModel p : listPengajuan) {
            if (p.getUserEmail() != null && p.getUserEmail().equalsIgnoreCase(email)) {
                userList.add(p);
            }
        }
        return userList;
    }

    public void tambahPengajuan(String namaUmkm, String namaEvent, String tanggal, String userEmail, String userName, String rentalRequestId,
                                String jenisUsaha, String deskripsiUsaha, String nib, String ktpUri, String nibUri) {
        String id = String.valueOf(listPengajuan.size() + 1);
        PengajuanModel p = new PengajuanModel(id, namaUmkm, namaEvent, tanggal, "Menunggu", userEmail, userName, rentalRequestId);
        p.setJenisUsaha(jenisUsaha);
        p.setDeskripsiUsaha(deskripsiUsaha);
        p.setNib(nib);
        p.setKtpUri(ktpUri);
        p.setNibUri(nibUri);
        listPengajuan.add(0, p);

        // Add Notification for Admin
        NotificationManager.getInstance().addNotification("Pengajuan Baru", 
            "UMKM " + namaUmkm + " telah mengajukan sewa untuk event " + namaEvent + ".");
    }

    public int getTotalEvent() { return 24; } 
    
    public int getTotalPengajuan() { return listPengajuan.size(); }

    public int getTotalPengajuanByUser(String email) {
        return getListPengajuanByUser(email).size();
    }
    
    public int getMenungguVerifikasi() {
        int count = 0;
        for (PengajuanModel p : listPengajuan) {
            if (p.getStatus().equals("Menunggu")) count++;
        }
        return count;
    }

    public int getDiproses() {
        int count = 0;
        for (PengajuanModel p : listPengajuan) {
            if (p.getStatus().equals("Diproses")) count++;
        }
        return count;
    }
    
    public int getDisetujui() {
        int count = 0;
        for (PengajuanModel p : listPengajuan) {
            if (p.getStatus().equals("Disetujui") || p.getStatus().equals("Aktif")) count++;
        }
        return count;
    }

    public int getDisetujuiByUser(String email) {
        int count = 0;
        for (PengajuanModel p : getListPengajuanByUser(email)) {
            if (p.getStatus().equals("Disetujui") || p.getStatus().equals("Aktif")) {
                count++;
            }
        }
        return count;
    }

    public int getDitolak() {
        int count = 0;
        for (PengajuanModel p : listPengajuan) {
            if (p.getStatus().equals("Ditolak")) count++;
        }
        return count;
    }

    public void updateStatus(String id, String newStatus) {
        for (PengajuanModel p : listPengajuan) {
            if (p.getId().equals(id)) {
                p.setStatus(newStatus);
                break;
            }
        }
    }
}
