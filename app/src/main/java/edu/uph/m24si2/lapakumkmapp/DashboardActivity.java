package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;

public class DashboardActivity extends AppCompatActivity {

    private MaterialCardView cardEvent1, cardEvent2;
    private Button btnExploreNow;
    private TextView tvLihatSemua, tvRekomendasiHeader, tvNoResults;
    private NestedScrollView nestedScrollView;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        cardEvent1 = findViewById(R.id.cardEvent1);
        cardEvent2 = findViewById(R.id.cardEvent2);
        btnExploreNow = findViewById(R.id.btnExploreNow);
        tvLihatSemua = findViewById(R.id.tvLihatSemua);
        tvRekomendasiHeader = findViewById(R.id.tvRekomendasiHeader);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        etSearch = findViewById(R.id.etSearch);
        tvNoResults = findViewById(R.id.tvNoResults);
        
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

        // 3. Klik "Lihat Semua" (Bisa ke Activity baru atau simulasi filter)
        tvLihatSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Untuk demo, kita buat toast saja atau arahkan ke list full
                Intent intent = new Intent(DashboardActivity.this, RecommendationListActivity.class);
                startActivity(intent);
            }
        });

        cardEvent1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bukaDetail("Festival Kuliner Nusantara", "Event Kuliner", 
                    "Nikmati berbagai hidangan khas dari seluruh nusantara.", "Alun-Alun Kota Bandung");
            }
        });

        cardEvent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bukaDetail("Pasar Malam Tahun Baru", "Event Tahunan", 
                    "Kemeriahan pasar malam menyambut tahun baru.", "Lapangan Gasibu Bandung");
            }
        });
    }

    private void bukaDetail(String nama, String kategori, String deskripsi, String lokasi) {
        Intent intent = new Intent(DashboardActivity.this, LapakDetailActivity.class);
        intent.putExtra("nama_lapak", nama);
        intent.putExtra("kategori_lapak", kategori);
        intent.putExtra("deskripsi_lapak", deskripsi);
        intent.putExtra("lokasi_lapak", lokasi);
        startActivity(intent);
    }
}
