package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        UserManager.getInstance().loadUsers(this);

        AdminNavigationHelper.setupNavigation(this, R.id.navAdminDashboard);
        loadApplications();
        updateStats();
        
        setupStatClicks();

        findViewById(R.id.ivAdminNotification).setOnClickListener(v -> {
            startActivity(new Intent(this, NotificationsActivity.class));
        });
    }

    private void setupStatClicks() {
        findViewById(R.id.cardStatsNew).setOnClickListener(v -> openPengajuanWithFilter("Baru"));
        findViewById(R.id.cardStatsProcessing).setOnClickListener(v -> openPengajuanWithFilter("Diproses"));
        findViewById(R.id.cardStatsApproved).setOnClickListener(v -> openPengajuanWithFilter("Disetujui"));
        findViewById(R.id.cardStatsRejected).setOnClickListener(v -> openPengajuanWithFilter("Ditolak"));
    }

    private void openPengajuanWithFilter(String filter) {
        Intent intent = new Intent(this, AdminPengajuanActivity.class);
        intent.putExtra("FILTER_STATUS", filter);
        startActivity(intent);
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

        for (PengajuanModel p : list) {
            View itemView = getLayoutInflater().inflate(R.layout.item_pengajuan_dashboard, container, false);
            
            TextView tvUsaha = itemView.findViewById(R.id.tvUmkmName);
            TextView tvCategory = itemView.findViewById(R.id.tvUmkmCategory);
            TextView tvStatus = itemView.findViewById(R.id.tvStatusBadge);

            tvUsaha.setText(p.getNamaUmkm());
            tvCategory.setText(p.getNamaEvent());
            tvStatus.setText(p.getStatus());
            
            // Status Styling
            if (p.getStatus().equals("Menunggu") || p.getStatus().equals("Baru")) {
                tvStatus.setText("Baru");
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.admin_primary));
                tvStatus.setBackgroundResource(R.drawable.bg_tag_new);
            } else if (p.getStatus().equals("Disetujui")) {
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_approved));
                tvStatus.setBackgroundResource(R.drawable.bg_tag_approved);
            } else if (p.getStatus().equals("Ditolak")) {
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_rejected));
                tvStatus.setBackgroundResource(R.drawable.bg_tag_rejected);
            } else if (p.getStatus().equals("Diproses")) {
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_processing));
                tvStatus.setBackgroundResource(R.drawable.bg_tag_processing);
            }

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(this, AdminPengajuanDetailActivity.class);
                intent.putExtra("pengajuan", p);
                startActivity(intent);
            });

            container.addView(itemView);
        }

        if (list.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("Belum ada pengajuan terbaru");
            emptyText.setGravity(android.view.Gravity.CENTER);
            emptyText.setPadding(0, 50, 0, 50);
            emptyText.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
            container.addView(emptyText);
        }
    }
}
