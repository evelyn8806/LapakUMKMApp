package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        AdminNavigationHelper.setupNavigation(this, R.id.navAdminDashboard);
        loadApplications();
        updateStats();
        
        findViewById(R.id.tvSeeAllSubmissions).setOnClickListener(v -> {
            startActivity(new Intent(this, AdminPengajuanActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadApplications();
        updateStats();
    }

    private void updateStats() {
        TextView tvTotalNew = findViewById(R.id.tvTotalNew);
        TextView tvTotalProcessing = findViewById(R.id.tvTotalProcessing);
        TextView tvTotalApproved = findViewById(R.id.tvTotalApproved);
        TextView tvTotalRejected = findViewById(R.id.tvTotalRejected);

        PengajuanManager manager = PengajuanManager.getInstance();
        tvTotalNew.setText(String.valueOf(manager.getMenungguVerifikasi()));
        tvTotalProcessing.setText(String.valueOf(manager.getDiproses())); 
        tvTotalApproved.setText(String.valueOf(manager.getDisetujui()));
        tvTotalRejected.setText(String.valueOf(manager.getDitolak()));
    }

    private void loadApplications() {
        LinearLayout container = findViewById(R.id.containerPengajuanAdmin);
        if (container == null) return;
        
        container.removeAllViews();
        List<PengajuanModel> list = PengajuanManager.getInstance().getListPengajuan();

        int count = 0;
        for (PengajuanModel p : list) {
            if (count >= 4) break;
            
            View itemView = getLayoutInflater().inflate(R.layout.item_pengajuan_dashboard, container, false);
            
            TextView tvUsaha = itemView.findViewById(R.id.tvUmkmName);
            TextView tvCategory = itemView.findViewById(R.id.tvUmkmCategory);
            TextView tvStatus = itemView.findViewById(R.id.tvStatusBadge);

            tvUsaha.setText(p.getNamaUmkm());
            tvCategory.setText("Kuliner"); // Simplified
            tvStatus.setText(p.getStatus());
            
            // Set status color and background
            if (p.getStatus().equals("Menunggu")) {
                tvStatus.setText("Baru");
                tvStatus.setTextColor(getResources().getColor(R.color.admin_primary));
                tvStatus.setBackgroundResource(R.drawable.bg_tag_new);
            } else if (p.getStatus().equals("Disetujui")) {
                tvStatus.setTextColor(getResources().getColor(R.color.status_approved));
                tvStatus.setBackgroundResource(R.drawable.bg_tag_approved);
            } else if (p.getStatus().equals("Ditolak")) {
                tvStatus.setTextColor(getResources().getColor(R.color.status_rejected));
                tvStatus.setBackgroundResource(R.drawable.bg_tag_rejected);
            }

            itemView.setOnClickListener(v -> {
                startActivity(new Intent(this, AdminPengajuanActivity.class));
            });

            container.addView(itemView);
            count++;
        }
    }
}
