package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class MyStallsActivity extends AppCompatActivity {

    private LinearLayout containerStalls;
    private Button btnSemua, btnProses, btnAktif, btnSelesai, btnDibatalkan;
    private List<Button> filterButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stalls);

        containerStalls = findViewById(R.id.containerStalls);
        
        btnSemua = findViewById(R.id.btnFilterSemua);
        btnProses = findViewById(R.id.btnFilterProses);
        btnAktif = findViewById(R.id.btnFilterAktif);
        btnSelesai = findViewById(R.id.btnFilterSelesai);
        btnDibatalkan = findViewById(R.id.btnFilterDibatalkan);

        filterButtons.add(btnSemua);
        filterButtons.add(btnProses);
        filterButtons.add(btnAktif);
        filterButtons.add(btnSelesai);
        filterButtons.add(btnDibatalkan);

        setupFilters();
        setupBottomNav();
        
        // Load initial data
        loadStalls("Semua");
    }

    private void setupFilters() {
        btnSemua.setOnClickListener(v -> loadStalls("Semua"));
        btnProses.setOnClickListener(v -> loadStalls("Proses"));
        btnAktif.setOnClickListener(v -> loadStalls("Aktif"));
        btnSelesai.setOnClickListener(v -> loadStalls("Selesai"));
        btnDibatalkan.setOnClickListener(v -> loadStalls("Dibatalkan"));
    }

    private void loadStalls(String filter) {
        updateFilterUI(filter);
        containerStalls.removeAllViews();
        
        List<PengajuanModel> allList = PengajuanManager.getInstance().getListPengajuan();
        List<PengajuanModel> filteredList = new ArrayList<>();

        for (PengajuanModel p : allList) {
            if (filter.equals("Semua")) {
                filteredList.add(p);
            } else if (filter.equals("Proses")) {
                if (p.getStatus().contains("Menunggu")) filteredList.add(p);
            } else if (filter.equals("Aktif")) {
                if (p.getStatus().equals("Aktif") || p.getStatus().equals("Disetujui")) filteredList.add(p);
            } else if (filter.equals("Selesai")) {
                if (p.getStatus().equals("Selesai")) filteredList.add(p);
            } else if (filter.equals("Dibatalkan")) {
                if (p.getStatus().equals("Dibatalkan") || p.getStatus().equals("Ditolak")) filteredList.add(p);
            }
        }

        if (filteredList.isEmpty()) {
            TextView emptyTv = new TextView(this);
            emptyTv.setText("Tidak ada data untuk kategori ini.");
            emptyTv.setGravity(android.view.Gravity.CENTER);
            emptyTv.setPadding(0, 100, 0, 0);
            containerStalls.addView(emptyTv);
            return;
        }

        for (PengajuanModel p : filteredList) {
            View itemView = getLayoutInflater().inflate(R.layout.item_my_stall, containerStalls, false);
            
            TextView tvName = itemView.findViewById(R.id.tvStallName);
            TextView tvLocation = itemView.findViewById(R.id.tvStallLocation);
            TextView tvStatus = itemView.findViewById(R.id.tvStallStatus);
            TextView tvDate = itemView.findViewById(R.id.tvStallDate);
            Button btnAction = itemView.findViewById(R.id.btnStallAction);
            ImageView ivImage = itemView.findViewById(R.id.ivStallImage);

            tvName.setText(p.getNamaEvent());
            tvLocation.setText("Lokasi Event"); // Dummy location if not in model
            tvStatus.setText(p.getStatus());
            tvDate.setText(p.getStatus().contains("Menunggu") ? "Diajukan: " + p.getTanggal() : p.getTanggal());

            // Set Status Style
            if (p.getStatus().equals("Menunggu")) {
                tvStatus.setBackgroundColor(Color.parseColor("#FFF8E1"));
                tvStatus.setTextColor(Color.parseColor("#FF8F00"));
                tvStatus.setText("Menunggu Verifikasi");
                btnAction.setText("Lihat Detail");
                btnAction.setOnClickListener(v -> onLihatDetailClick(v));
            } else if (p.getStatus().equals("Menunggu Pembayaran")) {
                tvStatus.setBackgroundColor(Color.parseColor("#FFF3E0"));
                tvStatus.setTextColor(Color.parseColor("#E65100"));
                btnAction.setText("Lanjutkan Pembayaran");
                btnAction.setOnClickListener(v -> onBayarClick(v));
                ivImage.setImageResource(R.drawable.pasar_malam);
            } else if (p.getStatus().equals("Aktif") || p.getStatus().equals("Disetujui")) {
                tvStatus.setBackgroundColor(Color.parseColor("#E8F5E9"));
                tvStatus.setTextColor(Color.parseColor("#2E7D32"));
                tvStatus.setText("Aktif");
                btnAction.setText("Lihat E-Tiket");
                btnAction.setOnClickListener(v -> onLihatETicketClick(v));
            } else if (p.getStatus().equals("Selesai")) {
                tvStatus.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvStatus.setTextColor(Color.parseColor("#757575"));
                btnAction.setVisibility(View.GONE);
            }

            containerStalls.addView(itemView);
        }
    }

    private void updateFilterUI(String selectedFilter) {
        for (Button btn : filterButtons) {
            if (btn.getText().toString().equals(selectedFilter)) {
                btn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.blue_primary));
                btn.setTextColor(Color.WHITE);
                // Switch to Unelevated style logic
            } else {
                btn.setBackgroundTintList(null);
                btn.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
                // Switch to Outlined style logic
            }
        }
    }

    public void onLihatDetailClick(View v) {
        Intent intent = new Intent(this, LapakDetailActivity.class);
        startActivity(intent);
    }

    public void onBayarClick(View v) {
        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }

    public void onLihatETicketClick(View v) {
        Intent intent = new Intent(this, ETicketActivity.class);
        startActivity(intent);
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
