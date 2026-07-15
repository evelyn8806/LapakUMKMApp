package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class MyStallsActivity extends AppCompatActivity {

    private LinearLayout containerStalls;
    private MaterialButton btnSemua, btnProses, btnAktif, btnDibatalkan;
    private List<MaterialButton> filterButtons = new ArrayList<>();
    private String currentFilter = "Semua";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stalls);

        containerStalls = findViewById(R.id.containerStalls);
        
        btnSemua = findViewById(R.id.btnFilterSemua);
        btnProses = findViewById(R.id.btnFilterProses);
        btnAktif = findViewById(R.id.btnFilterAktif);
        btnDibatalkan = findViewById(R.id.btnFilterDibatalkan);

        filterButtons.add(btnSemua);
        filterButtons.add(btnProses);
        filterButtons.add(btnAktif);
        filterButtons.add(btnDibatalkan);

        setupFilters();
        setupBottomNav();
        
        findViewById(R.id.btnBuatPengajuanBaru).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });
        
        // Load initial data
        loadStalls("Semua");
    }

    private void setupFilters() {
        for (MaterialButton btn : filterButtons) {
            btn.setOnClickListener(v -> {
                currentFilter = btn.getText().toString();
                loadStalls(currentFilter);
            });
        }
    }

    private void loadStalls(String filter) {
        updateFilterUI(filter);
        containerStalls.removeAllViews();
        View layoutEmptyState = findViewById(R.id.layoutEmptyState);
        View layoutFooter = findViewById(R.id.layoutFooterList);
        
        android.content.SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPref.getString("email", "");
        List<PengajuanModel> allList = PengajuanManager.getInstance().getListPengajuanByUser(email);
        List<PengajuanModel> filteredList = new ArrayList<>();

        for (PengajuanModel p : allList) {
            String status = p.getStatus();
            if (filter.equals("Semua")) {
                filteredList.add(p);
            } else if (filter.equals("Diproses")) {
                if (status.equals("Menunggu") || status.equals("Diproses")) filteredList.add(p);
            } else if (filter.equals("Disetujui")) {
                if (status.equals("Disetujui") || status.equals("Aktif")) filteredList.add(p);
            } else if (filter.equals("Ditolak")) {
                if (status.equals("Ditolak") || status.equals("Dibatalkan")) filteredList.add(p);
            }
        }

        if (filteredList.isEmpty()) {
            if (layoutEmptyState != null) layoutEmptyState.setVisibility(View.VISIBLE);
            if (layoutFooter != null) layoutFooter.setVisibility(View.GONE);
            return;
        } else {
            if (layoutEmptyState != null) layoutEmptyState.setVisibility(View.GONE);
            if (layoutFooter != null) layoutFooter.setVisibility(View.VISIBLE);
        }

        for (PengajuanModel p : filteredList) {
            View itemView = getLayoutInflater().inflate(R.layout.item_my_stall, containerStalls, false);
            
            TextView tvName = itemView.findViewById(R.id.tvStallName);
            TextView tvLocation = itemView.findViewById(R.id.tvStallLocation);
            TextView tvStatus = itemView.findViewById(R.id.tvStallStatus);
            TextView tvDate = itemView.findViewById(R.id.tvStallDate);
            ImageView ivImage = itemView.findViewById(R.id.ivStallImage);

            tvName.setText(p.getNamaEvent());
            tvLocation.setText("Lokasi Event"); 
            tvStatus.setText(p.getStatus());
            tvDate.setText("Diajukan: " + p.getTanggal());

            // Status Styling
            if (p.getStatus().equals("Menunggu") || p.getStatus().equals("Diproses")) {
                tvStatus.setText("Diproses");
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_processing));
                tvStatus.setBackgroundResource(R.drawable.bg_tag_processing);
            } else if (p.getStatus().equals("Disetujui") || p.getStatus().equals("Aktif")) {
                tvStatus.setText("Disetujui");
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_approved));
                tvStatus.setBackgroundResource(R.drawable.bg_tag_approved);
            } else if (p.getStatus().equals("Ditolak") || p.getStatus().equals("Dibatalkan")) {
                tvStatus.setText("Ditolak");
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_rejected));
                tvStatus.setBackgroundResource(R.drawable.bg_tag_rejected);
            }

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(this, LapakDetailActivity.class);
                intent.putExtra("nama_lapak", p.getNamaEvent());
                startActivity(intent);
            });

            containerStalls.addView(itemView);
        }
    }

    private void updateFilterUI(String selectedFilter) {
        for (MaterialButton btn : filterButtons) {
            if (btn.getText().toString().equals(selectedFilter)) {
                btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(ContextCompat.getColor(this, R.color.admin_primary)));
                btn.setTextColor(Color.WHITE);
            } else {
                btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#F5F5F5")));
                btn.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
            }
        }
    }

    private void setupBottomNav() {
        findViewById(R.id.navBeranda).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        findViewById(R.id.navEksplorasi).setOnClickListener(v -> {
            startActivity(new Intent(this, ExplorationMapActivity.class));
        });

        findViewById(R.id.navNotif).setOnClickListener(v -> {
            startActivity(new Intent(this, NotificationsActivity.class));
            finish();
        });

        findViewById(R.id.navAkun).setOnClickListener(v -> {
            startActivity(new Intent(this, AccountActivity.class));
            finish();
        });
    }
}
