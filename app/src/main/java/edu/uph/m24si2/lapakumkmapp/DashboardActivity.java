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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private MaterialCardView cardPendingPayment;
    private Button btnExploreNow, btnBayarSekarang;
    private TextView tvRekomendasiHeader, tvNoResults;
    private NestedScrollView nestedScrollView;
    private RecyclerView rvEvents;
    private EventAdapter adapter;
    private List<EventModel> allEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        cardPendingPayment = findViewById(R.id.cardPendingPayment);
        btnExploreNow = findViewById(R.id.btnExploreNow);
        btnBayarSekarang = findViewById(R.id.btnBayarSekarang);
        tvRekomendasiHeader = findViewById(R.id.tvRekomendasiHeader);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        rvEvents = findViewById(R.id.rvDashboardEvents);
        tvNoResults = findViewById(R.id.tvNoDashboardResults);

        UmkmNavigationHelper.setupNavigation(this, R.id.navBeranda);

        allEvents = EventManager.getAllEvents();
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventAdapter(allEvents, this);
        rvEvents.setAdapter(adapter);

        // Logic tampilkan card "Perlu Dibayar" jika ada expiry_time di prefs
        long expiryTime = getSharedPreferences("LapakUMKMPrefs", MODE_PRIVATE).getLong("expiry_time", 0);
        if (expiryTime > System.currentTimeMillis()) {
            cardPendingPayment.setVisibility(View.VISIBLE);
        } else {
            cardPendingPayment.setVisibility(View.GONE);
        }

        btnBayarSekarang.setOnClickListener(v -> {
            RentalRequest pending = null;
            for (RentalRequest r : RentalManager.getInstance().getRequests()) {
                if (r.getStatus() == RentalRequest.Status.MENUNGGU_PEMBAYARAN) {
                    pending = r;
                    break;
                }
            }

            Intent intent = new Intent(DashboardActivity.this, PaymentDetailActivity.class);
            // Default ke BCA Transfer untuk mempermudah alur sesuai permintaan
            intent.putExtra("PAYMENT_METHOD", "TRANSFER");
            intent.putExtra("BANK_NAME", "BCA");
            if (pending != null) {
                intent.putExtra("rental_request", pending);
            }
            startActivity(intent);
        });

        findViewById(R.id.menuFilter).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, FilterActivity.class));
        });

        findViewById(R.id.menuPengajuan).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, MyStallsActivity.class));
        });

        findViewById(R.id.menuHistory).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, HistoryActivity.class));
        });

        // Search Bar Listener (Real-time & Sensitive)
        EditText etSearchDashboard = findViewById(R.id.etSearchDashboard);
        if (etSearchDashboard != null) {
            etSearchDashboard.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterEvents(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            etSearchDashboard.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || 
                    actionId == EditorInfo.IME_ACTION_DONE) {
                    return true;
                }
                return false;
            });
        }
        
        // 2. Klik "Jelajahi Sekarang" scroll ke Rekomendasi
        btnExploreNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Scroll ke posisi header
                int y = ((View) tvRekomendasiHeader.getParent()).getTop();
                nestedScrollView.smoothScrollTo(0, y);
            }
        });

        findViewById(R.id.ivNotification).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, NotificationsActivity.class));
        });
    }

    private void filterEvents(String query) {
        if (query.isEmpty()) {
            adapter.updateList(allEvents);
            tvNoResults.setVisibility(View.GONE);
            rvEvents.setVisibility(View.VISIBLE);
            return;
        }

        List<EventModel> filteredList = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        // "Sensitive" search logic: check if any part of the name, category, or location matches
        for (EventModel event : allEvents) {
            if (event.getNama().toLowerCase().contains(lowerQuery) || 
                event.getKategori().toLowerCase().contains(lowerQuery) || 
                event.getLokasi().toLowerCase().contains(lowerQuery)) {
                filteredList.add(event);
            }
        }

        if (filteredList.isEmpty()) {
            tvNoResults.setVisibility(View.VISIBLE);
            rvEvents.setVisibility(View.GONE);
        } else {
            tvNoResults.setVisibility(View.GONE);
            rvEvents.setVisibility(View.VISIBLE);
            adapter.updateList(filteredList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh status pembayaran saat kembali ke dashboard
        long expiryTime = getSharedPreferences("LapakUMKMPrefs", MODE_PRIVATE).getLong("expiry_time", 0);
        if (expiryTime > System.currentTimeMillis()) {
            cardPendingPayment.setVisibility(View.VISIBLE);
        } else {
            cardPendingPayment.setVisibility(View.GONE);
        }
    }
}
