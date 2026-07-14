package edu.uph.m24si2.lapakumkmapp;

import java.util.ArrayList;
import java.util.List;

public class PengajuanManager {
    private static PengajuanManager instance;
    private List<PengajuanModel> listPengajuan;

    private PengajuanManager() {
        listPengajuan = new ArrayList<>();
        // Data Awal untuk Demo Admin (based on image)
        listPengajuan.add(new PengajuanModel("1", "Bakso Berkah", "Festival Kuliner", "2 jam lalu", "Menunggu"));
        listPengajuan.add(new PengajuanModel("2", "Kreasi Anyaman", "Bazaar Kerajinan", "5 jam lalu", "Diproses"));
        listPengajuan.add(new PengajuanModel("3", "Jajanan Tradisional", "Festival Kuliner", "1 hari lalu", "Disetujui"));
        listPengajuan.add(new PengajuanModel("4", "Dapur Mama", "Festival Kuliner", "2 hari lalu", "Ditolak"));
        
        listPengajuan.add(new PengajuanModel("5", "Kopi Nikmat", "Festival Kuliner Nusantara", "01 Des 2024", "Menunggu"));
        listPengajuan.add(new PengajuanModel("6", "Dapur Mbak Sari", "Pasar Malam Tahun Baru", "25 Des 2024", "Disetujui"));
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

    public void tambahPengajuan(String namaUmkm, String namaEvent, String tanggal) {
        String id = String.valueOf(listPengajuan.size() + 1);
        listPengajuan.add(0, new PengajuanModel(id, namaUmkm, namaEvent, tanggal, "Menunggu"));
    }

    public int getTotalEvent() { return 24; } // Dummy data
    
    public int getTotalPengajuan() { return listPengajuan.size(); }
    
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
