package edu.uph.m24si2.lapakumkmapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyStallsActivity extends AppCompatActivity {

    private RecyclerView rvMyStalls;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stalls);

        rvMyStalls = findViewById(R.id.rvMyStalls);
        tvEmpty = findViewById(R.id.tvEmptyStalls);

        rvMyStalls.setLayoutManager(new LinearLayoutManager(this));

        setupBottomNav();
        loadActiveStalls();
    }

    private void loadActiveStalls() {
        List<RentalRequest> activeRequests = RentalManager.getInstance().getRequestsByStatus(RentalRequest.Status.AKTIF);
        if (activeRequests.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvMyStalls.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvMyStalls.setVisibility(View.VISIBLE);
            RentalRequestAdapter adapter = new RentalRequestAdapter(activeRequests, this);
            rvMyStalls.setAdapter(adapter);
        }
    }

    private void setupBottomNav() {
        findViewById(R.id.navBeranda).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        findViewById(R.id.navEksplorasi).setOnClickListener(v -> {
            startActivity(new Intent(this, ExplorationMapActivity.class));
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

    @Override
    protected void onResume() {
        super.onResume();
        loadActiveStalls();
    }
}
