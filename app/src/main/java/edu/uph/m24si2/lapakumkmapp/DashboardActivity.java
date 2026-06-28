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
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;

public class DashboardActivity extends AppCompatActivity {

    private MaterialCardView cardEvent1, cardEvent2, cardEvent3;
    private Button btnExploreNow;
    private TextView tvLihatSemua, tvRekomendasiHeader, tvNoResults;
    private NestedScrollView nestedScrollView;
    private EditText etSearch;
    private View menuFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        cardEvent1 = findViewById(R.id.cardEvent1);
        cardEvent2 = findViewById(R.id.cardEvent2);
        cardEvent3 = findViewById(R.id.cardEvent3);
        btnExploreNow = findViewById(R.id.btnExploreNow);
        tvLihatSemua = findViewById(R.id.tvLihatSemua);
        tvRekomendasiHeader = findViewById(R.id.tvRekomendasiHeader);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        etSearch = findViewById(R.id.etSearch);
        tvNoResults = findViewById(R.id.tvNoResults);
        menuFilter = findViewById(R.id.menuFilter);

        // 0. Fitur Filter Menu
        menuFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, FilterActivity.class);
                startActivity(intent);
            }
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
                    cardEvent3.setVisibility(View.VISIBLE);
                    anyFound = true;
                } else {
                    // Cek nama atau deskripsi
                    String title1 = "Festival Kuliner Nusantara".toLowerCase();
                    String desc1 = "Nikmati berbagai hidangan khas dari seluruh nusantara.".toLowerCase();
                    if (title1.contains(query) || desc1.contains(query)) {
                        cardEvent1.setVisibility(View.VISIBLE);
                        anyFound = true;
                    } else {
                        cardEvent1.setVisibility(View.GONE);
                    }

                    String title2 = "Pasar Malam Tahun Baru".toLowerCase();
                    String desc2 = "Kemeriahan pasar malam menyambut tahun baru.".toLowerCase();
                    if (title2.contains(query) || desc2.contains(query)) {
                        cardEvent2.setVisibility(View.VISIBLE);
                        anyFound = true;
                    } else {
                        cardEvent2.setVisibility(View.GONE);
                    }

                    String title3 = "Festival Jajanan Pasar".toLowerCase();
                    String desc3 = "Nikmati berbagai jajanan pasar tradisional dari seluruh penjuru Jogja.".toLowerCase();
                    if (title3.contains(query) || desc3.contains(query)) {
                        cardEvent3.setVisibility(View.VISIBLE);
                        anyFound = true;
                    } else {
                        cardEvent3.setVisibility(View.GONE);
                    }
                }

                tvNoResults.setVisibility(anyFound ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Search action listener (Enter key)
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || 
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String query = etSearch.getText().toString();
                    Intent intent = new Intent(DashboardActivity.this, RecommendationListActivity.class);
                    intent.putExtra("search_query", query);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
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

        cardEvent3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bukaDetail("Festival Jajanan Pasar", "Bazar", 
                    "Nikmati berbagai jajanan pasar tradisional dari seluruh penjuru Jogja.", 
                    "Alun-Alun Utara Yogyakarta", R.drawable.festival_kuliner);
            }
        });
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
