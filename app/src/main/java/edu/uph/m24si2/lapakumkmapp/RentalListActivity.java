package edu.uph.m24si2.lapakumkmapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RentalListActivity extends AppCompatActivity {

    private RecyclerView rvRequests;
    private TextView tvTitle, tvEmpty;
    private boolean isHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_list);

        isHistory = getIntent().getBooleanExtra("IS_HISTORY", false);

        tvTitle = findViewById(R.id.tvListTitle);
        tvEmpty = findViewById(R.id.tvEmptyList);
        rvRequests = findViewById(R.id.rvRentalRequests);
        ImageView btnBack = findViewById(R.id.btnBackList);

        tvTitle.setText(isHistory ? "History" : "Pengajuan");
        btnBack.setOnClickListener(v -> finish());

        rvRequests.setLayoutManager(new LinearLayoutManager(this));
        
        // Simulation of Admin Approval
        simulateApproval();
        loadData();
    }

    private void simulateApproval() {
        for (RentalRequest r : RentalManager.getInstance().getRequests()) {
            if (r.getStatus() == RentalRequest.Status.MENUNGGU_PERSETUJUAN) {
                // Simulate approval after 2 seconds
                new Handler().postDelayed(() -> {
                    r.setStatus(RentalRequest.Status.AKTIF);
                    if (!isFinishing()) {
                        Toast.makeText(this, "Notifikasi: Pengajuan " + r.getEventName() + " telah disetujui!", Toast.LENGTH_LONG).show();
                        loadData();
                    }
                }, 4000);
            }
        }
    }

    private void loadData() {
        List<RentalRequest> all = RentalManager.getInstance().getRequests();
        List<RentalRequest> filtered = new ArrayList<>();

        for (RentalRequest r : all) {
            if (isHistory) {
                if (r.getStatus() == RentalRequest.Status.SELESAI) {
                    filtered.add(r);
                }
            } else {
                if (r.getStatus() != RentalRequest.Status.SELESAI) {
                    filtered.add(r);
                }
            }
        }

        if (filtered.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvRequests.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvRequests.setVisibility(View.VISIBLE);
            RentalRequestAdapter adapter = new RentalRequestAdapter(filtered, this);
            rvRequests.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}
