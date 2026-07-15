package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class ExplorationMapActivity extends AppCompatActivity {

    private RecyclerView rvExploration;
    private EventAdapter adapter;
    private List<EventModel> allEvents;
    private EditText etSearch;
    private TextView tvNoResults;
    private String currentCategory = "SEMUA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exploration);

        UserManager.getInstance().loadUsers(this);

        allEvents = EventManager.getAllEvents();
        
        findViewById(R.id.llSearch).setVisibility(View.VISIBLE);
        rvExploration = findViewById(R.id.rvExploration);
        rvExploration.setVisibility(View.VISIBLE);
        etSearch = findViewById(R.id.etSearchExploration);
        tvNoResults = findViewById(R.id.tvNoExplorationResults);
        
        // Hide map elements if they exist
        View mapFragment = findViewById(R.id.map);
        if (mapFragment != null) mapFragment.setVisibility(View.GONE);
        View fab = findViewById(R.id.fabMyLocation);
        if (fab != null) fab.setVisibility(View.GONE);
        
        setupRecyclerView();
        setupSearch();
        setupFilters();
        setupBottomNav();
    }

    private void setupRecyclerView() {
        rvExploration.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventAdapter(allEvents, this);
        rvExploration.setAdapter(adapter);
    }

    private void setupFilters() {
        LinearLayout layoutFilters = findViewById(R.id.layoutFilters);
        for (int i = 0; i < layoutFilters.getChildCount(); i++) {
            View v = layoutFilters.getChildAt(i);
            if (v instanceof MaterialButton) {
                MaterialButton btn = (MaterialButton) v;
                btn.setOnClickListener(view -> {
                    currentCategory = btn.getText().toString();
                    updateFilterStyles(layoutFilters);
                    applyFilters();
                });
            }
        }
    }

    private void updateFilterStyles(LinearLayout layoutFilters) {
        for (int i = 0; i < layoutFilters.getChildCount(); i++) {
            View v = layoutFilters.getChildAt(i);
            if (v instanceof MaterialButton) {
                MaterialButton btn = (MaterialButton) v;
                if (btn.getText().toString().equals(currentCategory)) {
                    btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(ContextCompat.getColor(this, R.color.admin_primary)));
                    btn.setTextColor(Color.WHITE);
                } else {
                    btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#F5F5F5")));
                    btn.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
                }
            }
        }
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void applyFilters() {
        String searchText = etSearch.getText().toString().toLowerCase();
        List<EventModel> filteredList = new ArrayList<>();
        
        for (EventModel item : allEvents) {
            boolean matchesSearch = item.getNama().toLowerCase().contains(searchText) || 
                                   item.getLokasi().toLowerCase().contains(searchText);
            
            boolean matchesCategory = currentCategory.equals("SEMUA") || 
                                     item.getKategori().equalsIgnoreCase(currentCategory);
            
            if (matchesSearch && matchesCategory) {
                filteredList.add(item);
            }
        }

        adapter.updateList(filteredList);
        tvNoResults.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void setupBottomNav() {
        String role = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("role", "UMKM");

        if ("ADMIN".equals(role)) {
            findViewById(R.id.umkmBottomNav).setVisibility(View.GONE);
            findViewById(R.id.adminBottomNav).setVisibility(View.VISIBLE);
            AdminNavigationHelper.setupNavigation(this, -1);
        } else {
            findViewById(R.id.umkmBottomNav).setVisibility(View.VISIBLE);
            findViewById(R.id.adminBottomNav).setVisibility(View.GONE);
            UmkmNavigationHelper.setupNavigation(this, R.id.navEksplorasi);
        }
    }
}
