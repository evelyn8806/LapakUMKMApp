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
        setContentView(R.layout.activity_maps);

        allEvents = EventManager.getAllEvents();
        
        rvExploration = findViewById(R.id.rvExploration);
        etSearch = findViewById(R.id.etSearchExploration);
        tvNoResults = findViewById(R.id.tvNoExplorationResults);
        
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
        findViewById(R.id.navBeranda).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
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
