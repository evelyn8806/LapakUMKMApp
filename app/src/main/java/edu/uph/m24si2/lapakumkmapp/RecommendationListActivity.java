package edu.uph.m24si2.lapakumkmapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RecommendationListActivity extends AppCompatActivity {

    private RecyclerView rvEvents;
    private EventAdapter adapter;
    private List<EventModel> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_list);

        // 1. Setup Navigation
        ImageView btnBack = findViewById(R.id.btnBack);
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
        if (rvEvents != null) {
            rvEvents.setLayoutManager(new LinearLayoutManager(this));
            
            eventList = new ArrayList<>();
            loadDummyData();
            
            // Check for search query from Intent
            String searchQuery = getIntent().getStringExtra("search_query");
            if (searchQuery != null && !searchQuery.isEmpty()) {
                List<EventModel> filteredList = new ArrayList<>();
                for (EventModel event : eventList) {
                    if (event.getNama().toLowerCase().contains(searchQuery.toLowerCase()) || 
                        event.getDeskripsi().toLowerCase().contains(searchQuery.toLowerCase())) {
                        filteredList.add(event);
                    }
                }
                eventList = filteredList;
            }

            adapter = new EventAdapter(eventList, this);
            rvEvents.setAdapter(adapter);
        }
    }

    private void loadDummyData() {
        eventList.clear();
        eventList.addAll(EventManager.getAllEvents());
    }
}
