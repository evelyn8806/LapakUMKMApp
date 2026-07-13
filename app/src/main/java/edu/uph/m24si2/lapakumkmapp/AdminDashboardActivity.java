package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView tvTotalEvent, tvTotalPengajuan, tvMenunggu, tvDisetujui;
    private LinearLayout containerPengajuan;
    private PengajuanManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        manager = PengajuanManager.getInstance();

        tvTotalEvent = findViewById(R.id.tvTotalEventAdmin);
        tvTotalPengajuan = findViewById(R.id.tvTotalPengajuanAdmin);
        tvMenunggu = findViewById(R.id.tvMenungguAdmin);
        tvDisetujui = findViewById(R.id.tvDisetujuiAdmin);
        containerPengajuan = findViewById(R.id.containerPengajuanAdmin);

        findViewById(R.id.btnLogoutAdmin).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        updateUI();
    }

    private void updateUI() {
        tvTotalEvent.setText(String.valueOf(manager.getTotalEvent()));
        tvTotalPengajuan.setText(String.valueOf(manager.getTotalPengajuan()));
        tvMenunggu.setText(String.valueOf(manager.getMenungguVerifikasi()));
        tvDisetujui.setText(String.valueOf(manager.getDisetujui()));

        containerPengajuan.removeAllViews();
        List<PengajuanModel> list = manager.getListPengajuan();

        for (PengajuanModel p : list) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.item_pengajuan_admin, containerPengajuan, false);
            
            TextView tvNama = itemView.findViewById(R.id.tvNamaUmkmItem);
            TextView tvEvent = itemView.findViewById(R.id.tvEventItem);
            TextView tvTanggal = itemView.findViewById(R.id.tvTanggalItem);
            TextView tvStatus = itemView.findViewById(R.id.tvStatusItem);

            tvNama.setText(p.getNamaUmkm());
            tvEvent.setText(p.getNamaEvent());
            tvTanggal.setText(p.getTanggal());
            tvStatus.setText(p.getStatus());

            if (p.getStatus().equals("Menunggu")) {
                tvStatus.setTextColor(Color.parseColor("#FF8F00"));
                tvStatus.setOnClickListener(v -> showActionDialog(p));
            } else if (p.getStatus().equals("Disetujui")) {
                tvStatus.setTextColor(Color.parseColor("#2E7D32"));
            } else {
                tvStatus.setTextColor(Color.RED);
            }

            containerPengajuan.addView(itemView);
        }
    }

    private void showActionDialog(PengajuanModel p) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Verifikasi Pengajuan");
        builder.setMessage("Apakah Anda ingin menyetujui atau menolak pengajuan dari " + p.getNamaUmkm() + "?");
        
        builder.setPositiveButton("Setujui", (dialog, which) -> {
            manager.updateStatus(p.getId(), "Disetujui");
            updateUI();
            Toast.makeText(this, "Pengajuan Disetujui", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Tolak", (dialog, which) -> {
            manager.updateStatus(p.getId(), "Ditolak");
            updateUI();
            Toast.makeText(this, "Pengajuan Ditolak", Toast.LENGTH_SHORT).show();
        });

        builder.show();
    }
}
