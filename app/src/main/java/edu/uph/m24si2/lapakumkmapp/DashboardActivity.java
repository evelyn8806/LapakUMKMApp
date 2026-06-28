package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView rvEvents;
    private EventAdapter adapter;
    private List<EventModel> allEvents;
    
    private Button btnExploreNow;
    private TextView tvRekomendasiHeader, tvNoResults;
    private NestedScrollView nestedScrollView;
    private EditText etSearch;
    private LinearLayout menuPeta, menuFilter, menuPengajuan, menuHistory;
    private LinearLayout navBeranda, navEksplorasi, navLapak, navNotifikasi, navAkun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        rvEvents = findViewById(R.id.rvDashboardEvents);
        btnExploreNow = findViewById(R.id.btnExploreNow);
        tvRekomendasiHeader = findViewById(R.id.tvRekomendasiHeader);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        etSearch = findViewById(R.id.etSearch);
        tvNoResults = findViewById(R.id.tvNoResults);

        // Menu Grid
        menuPeta = findViewById(R.id.menuPeta);
        menuFilter = findViewById(R.id.menuFilter);
        menuPengajuan = findViewById(R.id.menuPengajuan);
        menuHistory = findViewById(R.id.menuHistory);

        // Bottom Nav
        navBeranda = findViewById(R.id.navBeranda);
        navEksplorasi = findViewById(R.id.navEksplorasi);
        navLapak = findViewById(R.id.navLapak);
        navNotifikasi = findViewById(R.id.navNotifikasi);
        navAkun = findViewById(R.id.navAkun);

        // centralize data source
        allEvents = EventManager.getAllEvents();

        // Use a copy for the adapter so we can filter
        adapter = new EventAdapter(new ArrayList<>(allEvents), this);
        rvEvents.setAdapter(adapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));

        // Search Logic (Detects Name & Location)
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnExploreNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int y = ((View) tvRekomendasiHeader.getParent()).getTop();
                nestedScrollView.smoothScrollTo(0, y);
            }
        });

        // Set Click Listeners for Menu Grid
        menuPeta.setOnClickListener(v -> Toast.makeText(this, "Fitur Peta segera hadir!", Toast.LENGTH_SHORT).show());
        menuFilter.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, FilterActivity.class);
            startActivity(intent);
        });
        menuPengajuan.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, PengajuanSewaActivity.class);
            startActivity(intent);
        });
        menuHistory.setOnClickListener(v -> Toast.makeText(this, "Fitur History segera hadir!", Toast.LENGTH_SHORT).show());

        // Set Click Listeners for Bottom Nav
        navBeranda.setOnClickListener(v -> nestedScrollView.smoothScrollTo(0, 0));
        navEksplorasi.setOnClickListener(v -> {
            int y = ((View) tvRekomendasiHeader.getParent()).getTop();
            nestedScrollView.smoothScrollTo(0, y);
        });
        navLapak.setOnClickListener(v -> Toast.makeText(this, "Fitur Lapak Saya segera hadir!", Toast.LENGTH_SHORT).show());
        navNotifikasi.setOnClickListener(v -> Toast.makeText(this, "Fitur Notifikasi segera hadir!", Toast.LENGTH_SHORT).show());
        navAkun.setOnClickListener(v -> {
            SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
            
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void filter(String text) {
        List<EventModel> filteredList = new ArrayList<>();
        String query = text.toLowerCase().trim();
        
        if (query.isEmpty()) {
            filteredList.addAll(allEvents);
        } else {
            for (EventModel item : allEvents) {
                if (item.getNama().toLowerCase().contains(query) || 
                    item.getLokasi().toLowerCase().contains(query) ||
                    item.getKategori().toLowerCase().contains(query)) {
                    filteredList.add(item);
                }
            }
        }
        
        if (filteredList.isEmpty()) {
            tvNoResults.setVisibility(View.VISIBLE);
        } else {
            tvNoResults.setVisibility(View.GONE);
        }
        
        adapter.updateList(filteredList);
    }
}
