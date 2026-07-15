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
    private TextView tvLihatSemua, tvRekomendasiHeader;
    private NestedScrollView nestedScrollView;
    private RecyclerView rvDashboardEvents;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        cardPendingPayment = findViewById(R.id.cardPendingPayment);
        btnExploreNow = findViewById(R.id.btnExploreNow);
        btnBayarSekarang = findViewById(R.id.btnBayarSekarang);
        tvLihatSemua = findViewById(R.id.tvLihatSemua);
        tvRekomendasiHeader = findViewById(R.id.tvRekomendasiHeader);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        rvDashboardEvents = findViewById(R.id.rvDashboardEvents);

        setupRecyclerView();

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

        // Search Bar Listener
        android.widget.EditText etSearchDashboard = findViewById(R.id.etSearchDashboard);
        etSearchDashboard.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH || 
                actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                String query = etSearchDashboard.getText().toString();
                if (!query.isEmpty()) {
                    Intent intent = new Intent(DashboardActivity.this, RecommendationListActivity.class);
                    intent.putExtra("search_query", query);
                    startActivity(intent);
                    return true;
                }
            }
            return false;
        });

        findViewById(R.id.menuHistory).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, HistoryActivity.class));
        });

        // Bottom Nav Listeners
        findViewById(R.id.navEksplorasi).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, ExplorationMapActivity.class));
        });

        findViewById(R.id.navAkun).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, AccountActivity.class));
        });
        
        // 2. Klik "Jelajahi Sekarang" scroll ke Rekomendasi
        btnExploreNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Scroll ke posisi header
                int y = ((View) tvRekomendasiHeader.getParent()).getTop();
                nestedScrollView.smoothScrollTo(0, y);
            }
        });

        // 3. Klik "Lihat Semua"
        tvLihatSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, RecommendationListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupRecyclerView() {
        rvDashboardEvents.setLayoutManager(new LinearLayoutManager(this));
        List<EventModel> allEvents = EventManager.getAllEvents();
        // Ambil 2 event pertama untuk ditampilkan di dashboard
        List<EventModel> dashboardEvents = new ArrayList<>();
        if (allEvents.size() > 0) dashboardEvents.add(allEvents.get(0));
        if (allEvents.size() > 1) dashboardEvents.add(allEvents.get(1));
        
        eventAdapter = new EventAdapter(dashboardEvents, this);
        rvDashboardEvents.setAdapter(eventAdapter);
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

    private void bukaDetail(String nama, String kategori, String deskripsi, String lokasi, int imageResId, String harga) {
        Intent intent = new Intent(DashboardActivity.this, LapakDetailActivity.class);
        intent.putExtra("nama_lapak", nama);
        intent.putExtra("kategori_lapak", kategori);
        intent.putExtra("deskripsi_lapak", deskripsi);
        intent.putExtra("lokasi_lapak", lokasi);
        intent.putExtra("gambar_lapak", imageResId);
        intent.putExtra("harga_lapak", harga);
        startActivity(intent);
    }
}
