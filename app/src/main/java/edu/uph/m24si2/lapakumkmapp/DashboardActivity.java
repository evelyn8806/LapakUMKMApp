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

    private MaterialCardView cardEvent1, cardEvent2, cardEvent3;
    private TextView tvTitle1, tvTitle2, tvTitle3;
    private ImageView ivImage1, ivImage2, ivImage3;
    private Button btnExploreNow;
    private TextView tvLihatSemua, tvRekomendasiHeader, tvNoResults;
    private NestedScrollView nestedScrollView;
    private EditText etSearch;
    private View menuFilter, menuMaps;

    private List<EventModel> allEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        allEvents = EventManager.getAllEvents();

        cardEvent1 = findViewById(R.id.cardEvent1);
        cardEvent2 = findViewById(R.id.cardEvent2);
        cardEvent3 = findViewById(R.id.cardEvent3);
        
        tvTitle1 = findViewById(R.id.tvTitleEvent1);
        tvTitle2 = findViewById(R.id.tvTitleEvent2);
        tvTitle3 = findViewById(R.id.tvTitleEvent3);
        
        // Asumsi kita punya ID ivEvent1, dll di XML. Jika tidak, kita gunakan yang ada.
        // Di XML sebelumnya tidak ada ID eksplisit untuk ImageView di dalam card, 
        // tapi kita bisa menemukannya via parent atau menambahkannya.
        // Untuk demo, kita fokus ke fungsionalitas.

        btnExploreNow = findViewById(R.id.btnExploreNow);
        tvLihatSemua = findViewById(R.id.tvLihatSemua);
        tvRekomendasiHeader = findViewById(R.id.tvRekomendasiHeader);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        etSearch = findViewById(R.id.etSearch);
        tvNoResults = findViewById(R.id.tvNoResults);
        menuFilter = findViewById(R.id.menuFilter);
        menuMaps = findViewById(R.id.menuMaps);

        displayEvents();

        // 0. Fitur Filter Menu
        menuFilter.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, FilterActivity.class);
            startActivity(intent);
        });

        // 0.1 Fitur Peta Eksplorasi
        menuMaps.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MapsActivity.class);
            startActivity(intent);
        });
        
        // 1. Fitur Search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().toLowerCase();
                filterEvents(query);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || 
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = etSearch.getText().toString();
                Intent intent = new Intent(DashboardActivity.this, RecommendationListActivity.class);
                intent.putExtra("search_query", query);
                startActivity(intent);
                return true;
            }
            return false;
        });

        btnExploreNow.setOnClickListener(v -> {
            int y = ((View) tvRekomendasiHeader.getParent()).getTop();
            nestedScrollView.smoothScrollTo(0, y);
        });

        tvLihatSemua.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, RecommendationListActivity.class);
            startActivity(intent);
        });

        setupClickListeners();
    }

    private void displayEvents() {
        if (allEvents.size() >= 1) tvTitle1.setText(allEvents.get(0).getNama());
        if (allEvents.size() >= 2) tvTitle2.setText(allEvents.get(1).getNama());
        if (allEvents.size() >= 3) tvTitle3.setText(allEvents.get(2).getNama());
    }

    private void filterEvents(String query) {
        boolean anyFound = false;
        if (query.isEmpty()) {
            cardEvent1.setVisibility(View.VISIBLE);
            cardEvent2.setVisibility(View.VISIBLE);
            cardEvent3.setVisibility(View.VISIBLE);
            anyFound = true;
        } else {
            cardEvent1.setVisibility(isMatch(allEvents.get(0), query) ? View.VISIBLE : View.GONE);
            cardEvent2.setVisibility(isMatch(allEvents.get(1), query) ? View.VISIBLE : View.GONE);
            cardEvent3.setVisibility(isMatch(allEvents.get(2), query) ? View.VISIBLE : View.GONE);
            anyFound = cardEvent1.getVisibility() == View.VISIBLE || 
                       cardEvent2.getVisibility() == View.VISIBLE || 
                       cardEvent3.getVisibility() == View.VISIBLE;
        }
        tvNoResults.setVisibility(anyFound ? View.GONE : View.VISIBLE);
    }

    private boolean isMatch(EventModel event, String query) {
        return event.getNama().toLowerCase().contains(query) || 
               event.getDeskripsi().toLowerCase().contains(query);
    }

    private void setupClickListeners() {
        cardEvent1.setOnClickListener(v -> bukaDetail(allEvents.get(0)));
        cardEvent2.setOnClickListener(v -> bukaDetail(allEvents.get(1)));
        cardEvent3.setOnClickListener(v -> bukaDetail(allEvents.get(2)));
    }

    private void bukaDetail(EventModel event) {
        Intent intent = new Intent(DashboardActivity.this, LapakDetailActivity.class);
        intent.putExtra("nama_lapak", event.getNama());
        intent.putExtra("kategori_lapak", event.getKategori());
        intent.putExtra("deskripsi_lapak", event.getDeskripsi());
        intent.putExtra("lokasi_lapak", event.getLokasi());
        intent.putExtra("gambar_lapak", event.getGambar());
        startActivity(intent);
    }
}
