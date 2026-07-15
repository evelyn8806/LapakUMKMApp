package edu.uph.m24si2.lapakumkmapp;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PengajuanManager {
    private static PengajuanManager instance;
    private List<PengajuanModel> listPengajuan;
    private static final String PREF_NAME = "PengajuanPrefs";
    private static final String KEY_PENGAJUAN = "pengajuan_list";

    private PengajuanManager() {
        listPengajuan = new ArrayList<>();
    }

    public static synchronized PengajuanManager getInstance() {
        if (instance == null) {
            instance = new PengajuanManager();
        }
        return instance;
    }

    public void loadPengajuan(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_PENGAJUAN, null);
        listPengajuan.clear();
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    PengajuanModel p = new PengajuanModel(
                        obj.getString("id"),
                        obj.getString("namaUmkm"),
                        obj.getString("namaEvent"),
                        obj.getString("tanggal"),
                        obj.getString("status"),
                        obj.getString("userEmail"),
                        obj.getString("userName"),
                        obj.optString("deskripsi", ""),
                        obj.optString("harga", ""),
                        obj.optString("lokasi", "")
                    );
                    p.setRentalRequestId(obj.optString("rentalRequestId", ""));
                    p.setJenisUsaha(obj.optString("jenisUsaha", ""));
                    p.setDeskripsiUsaha(obj.optString("deskripsiUsaha", ""));
                    p.setNib(obj.optString("nib", ""));
                    p.setKtpUri(obj.optString("ktpUri", ""));
                    p.setNibUri(obj.optString("nibUri", ""));
                    listPengajuan.add(p);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePengajuan(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        try {
            for (PengajuanModel p : listPengajuan) {
                JSONObject obj = new JSONObject();
                obj.put("id", p.getId());
                obj.put("namaUmkm", p.getNamaUmkm());
                obj.put("namaEvent", p.getNamaEvent());
                obj.put("tanggal", p.getTanggal());
                obj.put("status", p.getStatus());
                obj.put("userEmail", p.getUserEmail());
                obj.put("userName", p.getUserName());
                obj.put("deskripsi", p.getDeskripsi());
                obj.put("harga", p.getHarga());
                obj.put("lokasi", p.getLokasi());
                obj.put("rentalRequestId", p.getRentalRequestId());
                obj.put("jenisUsaha", p.getJenisUsaha());
                obj.put("deskripsiUsaha", p.getDeskripsiUsaha());
                obj.put("nib", p.getNib());
                obj.put("ktpUri", p.getKtpUri());
                obj.put("nibUri", p.getNibUri());
                jsonArray.put(obj);
            }
            prefs.edit().putString(KEY_PENGAJUAN, jsonArray.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void tambahPengajuan(Context context, String namaUmkm, String namaEvent, String tanggal, String userEmail, String userName, String rentalRequestId,
                                String jenisUsaha, String deskripsiUsaha, String nib, String ktpUri, String nibUri, String deskripsi, String harga, String lokasi) {
        String id = String.valueOf(System.currentTimeMillis());
        PengajuanModel p = new PengajuanModel(id, namaUmkm, namaEvent, tanggal, "Menunggu", userEmail, userName, deskripsi, harga, lokasi);
        p.setRentalRequestId(rentalRequestId);
        p.setJenisUsaha(jenisUsaha);
        p.setDeskripsiUsaha(deskripsiUsaha);
        p.setNib(nib);
        p.setKtpUri(ktpUri);
        p.setNibUri(nibUri);
        listPengajuan.add(0, p);
        savePengajuan(context);

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

    public void updateStatus(Context context, String id, String newStatus) {
        for (PengajuanModel p : listPengajuan) {
            if (p.getId().equals(id)) {
                p.setStatus(newStatus);
                break;
            }
        }
        savePengajuan(context);
    }
}
