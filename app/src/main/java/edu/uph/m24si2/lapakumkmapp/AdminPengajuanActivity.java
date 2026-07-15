package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class AdminPengajuanActivity extends AppCompatActivity {

    private String currentFilter = "Semua";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pengajuan);

        UserManager.getInstance().loadUsers(this);
        PengajuanManager.getInstance().loadPengajuan(this);

        AdminNavigationHelper.setupNavigation(this, R.id.navAdminPengajuan);
        
        String filterExtra = getIntent().getStringExtra("FILTER_STATUS");
        if (filterExtra != null) {
            currentFilter = filterExtra;
        }

        setupTabs();
        loadApplications();
    }

    private void setupTabs() {
        LinearLayout tabsLayout = (LinearLayout) ((android.widget.HorizontalScrollView) findViewById(R.id.tabsAdmin)).getChildAt(0);
        updateTabStyles(tabsLayout);
        for (int i = 0; i < tabsLayout.getChildCount(); i++) {
            View view = tabsLayout.getChildAt(i);
            if (view instanceof MaterialButton) {
                MaterialButton btn = (MaterialButton) view;
                btn.setOnClickListener(v -> {
                    currentFilter = btn.getText().toString();
                    updateTabStyles(tabsLayout);
                    loadApplications();
                });
            }
        }
    }

    private void updateTabStyles(LinearLayout tabsLayout) {
        for (int i = 0; i < tabsLayout.getChildCount(); i++) {
            View view = tabsLayout.getChildAt(i);
            if (view instanceof MaterialButton) {
                MaterialButton btn = (MaterialButton) view;
                if (btn.getText().toString().equals(currentFilter)) {
                    btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(ContextCompat.getColor(this, R.color.admin_primary)));
                    btn.setTextColor(ContextCompat.getColor(this, R.color.white));
                } else {
                    btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#F5F5F5")));
                    btn.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
                }
            }
        }
    }

    private void loadApplications() {
        LinearLayout container = findViewById(R.id.containerPengajuanFull);
        if (container == null) return;
        
        container.removeAllViews();
        List<PengajuanModel> list = PengajuanManager.getInstance().getListPengajuan();

        for (PengajuanModel p : list) {
            // Filter logic
            if (!currentFilter.equals("Semua")) {
                String status = p.getStatus();
                if (currentFilter.equals("Baru") && !status.equals("Menunggu")) continue;
                if (currentFilter.equals("Disetujui") && !status.equals("Disetujui")) continue;
                if (currentFilter.equals("Ditolak") && !status.equals("Ditolak")) continue;
                if (currentFilter.equals("Diproses") && !status.equals("Diproses")) continue;
            }

            View itemView = getLayoutInflater().inflate(R.layout.item_pengajuan_admin, container, false);
            
            TextView tvUsaha = itemView.findViewById(R.id.tvUmkmNameFull);
            TextView tvCategory = itemView.findViewById(R.id.tvUmkmCategoryFull);
            TextView tvStatus = itemView.findViewById(R.id.tvStatusBadgeFull);
            TextView tvTime = itemView.findViewById(R.id.tvTimeInfo);
            TextView tvUser = itemView.findViewById(R.id.tvUserInfo);
            
            TextView tvLokasi = itemView.findViewById(R.id.tvAdminReviewLokasi);
            TextView tvDeskripsi = itemView.findViewById(R.id.tvAdminReviewDeskripsi);
            TextView tvHarga = itemView.findViewById(R.id.tvAdminReviewHarga);
            
            MaterialButton btnReject = itemView.findViewById(R.id.btnRejectFull);
            MaterialButton btnProcess = itemView.findViewById(R.id.btnProcessFull);

            tvUsaha.setText(p.getNamaUmkm());
            tvCategory.setText(p.getNamaEvent());
            tvStatus.setText(p.getStatus());
            tvTime.setText("Diajukan: " + p.getTanggal());
            tvUser.setText("Oleh: " + p.getUserName());
            
            tvLokasi.setText("Lokasi: " + p.getLokasi());
            tvDeskripsi.setText("Deskripsi: " + p.getDeskripsi());
            tvHarga.setText("Biaya Sewa: " + p.getHarga());

            // Set status appearance
            if (p.getStatus().equals("Menunggu")) {
                tvStatus.setText("Baru");
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.admin_primary));
                tvStatus.setBackgroundResource(R.drawable.bg_tag_new);
                btnReject.setVisibility(View.VISIBLE);
                btnProcess.setText("Setujui");
            } else if (p.getStatus().equals("Disetujui")) {
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_approved));
                tvStatus.setBackgroundResource(R.drawable.bg_tag_approved);
                btnReject.setVisibility(View.GONE);
                btnProcess.setText("Lihat");
            } else if (p.getStatus().equals("Ditolak")) {
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_rejected));
                tvStatus.setBackgroundResource(R.drawable.bg_tag_rejected);
                btnReject.setVisibility(View.GONE);
                btnProcess.setText("Lihat");
            } else if (p.getStatus().equals("Diproses")) {
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_processing));
                tvStatus.setBackgroundResource(R.drawable.bg_tag_processing);
                btnReject.setVisibility(View.VISIBLE);
                btnProcess.setText("Proses");
            } else {
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_processing));
                tvStatus.setBackgroundResource(R.drawable.bg_tag_processing);
                btnReject.setVisibility(View.VISIBLE);
                btnProcess.setText("Lihat");
            }

            btnProcess.setOnClickListener(v -> {
                Intent intent = new Intent(this, AdminPengajuanDetailActivity.class);
                intent.putExtra("pengajuan", p);
                startActivity(intent);
            });

            btnReject.setOnClickListener(v -> {
                PengajuanManager.getInstance().updateStatus(p.getId(), "Ditolak");
                Toast.makeText(this, "Ditolak", Toast.LENGTH_SHORT).show();
                loadApplications();
            });

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(this, AdminPengajuanDetailActivity.class);
                intent.putExtra("pengajuan", p);
                startActivity(intent);
            });

            container.addView(itemView);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadApplications();
    }
}
