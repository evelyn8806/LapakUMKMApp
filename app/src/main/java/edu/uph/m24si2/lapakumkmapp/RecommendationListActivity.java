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

            adapter = new EventAdapter(eventList, this);
            rvEvents.setAdapter(adapter);
        }
    }

    private void loadDummyData() {
        eventList.add(new EventModel(
            "Festival Jajanan Pasar", 
            "Kuliner", 
            "Alun-Alun Utara Yogyakarta", 
            "Rp. 150,000 / Hari", 
            R.drawable.festival_kuliner,
            "Nikmati berbagai jajanan pasar tradisional dari seluruh penjuru Jogja."
        ));

        eventList.add(new EventModel(
            "Pasar Malam Rakyat", 
            "Hiburan", 
            "Lapangan Puputan Renon, Denpasar", 
            "Rp. 100,000 / Hari", 
            R.drawable.pasar_malam,
            "Hiburan keluarga dengan berbagai wahana permainan dan stand UMKM lokal."
        ));

        eventList.add(new EventModel(
            "Festival Kuliner Nusantara",
            "Kuliner",
            "Alun-Alun Kota Bandung",
            "Rp. 250.000 / 3 hari",
            R.drawable.festival_kuliner,
            "Nikmati hidangan lezat dari berbagai daerah di Indonesia."
        ));
    }
}
