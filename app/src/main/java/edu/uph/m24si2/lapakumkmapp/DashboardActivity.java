package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private MaterialCardView cardEvent1, cardEvent2, cardPendingPayment;
    private Button btnExploreNow, btnBayarSekarang;
    private TextView tvLihatSemua, tvRekomendasiHeader, tvNoResults;
    private NestedScrollView nestedScrollView;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        cardEvent1 = findViewById(R.id.cardEvent1);
        cardEvent2 = findViewById(R.id.cardEvent2);
        cardPendingPayment = findViewById(R.id.cardPendingPayment);
        btnExploreNow = findViewById(R.id.btnExploreNow);
        btnBayarSekarang = findViewById(R.id.btnBayarSekarang);
        tvLihatSemua = findViewById(R.id.tvLihatSemua);
        tvRekomendasiHeader = findViewById(R.id.tvRekomendasiHeader);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        etSearch = findViewById(R.id.etSearch);
        tvNoResults = findViewById(R.id.tvNoResults);

        // Logic tampilkan card "Perlu Dibayar" jika ada expiry_time di prefs
        long expiryTime = getSharedPreferences("LapakUMKMPrefs", MODE_PRIVATE).getLong("expiry_time", 0);
        if (expiryTime > System.currentTimeMillis()) {
            cardPendingPayment.setVisibility(View.VISIBLE);
        } else {
            cardPendingPayment.setVisibility(View.GONE);
        }

        btnBayarSekarang.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, PaymentDetailActivity.class);
            // Default ke BCA Transfer untuk mempermudah alur sesuai permintaan
            intent.putExtra("PAYMENT_METHOD", "TRANSFER");
            intent.putExtra("BANK_NAME", "BCA");
            startActivity(intent);
        });
        findViewById(R.id.menuMaps).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, ExplorationMapActivity.class));
        });

        findViewById(R.id.menuFilter).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, FilterActivity.class));
        });

        // Bottom Nav Listeners
        findViewById(R.id.navEksplorasi).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, ExplorationMapActivity.class));
        });

        findViewById(R.id.navLapak).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, MyStallsActivity.class));
        });

        findViewById(R.id.navNotif).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, NotificationsActivity.class));
        });
        
        // 1. Fitur Search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().toLowerCase();
                boolean anyFound = false;

                if (query.isEmpty()) {
                    cardEvent1.setVisibility(View.VISIBLE);
                    cardEvent2.setVisibility(View.VISIBLE);
                    anyFound = true;
                } else {
                    if ("Festival Kuliner Nusantara".toLowerCase().contains(query)) {
                        cardEvent1.setVisibility(View.VISIBLE);
                        anyFound = true;
                    } else {
                        cardEvent1.setVisibility(View.GONE);
                    }

                    if ("Pasar Malam Tahun Baru".toLowerCase().contains(query)) {
                        cardEvent2.setVisibility(View.VISIBLE);
                        anyFound = true;
                    } else {
                        cardEvent2.setVisibility(View.GONE);
                    }
                }

                tvNoResults.setVisibility(anyFound ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 2. Klik "Jelajahi Sekarang" scroll ke Rekomendasi
        btnExploreNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Scroll ke posisi header
                int y = ((View) tvRekomendasiHeader.getParent()).getTop();
                nestedScrollView.smoothScrollTo(0, y);
            }
        });

        // 3. Klik "Lihat Semua"
        tvLihatSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, RecommendationListActivity.class);
                startActivity(intent);
            }
        });

        cardEvent1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bukaDetail("Festival Kuliner Nusantara", "Event Kuliner", 
                    "Nikmati berbagai hidangan khas dari seluruh nusantara.", 
                    "Alun-Alun Kota Bandung", R.drawable.festival_kuliner);
            }
        });

        cardEvent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bukaDetail("Pasar Malam Tahun Baru", "Event Tahunan", 
                    "Kemeriahan pasar malam menyambut tahun baru.", 
                    "Lapangan Gasibu Bandung", R.drawable.pasar_malam);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh status pembayaran saat kembali ke dashboard
        long expiryTime = getSharedPreferences("LapakUMKMPrefs", MODE_PRIVATE).getLong("expiry_time", 0);
        if (expiryTime > System.currentTimeMillis()) {
            cardPendingPayment.setVisibility(View.VISIBLE);
        } else {
            cardPendingPayment.setVisibility(View.GONE);
        }
    }

    private void bukaDetail(String nama, String kategori, String deskripsi, String lokasi, int imageResId) {
        Intent intent = new Intent(DashboardActivity.this, LapakDetailActivity.class);
        intent.putExtra("nama_lapak", nama);
        intent.putExtra("kategori_lapak", kategori);
        intent.putExtra("deskripsi_lapak", deskripsi);
        intent.putExtra("lokasi_lapak", lokasi);
        intent.putExtra("gambar_lapak", imageResId);
        startActivity(intent);
    }
}
