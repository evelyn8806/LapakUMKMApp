package edu.uph.m24si2.lapakumkmapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecommendationListActivity extends AppCompatActivity {

    private RecyclerView rvEvents;
    private EventAdapter adapter;
    private TextView tvNoResults, tvOtherRecommendationsHeader;
    private List<EventModel> allEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_list);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        tvNoResults = findViewById(R.id.tvNoResults);
        tvOtherRecommendationsHeader = findViewById(R.id.tvOtherRecommendationsHeader);
        rvEvents = findViewById(R.id.rvEvents);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));

        allEvents = EventManager.getAllEvents();

        String jenis = getIntent().getStringExtra("jenis");
        float minPrice = getIntent().getFloatExtra("minPrice", 0);
        float maxPrice = getIntent().getFloatExtra("maxPrice", Float.MAX_VALUE);
        String lokasi = getIntent().getStringExtra("lokasi");
        String sort = getIntent().getStringExtra("sort");

        applyFilter(jenis, minPrice, maxPrice, lokasi, sort);
    }

    private void applyFilter(String jenis, float minPrice, float maxPrice, String lokasi, String sort) {
        List<EventModel> filteredList = new ArrayList<>();

        for (EventModel event : allEvents) {
            boolean matches = true;

            // 1. Filter Harga
            if (event.getNumericHarga() < minPrice || event.getNumericHarga() > maxPrice) {
                matches = false;
            }

            // 2. Filter Lokasi
            if (matches && lokasi != null && !lokasi.equalsIgnoreCase("Pilih kota") && !event.getKota().equalsIgnoreCase(lokasi)) {
                matches = false;
            }

            // 3. Filter Jenis Lapak
            if (matches && jenis != null && !jenis.equalsIgnoreCase("Semua")) {
                if (!event.getKategori().toLowerCase().contains(jenis.toLowerCase())) {
                    matches = false;
                }
            }

            if (matches) {
                filteredList.add(event);
            }
        }

        // 4. Urutkan berdasarkan
        if (!filteredList.isEmpty() && sort != null) {
            if (sort.equalsIgnoreCase("Termurah")) {
                Collections.sort(filteredList, Comparator.comparingInt(EventModel::getNumericHarga));
            } else if (sort.equalsIgnoreCase("Termahal")) {
                Collections.sort(filteredList, (e1, e2) -> Integer.compare(e2.getNumericHarga(), e1.getNumericHarga()));
            }
        }

        if (filteredList.isEmpty()) {
            tvNoResults.setVisibility(View.VISIBLE);
            tvOtherRecommendationsHeader.setVisibility(View.VISIBLE);
            adapter = new EventAdapter(allEvents, this);
        } else {
            tvNoResults.setVisibility(View.GONE);
            tvOtherRecommendationsHeader.setVisibility(View.GONE);
            adapter = new EventAdapter(filteredList, this);
        }
        rvEvents.setAdapter(adapter);
    }
}
