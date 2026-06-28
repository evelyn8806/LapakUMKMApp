package edu.uph.m24si2.lapakumkmapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RecommendationListActivity extends AppCompatActivity {

    private RecyclerView rvEvents;
    private TextView tvEmptyResults;
    private EventAdapter adapter;
    private List<EventModel> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_list);

        // 1. Setup Navigation
        ImageView btnBack = findViewById(R.id.btnBack);
        TextView tvTitle = findViewById(R.id.tvTitle);
        
        if (getIntent().hasExtra("jenis")) {
            tvTitle.setText("Hasil Pencarian");
        }

        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        // 2. Setup RecyclerView
        rvEvents = findViewById(R.id.rvEvents);
        tvEmptyResults = findViewById(R.id.tvEmptyResults);
        if (rvEvents != null) {
            rvEvents.setLayoutManager(new LinearLayoutManager(this));
            
            eventList = new ArrayList<>();
            loadDummyData();

            adapter = new EventAdapter(eventList, this);
            rvEvents.setAdapter(adapter);

            if (eventList.isEmpty()) {
                tvEmptyResults.setVisibility(View.VISIBLE);
                rvEvents.setVisibility(View.GONE);
            } else {
                tvEmptyResults.setVisibility(View.GONE);
                rvEvents.setVisibility(View.VISIBLE);
            }
        }
    }

    private void loadDummyData() {
        // Ambil filter dari Intent
        String filterJenis = getIntent().getStringExtra("jenis");
        String filterLokasi = getIntent().getStringExtra("lokasi");
        String searchQuery = getIntent().getStringExtra("search_query");
        float minPrice = getIntent().getFloatExtra("minPrice", 0);
        float maxPrice = getIntent().getFloatExtra("maxPrice", 2000000);

        List<EventModel> allEvents = new ArrayList<>();
        // ... (data initialization) ...
        allEvents.add(new EventModel(
            "Festival Jajanan Pasar", 
            "Bazar", 
            "Alun-Alun Utara Yogyakarta", 
            "Rp. 150,000 / Hari", 
            150000,
            R.drawable.festival_kuliner,
            "Nikmati berbagai jajanan pasar tradisional dari seluruh penjuru Jogja."
        ));

        allEvents.add(new EventModel(
            "Pasar Malam Rakyat", 
            "Pasar Malam", 
            "Lapangan Puputan Renon, Denpasar", 
            "Rp. 100,000 / Hari", 
            100000,
            R.drawable.pasar_malam,
            "Hiburan keluarga dengan berbagai wahana permainan dan stand UMKM lokal."
        ));

        allEvents.add(new EventModel(
            "Festival Kuliner Nusantara",
            "Event",
            "Alun-Alun Kota Bandung",
            "Rp. 250.000 / 3 hari",
            250000,
            R.drawable.festival_kuliner,
            "Nikmati hidangan lezat dari berbagai daerah di Indonesia."
        ));

        // Terapkan Filter (Sensitive: At least one match)
        for (EventModel event : allEvents) {
            boolean hasFilter = false;
            boolean matchAny = false;

            // Search Query Filter
            if (searchQuery != null && !searchQuery.isEmpty()) {
                hasFilter = true;
                String q = searchQuery.toLowerCase();
                if (event.getNama().toLowerCase().contains(q) || 
                    event.getDeskripsi().toLowerCase().contains(q) ||
                    event.getKategori().toLowerCase().contains(q) ||
                    event.getLokasi().toLowerCase().contains(q)) {
                    matchAny = true;
                }
            }

            if (filterJenis != null && !filterJenis.equals("Semua")) {
                hasFilter = true;
                if (event.getKategori().equalsIgnoreCase(filterJenis)) matchAny = true;
            }

            if (filterLokasi != null && !filterLokasi.equals("Pilih kota")) {
                hasFilter = true;
                if (event.getLokasi().toLowerCase().contains(filterLokasi.toLowerCase())) matchAny = true;
            }

            // Price range check (sensitive: if it fits the range)
            if (minPrice > 0 || maxPrice < 2000000) {
                hasFilter = true;
                if (event.getNumericHarga() >= minPrice && event.getNumericHarga() <= maxPrice) matchAny = true;
            }

            // If no filters applied, show all. If filters applied, show if any match.
            if (!hasFilter || matchAny) {
                eventList.add(event);
            }
        }
    }
}
